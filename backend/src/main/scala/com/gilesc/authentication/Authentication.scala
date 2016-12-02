package com.gilesc.authentication

import cats.data.{Kleisli, ReaderT}
import com.gilesc.user._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Authentication {
  val login: AuthenticationInfo =>
    ReaderT[Future, AuthenticationEnv, AuthResult] = { info =>
      Kleisli { env =>
         import env.database._
         import env.services._

         // TODO: when LoginAttemptsRepository gets built
         // env.services.loginAttempts.record(email)
        users.find(FindByEmail(info.email)).local[AuthenticationEnv](_.database).run(env) map {
          case None => UserNotFoundError
          case Some(user) =>
            if (password.verify(info.password, user.password)) {
              AuthSuccess(user)
            }
            else InvalidCredentialsError
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

