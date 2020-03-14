package core

import akka.http.scaladsl.model._
import io.circe.Json

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iodone on {19-11-27}.
  */




trait Handler {
  import Handler._

  def recieve: PartialFunction[FluentHttpRequest, Future[FluentHttpResponse]]




//  def extractRequest[T](action: T => Future[(StatusCode, Json)])(httpRequest: HttpRequest): Future[HttpResponse] = ???
}

object Handler {

   implicit def transform(fh: FluentHttpHandler)(implicit ec: ExecutionContext): HttpHandler = { req =>
    val fluentReq = new FluentHttpRequest {

      override val httpRequest: HttpRequest = req
      override val version: String = req.protocol.value
      override val method: String = req.method.value
      override val entity: MessageEntity = req.entity
      override val uri: Uri = req.uri

      override def path: String = uri.path.toString()
    }
    fh(fluentReq).map { resp =>
      HttpResponse(resp._1, entity = HttpEntity(ContentTypes.`application/json`, resp._2.noSpaces))
    }
  }
  type HttpHandler = HttpRequest => Future[HttpResponse]
  type FluentHttpHandler = FluentHttpRequest => Future[FluentHttpResponse]
  type FluentHttpResponse = (StatusCode, Json)
  trait FluentHttpRequest {
    val httpRequest: HttpRequest
    val entity: RequestEntity
    val version: String
    val method: String
    val uri: Uri
    def path: String
  }


}


