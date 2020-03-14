package app.demo.domain.exception

/**
  * Created by iodone on {19-11-13}.
  */
import akka.http.scaladsl.model.StatusCodes._
import core.BaseException

case class ValidateError(override val message: String)
  extends BaseException(4007401, message, BadRequest)

case class JarNotFoundException(override val message: String = "未找到启动文件")
  extends BaseException(4007403, message, BadRequest)

case class ResourceInsufficientException(override val message: String = "资源不足")
  extends BaseException(4007404, message, BadRequest)

case class HDFSEXception(override val message: String)
  extends BaseException(4007500, message, InternalServerError)

case class StateTransitionException(override val message: String)
  extends BaseException(4007501, message, InternalServerError)

case class YarnException(override val message: String)
  extends BaseException(4007502, message, InternalServerError)

case class SecurityException(override val message: String)
  extends BaseException(4007405, message, BadRequest)

case class ZookeeperException(override val message: String)
  extends BaseException(4007503, message, InternalServerError)

case class SparkServiceException(override val message: String)
  extends BaseException(4007504, message, InternalServerError)

case class TaskKillException(override val message: String)
  extends BaseException(4007505, message, InternalServerError)

case class MetaServiceException(override val message: String)
  extends BaseException(4007506, message, InternalServerError)

case class XqlProcessException(override val message: String)
  extends BaseException(4007507, message, InternalServerError)

case class SyntaxException(override val message: String) extends
  BaseException(4007405, message, BadRequest)

case class SyntaxServiceException(override val message: String)
  extends BaseException(4007508, message, InternalServerError)

case class ConfigFileNotFoundException(override val message: String)
  extends BaseException(4007405, message, InternalServerError)

case class ElasticSearchQueryException(override val message: String)
  extends BaseException(4007509, message, InternalServerError)

case class ApplicationNotAvaliableException(override val message: String)
  extends BaseException(4007406, message, BadRequest)

case class SerilizationException(override val message: String)
  extends BaseException(4007510, message, InternalServerError)

case class DecompressionException(override val message: String)
  extends BaseException(4007511, message, InternalServerError)

case class UploadException(override val message: String)
  extends BaseException(4007512, message, InternalServerError)

case class JarConflictException(override val message: String)
  extends BaseException(4007407, message, BadRequest)

case class NotFoundException(override val message: String)
  extends BaseException(4007408, message, NotFound)

case class SourceNotFoundException(override val message: String)
  extends BaseException(4007409, message, BadRequest)

case class MetricsDataNotFound(override val message: String)
  extends BaseException(40074010, message, BadRequest)
