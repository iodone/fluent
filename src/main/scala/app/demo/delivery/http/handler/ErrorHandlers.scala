package app.demo.delivery.http.handler

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.{ExceptionHandler, RejectionHandler}
import app.demo.domain.entity.ResponseEntity.{Meta, Response}
import com.typesafe.scalalogging.LazyLogging
import io.circe.syntax._
import io.circe.generic.auto._

/**
  * Created by iodone on {19-11-25}.
  */

object RejectionHandlers {
  implicit val rejectionHandler = RejectionHandler.default
    .mapRejectionResponse {
      case res @ HttpResponse(_, _, ent: HttpEntity.Strict, _) =>
        // since all Akka default rejection responses are Strict this will handle all rejections
        val message = ent.data.utf8String.replaceAll("\"", """\"""")

        // we copy the response in order to keep all headers and status code, wrapping the message as hand rolled JSON
        // you could the entity using your favourite marshalling library (e.g. spray json or anything else)
        val resContent = Response(Meta(1000100, message), Nil).asJson.noSpaces

        res.copy(entity = HttpEntity(ContentTypes.`application/json`, resContent))
      // pass through all other types of responses
      case x => {
        x
      }
    }
}

object ExceptionHandlers extends LazyLogging {
  implicit val exceptionHandler = ExceptionHandler {
    case e: Exception =>
      logger.error("Internal error.", e)
      val resContent = Response(Meta(1000200, "Internal error"), Nil).asJson.noSpaces
      complete(500 -> HttpEntity(ContentTypes.`application/json`, resContent))
  }

}