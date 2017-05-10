
import Boot._

import akka.actor.Actor
import com.datastax.driver.core.BoundStatement
import java.math.BigInteger
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer


import scala.concurrent.{ExecutionContext, Future}

object Engine extends Actor {
  implicit val ec: ExecutionContext = system.dispatcher

   val AllOrders = {
     //read from file
     ListBuffer.empty[StockOrder];
   }

  def saveOrder(ord: StockOrder):Boolean = {
    //save to file
   System.out.println("orderrecvd:"+ord.toString)

    if(ord.numShares % 3 == 0)
    {
      AllOrders += ord
      true
    } else false



  }

  def receive: Receive = {

    case buySell: StockOrder => if(saveOrder(buySell))  // try save if ok success else failure
                            sender ! StockOrderSuccess(buySell.orderId)
                            else
                            sender ! StockOrderFailure

    case statusRequest: OrderStatusRequest => sender ! OrderStatusResponse(AllOrders.filter( x =>  x.orderId.equals(statusRequest.Id)).headOption)

    case req: InfoRequest => sender ! InfoResponse(AllOrders.filter( x =>  x.stk.equals(req.stk) && x.orderStatus == StockOrder.EXECUTED).sortBy(_.time).take(10).toList)






    case Ping() => val reqstr = sender
      reqstr ! Future {
        Pong("Pong=" + System.nanoTime())
      }
  }
}
