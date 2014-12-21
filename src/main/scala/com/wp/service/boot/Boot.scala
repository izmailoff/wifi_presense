package com.wp.service.boot

import akka.actor.{ActorSystem, Props}
import com.wp.service.producer.EventReceiver
import com.wp.utils.logging.AkkaLoggingHelper

/**
 * Main class that starts actor system and other actors.
 */
object Boot extends App with AkkaLoggingHelper {
  import log._

  val system = ActorSystem("wifi_presense_system")

  override val globalSystem = system

  val banner =
    """
      |__        ___       _____ _   ____
      |\ \      / (_)     |  ___(_) |  _ \ _ __ ___  ___  ___ _ __  ___  ___
      | \ \ /\ / /| |_____| |_  | | | |_) | '__/ _ \/ __|/ _ \ '_ \/ __|/ _ \
      |  \ V  V / | |_____|  _| | | |  __/| | |  __/\__ \  __/ | | \__ \  __/
      |   \_/\_/  |_|     |_|   |_| |_|   |_|  \___||___/\___|_| |_|___/\___|
      |
    """.stripMargin

  info(banner)

  system.actorOf(Props[EventReceiver])

  info("APPLICATION STARTED!")
}


