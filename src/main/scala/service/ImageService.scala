package service

import entities.Image
import scala.concurrent.{ExecutionContext, Future}

class ImageService(implicit val executionContext: ExecutionContext) {
  println("ImageService initialized")
  println(s"executionContext = $executionContext")

  //TODO: Be able to save and request the images first
  //Acting as our Database First
  var data = Vector.empty[Image]


  def createEntity(image: Image): Future[Option[String]] = Future {
    data.find(_.id == image.id) match {
      case Some(i) => None //ID Image already Exists
      case None =>
        data = data :+ image
        Some(image.id)
    }
  }
  def getEntity(id: String): Future[Option[Image]] = Future {
    data.find(_.id == id)
  }
}
