package org.scalabridge.meetrix
import InputParsing.parseInput
import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.client.blaze.BlazeClientBuilder
import org.scalabridge.meetrix.models.CommandInput
import org.scalabridge.meetrix.models.CommandInput.ListEvents
import org.scalabridge.meetrix.services.MeetupApiService

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val parseResult = parseInput(args)

    parseResult match {
      case Right(result) =>
        BlazeClientBuilder[IO](global).resource.use { httpClient =>
          val meetupApiService = new MeetupApiService(httpClient)

          result match {
            case cmd: ListEvents => {
              meetupApiService.listEvents(cmd).map { response =>
                pprint.pprintln(response, height = 10000)
                ExitCode.Success
              }
            }
            case _: CommandInput.ListMyGroups => IO { ??? }
            case _: CommandInput.FindGroups => IO { ??? }
            case _: CommandInput.FindEvents => IO { ??? }
          }
        }
      case Left(parseError) => IO {
        println(parseError.errMessage)
        ExitCode.Error
      }
    }

  }

}
