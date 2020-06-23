package core

/**
  * Created by iodone on {20-6-23}.
  */
trait Entity

object ResponseEntity {
    final case class Meta(errCode: Int, errMsg: String) extends Entity
    final case class Response[T](meta: Meta, data: T) extends Entity
}
