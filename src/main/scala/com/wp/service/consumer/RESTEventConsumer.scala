package com.wp.service.consumer

import akka.actor.ActorSystem
import com.wp.config.GlobalAppConfig.Application.Consumers.RestConsumer
import com.wp.messages.EventMessages.ClientPacket
import com.wp.service.boot.Boot
import com.wp.utils.logging.AkkaLoggingHelper
import spray.client.pipelining._
import spray.http.{HttpRequest, HttpResponse}
import spray.httpx.SprayJsonSupport._
import spray.json._

import scala.concurrent.Future

class RESTEventConsumer extends EventConsumer with AkkaLoggingHelper {

  import log._

  override implicit val globalSystem = Boot.system
  import globalSystem.dispatcher

  object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val eventFormat = jsonFormat3(ClientPacket)
  }
  import MyJsonProtocol._

  override def process(event: ClientPacket): Unit = {
    val response: Future[HttpResponse] =
      pipeline(Post(RestConsumer.submitUrl, event))
    response.onSuccess { case response => info(response.toString)} // TODO: check how the status code works here 20x
    response.onFailure { case err => error(err.getMessage)}
  }

  val pipeline: HttpRequest => Future[HttpResponse] =
    sendReceive

}

//case class EventConfirmation()