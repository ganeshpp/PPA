import Boot._
import akka.actor.Actor
import scala.concurrent._
import scala.util._



object StockEngine extends Actor {

  implicit val ec: ExecutionContext = system.dispatcher

  def receive: Receive = {

    case runEngine => {
      System.out.println("Running Engine processing")
      val all = Engine.AllOrders.filter(x => x.orderStatus!=StockOrder.EXECUTED)
       val uniqStks= all.map(_.stk).distinct

      for(stk <- uniqStks){
       val sell = all.filter(x=>x.stk.equals(stk) && x.orderType == StockOrder.SELL)
       val buy = all.filter(x=>x.stk.equals(stk) && x.orderType == StockOrder.BUY)
        for(s <- sell ){

          val buyer = buy.filter( x => x.askOrBid == s.askOrBid && x.numShares == s.numShares && x.orderStatus!=StockOrder.EXECUTED)

          if(buyer.headOption.isDefined) {
            val finalbuyer =buyer.headOption.get
            finalbuyer.orderClearPrice = s.askOrBid
            finalbuyer.orderStatus = StockOrder.EXECUTED
            s.orderClearPrice = finalbuyer.askOrBid
            s.orderStatus = StockOrder.EXECUTED
          }

        }
      }

    }
  }
}
