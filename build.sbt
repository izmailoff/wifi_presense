
name := "wifi_presense"

version := "0.1"
     
scalaVersion := "2.11.7"

javacOptions ++= Seq("-target", "1.8")

scalacOptions ++= Seq(
  "-feature",
  "-language:postfixOps",
  "-unchecked",
  "-deprecation"
//  "-Xfatal-warnings",
//  "-Xlint",
//  "-Xfuture",
//  "-Ywarn-unused-import"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.12",
  "com.github.nscala-time" %% "nscala-time" % "2.4.0",
  "io.spray" %% "spray-client" % "1.3.3",
  "io.spray" %%  "spray-json" % "1.3.2",
  "com.pi4j" % "pi4j-core" % "1.0" % "compile",
  "com.pi4j" % "pi4j-device" % "1.0" % "compile",
  "com.sandinh" % "paho-akka_2.11" % "1.1.1",
  "org.java-websocket" % "Java-WebSocket" % "1.3.0"
)

resolvers ++= Seq(
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
  "Paho Eclipse Repository" at "https://repo.eclipse.org/content/repositories/paho-releases/"
)

