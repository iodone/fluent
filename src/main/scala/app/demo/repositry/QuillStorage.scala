package app.demo.repositry

import common.Config
import common.db.DatabaseConnector
import core.Storage
import io.getquill.{MysqlJdbcContext, SnakeCase}

/**
  * Created by iodone on {19-11-26}.
  */

private[repositry] trait QuillStorage extends Storage {

  val databaseConnector: DatabaseConnector = DatabaseConnector.getOrCreate(Config.conf.fluent.database)
  val dbContext = new MysqlJdbcContext(SnakeCase, databaseConnector.datasource)

}
