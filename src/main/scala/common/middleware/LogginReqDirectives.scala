package common.middleware

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives.{extractRequestContext, handleExceptions, handleRejections, mapResponse, respondWithHeaders}
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.duration.FiniteDuration

import common.handler._

/**
  * Created by iodone on {20-6-23}.
  */
object LogginReqDirectives extends LazyLogging {
  def loggingReqInfo = {

    extractRequestContext.flatMap { ctx =>

      val reqId = ctx.request.headers.find(_.name == "X-Request-ID").map(_.value).getOrElse("")
      val start = System.currentTimeMillis()
//      val curContext = kamon.Kamon.currentContext().withTag("X-Request-ID", reqId)
//      val scope = kamon.Kamon.storeContext(curContext)
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
//          scope.close()
          resp.withHeaders(corsResponseHeaders)
          resp.withHeaders(RawHeader("X-Request-Id", reqId))
        } & handleRejections(RejectionHandlers.rejectionHandler) & handleExceptions(ExceptionHandlers.exceptionHandler)
      }
    }
  }
}
