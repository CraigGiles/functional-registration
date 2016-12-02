import sbt._
import Keys._

object Dependencies {
  val scope = "test"

  lazy val core = Seq(
    config,
    cats,
    scalatest(scope),
    scalalogging,
    logback
  )

  lazy val flyway = Seq(
      flywayCore,
      mysqlConnectorJava
    )

  lazy val presentation = core ++ Seq()

  lazy val backend = core ++ Seq(
      bcrypt,
      slick,
      slickHikariCP,
      mysqlConnectorJava,
      h2database,
      slickCodegen
    )

  // Category Theory
  def cats = "org.typelevel" %% "cats" % "0.8.1"
  def scalatest(scope: String) = "org.scalatest" %% "scalatest" % "3.0.0" % scope
  def scalalogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0"
  def logback = "ch.qos.logback" % "logback-classic" % "1.1.2"

  // Security
  def bcrypt = "org.mindrot" % "jbcrypt" % "0.3m"

  // Database
  val slickVersion = "3.1.1"
  def flywayCore = "org.flywaydb" % "flyway-core" % "4.0"
  def slick = "com.typesafe.slick" %% "slick" % slickVersion
  def slickHikariCP = "com.typesafe.slick" %% "slick-hikaricp" % slickVersion
  def slickCodegen = "com.typesafe.slick" %% "slick-codegen" % slickVersion
  def h2database = "com.h2database"  %  "h2" % "1.4.192"

  // NOTE: plugins.sbt has the same version of mysql-connector-java.
  //       Please keep these two versions in sync.
  def mysqlConnectorJava = "mysql" % "mysql-connector-java" % "5.1.36"

  def config = "com.typesafe" % "config" % "1.3.1"
}
