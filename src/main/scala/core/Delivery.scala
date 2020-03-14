package core

import core.Handler.{FluentHttpHandler, FluentHttpRequest, FluentHttpResponse, HttpHandler}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iodone on {19-11-13}.
  */


trait Delivery[T] {

  def handle:T

}

abstract class BaseHandlerDelivery {
  val mapfluentHttpHandlers: Vector[FluentHttpHandler => FluentHttpHandler]
  val handlers: Vector[Handler]

  def handle = {
    val httpHandler = (((x: FluentHttpHandler) => x) +: mapfluentHttpHandlers).reduce((acc, x) => acc compose x)
    val fluentHttpHandler = handlers.map { h =>
      h.recieve
    }.reduce((acc, x) => acc orElse x)

    httpHandler(fluentHttpHandler)
  }

}

