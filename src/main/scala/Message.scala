import com.datastax.driver.core.Row
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.io.Source
import scala.util._






class mesgRequest()

class mesgResponse()

case class GetQuote(id: String) extends mesgRequest

case class GetAllQuotes() extends mesgRequest

case object FetchQuotes extends mesgRequest

case class Ping() extends mesgRequest

case class Pong(x: String) extends mesgResponse

case class StockOrder(time:Long,orderId: String,orderType: Int,stk: String,numShares: Long,askOrBid: Double,var orderStatus: Long,var orderClearPrice: Double) extends mesgRequest

case class StockTransaction(time:Long,tId:String,orderId:String,orderType: Int,stk: String,numShares: Long,askOrBid: Double, orderClearPrice: Double) extends mesgRequest

case class StockOrderSuccess(Id: String) extends mesgResponse
case class StockOrderFailure()  extends mesgResponse

case class OrderStatusRequest(Id:String) extends mesgRequest

case class OrderStatusResponse(order: Option[StockOrder]) extends mesgResponse

case class InfoRequest(stk:String) extends mesgRequest

case class InfoResponse(orders:List[StockOrder]) extends mesgResponse

case class runEngine() extends mesgRequest

object StockOrder{
 val BUY= 5
 val SELL= 10

 val PENDING=111
 val EXECUTED = 1000
 val PARTIAL= 500

}




