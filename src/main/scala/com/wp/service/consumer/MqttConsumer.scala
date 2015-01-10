package com.wp.service.consumer

import akka.actor.{Actor, ActorSystem, Props}
import com.sandinh.paho.akka.MqttPubSub.{SubscribeAck, Subscribe, Publish, PSConfig}
import com.wp.messages.EventMessages.ClientPacket
import com.sandinh.paho.akka._
import scala.concurrent.duration._
import com.sandinh.paho.akka.ByteArrayConverters._

class MqttConsumer extends EventConsumer {

  val system = ActorSystem("mqtt") // can reuse existing one?

  val broker = system.actorOf(Props(classOf[MqttPubSub], PSConfig(
    brokerUrl = "tcp://localhost:1883",//"tcp://test.mosquitto.org:1883", //all params is optional except brokerUrl
    userName = null,
    password = null,
    stashTimeToLive = 1.minute,
    stashCapacity = 8000, //stash messages will be drop first haft elems when reach this size
    reconnectDelayMin = 10.millis, //for fine tuning re-connection logic
    reconnectDelayMax = 30.seconds
  )))

  override def process(event: ClientPacket): Unit = {
    broker ! new Publish("wifipresense"/*event.clientAddress*/, event.toString.getBytes())// use implicit???
  }
}
