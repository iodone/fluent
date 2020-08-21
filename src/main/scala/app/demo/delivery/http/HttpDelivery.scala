package app.demo.delivery.http

/**
  * Created by iodone on {19-11-25}.
  */

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import app.demo.application.OrderAppService
import io.circe.generic.auto._
import core.Delivery
import core.Router
import app.demo.domain.service._
import app.demo.gateways.repositry._
import app.demo.delivery.http.router._
import common.middleware.LogginReqDirectives._


case class HttpDelivery(implicit ec: ExecutionContext) extends Delivery[Route] {

  def handle = {
    appRouter.routes
  }

  // DI
  import com.softwaremill.macwire._
  lazy val ordeRepo = wire[OrderRepositryImp]
  lazy val os = wire[PlaceOrderAppService]
  lazy val oas = wire[OrderAppService]
  lazy val or = wire[OrderRouter]


  val appRouter: Router = new Router {
    override def routes: Route = {
      loggingReqInfo {
        pathPrefix("api") {
          pathPrefix("v1") {
            or.routes
          }
        } ~
          pathPrefix("healthcheck") {
            get {
              complete(HttpEntity(ContentTypes.`application/json`, """{"healthStatus": "OK"}"""))
            }
          }
      }
    }
  }
}
