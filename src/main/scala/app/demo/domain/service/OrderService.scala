package app.demo.domain.service

/**
  * Created by iodone on {19-11-20}.
  */

import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.{ExecutionContext, Future}

import app.demo.domain.repositry.OrderRepositry
import core.Entity.Id
import app.demo.domain.Entity._

class PlaceOrderAppService(orderRepo: OrderRepositry)(implicit ec: ExecutionContext) extends LazyLogging {

  def place(order: Order): Future[Id] = {
    logger.info("test===========")
    orderRepo.save(order)
  }

}
