package com.gilesc.dataaccess.mysql.repository

import java.sql.Timestamp

import com.gilesc.dataaccess.Tables._
import com.gilesc.dataaccess.UserRepository
import com.gilesc.dataaccess.mysql.SlickDatabaseProfile
import cats.data.{Kleisli, ReaderT}
import com.gilesc.authentication.HashedPassword
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

object SlickUserRepository extends UserRepository {
  import com.gilesc.user._
  import com.gilesc.dataaccess._
  import com.gilesc.dataaccess.role._
  import com.gilesc.dataaccess.Tables._

  type DuplicateKey = MySQLIntegrityConstraintViolationException

  def find(findBy: FindBy): ReaderT[Future, DatabaseEnv, Option[User]] = Kleisli { db =>
    findBy match {
      case FindById(id) => find(id)(db)
      case FindByEmail(email) => find(email)(db)
    }
  }

  def save(context: SaveContext): ReaderT[Future, DatabaseEnv, SaveResult] = Kleisli { env =>
    val database = SlickDatabaseProfile(env.config)
    import database._
    import database.profile.api._

    val usersInsertQuery = Users returning Users.map(_.id) into ((user, id) => user.copy(id = id))
    val ts = Timestamp.valueOf(java.time.OffsetDateTime.now().toLocalDateTime)
    val action = usersInsertQuery += UsersRow(0,
      context.username.value,
      context.email.value,
      context.pass.hash,
      context.pass.salt,
      ts, ts, None)
    val CustomerRole = Customer.id

    execute(action.asTry).map {
        case scala.util.Success(r) =>
          execute(UserRoles += UserRolesRow(r.id, CustomerRole.value, ts))
          Success(usersRowToUser(r))

        case scala.util.Failure(e) => e match {
          case ex: DuplicateKey if ex.getMessage.contains("username") =>
            DuplicateUsernameError

          case ex: DuplicateKey if ex.getMessage.contains("email") =>
            DuplicateEmailError

          case ex =>
            UnknownError
        }
    }
  }

  def find(id: UserId): ReaderT[Future, DatabaseEnv, Option[User]] =
    Kleisli { env =>
      val database = SlickDatabaseProfile(env.config)
      import database._
      import database.profile.api._

      val query = Users.filter(_.id === id.value).take(1).result

      execute(query) map (_.headOption) map {
        case Some(row) => Some(usersRowToUser(row))
        case None => None
      }
  }

  def find(email: Email): ReaderT[Future, DatabaseEnv, Option[User]] = Kleisli { env =>
    val database = SlickDatabaseProfile(env.config)
    import database._
    import database.profile.api._

    execute(Users.filter(_.email === email.value).take(1).result) map (_.headOption) map {
      case Some(row) => Some(usersRowToUser(row))
      case None => None
    }
  }

  private[this] def usersRowToUser(row: UsersRow): User =
    User(UserId(row.id),
      Username(row.username),
      Email(row.email),
      HashedPassword(row.passwordHash, row.passwordSalt))
}

