
import Boot._
import akka.actor.Actor
import scala.concurrent._
import scala.util._


object CassandraReaderActor extends Actor {

  implicit val ec: ExecutionContext = system.dispatcher

  def receive: Receive = {
    case ft: Future[Any] => ft.onComplete {
      case Success(x) => x match {
        case qts: Quotes => println("received quotes,writing to db")
                             cassandra ! qts.quotes
        case qlist: List[Quotes] => println("received quotes,writing to db")
          cassandra ! qlist
      }
      case Failure(e) => e.printStackTrace
    }
  }
}