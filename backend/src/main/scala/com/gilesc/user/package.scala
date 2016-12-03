package com.gilesc

import com.gilesc.authentication._

package object user {
  case class UserId(value: Long) extends AnyVal
  case class Username(value: String) extends AnyVal
  case class Email(value: String) extends AnyVal

  case class User(id: UserId, username: Username, email: Email, password: HashedPassword)

  // ADT to support find
  sealed trait FindBy
  final case class FindByEmail(value: Email) extends FindBy
  final case class FindById(value: UserId) extends FindBy

  // Classes and ADT to support save
  final case class SaveContext(username: Username, email: Email, pass: HashedPassword)

  sealed trait SaveResult
  final case class Success(user: User) extends SaveResult
  case object DuplicateUsernameError extends SaveResult
  case object DuplicateEmailError extends SaveResult
  case object UnknownError extends SaveResult
}
