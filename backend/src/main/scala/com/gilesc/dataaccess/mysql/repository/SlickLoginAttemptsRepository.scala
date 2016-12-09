package com.gilesc.dataaccess.mysql.repository

import scala.concurrent.Future
import cats.data.{Kleisli,ReaderT}
import com.gilesc.UniqueIdentifier
import com.gilesc.dataaccess.Tables._
import com.gilesc.dataaccess.LoginAttemptsRepository
import com.gilesc.dataaccess.DatabaseEnv
import com.gilesc.dataaccess.mysql.SlickDatabaseProfile
import java.sql.Timestamp
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object SlickLoginAttemptsRepository extends LoginAttemptsRepository with UniqueIdentifier {
  import com.gilesc.user._
  import com.gilesc.dataaccess.login._

  def record(attempt: LoginAttempt): ReaderT[Future, DatabaseEnv, Boolean] =
    Kleisli { env =>
      val database = SlickDatabaseProfile(env.config)
      import database._
      import database.profile.api._

      val action = LoginAttempts += LoginAttemptsRow(generateUUID().toString,
        attempt.userId.value.toString,
        attempt.success,
        Timestamp.valueOf(java.time.OffsetDateTime.now().toLocalDateTime))

      execute(action) map(x => x >= 1)
    }
}
