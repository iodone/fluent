package app.demo.domain.repositry

/**
  * Created by iodone on {20-8-20}.
  */

import scala.concurrent.Future
import core.Entity._
import app.demo.domain.Entity._

trait OrderRepositry {

  def save(order: Order): Future[Id]
  def getOrder(id: Id): Future[Option[Order]]
  def fetchItem(id: Id): Future[Option[Item]]
  def save(id: OrderId, items: List[Item]): Future[List[Id]]

}
