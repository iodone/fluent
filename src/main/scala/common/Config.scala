package common

/**
  * Created by iodone on {19-11-25}.
  */

import pureconfig.loadConfig

trait Config

case class Akka() extends Config
case class FluentConfig(http: HttpConfig, database: DatabaseConfig) extends Config
case class HttpConfig(host: String, port: Int, accessKey: String = "") extends Config
case class DatabaseConfig(jdbcUrl: String, username: String, password: String) extends Config

case class AppConfig(akka: Akka, fluent: FluentConfig) extends Config

object Config {
  var conf:AppConfig = null

  def load = this.synchronized {
    if (conf == null) {
      conf = loadConfig[AppConfig] match {
        case Right(config) => config
        case Left(error) =>
          throw new RuntimeException(s"Cannot read config file, errors: \n ${error.toList.mkString("\n")}")
      }
    }
    conf
  }
}
