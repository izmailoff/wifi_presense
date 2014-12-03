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

  var consumer: EventConsumer = null // FIXME: better (immutable) way of doing it?

  override def preStart(): Unit = {
    val clazz = Class.forName(Consumer.consumerClass).asInstanceOf[Class[_ <: EventConsumer]]
    //clazz.newInstance()
    //consumer = clazz.getConstructor(ActorSystem.getClass).newInstance(globalSystem)
    //classOf[C].getConstructor(classOf[String]).newInstance("string")
    consumer = clazz.newInstance()
  }

  def receive = {
    case event@ClientPacket(time, addr, signal) =>
      val eventTime = new DateTime(time)
      info(s"EVENT: [$eventTime] -> [$addr] -> [$signal].")
      consumer.process(event)
      // TODO: needs to aggregate events and ship them sowhere: file, DB, REST, socket, etc.
  }

}
