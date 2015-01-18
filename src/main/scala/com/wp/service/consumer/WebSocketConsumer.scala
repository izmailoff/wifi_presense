package com.wp.service.consumer

import java.net.URI

import com.wp.messages.EventMessages.ClientPacket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.{Draft, Draft_10}
import org.java_websocket.handshake.ServerHandshake
import com.wp.config.GlobalAppConfig.Application.Consumers.WebSocketConsumer

class WebSocketConsumer
  extends EventConsumer {

  // TODO: make draft configurable, see available options here:
  // https://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
  val client = new WebSocketSender(new URI(WebSocketConsumer.serverUrl), new Draft_10())
  client.connect()

  override def process(event: ClientPacket): Unit =
    client.send(toJsonStr(event))
}


class WebSocketSender(serverUri: URI, draft: Draft) extends WebSocketClient(serverUri, draft) {

  override def onOpen(handshakedata: ServerHandshake): Unit =
    println("Websocket connection opened.")

  override def onMessage(message: String): Unit = {
    // ignore all messages
  }

  // TODO: perhaps restart the connection and resend
  override def onError(ex: Exception): Unit =
    println(s"Got an error: [${ex.getMessage}}]")

  override def onClose(code: Int, reason: String, remote: Boolean): Unit = {
    val remoteIndicator = if (remote) "remote server" else "us"
    println(s"Websocket connection closed with code: [$code], reason: [$reason], by $remoteIndicator.")
  }
}
