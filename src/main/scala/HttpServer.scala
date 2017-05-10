import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import org.joda.time
import org.joda.time.Seconds
import spray.can.Http
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


object Boot extends App {
  implicit val system = ActorSystem("SprayServer")
  val engine = system.actorOf(Props(Engine))
  val processor = system.actorOf(Props(StockEngine))  //monitor for proceesor?
  implicit val timeout = Timeout(50,SECONDS)

  val restService = system.actorOf(Props(new ServiceActor), "api-endpoint")
  IO(Http) ! Http.Bind(restService, "localhost", 1080)

  println("server started")
  system.scheduler.schedule(0 seconds, 2 seconds, processor, runEngine)

}
