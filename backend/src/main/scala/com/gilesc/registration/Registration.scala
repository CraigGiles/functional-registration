package com.gilesc.registration

import cats.data.{Kleisli, ReaderT}
import com.gilesc.user._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Registration {

  val register: RegistrationContext =>
    ReaderT[Future, RegistrationEnv, RegistrationResult] = { ctx =>
      Kleisli { env =>
        import env.database.users._
        val saveCtx = SaveContext(ctx.username, ctx.email)

        save(saveCtx).local[RegistrationEnv](_.database).run(env) map {
          case Success(usr) => RegistrationSuccess(usr)
          case DuplicateEmailError => InvalidEmailError
          case DuplicateUsernameError => InvalidUsernameError
          case _ => InvalidUsernameError
        }
      }
    }
}
