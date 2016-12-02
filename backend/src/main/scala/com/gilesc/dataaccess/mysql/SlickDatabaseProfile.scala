package com.gilesc.dataaccess.mysql

import com.gilesc.dataaccess.DatabaseProfile
import com.typesafe.config.Config
import slick.driver.{H2Driver, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

object SlickDatabaseProfile {
  def apply(config: Config)(implicit ec: ExecutionContext): SlickDatabaseProfile = {
    val profile = config.getString("profile") match {
      case "mysql" => MySqlDatabaseDriver
      // case _ => H2Driver
      case _ => MySqlDatabaseDriver
    }

    new SlickDatabaseProfile(profile, config)
  }
}

class SlickDatabaseProfile(val profile: JdbcProfile, config: Config)
    (implicit val ec: ExecutionContext) extends DatabaseProfile {

  import profile.api._

  val db = Database.forConfig("database", config)
  def execute[T](action: DBIO[T]): Future[T] = db.run(action)
}
