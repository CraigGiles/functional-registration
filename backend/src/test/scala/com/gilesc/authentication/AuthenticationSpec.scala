package com.gilesc.authentication

import com.gilesc.UnitSpec
import com.gilesc.user._
import com.gilesc.authentication._
import com.gilesc.authentication.Authentication._
import com.gilesc.authentication.bcrypt._
import com.gilesc.registration.InMemoryUserRepo
import com.gilesc.registration.TestUser

import scala.concurrent.Await
import scala.concurrent.duration._

class AuthenticationSpec extends UnitSpec with TestUser {
  val env = AuthenticationEnv(usr = InMemoryUserRepo)

  "a password hashing algorithm" should {
    val plain = "mypassword"
    val raw = RawPassword(plain)
    val salt = """$2a$10$fztyjhMHpnXW4wn4/bnuB."""
    val hashed = """$2a$10$fztyjhMHpnXW4wn4/bnuB.b.abfUR74lUrQa/tHcc.8AT1JGFbTWu"""

    "hash a password if given a salt" in {
      def getsalt(): Salt = salt
      val raw = RawPassword(plain)
      val expected = HashedPassword(hashed, salt)

      hash(raw, getsalt) should be(expected)
    }

    "verify a password if given a HashedPasword" in {
      val hashedPw = HashedPassword(hashed, salt)

      verify(raw, hashedPw) should be(true)
    }
  }

  "A login system" should {
    "allow you to login given valid credentials" in {
      val info = AuthenticationInfo(testEmail, rawPassword)

      val future = login(info).run(env)
      val f = Await.result(future, 1 second)
      f should be(AuthSuccess(testUser))
    }

    "now allow you to login if invalid credentials are given" in {
      val info = AuthenticationInfo(testEmail, RawPassword("invalid"))

      val future = login(info).run(env)
      val f = Await.result(future, 1 second)
      f should be(InvalidCredentialsError)
    }
  }
}
