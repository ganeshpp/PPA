import com.datastax.driver.core.Row
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.io.Source
import scala.util._


object FERates {

  val url = "http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json"

  def getRates: Future[Quotes] = {
    val ret = Promise[Quotes]
    Future {
      //  val map=json.split("\\W+").foldLeft(Map.empty[String, Int]){
      //   (count, word) => count + (word -> (count.getOrElse(word, 0) + 1))}
      //   println( map.get("symbol"))
     val quotes = (Json.parse(Source.fromURL(url).mkString) \ "list" \ "resources").as[List[Quote]]
      println("####" + quotes.filter(_.id == "INR=X").head.toString)
      quotes
    }.onComplete {
      case Success(q: List[Quote]) => ret.success(Quotes(q))
      case Failure(e: Throwable) => ret.failure(e)
    }
    ret.future
  }
}

case class Quote(id: String, value: Double, time: Long, qtype: String, name: String)
case class Quotes(quotes: List[Quote])


object Quote {
  def apply1(id: String, value: String, time: String, qtype: String, name: String): Quote = Quote(id, value.toDouble, time.toLong, qtype, name)

  def apply(row: Row): Quote = Quote(row.getString("symbol"), row.getDouble("value"), row.getVarint("time").toString.toLong, row.getString("name"), row.getString("type"))

  implicit val quoteReads: Reads[Quote] = (
    (JsPath \\ ("symbol")).read[String] and
      (JsPath \\ ("price")).read[String] and
      (JsPath \\ ("ts")).read[String] and
      (JsPath \\ ("type")).read[String] and
      (JsPath \\ ("name")).read[String]
    )(Quote.apply1 _)


  implicit val quoteWrites = new Writes[Quote] {
    def writes(c: Quote): JsValue = {
      Json.obj(
        "id" -> c.id,
        "price" -> c.value,
        "ts" -> c.time,
        "type" -> c.qtype,
        "name" -> c.name
      )
    }
  }
}

class mesgRequest()

class mesgResponse()

case class GetQuote(id: String) extends mesgRequest

case class GetAllQuotes() extends mesgRequest

case object FetchQuotes extends mesgRequest

case class Ping() extends mesgRequest

case class Pong(x: String) extends mesgResponse




