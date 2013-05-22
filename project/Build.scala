import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtStartScript

object Build extends sbt.Build {
  import Dependencies._

  lazy val photoProject = Project("photo2-spray", file("."))
    .settings(SbtStartScript.startScriptForClassesSettings: _*)
    .settings(
      organization  := "net.rznc",
      version       := "1.0",
      scalaVersion  := "2.10.1",
      scalacOptions := Seq("-deprecation", "-encoding", "utf8"),
      resolvers     ++= resolutionRepos,
      libraryDependencies ++= Seq(
        akkaActor % "compile",
        sprayCan % "compile",
        sprayRouting % "compile",

        akkaSlf4j % "compile",
        slf4j % "compile",
        logback % "compile",

        specs2 % "test",
        sprayTestkit % "test"
      )
      // unmanagedResourceDirectories in Compile <++= baseDirectory {
      //  base => Seq( base / "src/main/webapp" )
      // }
    )
}


object Dependencies {
  val resolutionRepos = Seq(
    "Spray Repository" at "http://repo.spray.io"
  )

  object V {
    val akka      = "2.1.2"
    val spray     = "1.1-M7"
    val specs2    = "1.14"
    val slf4j     = "1.7.5"
    val logback   = "1.0.12"
  }

  lazy val akkaActor    = "com.typesafe.akka"      %% "akka-actor"      % V.akka
  lazy val sprayCan     = "io.spray"               %  "spray-can"       % V.spray
  lazy val sprayRouting = "io.spray"               %  "spray-routing"   % V.spray

  lazy val specs2       = "org.specs2"             %% "specs2"          % V.specs2
  lazy val sprayTestkit = "io.spray"               %  "spray-testkit"   % V.spray

  lazy val akkaSlf4j    = "com.typesafe.akka"      %% "akka-slf4j"      % V.akka
  lazy val logback      = "ch.qos.logback"         %  "logback-classic" % V.logback
  lazy val slf4j        = "org.slf4j"              %  "slf4j-api"       % V.slf4j
}

