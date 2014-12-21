package com.wp.service.producer

import java.io.InputStream

import akka.actor.{Actor, Props}
import com.wp.config.GlobalAppConfig.Application.Sniffer
import com.wp.messages.EventMessages.{ClientPacket, ClientPacketHelper}
import com.wp.utils.logging.AkkaLoggingHelper

import scala.sys.process.{Process, ProcessIO}
import scala.util.{Failure, Success}


// TODO: need to monitor if process exits and restart the actor

/**
 * Receives Wi-Fi events from sniffer library/tool and sends them over to the [[EventProcessor]]
 */
class EventReceiver extends Actor with AkkaLoggingHelper {
  override val globalSystem = context.system

  import log._

  val eventProcessor =  context.actorOf(Props[EventProcessor])

  override def preStart(): Unit = {
    info(s"Starting Wi-Fi sniffer with these cmd args: [${Sniffer.cmd.mkString(" ")}].")
    val sniffProcessBuilder = Process(Sniffer.cmd)
    val sniffProcessIOHandler = new ProcessIO(_ => (), stdOutHandler, errOutHandler)
    val sniffProcess = sniffProcessBuilder.run(sniffProcessIOHandler)
    context.system.registerOnTermination(sniffProcess.destroy())
  }

  private def stdOutHandler: InputStream => Unit =
    stdOut => scala.io.Source.fromInputStream(stdOut).getLines.foreach { line =>
      ClientPacketHelper(line) match {
        case Success(event) => self ! event
        case Failure(err) => warning(err.getMessage)
      }
    }

  private def errOutHandler: InputStream => Unit =
    stdErr => scala.io.Source.fromInputStream(stdErr).getLines.foreach { line =>
      error(s"Sniffer produced non-fatal error: [$line].")
    }

  def receive = {
    case event: ClientPacket => eventProcessor ! event
  }
}
