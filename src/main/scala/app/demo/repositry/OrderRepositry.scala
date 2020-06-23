package app.demo.repositry

/**
  * Created by iodone on {18-11-12}.
  */

import app.demo.domain.Entity._
import app.demo.domain.interface._
import common.storage.QuillStorage
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}
import io.getquill._


trait OrderStorage extends QuillStorage {
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


case class OrderRepositryImp(implicit ec: ExecutionContext) extends OrderRepositry with OrderStorage with LazyLogging {

  import dbContext._

  // insert into a table with auto_increment primary key
  def save(order: OrderSchema): Future[Id] = Future {
    val a = quote {
      ordersT.insert(lift(order))
    }
    dbContext.run(a)
  }

  def getOrder(id: Id): Future[Option[OrderSchema]] = Future {
    val q = quote {
      ordersT.filter(_.id == lift(id))
    }
    dbContext.run(q).headOption
  }

  def fetchItem(id: Id): Future[Option[ItemSchema]] = Future {
    val q = quote {
      itemsT.filter(_.id == lift(id))
    }
    dbContext.run(q).headOption
  }

  def save(items: List[ItemSchema]): Future[List[Id]] = Future {
    logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
    val a = quote {
      liftQuery(items).foreach(itemsT.insert(_))
    }
    dbContext.run(a)
  }

}

case class MemoryOrderRepositry(implicit ec: ExecutionContext) extends OrderRepositry {
  def save(order: OrderSchema): Future[Id] = ???
  def getOrder(id: Id): Future[Option[OrderSchema]] = ???
  def fetchItem(id: Id): Future[Option[ItemSchema]] = ???
  def save(items: List[ItemSchema]): Future[List[Id]] = ???
}

