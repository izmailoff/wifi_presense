package com.wp.config

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.{FiniteDuration, _}
import scala.collection.JavaConversions._

object GlobalAppConfig {

  private val config = ConfigFactory.load()

  object Application {

    object Sniffer {
      private lazy val snifferConf = config.getConfig("application.sniffer")
      lazy val cmd = snifferConf.getStringList("cmd").toList
      lazy val fieldSeparator = snifferConf.getString("fieldSeparator")
    }

    object Metrics {
      private lazy val metricsConf = config.getConfig("application.metrics")
      lazy val eventAggregateInterval: FiniteDuration =
        metricsConf.getDuration("eventAggregateInterval", TimeUnit.SECONDS) seconds
    }

    object Consumers {
      private lazy val consumerConf = config.getConfig("application.consumers")
      lazy val consumers = consumerConf.getStringList("implementers").toList

      object RestConsumer {
        private lazy val restConsumerConf = config.getConfig("application.consumers.restConsumer")
        lazy val submitUrl = restConsumerConf.getString("submitUrl")
      }

      object MqttConsumer {
        private lazy val mqqtConsumerConf = config.getConfig("application.consumers.mqttConsumer")
        lazy val brokerUrl = mqqtConsumerConf.getString("brokerUrl")
        lazy val userName = mqqtConsumerConf.getString("userName")
        lazy val password = mqqtConsumerConf.getString("password")
        lazy val stashTimeToLive: FiniteDuration =
          mqqtConsumerConf.getDuration("stashTimeToLive", TimeUnit.SECONDS) seconds
        lazy val stashCapacity = mqqtConsumerConf.getInt("stashCapacity")
        lazy val reconnectDelayMin: FiniteDuration =
          mqqtConsumerConf.getDuration("reconnectDelayMin", TimeUnit.SECONDS) seconds
        lazy val reconnectDelayMax: FiniteDuration =
          mqqtConsumerConf.getDuration("reconnectDelayMax", TimeUnit.SECONDS) seconds
      }

      object WebSocketConsumer {
        private lazy val webSocketConsumerConf = config.getConfig("application.consumers.webSocketConsumer")
        lazy val serverUrl = webSocketConsumerConf.getString("serverUrl")
      }
    }

  }

}
