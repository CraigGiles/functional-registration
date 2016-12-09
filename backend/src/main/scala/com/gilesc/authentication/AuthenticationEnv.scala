package com.gilesc.authentication

import com.gilesc.dataaccess.mysql.repository.SlickUserRepository
import com.gilesc.dataaccess.mysql.repository.SlickLoginAttemptsRepository
import com.gilesc.dataaccess.{DatabaseEnv, UserRepository, LoginAttemptsRepository}
import com.typesafe.config.{Config, ConfigFactory}

object AuthenticationEnv {
  def apply(
     config: Config = ConfigFactory.load(),
     usr: UserRepository = SlickUserRepository,
     loginAttempt: LoginAttemptsRepository = SlickLoginAttemptsRepository,
     password: PasswordHashing = bcrypt
   ): AuthenticationEnv = {

    val svc = AuthenticationServiceEnv(password, loginAttempt)
    val db = AuthenticationDatabaseEnv(usr, config.getConfig("regapp"))

    AuthenticationEnv(svc, db)
  }
}

case class AuthenticationEnv(
  services: AuthenticationServiceEnv,
  database: AuthenticationDatabaseEnv)

case class AuthenticationServiceEnv(
  password: PasswordHashing,
  loginAttempt: LoginAttemptsRepository)

case class AuthenticationDatabaseEnv(
    users: UserRepository,
    config: Config) extends DatabaseEnv
