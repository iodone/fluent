package common.db

/**
  * Created by iodone on {18-10-29}.
  */

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import common.DatabaseConfig

class DatabaseConnector(config: DatabaseConfig) {

  val datasource = {
    val hikariConfig = new HikariConfig()
    hikariConfig.setJdbcUrl(config.jdbcUrl)
    hikariConfig.setUsername(config.username)
    hikariConfig.setPassword(config.password)
    hikariConfig.setDriverClassName("com.mysql.jdbc.Driver")

    new HikariDataSource(hikariConfig)
  }


}

object DatabaseConnector {
  private var connector = null.asInstanceOf[DatabaseConnector]

  def getOrCreate(config: DatabaseConfig): DatabaseConnector = this.synchronized {
    if (connector == null) {
      connector = newConnector(config)
    }
    connector
  }

  def newConnector(config: DatabaseConfig) = {
    new DatabaseConnector(config)
  }

}
