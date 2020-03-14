package app.demo.delivery.http
import app.demo.delivery.http.handler.OrderHandler
import app.demo.domain.entity.ResponseEntity.{Meta, Response}
import app.demo.repositry.DbOrderRepo
import app.demo.service.OrderService
import akka.http.scaladsl.model.StatusCodes
import io.circe.generic.auto._
import io.circe.syntax._
import com.softwaremill.macwire._

import scala.concurrent.{ExecutionContext, Future}
import core.{BaseHandlerDelivery, Delivery, Handler}
import core.Handler.{FluentHttpHandler, HttpHandler}

/**
  * Created by iodone on {19-11-28}.
  */
case class HandlerDelivery(implicit ec: ExecutionContext) extends BaseHandlerDelivery with Delivery[FluentHttpHandler] {



  lazy val orderRepo = wire[DbOrderRepo]
  lazy val os = wire[OrderService]
  lazy val handler = wire[OrderHandler]

  val lastHandler = new Handler {
    def recieve = {
      case _ => Future.successful(StatusCodes.NotFound -> Response(Meta(1000200, "NotFound"), Nil).asJson)
    }
  }
  override val handlers: Vector[Handler] = Vector(handler, lastHandler)
  override val mapfluentHttpHandlers: Vector[FluentHttpHandler => FluentHttpHandler] = Vector()


}
