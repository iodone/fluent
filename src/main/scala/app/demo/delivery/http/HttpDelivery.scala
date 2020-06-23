package app.demo.delivery.http

/**
  * Created by iodone on {19-11-25}.
  */

import java.util.UUID
import java.util.concurrent.TimeUnit

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.FiniteDuration
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import kamon.Kamon
import org.slf4j.MDC
import core.Delivery
import core.Router
import app.demo.repositry._
import app.demo.service._
import app.demo.delivery.http.router._
import app.demo.delivery.http.handler._
import kamon.context.Context



case class HttpDelivery(implicit ec: ExecutionContext) extends Delivery[Route] {
  import com.softwaremill.macwire._

  def handle = {
    appRouter.routes
  }


  lazy val orderRepo = wire[OrderRegistryImp]
  lazy val os = wire[OrderServiceImp]
  lazy val or = wire[OrderRouterImp]


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


    def loggingReqInfo = {

        extractRequestContext.flatMap { ctx =>

            val reqId = ctx.request.headers.find(_.name == "X-Request-Id").map(_.value).getOrElse(UUID.randomUUID.toString)
//          val requestId = kamon.context.Context.key("requestId", "nonono")

            val start = System.currentTimeMillis()
            // 支持跨域访问
            val corsResponseHeaders = List(
              `Access-Control-Allow-Origin`.*,
              RawHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, DELETE"),
              `Access-Control-Allow-Credentials`(true),
              `Access-Control-Allow-Headers`("x-access-key",
                "Content-Type", "X-Requested-With"),
              `Access-Control-Max-Age`(FiniteDuration(1, TimeUnit.DAYS).toMillis)
            )
            if (ctx.request.method == HttpMethods.OPTIONS) {
              respondWithHeaders(corsResponseHeaders)
            } else {
              mapResponse { resp =>

                val d = System.currentTimeMillis() - start
                logger.info(s"${ctx.request.method.name} ${ctx.request.uri} response status: ${resp.status.intValue()}, took: ${d}ms")
                resp.withHeaders(corsResponseHeaders)
                resp.withHeaders(RawHeader("X-Request-Id", reqId))
              } & handleRejections(RejectionHandlers.rejectionHandler) & handleExceptions(ExceptionHandlers.exceptionHandler)
            }
        }
    }
  }


}
