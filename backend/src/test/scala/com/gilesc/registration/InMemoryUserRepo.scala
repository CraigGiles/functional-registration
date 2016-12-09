package com.gilesc.registration

import cats.data.{Kleisli, ReaderT}
import com.gilesc.authentication.HashedPassword
import com.gilesc.authentication.RawPassword
import com.gilesc.user._
import com.gilesc.dataaccess.{DatabaseEnv, UserRepository}
import com.gilesc.UniqueID
import com.gilesc.UniqueIdentifier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TestUser extends UniqueIdentifier {
  val plainPassword = "mypassword"
  val rawPassword = RawPassword(plainPassword)
  val salt = """$2a$10$fztyjhMHpnXW4wn4/bnuB."""
  val hashedRaw = """$2a$10$fztyjhMHpnXW4wn4/bnuB.b.abfUR74lUrQa/tHcc.8AT1JGFbTWu"""

  val generatedId = generateUUID()
  val testId = UserId(generatedId)
  val testUsername = Username("testuser")
  val testEmail = Email("test@email.com")
  val testPassword = HashedPassword(hashedRaw, salt)

  val testUser = User(testId, testUsername, testEmail, testPassword)
}

object InMemoryUserRepo extends UserRepository with UniqueIdentifier {
  import TestUser._
  var users = List.empty[User]

  override def find(findBy: FindBy): ReaderT[Future, DatabaseEnv, Option[User]] = Kleisli { c =>
      Future { findBy match {
        case FindByEmail(email) if email == testEmail => Option(testUser)
        case FindByEmail(email) => users.find(_.email == email)
        case FindById(id) => users.find(_.id == id)
      }
    }
  }

  override def save(ctx: SaveContext): ReaderT[Future, DatabaseEnv, SaveResult] = Kleisli { c =>
    val user = User(testId, ctx.username, ctx.email, ctx.pass)

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

