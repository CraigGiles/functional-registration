package com.gilesc.dataaccess.mysql

import java.util.Properties

import com.gilesc.dataaccess.DatabaseProfile
import com.typesafe.config.Config
import slick.driver.{H2Driver, JdbcProfile, MySQLDriver}

import scala.concurrent.{ExecutionContext, Future}

object SlickDatabaseProfile {
  def apply(config: Config)(implicit ec: ExecutionContext): SlickDatabaseProfile = {
    val profile = config.getString("database.profile") match {
      case "mysql" => MySQLDriver.profile
       case _ => H2Driver
    }

    new SlickDatabaseProfile(profile, config)
  }
}

class SlickDatabaseProfile(val profile: JdbcProfile, config: Config)
    (implicit val ec: ExecutionContext) extends DatabaseProfile {

  import profile.api._
  val properties = new Properties()
  properties.setProperty("connectionPool", "disabled")
  properties.setProperty("user", config.getString("database.user"))
  properties.setProperty("password", config.getString("database.password"))
  properties.setProperty("keepAliveConnection", "true")

  val dbConfig: profile.backend.DatabaseDef = profile.backend.Database.forURL(
    url = config.getString("database.url"),
    prop = properties
  )

  def execute[T](action: DBIO[T]): Future[T] = dbConfig.run(action)
}
