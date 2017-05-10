

import Boot._
import akka.actor._
import play.api.libs.json._
import spray.routing.RejectionHandler._
import spray.routing.{HttpService, RequestContext}
import scala.concurrent.duration.SECONDS
import StockOrder._
import java.util.UUID;

import scala.concurrent.{ExecutionContext, Future}
import scala.util._

class ServiceActor extends Actor with Service {
  def actorRefFactory = context

  implicit val system = context.system

  def receive = runRoute(apiRouter)
}

trait Service extends HttpService {

  val apiRouter = {
    path("ping") {
      get {
        ctx => system.actorOf(Props(new processActor(ctx, Ping())))
      }
    } ~
      path("buy" / Segment / LongNumber / DoubleNumber) { (stk: String, shares: Long, bid: Double) =>
        get {
          ctx => system.actorOf(Props(new processActor(ctx, StockOrder(System.nanoTime(), UUID.randomUUID().toString, BUY, stk, shares, bid, PENDING, 0))))
        }
      }~
          path("sell" / Segment / LongNumber / DoubleNumber) { (stk: String, shares: Long, bid: Double) =>
            get {
              ctx => system.actorOf(Props(new processActor(ctx, StockOrder(System.nanoTime(), UUID.randomUUID().toString, SELL, stk, shares, bid, PENDING, 0))))
            }
          }~
              path("status" / Segment) { stk =>
                get {
                  ctx => system.actorOf(Props(new processActor(ctx, OrderStatusRequest(stk))))
                }
              }~
                  path("info" / Segment) { stk =>
                    get {
                      ctx => system.actorOf(Props(new processActor(ctx, InfoRequest(stk))))
                    }
                  }~
                    path("all") {
                    get {
                      ctx =>  ctx.complete(200,Engine.AllOrders.map( _.toString).mkString(" \n"))
                    }
                  }
              }

}


class processActor(ctx: RequestContext, request: mesgRequest) extends Actor with ActorLogging {
  implicit val ec: ExecutionContext = system.dispatcher
  engine ! request
  context.setReceiveTimeout(10, SECONDS)

  def receive = {

    case succ: StockOrderSuccess => ctx.complete(200, succ.Id)
      self ! PoisonPill
    case fail: StockOrderFailure => ctx.complete(500, "Error")
      self ! PoisonPill
    case orderStatus: OrderStatusResponse => if (orderStatus.order.isDefined) ctx.complete(200, orderStatus.order.toString()) else ctx.complete(500, "Error")
      self ! PoisonPill
    case res: InfoResponse => if (res.orders.size > 0) {

      val avgPrice = res.orders.map(_.askOrBid).toList.sum / res.orders.map(_.numShares).toList.sum
      val ret = res.orders.head.stk + " avgPrice:" + avgPrice + " \n" + res.orders.map(x => x.numShares + " :" + x.orderClearPrice).mkString(" \n")
      ctx.complete(200, ret)
    }
    else ctx.complete(500, "InfoFailed No transactions")
      self ! PoisonPill




    case _ => println("***********************")
      ctx.complete("unknown message")
      self ! PoisonPill
  }
}






