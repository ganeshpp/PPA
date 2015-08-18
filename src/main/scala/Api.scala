

import Boot._
import akka.actor._
import play.api.libs.json._
import spray.routing.RejectionHandler._
import spray.routing.{HttpService, RequestContext}
import scala.concurrent.duration.SECONDS

import scala.concurrent.{ExecutionContext, Future}
import scala.util._

class ServiceActor extends Actor with Service {
  def actorRefFactory = context

  implicit val system = context.system

  def receive = runRoute(apiRouter)
}

trait Service extends HttpService {

  val apiRouter = {
    path("quotes") {
      get {
        ctx => system.actorOf(Props(new processActor(ctx, GetAllQuotes())))
      }
    } ~
      path("quote" / Segment) { quote =>
        get {
          ctx => system.actorOf(Props(new processActor(ctx, GetQuote(quote))))
        }
      } ~
      path("ping") {
        get {
          ctx => system.actorOf(Props(new processActor(ctx, Ping())))
        }
      }
  }
}


class processActor(ctx: RequestContext, request: mesgRequest) extends Actor with ActorLogging {
  implicit val ec: ExecutionContext = system.dispatcher
  cassandra ! request
  context.setReceiveTimeout(10, SECONDS)

  def receive = {

    case fut: Future[Any] => fut.onComplete {
      case Success(res) => res match {
        case Pong(x) => ctx.complete(x)
        case quotes: List[Quote] => ctx.complete(Json.prettyPrint(Json.toJson(quotes)))
      }
      case Failure(e) => e.printStackTrace
        ctx.complete(e.getMessage)
    }
      self ! PoisonPill

    case _ => println("***********************")
      ctx.complete("unknown message")
      self ! PoisonPill
  }
}




