package com.wp.messages

import java.util.regex.Pattern

import com.wp.config.GlobalAppConfig.Application._

import scala.util.control.NoStackTrace
import scala.util.{Failure, Try}

object EventMessages {

  //  1417578677.739100000|00:26:75:d8:00:68|-34,-37,-34
  //frame.time_epoch   wlan.sa   radiotap.dbm_antsignal
  // TODO: more typesafe types for address, etc

  case class ClientPacket(curTimeMillis: Long, clientAddress: String, signalStrength: List[Int])

  object ClientPacketHelper {
    val separator = Pattern.quote(Sniffer.fieldSeparator)

    def apply(epoch: String, macAddress: String, signal: String): Try[ClientPacket] =
      Try {
        val curTimeMillis = (epoch.toDouble * 1000).toLong
        val signalStrength = signal.split(",").toList.map(_.toInt)
        ClientPacket(curTimeMillis, macAddress, signalStrength)
      }

    def apply(line: String): Try[ClientPacket] =
      line.split(separator).toList match {
        case epoch :: mac :: signal :: Nil =>
          apply(epoch, mac, signal)
        case _ =>
          Failure(new IllegalArgumentException(s"Invalid line format, got: [$line].") with NoStackTrace)
      }

  }

}
