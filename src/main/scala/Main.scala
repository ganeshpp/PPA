/**
 * Created by gp4587 on 8/9/15.
 */
import scala.util._
import scala.concurrent._
import scala.concurrent.duration._
import concurrent.ExecutionContext.Implicits.global
object Main {

  def main1(Args: Array[String]){
    val url ="http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json"
   /* val fut = FERates(url).getRates
     /* fut.onComplete {
       case Success(x:Quotes) => println(x.quotes.size)
       case Failure(e) => e.printStackTrace
     }*/
    Await.result(fut, 55000 millis);
    fut.value.get.get.quotes.zipWithIndex.map( x => println(x._2 +":"+x._1))*/

  }

}
