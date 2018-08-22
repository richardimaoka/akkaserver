package resources

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.{MultipartUnmarshallers, Unmarshaller}
import entities.Image
import routing.MyResource
import service.ImageService



trait TensorFlowResources extends MyResource {
  println("TensorFlowResources being initialized")

  //ImageService needs implicit ExecutionContext at initialization, but when the below line is called from RestInterface,
  //ExecutionContext is not instantiated, since the below ...

  //ORIGINAL EXECUTION APPROACH
  //val imageService = new ImageService


  //NEW OBSERVATION:
  // Image Service was never supposed to initialized initially, it was supposed to be initialized inside of Tensorflow
  //Resources. After this is declared here, it is instantiated in RestInterface Trait and since it is lazy val
  // it only comes when needed and this will be when there is a proper execution context..
  val imageService: ImageService
  // ... is called beofore the following line inside Main
  //  > implicit val executionContext = system.dispatcher

  def imageRoutes: Route = pathPrefix("images") {
    pathEnd {
      post {
        entity(as[Image]) { image =>
            //println(s"imageService = $imageService")
            println(s"image = $image")
            completeWithLocationHeader(
              resourceId = imageService.createEntity(image),
              ifDefinedStatus = 201, ifEmptyStatus = 409
            )}
      } ~
      path(Segment) { id =>
        get {
          complete(imageService.getEntity(id))
        }
      }
    }
  }
}
