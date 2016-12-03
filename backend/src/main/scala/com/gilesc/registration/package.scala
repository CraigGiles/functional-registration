package com.gilesc

package object registration {
  import com.gilesc.user._
  import com.gilesc.authentication._

  case class RegistrationContext(username: Username, email: Email, password: RawPassword)

  sealed trait RegistrationResult
  case class RegistrationSuccess(user: User) extends RegistrationResult
  case object InvalidUsernameError extends RegistrationResult
  case object InvalidEmailError extends RegistrationResult
}
