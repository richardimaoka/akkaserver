import akka.actor._
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object Main extends App with RestInterface {


  implicit val system = ActorSystem("model-server")
  implicit val materializer = ActorMaterializer()


  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

  val interfaces = new RestInterface {
    override implicit def executionContext: ExecutionContext = system.dispatcher
  }
  val api = interfaces.routes

  Http().bindAndHandle(handler = api, "localhost", 8080) map {
    binding => println(s"REST interface bound to ${binding.localAddress}")
  } recover { case ex =>
    println(s"REST interface could not bind to localhost", ex.getMessage)
  }
}
