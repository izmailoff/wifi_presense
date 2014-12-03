package com.wp.service.consumer

import akka.actor.ActorSystem
import com.wp.messages.EventMessages.ClientPacket
import com.wp.utils.logging.AkkaLoggingHelper

class StdOutEventConsumer //(override val globalSystem: ActorSystem)
  extends EventConsumer {  //(globalSystem)
  //with AkkaLoggingHelper {

//  import log._

  override def process(event: ClientPacket): Unit =
    println(event)
    //info(event.toString)

}
