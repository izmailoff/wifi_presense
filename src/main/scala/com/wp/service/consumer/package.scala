package com.wp.service

import com.wp.messages.EventMessages.ClientPacket

package object consumer {

  def toJsonStr(event: ClientPacket): String = {
    import event._
    s"""{"t":$curTimeMillis,"a":"$clientAddress","s":${signalStrength.head}}""" // FIXME: signal strength will be fixed
  }
}
