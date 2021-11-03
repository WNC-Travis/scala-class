import sbt._

object Dependencies {
  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.9"

  val postgres: Seq[ModuleID] = {
    val slickVersion = "3.3.3"
    Seq(
      "org.postgresql" % "postgresql" % "42.2.18",
      "org.slf4j" % "slf4j-api" % "2.0.0-alpha5",
      "org.slf4j" % "slf4j-simple" % "2.0.0-alpha5",
      "com.github.tminglei" %% "slick-pg" % "0.19.4",
      "com.github.tminglei" %% "slick-pg_json4s" % "0.19.4",
      "com.typesafe.slick" %% "slick" % slickVersion,
      "com.typesafe.slick" %% "slick-hikaricp" % slickVersion
    )
  }
}
