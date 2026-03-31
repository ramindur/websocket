package io.github.ramindur.websocket

import cats.effect.kernel.Async
import cats.effect.std.Queue
import cats.syntax.all._
import fs2.Stream
import fs2.concurrent.Topic
import org.http4s.websocket.WebSocketFrame
import org.typelevel.log4cats.{Logger => Log4CatsLogger}

import scala.concurrent.duration._

object Server {

  def program[F[_]: Async: Log4CatsLogger]: F[Unit] =
    for {
      q <- Queue.bounded[F, WebSocketFrame](1000)
      t <- Topic[F, WebSocketFrame]
      publisher = Stream.fromQueueUnterminated(q).evalMap(t.publish1)
      heartbeat = Stream.awakeEvery[F](30.seconds).evalMap(_ => t.publish1(WebSocketFrame.Ping()))
      server = Stream.eval(HttpServer.server[F](q, t))
      _ <- Stream(publisher, heartbeat, server)
        .parJoin(3)
        .compile
        .drain
    } yield ()

}
