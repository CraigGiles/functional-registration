package com.gilesc.dataaccess

import scala.concurrent.Future
import com.gilesc.dataaccess.DatabaseEnv
import cats.data.ReaderT

object login {
  import com.gilesc.user._
  case class LoginAttempt(userId: UserId, success: Boolean)
}

trait LoginAttemptsRepository {
  import login._
  import com.gilesc.user._

  def record(attempt: LoginAttempt): ReaderT[Future, DatabaseEnv, Boolean]
}
