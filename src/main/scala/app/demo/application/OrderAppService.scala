package app.demo.application

/**
  * Created by iodone on {19-11-20}.
  */

import scala.concurrent.{Future, ExecutionContext}

import com.typesafe.scalalogging.LazyLogging

import core.Entity.Id
import app.demo.domain.service.PlaceOrderAppService
import app.demo.domain.repositry.OrderRepositry
import app.demo.domain.Entity._


class OrderAppService(placeOrderService: PlaceOrderAppService, orderRepo: OrderRepositry)(implicit ec: ExecutionContext) extends LazyLogging {

  def saveOrder(orderId: Id, items: List[Item]): Future[Id] = {
    placeOrderService.place(Order(orderId, "ordername", items))
  }

  def fetchItem(id: Id): Future[Option[Item]] = {
    orderRepo.fetchItem(id)
  }

}
