package com.gilesc.authentication

import com.gilesc.dataaccess.mysql.repository.SlickUserRepository
import com.gilesc.dataaccess.{DatabaseEnv, UserRepository}
import com.typesafe.config.{Config, ConfigFactory}

case class LoginAttemptsRepository()

object AuthenticationEnv {
  def apply(
     config: Config = ConfigFactory.load(),
     usr: UserRepository = SlickUserRepository,
     login: LoginAttemptsRepository = LoginAttemptsRepository(),
     password: PasswordHashing = bcrypt
   ): AuthenticationEnv = {

    val svc = AuthenticationServiceEnv(password)
    val db = AuthenticationDatabaseEnv(usr, config.getConfig("regenv"))

    AuthenticationEnv(svc, db)
  }
}

case class AuthenticationEnv(
  services: AuthenticationServiceEnv,
  database: AuthenticationDatabaseEnv)

case class AuthenticationServiceEnv(
  password: PasswordHashing)

case class AuthenticationDatabaseEnv(
    users: UserRepository,
    config: Config) extends DatabaseEnv
