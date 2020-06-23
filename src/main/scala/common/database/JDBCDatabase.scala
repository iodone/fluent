package common.database

/**
  * Created by iodone on {18-10-29}.
  */

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import common.Config
import common.DbConfig

class Database(config: DbConfig) {

  val datasource = {
    val hikariConfig = new HikariConfig()

    hikariConfig.setJdbcUrl(config.url)
    hikariConfig.setUsername(config.user)
    hikariConfig.setPassword(config.password)
    hikariConfig.setDriverClassName(config.driver)
    hikariConfig.setMaximumPoolSize(100)
    hikariConfig.setLeakDetectionThreshold(300000)

    new HikariDataSource(hikariConfig)
  }

}


object Database {

  lazy val mysqlDb = new Database(Config.appConfig.fluent.databases.mysql)

}
