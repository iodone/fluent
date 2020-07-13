package app

/**
  * Created by iodone on {19-11-13}.
  */

import akka.actor.ActorSystem
import app.demo.delivery.http.HttpDelivery
import common.Config
import common.db.DatabaseMigration
import core.Fluent
import kamon.Kamon

object Demo {

  def main(args: Array[String]): Unit = {
    Kamon.init()

    val actorSystem = ActorSystem("Demo-App")

    val conf = Config.appConfig
    val host = conf.fluent.http.host
    val port = conf.fluent.http.port

    val fluent = new Fluent(host, port, actorSystem)
    import fluent._

    fluent.startHttpServer(HttpDelivery().handle)
  }
}

object DbMigrition {
  def main(args: Array[String]): Unit = {
    val migration = DatabaseMigration(Config.appConfig.fluent.databases.mysql)
    migration.clean
    migration.migrate
  }
}
