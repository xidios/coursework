name := "coursework"

version := "1.0"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.2.24",
  "org.typelevel" %% "cats-effect" % "3.2.9",
  "org.http4s" %% "http4s-blaze-client" % "0.23.6",
  "org.http4s" %% "http4s-blaze-server" % "0.23.6",
  "org.http4s" %% "http4s-dsl" % "0.23.6",
  "org.http4s" %% "http4s-circe" % "0.23.6",
  "org.http4s" %% "http4s-core" % "0.23.6",
  "org.http4s" %% "http4s-client" % "0.23.6",
  "org.scalatest" %% "scalatest" % "3.2.9" % "test",
  "org.tpolecat" %% "doobie-core" % "1.0.0-M5",
  "org.tpolecat" %% "doobie-hikari" % "1.0.0-M5",
  "com.softwaremill.sttp.client3" %% "core" % "3.3.15",
  "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % "3.3.15",
  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1",
  "org.slf4j" % "slf4j-nop" % "1.7.32",
  "org.mockito" %% "mockito-scala" % "1.16.0" % Test

)
