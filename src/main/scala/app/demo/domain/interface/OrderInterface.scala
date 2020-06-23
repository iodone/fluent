package app.demo.domain.interface

import app.demo.domain.Entity._

import scala.concurrent.Future

trait OrderRepositry {
  def save(order: OrderSchema): Future[Id]
  def getOrder(id: Id): Future[Option[OrderSchema]]
  def fetchItem(id: Id): Future[Option[ItemSchema]]
  def save(items: List[ItemSchema]): Future[List[Id]]
}

trait OrderService {
  def fetchItem(id: Id): Future[Option[Item]] 
  def saveOrder(orderId: Id, items: List[Item]): Future[Id]
}