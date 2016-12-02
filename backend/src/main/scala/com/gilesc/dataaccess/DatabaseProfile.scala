package com.gilesc.dataaccess

import com.typesafe.config.Config

trait DatabaseEnv {
  val config: Config
}

trait DatabaseProfile
