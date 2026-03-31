import sbt._

object Dependencies {
  private lazy val scalacticVersion = "3.2.19"
  private lazy val http4sVersion = "0.23.33"
  private lazy val circeVersion = "0.14.15"
  private lazy val catsParseVersion = "1.1.0"
  private lazy val logbackVersion = "1.5.32"
  private lazy val log4CatsVersion = "2.8.0"

  lazy val all = Seq(
    "org.scalactic" %% "scalactic" % scalacticVersion,
    "org.http4s" %% "http4s-ember-server" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.typelevel" %% "cats-parse" % catsParseVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "org.typelevel" %% "log4cats-core" % log4CatsVersion,
    "org.typelevel" %% "log4cats-slf4j" % log4CatsVersion,
    "org.scalatest" %% "scalatest" % scalacticVersion % Test
  )
}
