package io.github.ramindur.websocket

import cats.effect.kernel.Async
import cats.effect.std.Queue
import cats.syntax.all._
import fs2.Pipe
import fs2.concurrent.Topic
import fs2.io.file.Files
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.websocket.WebSocketFrame
import org.typelevel.log4cats.{Logger => Log4CatsLogger}

class Routes[F[_]: Files: Log4CatsLogger: Async] {
  def service(
      wsb: WebSocketBuilder2[F],
      queue: Queue[F, WebSocketFrame],
      topic: Topic[F, WebSocketFrame]
  ): HttpApp[F] =
    HttpRoutes
      .of[F] {
        case request @ GET -> Root / "chat.html" =>
          StaticFile
            .fromPath(
              fs2.io.file.Path(getClass.getClassLoader.getResource("chat.html").getFile),
              Some(request)
            )
            .getOrElse(Response.notFound[F])
        case GET -> Root / "ws" =>
          val send =
            topic.subscribe(maxQueued = 1000)
          val receive: Pipe[F, WebSocketFrame, Unit] =
            _.evalMap(m => publishToQueue(queue, m))

          wsb.build(send, receive)

      }
      .orNotFound

  private def publishToQueue[F[_]: Async: Log4CatsLogger](
      queue: Queue[F, WebSocketFrame],
      m: WebSocketFrame
  ): F[Unit] =
    for {
      _ <- Log4CatsLogger[F].info(s"Publishing message to queue: ${m}")
      _ <- queue.offer(m)
    } yield ()

}
