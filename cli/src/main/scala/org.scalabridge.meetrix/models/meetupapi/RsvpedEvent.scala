package org.scalabridge.meetrix.models.meetupapi

import java.time.{LocalDate, LocalDateTime, LocalTime}

import cats.implicits._
import io.circe.{Decoder, DecodingFailure, HCursor}
import org.http4s.Uri

/** An event the user has RSVPed to */
final case class RsvpedEvent(
  name: String,
  groupName: String,
  localDateTime: LocalDateTime,
  description: Option[String],
  link: Uri
)

object RsvpedEvent {
  implicit val decoder: Decoder[RsvpedEvent] = new Decoder[RsvpedEvent] {
    override def apply(c: HCursor): Either[DecodingFailure, RsvpedEvent] = {
      for {
        name      <- c.downField("name").as[String]
        groupName <- c.downField("group").downField("name").as[String]
        localDate <- c.downField("local_date").as[LocalDate]
        localTime <- c.downField("local_time").as[LocalTime]
        link <- Decoder[String]
                 .emap(str => Uri.fromString(str).leftMap(_.details))
                 .prepare(_.downField("link"))
                 .apply(c)
        description <- c.downField("description").as[Option[String]]
      } yield {
        val localDateTime = LocalDateTime.of(localDate, localTime)
        RsvpedEvent(
          name          = name,
          groupName     = groupName,
          localDateTime = localDateTime,
          description   = description,
          link          = link
        )
      }
    }
  }

}
