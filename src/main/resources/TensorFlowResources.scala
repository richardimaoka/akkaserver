package com.mlserver.reactivefacenet.akkaserver.resources

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.{MultipartUnmarshallers, Unmarshaller}
import com.mlserver.reactivefacenet.akkaserver.entities.Image
import com.mlserver.reactivefacenet.akkaserver.routing.MyResource
import com.mlserver.reactivefacenet.akkaserver.service.ImageService



trait TensorFlowResources extends MyResource {
  val imageService = new ImageService

  def imageRoutes: Route = pathPrefix("images") {
    pathEnd {
      post {
        entity(as[Image]) { image =>
            completeWithLocationHeader(resourceId = imageService.createEntity(image),
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