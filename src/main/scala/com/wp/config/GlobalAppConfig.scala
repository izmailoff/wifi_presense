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

    object Consumer {
      private lazy val consumerConf = config.getConfig("application.consumer")
      lazy val consumerClass = consumerConf.getString("implementer") // TODO: maybe this can be a list
    }

  }

}
