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

/**
 * Execution context proxy for propagating SLF4J diagnostic context from caller thread to execution thread.
 */
class MdcExecutionContext(executionContext: ExecutionContext) extends ExecutionContext {
  override def execute(runnable: Runnable): Unit =  {
    val callerMdc = MDC.getCopyOfContextMap // get a copy of the MDC data
    executionContext.execute(new Runnable {
      def run(): Unit = {
        // copy caller thread MDC to the new execution thread
        if(callerMdc != null) MDC.setContextMap(callerMdc)
        try {
          runnable.run
        } finally {
          // the thread might be reused, so we clean up for the next use
          MDC.clear
        }
      }
    })
  }

  override def reportFailure(cause: Throwable): Unit = executionContext.reportFailure(cause)
}


final class Fluent(host: String, port: Int, actorSystem: ActorSystem) extends LazyLogging {

  implicit val system: ActorSystem = actorSystem
  implicit val executionContext: ExecutionContext = new MdcExecutionContext(system.dispatcher)
//  implicit val executionContext: ExecutionContext = new MdcExecutionContext(
//    ExecutionContext.fromExecutor(      // this gives us an ExecutionContext we piggyback on in MdcExecutionContext
//      Executors.newWorkStealingPool(4) // this creates the ForkJoinPool with a maximum of 10 threads
//    )
//  )
//  implicit val materializer = ActorMaterializer()


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
