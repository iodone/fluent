package common.storage

import common.database.Database.mysqlDb
import core.Storage
import io.getquill.{MysqlJdbcContext, SnakeCase}

/**
  * Created by iodone on {19-11-26}.
  */

trait QuillStorage extends Storage {
  val dbContext = new MysqlJdbcContext(SnakeCase, mysqlDb.datasource)
}
