
import Boot._
import CassandraConfig._
import akka.actor.Actor
import com.datastax.driver.core.BoundStatement
import java.math.BigInteger
import scala.collection.JavaConversions._


import scala.concurrent.{ExecutionContext, Future}

object CassandraActor extends Actor {
  implicit val ec: ExecutionContext = system.dispatcher
  val stmt = session.prepare("select * from Quotes where symbol = ?")
  val allStmt = session.prepare("select * from Quotes LIMIT 1000 ALLOW FILTERING")
  val preparedStatement = session.prepare("INSERT INTO quotes(symbol,value,time,name,type) VALUES (?, ?, ?, ?, ?);")

  def saveQuote(q: Quote): Unit = {
    val boundStatement = new BoundStatement(preparedStatement)
    boundStatement.setString(0, q.id)
    boundStatement.setDouble(1, q.value)
    boundStatement.setVarint(2, BigInteger.valueOf(q.time))
    boundStatement.setString(3, q.qtype)
    boundStatement.setString(4, q.name)
    session.executeAsync(boundStatement)
  }

  def receive: Receive = {

    case quotes: List[Quote] => quotes foreach saveQuote
                                quotes.filter(_.id == "INR=X").map(println(_))

    case quote: Quote => saveQuote(quote)

    case GetQuote(symbol) => val reqstr = sender
      reqstr ! Future {
        session.executeAsync(new BoundStatement(stmt).setString(0, symbol)).get.all().map(Quote(_)).toList
      }

    case GetAllQuotes() => val reqstr = sender
      reqstr ! Future {
        session.executeAsync(new BoundStatement(allStmt)).get.all().map(Quote(_)).toList
      }

    case Ping() => val reqstr = sender
      reqstr ! Future {
        Pong("Pong=" + System.nanoTime())
      }
  }
}
