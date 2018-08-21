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
  val imageService = new ImageService
  // ... is called beofore the following line inside Main
  //  > implicit val executionContext = system.dispatcher

  def imageRoutes: Route = pathPrefix("images") {
    pathEnd {
      post {
        entity(as[Image]) { image =>
            println(s"imageService = $imageService")
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
