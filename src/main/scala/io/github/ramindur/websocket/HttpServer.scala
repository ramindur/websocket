package io.github.ramindur.websocket

import cats.effect.kernel.Async
import com.comcast.ip4s._
import fs2.io.file.Files
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import fs2.concurrent.Topic
import org.http4s.websocket.WebSocketFrame
import cats.effect.std.Queue
import org.typelevel.log4cats.{Logger => Log4CatsLogger}

object HttpServer {

  def server[F[_]: Async: Files: Network: Log4CatsLogger](
      queue: Queue[F, WebSocketFrame],
      topic: Topic[F, WebSocketFrame]
  ) = {
    val host = host"localhost"
    val port = port"8080"
    EmberServerBuilder
      .default[F]
      .withHost(host)
      .withPort(port)
      .withHttpWebSocketApp(wsb => new Routes[F].service(wsb, queue, topic))
      .build
      .useForever
  }

}
