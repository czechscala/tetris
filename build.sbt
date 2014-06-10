name := "tetris"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"
 
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq (
  "com.typesafe.akka" %% "akka-actor" % "2.3.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.3" % "test",
  "org.scalatest" %% "scalatest" % "2.2.0" % "test"
)
