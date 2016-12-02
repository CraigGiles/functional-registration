package com.gilesc.dataaccess

import com.gilesc.user._
import cats.data.ReaderT
import scala.concurrent.Future

object role {
  // TODO: Currently the only way to add roles is to do a database migration
  // final case class RoleContext(name: String, description: String)

  implicit val longToRoleId: Long => RoleId = id => RoleId(id)
  implicit val intToRoleId: Int => RoleId = id => RoleId(id)

  case class RoleId(value: Long) extends AnyVal
  sealed abstract class Role(val id: RoleId)
  case object Customer extends Role(1)
  case object MemberSupport extends Role(2)
}

trait UserRolesRepository {
  import role._

  // TODO: Currently the only way to add roles is to do a database migration
  // def insert(ctx: RoleContext): ReaderT[Future, DatabaseEnv, Role]
  def all(): ReaderT[Future, DatabaseEnv, List[Role]]
  def rolesForUser(user: User): ReaderT[Future, DatabaseEnv, List[Role]]
}

