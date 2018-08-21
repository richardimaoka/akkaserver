import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Route
import exceptionhandler.MyImplicitExceptionHandler
import resources.TensorFlowResources
import service.ImageService

trait RestInterface extends Resources with MyImplicitExceptionHandler{
  println("RestInterface being initialized")
  // This doesn't have definition, so until Main hits the below line
  // > implicit val executionContext = system.dispatcher
  // this is left null
  implicit def executionContext: ExecutionContext

  lazy val imService = new ImageService
  val routes: Route =  imageRoutes
}

trait Resources extends TensorFlowResources
