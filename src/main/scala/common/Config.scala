package common

/**
  * Created by iodone on {19-11-25}.
  */


import pureconfig._
import pureconfig.generic.auto._

trait Config

case class Akka() extends Config

case class DbConfig(url: String, user: String, password: String, driver: String)
case class DatabasesConfig(mysql: DbConfig)

case class HttpConfig(host: String, port: Int, accessKey: String = "") extends Config

case class FluentConfig(http: HttpConfig, databases: DatabasesConfig) extends Config
case class AppConfig(akka: Akka, fluent: FluentConfig) extends Config

object Config {
  lazy val appConfig =  ConfigSource.default.loadOrThrow[AppConfig]
}
