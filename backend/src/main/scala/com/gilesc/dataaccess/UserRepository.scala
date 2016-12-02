package com.gilesc.dataaccess

import com.gilesc.user._
import cats.data.ReaderT
import scala.concurrent.Future

trait UserRepository {
  def find(findBy: FindBy): ReaderT[Future, DatabaseEnv, Option[User]]
  def save(context: SaveContext): ReaderT[Future, DatabaseEnv, SaveResult]
}
