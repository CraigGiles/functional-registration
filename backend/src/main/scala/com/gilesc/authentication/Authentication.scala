package com.gilesc.authentication

import cats.data.{Kleisli, ReaderT}
import com.gilesc.user._

import com.gilesc.dataaccess.login.LoginAttempt
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Authentication {
  val login: AuthenticationInfo =>
    ReaderT[Future, AuthenticationEnv, AuthResult] = { info =>
      Kleisli { env =>
         import env.database._
         import env.services._

        users.find(FindByEmail(info.email)).local[AuthenticationEnv](_.database).run(env) map {
          case None => UserNotFoundError
          case Some(user) =>
            val auth = if (password.verify(info.password, user.password)) {
              AuthSuccess(user)
            }
            else InvalidCredentialsError
            loginAttempt.record(LoginAttempt(user.id, auth == AuthSuccess(user))).run(env.database)

            auth
        }
      }
    }

  val logout: UserId => ReaderT[Future, AuthenticationEnv, Boolean] = { id =>
    Kleisli { env =>
      // TODO: implement
      // env.services.session.invalidate(id)
      Future(true)
    }
  }
}

trait PasswordHashing {
  def gensalt(): Salt
  def hash(raw: RawPassword): HashedPassword
  def hash(raw: RawPassword, salt: () => Salt): HashedPassword
  def verify(plain: RawPassword, hashed: HashedPassword): Boolean
}

object bcrypt extends PasswordHashing {
  import org.mindrot.jbcrypt.BCrypt

  override def gensalt(): Salt = BCrypt.gensalt()
  override def hash(raw: RawPassword): HashedPassword = hash(raw, gensalt)
  override def hash(raw: RawPassword, salt: () => Salt): HashedPassword = {
    val s = salt()

    HashedPassword(BCrypt.hashpw(raw.value, s), s)
  }

  override def verify(plain: RawPassword, hashed: HashedPassword): Boolean =
    BCrypt.checkpw(plain.value, hashed.hash)
}

