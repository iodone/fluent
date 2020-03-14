package app.demo.delivery.http.handler

import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import io.circe.generic.auto._
import io.circe.syntax._
import app.demo.domain.entity.ResponseEntity._
import app.demo.domain.entity.RequestEntity._
import app.demo.domain.exception.NotFoundException
import app.demo.service.OrderService
import core.{BaseException, Handler}
import core.Extractor._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iodone on {19-11-28}.
  */
class OrderHandler(os: OrderService)(implicit ec: ExecutionContext) extends Handler {

  def recieve = {
    case POST(Path("/api/v1/test")) => {
      Future.successful(StatusCodes.OK->Response(Meta(0, ""), 1).asJson)
    }
  }

  def saveOrder(order: Order) = {
    os.saveOrder(order.id, order.items) map { id =>
      OK -> Response(Meta(0, ""), id).asJson
    } recover {
      case ex: BaseException => ex.statusCode -> Response(Meta(ex.errorCode, ex.message), Nil).asJson
    }
  }

  def getItems(order: OrderId) = {
    os.fetchItem(order.id) map {
      case Some(item) => OK -> Response(Meta(0, ""), item).asJson
      case None => throw NotFoundException(s"Not found Item by ${order.id}")
    } recover {
      case ex: BaseException => ex.statusCode -> Response(Meta(ex.errorCode, ex.message), Nil).asJson
    }
  }


}
