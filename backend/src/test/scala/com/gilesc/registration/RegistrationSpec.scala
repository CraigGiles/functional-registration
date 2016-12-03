package com.gilesc.registration

import com.gilesc.UnitSpec
import com.gilesc.authentication._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random

class RegistrationSpec extends UnitSpec {
  import Registration._
  import com.gilesc.user._
  val rnd = Random
  val env = RegistrationEnv(usr = InMemoryUserRepo)

  def randomString(limit: Int): String =
    Random.alphanumeric take limit mkString("")

  "Registration System" should {
    "let me register a user" in {
      val username = Username("username" + randomString(20))
      val email = Email("email" + randomString(20) + ".com")
      val password = RawPassword("mypassword")

      val ctx = RegistrationContext(username, email, password)
      val f = Await.result(register(ctx).run(env), 1 second)

      verify(username, email, f) should be (true)
    }

    "not let me register two users with the same username" in {
      val username = Username("username" + randomString(20))
      val email = Email("email" + randomString(20) + ".com")
      val password = RawPassword("mypassword")

      val ctx = RegistrationContext(username, email, password)
      val f = Await.result(register(ctx).run(env), 1 second)

      verify(username, email, f) should be (true)

      val email2 = Email("email" + randomString(20) + ".com")
      val ctx2 = RegistrationContext(username, email2, password)
      val f2 = Await.result(register(ctx2).run(env), 1 second)
      f2 should be(InvalidUsernameError)
    }

    "not let me register two users with the same email" in {
      val username = Username("username" + randomString(20))
      val email = Email("email" + randomString(20) + ".com")
      val password = RawPassword("mypassword")

      val ctx = RegistrationContext(username, email, password)
      val f = Await.result(register(ctx).run(env), 1 second)

      verify(username, email, f) should be (true)

      val username2 = Username("username" + randomString(20))
      val ctx2 = RegistrationContext(username2, email, password)
      val f2 = Await.result(register(ctx2).run(env), 1 second)
      f2 should be(InvalidEmailError)
    }
  }

  def verify(username: Username, email: Email, f: RegistrationResult): Boolean = {
    f match {
      case RegistrationSuccess(user) =>
        user.username should be(username)
        user.email should be(email)
        true

      case x => false
    }
  }
}
