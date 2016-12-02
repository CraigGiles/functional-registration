package com.gilesc

package object registration {
  import com.gilesc.user._

  case class RegistrationContext(username: Username, email: Email)

  sealed trait RegistrationResult
  case class RegistrationSuccess(user: User) extends RegistrationResult
  case object InvalidUsernameError extends RegistrationResult
  case object InvalidEmailError extends RegistrationResult
}
