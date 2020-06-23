package app.demo.service

/**
  * Created by iodone on {19-11-20}.
  */

import scala.concurrent.{Future, ExecutionContext}

import app.demo.domain.Entity._
import app.demo.domain.interface._
import core.Service

class OrderServiceImp(orderRepo: OrderRepositry)(implicit ec: ExecutionContext) extends OrderService with Service {

  def saveOrder(orderId: Id, items: List[Item]): Future[Id] = {
    logger.info("test===========")
    val order = OrderSchema(0, orderId, "order1")
    for {
      orderId <- orderRepo.save(order)
      _ <- orderRepo.save(items.map(x => new ItemSchema(0, x.id, orderId, x.name)))
    } yield orderId
  }

  def fetchItem(id: Id): Future[Option[Item]] = {
    orderRepo.fetchItem(id).map { x =>
      x.map(i => Item(i.id, i.name))
    }
  }

}
