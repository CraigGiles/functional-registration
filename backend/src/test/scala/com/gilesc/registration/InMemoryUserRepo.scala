package com.gilesc.registration

import cats.data.{Kleisli, ReaderT}
import com.gilesc.authentication.HashedPassword
import com.gilesc.user._
import com.gilesc.dataaccess.{DatabaseEnv, UserRepository}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object InMemoryUserRepo extends UserRepository {
  var users = List.empty[User]

  override def find(findBy: FindBy): ReaderT[Future, DatabaseEnv, Option[User]] = Kleisli { c =>
      Future { findBy match {
        case FindByEmail(email) => users.find(_.email == email)
        case FindById(id) => users.find(_.id == id)
      }
    }
  }

  override def save(ctx: SaveContext): ReaderT[Future, DatabaseEnv, SaveResult] = Kleisli { c =>
    val savecontext = SaveContext(ctx.username, ctx.email)
    val user = User(UserId(users.size.toLong + 1), ctx.username, ctx.email, HashedPassword("", ""))

    // TODO: wow this is ugly
    Future { users.exists(_.username == ctx.username) match {
      case true => DuplicateUsernameError
      case false =>
        users.exists(_.email == ctx.email) match {
          case true => DuplicateEmailError
          case false =>
            users = users :+ user
            Success(user)
        }

    }
    }
  }
}

