
name := "wifi_presense"

version := "0.1"
     
scalaVersion := "2.11.4"

javacOptions ++= Seq("-target", "1.7")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.8",
  "com.github.nscala-time" %% "nscala-time" % "1.6.0",
  "io.spray" %% "spray-client" % "1.3.2",
  "io.spray" %%  "spray-json" % "1.3.1",
  "com.pi4j" % "pi4j-core" % "0.0.5" % "compile",
  "com.pi4j" % "pi4j-device" % "0.0.5" % "compile",
  "com.sandinh" % "paho-akka_2.11" % "1.0.1"
)

resolvers ++= Seq(
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
  "Paho Eclipse Repository" at "https://repo.eclipse.org/content/repositories/paho-releases/")




