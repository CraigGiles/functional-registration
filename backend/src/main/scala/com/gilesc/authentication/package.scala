package com.gilesc

import com.gilesc.user._

package object authentication {
  // password information
  type Salt = String
  case class RawPassword(value: String) extends AnyVal
  case class HashedPassword(hash: String, salt: String)

  // Authentication
  case class AuthenticationInfo(email: Email, password: RawPassword)

  sealed trait AuthResult
  case class AuthSuccess(user: User) extends AuthResult
  case object UserNotFoundError extends AuthResult
  case object InvalidCredentialsError extends AuthResult
}

