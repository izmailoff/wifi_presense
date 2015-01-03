package com.wp.service.producer

import akka.actor.{ActorSystem, Actor}
import com.wp.messages.EventMessages.ClientPacket
import com.wp.service.consumer.EventConsumer
import com.wp.utils.logging.AkkaLoggingHelper
import org.joda.time.DateTime
import com.wp.config.GlobalAppConfig.Application._

/**
 * Processes all received events.
 */
class EventProcessor extends Actor with AkkaLoggingHelper {
  override val globalSystem = context.system

  import log._

  var consumers: List[EventConsumer] = List()

  override def preStart(): Unit = {
    val classes = Consumer.consumers.map(Class.forName(_).asInstanceOf[Class[_ <: EventConsumer]])
    consumers = classes.map(_.newInstance())
  }

  def receive = {
    case event@ClientPacket(time, addr, signal) =>
      val eventTime = new DateTime(time)
      debug(s"EVENT: [$eventTime] -> [$addr] -> [$signal].")
      consumers.foreach(_.process(event))
  }

}
