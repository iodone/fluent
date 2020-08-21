package app.demo.gateways.repositry

/**
  * Created by iodone on {18-11-12}.
  */

import app.demo.domain.repositry._
import app.demo.domain.Entity._
import core.Entity.Id
import common.storage.QuillStorage
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}
import io.getquill._


object MemStorage {

}

object OrderStorage extends QuillStorage {

  // mysql model
  final case class OrderSchema(id: Id, name: String)
  final case class ItemSchema(id: Id, orderId: Id, name: String)

  import dbContext._

  val ordersT = quote {
    querySchema[OrderSchema](
      "order_t"
    )
  }

  val itemsT = quote {
    querySchema[ItemSchema](
      "item_t"
    )
  }
}


case class OrderRepositryImp(implicit ec: ExecutionContext) extends OrderRepositry with LazyLogging {
  import OrderStorage._
  import OrderStorage.dbContext._

  // insert into a table with auto_increment primary key
  def save(order: Order): Future[Id] = Future {

    val orderData = OrderSchema(0, order.name)
    val a = quote {
      ordersT.insert(lift(orderData))
    }
    val orderId = dbContext.run(a)

    val itemsData = order.items.map(x => ItemSchema(x.id, orderId, x.name))
    val b = quote {
      liftQuery(itemsData).foreach(itemsT.insert(_))
    }
    dbContext.run(b)
    orderId
  }

  def getOrder(id: Id): Future[Option[Order]] = Future {
    val q = quote {
      ordersT.filter(_.id == lift(id))
    }

    dbContext.run(q).headOption.map {x =>
      val qq = quote {
        itemsT.filter(_.orderId == lift(x.id))
      }
      val item = dbContext.run(qq).map(x => Item(x.id, x.name))
      Order(x.id, x.name, item)
    }
  }

  def fetchItem(id: Id): Future[Option[Item]] = Future {
    val q = quote {
      itemsT.filter(_.id == lift(id))
    }
    dbContext.run(q).headOption.map(x => Item(x.id, x.name))
  }

  def save(id: OrderId, items: List[Item]): Future[List[Id]] = Future {
    val itemsData = items.map(x => ItemSchema(x.id, id.id, x.name))
    val a = quote {
      liftQuery(itemsData).foreach(itemsT.insert(_))
    }
    dbContext.run(a)
  }

}

case class MemoryOrderRepositry(implicit ec: ExecutionContext) extends OrderRepositry {

  def save(order: Order): Future[Id] = ???
  def getOrder(id: Id): Future[Option[Order]] = ???
  def fetchItem(id: Id): Future[Option[Item]] = ???
  def save(id: OrderId, items: List[Item]): Future[List[Id]] = ???
}

