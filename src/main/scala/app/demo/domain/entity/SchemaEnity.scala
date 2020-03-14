package app.demo.domain.entity

/**
  * Created by iodone on {19-11-26}.
  */

// entities used to define schema of data model
object SchemaEntity extends Entity {

  // Model entity
  final case class OrderSchema(id: Id, orderId: Id, name: String)
  final case class ItemSchema(id: Id, itemId: Id, orderId: Id, name: String)
}

