package app.demo.domain

/**
  * Created by iodone on {19-11-26}.
  */

import core.Entity._
import core.Entity

object Entity extends Entity {

  final case class OrderId(id: Id) extends Entity
  final case class Item(id: Id = 0, name: String) extends Entity
  final case class Order(id: Id = 0, name: String, items: List[Item]) extends Entity

}





