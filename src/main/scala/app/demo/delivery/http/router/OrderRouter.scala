package app.demo.delivery.http.router

/**
  * Created by iodone on {19-11-14}.
  */


import scala.concurrent.ExecutionContext
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import app.demo.application.OrderAppService

import io.circe.generic.auto._
import io.circe.syntax._
import app.demo.domain.Entity._
import core.Entity.Id
import app.demo.domain.exception._
import core.{BaseException, Router}
import core.ResponseEntity._

class OrderRouter(oas: OrderAppService)(implicit ec: ExecutionContext) extends Router {

  object OrderValidators  {
    import com.wix.accord.dsl._

    // define validator for routes under /order path
    implicit val itemValidator = validator[Item] { p =>
      p.name  is notEmpty
      p.id is between(0L, 1000L)
    }
    implicit val orderValidator = validator[Order] { p =>
      p.id is between(0L, 1000L)
      p.items.each is valid
      p.items have size > 0
    }
  }


  override def routes = {

    pathPrefix("order") {

      path("save") {
        post {
          entity(as[Order]) { order =>
            println(order)
            import common.middleware.ValidationDirectives._
            import OrderValidators._

            validate(order) {
              val respF = oas.saveOrder(order.id, order.items) map { id =>
                OK -> Response(Meta(0, ""), id).asJson
              } recover {
                case ex: BaseException => ex.statusCode -> Response(Meta(ex.errorCode, ex.message), Nil).asJson
              }
              complete(respF)
            }
          }
        }
      } ~
      path("get-item")  {
        post {
          entity(as[Id]) { id =>
            val respF = oas.fetchItem(id) map {
              case Some(item) => OK -> Response(Meta(0, ""), item).asJson
              case None => throw NotFoundException(s"Not found Item by ${id}")
            } recover {
              case ex: BaseException => ex.statusCode -> Response(Meta(ex.errorCode, ex.message), Nil).asJson
            }
            complete(respF)
          }
        }
      }
    }
  }

}

