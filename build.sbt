name := "Projects"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
    "com.typesafe" % "config" % "1.3.1",
    "com.typesafe.akka" %% "akka-actor" % "2.5.2",
    "com.typesafe.akka" %% "akka-testkit" % "2.5.2" % "test",
    "com.typesafe.play" %% "play-json" % "2.6.0-RC2",
    // "org.scalactic" %% "scalactic" % "3.0.1",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

scalacOptions ++= Seq(
  "-deprecation",  // issue warning if we use any deprecated API features
  "-feature",  // tells the compiler to provide information about misused language features
  "-language:implicitConversions",  // eliminates the need to import implicit conversions for each usage
  "-Xlint",
  "-Ywarn-unused-import",
  "-Ywarn-dead-code"
)