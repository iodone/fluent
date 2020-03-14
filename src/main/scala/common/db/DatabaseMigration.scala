package common.db

/**
  * Created by iodone on {18-11-1}.
  */
import org.flywaydb.core.Flyway
import common.DatabaseConfig

case class DatabaseMigration(config: DatabaseConfig) {

  private val flyway = Flyway.configure().dataSource(config.jdbcUrl, config.username, config.password).load()

  def migrate = flyway.migrate()

  def clean = flyway.clean()

  def undo = flyway.undo()

  def info = flyway.info()
}
