package com.gilesc

class UniqueIdentifierSpec extends UnitSpec with UniqueIdentifier {
  "a unique identifier" should {
    "give a unique ID when asked" in {
      val uuid = generateUUID()
    }
  }
}
