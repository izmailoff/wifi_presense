package com.wp.boot

import akka.actor.{Actor, ActorSystem, Props}
import akka.kernel.Bootable

import scala.sys.process.Process

case object Start

class HelloActor extends Actor {
  val worldActor = context.actorOf(Props[WorldActor])

  def receive = {
    case Start => worldActor ! "Hello"
    case message: String =>
      println(s"Received message [$message].")
  }
}

class WorldActor extends Actor {
  def receive = {
    case message: String => sender() ! (message.toUpperCase + " world!")
  }
}

class Boot extends Bootable {
  val system = ActorSystem("wifi_presense_kernel")

  lazy val eventConsumer = system.actorOf(Props[HelloActor])

  def startup = {
    eventConsumer ! Start
  }

  def shutdown = {
    system.terminate()
  }


  //val cmd = //"ls"
    //"""stdbuf -oL tshark -i wlp3s0 -I -f 'broadcast' -Y 'wlan.fc.type == 0 && wlan.fc.subtype == 4' -T fields -e frame.time_epoch -e wlan.sa -e radiotap.dbm_antsignal"""
  //val tsharkCmd = """/usr/sbin/tshark -i wlp3s0 -I -f 'broadcast' -Y 'wlan.fc.type == 0 && wlan.fc.subtype == 4' -T fields -e frame.time_epoch -e wlan.sa -e radiotap.dbm_antsignal"""
  val cmdArgs = Seq("/usr/sbin/tshark", "-iwlp3s0", "-I",
  "-fbroadcast",  "-Tfields", "-eframe.time_epoch", //"-Y'wlan.fc.type == 0 && wlan.fc.subtype == 4'",
  "-ewlan.sa", "-eradiotap.dbm_antsignal") //"stdbuf", "-oL",
  println(cmdArgs)
  val pb = Process(cmdArgs)
  println(pb.toString)
  //Thread.sleep(20000)
  //linesStream foreach (line => eventConsumer ! line)

  import scala.sys.process.ProcessIO
  val pio = new ProcessIO(_ => (),
    stdout => scala.io.Source.fromInputStream(stdout).getLines.foreach(line => eventConsumer ! line),
    stderr => scala.io.Source.fromInputStream(stderr).getLines.foreach(line => eventConsumer ! "ERR:" + line))
  pb.run(pio)

  println("FINISHED!!!!!!!!!!!!")
}


