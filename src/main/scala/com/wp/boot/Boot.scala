package com.wp.boot

import akka.actor.{Actor, ActorSystem, Props}
import akka.kernel.Bootable
import com.wp.boot.utils.logging.AkkaLoggingHelper
import com.wp.config.GlobalAppConfig.Application._
import scala.sys.process.Process

case object Start

class HelloActor extends Actor with AkkaLoggingHelper {
  override val globalSystem = context.system

  import log._

  val worldActor = context.actorOf(Props[WorldActor])

  def receive = {
    case Start => worldActor ! "Hello"
    case message: String =>
      info(s"Received message [$message].")
  }
}

class WorldActor extends Actor with AkkaLoggingHelper {
  override val globalSystem = context.system

  import log._

  def receive = {
    case message: String => sender() ! (message.toUpperCase + " world!")
  }
}

class Boot extends Bootable with AkkaLoggingHelper {
  val system = ActorSystem("wifi_presense_kernel")

  override val globalSystem = system

  import log._

  lazy val eventConsumer = system.actorOf(Props[HelloActor])

  def startup = {
    eventConsumer ! Start
  }

  def shutdown = {
    system.terminate()
  }

  info(Sniffer.cmd.toString)
  val pb = Process(Sniffer.cmd)

  import scala.sys.process.ProcessIO

  val pio = new ProcessIO(_ => (),
    stdout => scala.io.Source.fromInputStream(stdout).getLines.foreach(line => eventConsumer ! line),
    stderr => scala.io.Source.fromInputStream(stderr).getLines.foreach(line => eventConsumer ! "ERR:" + line))
  pb.run(pio)

  info("APPLICATION STARTED")
}


