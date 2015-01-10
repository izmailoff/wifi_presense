package com.wp.messages

import java.util.regex.Pattern

import com.wp.config.GlobalAppConfig.Application._

import scala.util.control.NoStackTrace
import scala.util.{Failure, Try}

object EventMessages {

  //  1417578677.739100000|00:26:75:d8:00:68|-34,-37,-34
  //frame.time_epoch   wlan.sa   radiotap.dbm_antsignal
  // TODO: more typesafe types for address, etc

  /*
  radiotap.data_past_header 	Expert Info 	Label 	1.12.0 to 1.12.2
radiotap.datarate 	Data rate (Mb/s) 	Floating point (single-precision) 	1.0.0 to 1.12.2

// RF noise power at the antenna, decibel difference from an arbitrary, fixed reference. This field contains a single unsigned 8-bit value.
radiotap.db_antnoise 	SSI Noise 	Unsigned integer, 4 bytes 	1.0.0 to 1.12.2

// RF signal power at the antenna, decibel difference from an arbitrary, fixed reference. This field contains a single unsigned 8-bit value.
radiotap.db_antsignal 	SSI Signal 	Unsigned integer, 4 bytes 	1.0.0 to 1.12.2

// Transmit power expressed as unitless distance from max power set at factory calibration. 0 is max power. Monotonically nondecreasing with lower power levels.
???

// Transmit power expressed as decibel distance from max power set at factory calibration. 0 is max power. Monotonically nondecreasing with lower power levels.
radiotap.db_txattenuation 	Transmit attenuation (dB) 	Unsigned integer, 2 bytes 	1.0.0 to 1.12.2

// RF noise power at the antenna. This field contains a single signed 8-bit value, which indicates the RF signal power at the antenna, in decibels difference from 1mW.
radiotap.dbm_antnoise 	SSI Noise 	Signed integer, 4 bytes 	1.6.0 to 1.12.2

// RF signal power at the antenna. This field contains a single signed 8-bit value, which indicates the RF signal power at the antenna, in decibels difference from 1mW.
radiotap.dbm_antsignal 	SSI Signal 	Signed integer, 4 bytes 	1.0.0 to 1.12.2

// Transmit power expressed as dBm (decibels from a 1 milliwatt reference). This is the absolute power level measured at the antenna port.
???

radiotap.fcs 	802.11 FCS 	Unsigned integer, 4 bytes 	1.0.0 to 1.12.2
radiotap.fcs_bad 	Bad FCS 	Boolean 	1.0.0 to 1.12.2

radiotap.flags.badfcs 	Bad FCS 	Boolean 	1.0.0 to 1.12.2
radiotap.flags.cfp 	CFP 	Boolean 	1.0.0 to 1.12.2
radiotap.flags.datapad 	Data Pad 	Boolean 	1.0.0 to 1.12.2
radiotap.flags.fcs 	FCS at end 	Boolean 	1.0.0 to 1.12.2

??? ----------------------------
radiotap.present 	Present flags 	Label 	1.0.0 to 1.12.2
radiotap.present.ampdu 	A-MPDU Status 	Boolean 	1.10.0 to 1.12.2
radiotap.present.antenna 	Antenna 	Boolean 	1.0.0 to 1.12.2
radiotap.present.channel 	Channel 	Boolean 	1.0.0 to 1.12.2
radiotap.present.db_antnoise 	dB Antenna Noise 	Boolean 	1.0.0 to 1.12.2
radiotap.present.db_antsignal 	dB Antenna Signal 	Boolean 	1.0.0 to 1.12.2
radiotap.present.db_tx_attenuation 	dB TX Attenuation 	Boolean 	1.0.0 to 1.12.2
radiotap.present.dbm_antnoise 	dBm Antenna Noise 	Boolean 	1.0.0 to 1.12.2
radiotap.present.dbm_antsignal 	dBm Antenna Signal 	Boolean 	1.0.0 to 1.12.2
radiotap.present.dbm_tx_attenuation 	DBM TX Attenuation 	Boolean 	1.0.0 to 1.4.11, 1.6.0 to 1.6.5
radiotap.present.dbm_tx_power 	dBm TX Power 	Boolean 	1.4.12 to 1.4.15, 1.6.6 to 1.12.2
   */
  case class SignalStrength()

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
