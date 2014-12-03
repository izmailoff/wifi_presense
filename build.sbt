import NativePackagerKeys._
     
name := "wifi_presense"

version := "0.1"
     
scalaVersion := "2.10.4"

packageArchetype.akka_application
     
mainClass in Compile := Some("com.wp.service.boot.Boot")
     
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-kernel" % "2.4-SNAPSHOT",
  "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT",
  "com.github.nscala-time" %% "nscala-time" % "1.6.0"
)

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"




