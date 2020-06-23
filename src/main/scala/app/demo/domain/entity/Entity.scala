package app.demo.domain

/**
  * Created by iodone on {19-11-26}.
  */

import core.Entity


object Entity {

  type Id = Long

  // for bussiness logic
  final case class OrderId(id: Id) extends Entity
  final case class Item(id: Id, name: String) extends Entity
  final case class Order(id: Id, items: List[Item]) extends Entity

  // for data model
  final case class OrderSchema(id: Id, orderId: Id, name: String) extends Entity
  final case class ItemSchema(id: Id, itemId: Id, orderId: Id, name: String) extends Entity

}





