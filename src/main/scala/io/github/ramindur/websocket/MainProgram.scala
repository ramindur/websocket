package io.github.ramindur.websocket

import cats.effect.{IO, IOApp}
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object MainProgram extends IOApp.Simple {

  override def run: IO[Unit] = {
    implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]
    Server.program[IO]
  }

}
