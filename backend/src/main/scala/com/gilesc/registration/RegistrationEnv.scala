package com.gilesc.registration

import com.gilesc.dataaccess.mysql.repository.SlickUserRepository
import com.gilesc.dataaccess.mysql.repository.SlickLoginAttemptsRepository
import com.gilesc.dataaccess.{DatabaseEnv, UserRepository, LoginAttemptsRepository}
import com.gilesc.authentication.bcrypt
import com.gilesc.authentication.PasswordHashing
import com.typesafe.config.{Config, ConfigFactory}

case class EmailService()
case class RoleRepository()
case class AuthenticationService()

object RegistrationEnv {
  def apply(
     config: Config = ConfigFactory.load(),
     auth: AuthenticationService = AuthenticationService(),
     passwords: PasswordHashing = bcrypt,
     email: EmailService = EmailService(),
     usr: UserRepository = SlickUserRepository,
     role: RoleRepository = RoleRepository(),
     login: LoginAttemptsRepository = SlickLoginAttemptsRepository
   ): RegistrationEnv = {

    val svc = RegistrationServiceEnv(auth, passwords, email)
    val db = RegistrationDatabaseEnv(usr, role, login, config.getConfig("regapp"))

    RegistrationEnv(svc, db)
  }
}

case class RegistrationEnv(
  services: RegistrationServiceEnv,
  database: RegistrationDatabaseEnv)

case class RegistrationServiceEnv(
  auth: AuthenticationService,
  passwords: PasswordHashing,
  email: EmailService)

case class RegistrationDatabaseEnv(
    users: UserRepository,
    roles: RoleRepository,
    loginAttempts: LoginAttemptsRepository,
    config: Config) extends DatabaseEnv
