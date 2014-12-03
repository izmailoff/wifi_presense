package com.wp.service.consumer

import akka.actor.ActorSystem
import com.wp.messages.EventMessages.ClientPacket

/**
 * Interface to external event consumer. All aggregated events will be sent to it.
 * It's up to the client to implement it and it will most likely persist events to DB, file, etc.
 */
abstract class EventConsumer {//(globalConfig: ActorSystem) {

  /**
   * Callback for exporting events to this consumer. It has to be implemented in non-blocking way so that
   * the main app will not get affected by external processing of the requests.
   * @param event
   */
  def process(event: ClientPacket): Unit

}
