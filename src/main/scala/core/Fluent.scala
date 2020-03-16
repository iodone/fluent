package core

/**
  * Created by iodone on {19-11-14}.
  */

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow
import com.typesafe.scalalogging.LazyLogging
import org.slf4j.MDC


final class Fluent(host: String, port: Int, actorSystem: ActorSystem) extends LazyLogging {

  implicit val system: ActorSystem = actorSystem
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()


  var bindingFuture: Future[ServerBinding] = _

  def startHttpServerByHandler(handler: HttpRequest => Future[HttpResponse]): Unit = {
    bindingFuture = Http().bindAndHandleAsync(handler, host, port)
    logger.info(s"Waiting for requests at http://${host}:${port}/...")
  }


  def startHttpServer(handler: Flow[HttpRequest, HttpResponse, Any]): Unit = {
    bindingFuture = Http().bindAndHandle(handler, host, port)
    logger.info(s"Waiting for requests at http://${host}:${port}/...")
  }

  def stopHttpServer(): Unit = {
    if (bindingFuture != null) {
      bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => system.terminate)
    }
  }
}
