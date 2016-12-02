package com.gilesc.dataaccess.mysql

import slick.driver.MySQLDriver

/**
  * A mysql driver with extended support.
  */
trait MySqlDatabaseDriver extends MySQLDriver {

  object MyAPI extends API {
    // TODO: any specific overrides that i would need to do, do them here
  }

  override val api = MyAPI
}

object MySqlDatabaseDriver extends MySqlDatabaseDriver


