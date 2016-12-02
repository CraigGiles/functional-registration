package com.gilesc

import org.scalatest.{Matchers, WordSpecLike}
import org.scalatest.concurrent._

class UnitSpec extends WordSpecLike
  with Matchers
  with StackModule
  with Futures
