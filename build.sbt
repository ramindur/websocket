import sbt.librarymanagement.CrossVersion
import sbt.{ThisBuild, url}


lazy val scala2 = "2.13.18"
lazy val scala38 = "3.8.2"
lazy val supportedScalaVersions = List(scala2, scala38)

lazy val commonSettings = Seq(
  scalaVersion := scala38,
  libraryDependencies ++= Dependencies.all
)

lazy val root = (project in file("."))
  .enablePlugins(
    ScalafmtPlugin
  )
  .settings(
    commonSettings,
    name := "websocket",
    scalacOptions ++= Scalac.options,
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2,13)) => Seq("-Ytasty-reader")
        case _ => Seq("-Yretain-trees")
      }
    },
    crossScalaVersions := supportedScalaVersions
  )

ThisBuild / versionScheme := Some("early-semver")

addCommandAlias("cleanTest", ";clean;scalafmt;test:scalafmt;test;")
addCommandAlias("cleanCoverage", ";clean;scalafmt;test:scalafmt;coverage;test;coverageReport;")