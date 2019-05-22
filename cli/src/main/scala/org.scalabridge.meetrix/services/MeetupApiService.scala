package org.scalabridge.meetrix.services

import java.time.ZonedDateTime

import cats.effect.{ContextShift, IO}
import org.http4s.client.Client
import org.http4s.headers.Authorization
import org.http4s.syntax.all._
import org.http4s.{Credentials, Headers, Query, Request, Uri}
import org.scalabridge.meetrix.models.CommandInput.ListEvents
import org.scalabridge.meetrix.models.meetupapi.RsvpedEvent

/** Calls Meetup API */
class MeetupApiService(httpClient: Client[IO])(implicit contextShift: ContextShift[IO]) {

  private val API_KEY = "3cc92f04aebce968317a7efaf0aea12b"

  // https://www.meetup.com/meetup_api/docs/self/events/
  def listEvents(list: ListEvents): IO[List[RsvpedEvent]] = {
    // Decodes the body of the HTTP response (which is in JSON) into our model using the decoder we defined
    import org.http4s.circe.CirceEntityDecoder._

    // Meetup API does not timezone offset thus we use local time i.e. "2018-06-01T00:00:00.000"
    val now             = ZonedDateTime.now.toLocalDateTime.toString
    val queryPastEvents = list.past.getOrElse(false)
    val pastOrFutureQueryParameter = if (queryPastEvents) {
      "no_later_than" -> now.toString
    } else {
      "no_earlier_than" -> now.toString
    }

    httpClient
      .expect[List[RsvpedEvent]](
        Request[IO](
          uri = Uri
            .uri("https://api.meetup.com/self/events")
            .copy(query = Query.fromPairs("desc" -> list.desc.getOrElse(true).toString, pastOrFutureQueryParameter)),
          headers = Headers.of(Authorization(Credentials.Token("Bearer".ci, API_KEY)))
        )
      )
  }
}
