package com.wp.service.consumer

import akka.actor.Props
import com.sandinh.paho.akka.MqttPubSub.{Publish, PSConfig}
import com.wp.config.GlobalAppConfig.Application.Consumers.MqttConsumer
import com.wp.messages.EventMessages.ClientPacket
import com.sandinh.paho.akka._
import com.wp.service.boot.Boot

class MqttConsumer extends EventConsumer {

  lazy val broker = Boot.system.actorOf(Props(classOf[MqttPubSub], PSConfig(
    brokerUrl = MqttConsumer.brokerUrl,
    userName = emptyToNull(MqttConsumer.userName),
    password = emptyToNull(MqttConsumer.password),
    stashTimeToLive = MqttConsumer.stashTimeToLive,
    stashCapacity = MqttConsumer.stashCapacity,
    reconnectDelayMin = MqttConsumer.reconnectDelayMin,
    reconnectDelayMax = MqttConsumer.reconnectDelayMax
  )))

  override def process(event: ClientPacket): Unit = {
    broker ! new Publish("wifipresense"/*event.clientAddress*/, toJsonStr(event).getBytes())
  }

  private def toJsonStr(event: ClientPacket): String = {
    import event._
    s"""{"t":$curTimeMillis,"a":"$clientAddress","s":${signalStrength.head}}""" // FIXME: signal strength will be fixed
  }

  private def emptyToNull(str: String) =
    if(str == null || str.trim.isEmpty) null
  else str
}
