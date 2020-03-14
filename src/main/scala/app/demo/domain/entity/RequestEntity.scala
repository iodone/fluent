package app.demo.domain.entity

/**
  * Created by iodone on {19-11-13}.
  */

object RequestEntity extends Entity {

  final case class OrderId(id: Id)
  final case class Item(id: Id, name: String)
  final case class Order(id: Id, items: List[Item])

}
