import scala.concurrent.duration._
import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.stream.ActorMaterializer
import akka.util.Timeout

object Main extends App with RestInterface {
  println("Main being initalizing")

  implicit val system = ActorSystem("model-server")
  implicit val materializer = ActorMaterializer()

  // This overrides implicit def executionContext: ExecutionContext of RestInterface,
  // but **AFTER** RestInterface is initialized (should I say constructed?)
  println("Main is initalizing executionContext")
  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

  val api = routes

  Http().bindAndHandle(handler = api, "localhost", 8080) map {
    binding => println(s"REST interface bound to ${binding.localAddress}")
  } recover { case ex =>
    println(s"REST interface could not bind to localhost", ex.getMessage)
  }
}

//class Something
//
//trait MyParentTrait {
//  val something = new Something
//}
//
//trait MyTrait extends MyParentTrait{
//
//}
//
//object MyTrait extends MyParentTrait
//
//object Main extends App with MyTrait {
//  println(something)
//}
