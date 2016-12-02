package com.gilesc.registration

import com.gilesc.dataaccess.mysql.repository.SlickUserRepository
import com.gilesc.dataaccess.{DatabaseEnv, UserRepository}
import com.typesafe.config.{Config, ConfigFactory}

case class AuthenticationService()
case class EmailService()
case class RoleRepository()
case class LoginAttemptsRepository()

object RegistrationEnv {
  def apply(
     config: Config = ConfigFactory.load(),
     auth: AuthenticationService = AuthenticationService(),
     email: EmailService = EmailService(),
     usr: UserRepository = SlickUserRepository,
     role: RoleRepository = RoleRepository(),
     login: LoginAttemptsRepository = LoginAttemptsRepository()
   ): RegistrationEnv = {

    val svc = RegistrationServiceEnv(auth, email)
    val db = RegistrationDatabaseEnv(usr, role, login, config.getConfig("regenv"))

    RegistrationEnv(svc, db)
  }
}

case class RegistrationEnv(
  services: RegistrationServiceEnv,
  database: RegistrationDatabaseEnv)

case class RegistrationServiceEnv(
  auth: AuthenticationService,
  email: EmailService)

case class RegistrationDatabaseEnv(
    users: UserRepository,
    roles: RoleRepository,
    loginAttempts: LoginAttemptsRepository,
    config: Config) extends DatabaseEnv
