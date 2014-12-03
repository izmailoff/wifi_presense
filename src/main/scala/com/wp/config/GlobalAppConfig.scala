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
      lazy val cmd: List[String] = snifferConf.getStringList("cmd").toList
    }

    object Metrics {
      private lazy val metricsConf = config.getConfig("application.metrics")
      lazy val eventAggregateInterval: FiniteDuration =
        metricsConf.getDuration("eventAggregateInterval", TimeUnit.SECONDS) seconds
    }

  }

}

