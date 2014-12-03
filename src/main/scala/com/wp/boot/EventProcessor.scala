package com.wp.boot

import akka.actor.Actor
import com.wp.messages.EventMessages.ClientPacket
import com.wp.utils.logging.AkkaLoggingHelper
import org.joda.time.DateTime

/**
 * Processes all received events.
 */
class EventProcessor extends Actor with AkkaLoggingHelper {
  override val globalSystem = context.system

  import log._

  def receive = {
    case event@ClientPacket(time, addr, signal) =>
      val eventTime = new DateTime(time)
      info(s"EVENT: [$eventTime] -> [$addr] -> [$signal].")
      // TODO: needs to aggregate events and ship them sowhere: file, DB, REST, socket, etc.
  }

}
