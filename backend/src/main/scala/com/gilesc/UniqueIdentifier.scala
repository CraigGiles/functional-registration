package com.gilesc

import java.util.UUID
case class UniqueID(value: UUID)

object UniqueID {
  def fromString(value: String): UniqueID = UniqueID(UUID.fromString(value))
}

trait UniqueIdentifier {
  val generateUUID: () => UniqueID = { () =>
    UniqueID(java.util.UUID.randomUUID)
  }
}
