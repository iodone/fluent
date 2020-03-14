package app.demo.domain.entity

/**
  * Created by iodone on {19-11-20}.
  */
object ResponseEntity {
  final case class Meta(errCode: Int, errMsg: String)
  final case class Response[T](meta: Meta, data: T)
}
