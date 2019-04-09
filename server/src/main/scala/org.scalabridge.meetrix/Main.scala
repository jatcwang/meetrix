package org.scalabridge.meetrix

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.scalabridge.meetrix.routes.ExampleRoutes

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    buildServer.compile.drain.as(ExitCode.Success)
  }

  def buildServer: Stream[IO, Nothing] = {
    val allRoutes = ExampleRoutes.makeRoute().orNotFound
    for {
      exitCode <- BlazeServerBuilder[IO]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(allRoutes)
        .serve
    } yield exitCode
  }.drain

}
