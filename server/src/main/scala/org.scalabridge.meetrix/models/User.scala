package org.scalabridge.meetrix.models

import cats.effect.IO
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

final case class User(
  name: String,
  age: Int
)

object User {
  // We know how to encode a case class to a JSON (and decode from JSON)
  // if we know how to encode all its fields!
  implicit val decoder: Decoder[User] = deriveDecoder[User]
  implicit val encoder: Encoder[User] = deriveEncoder[User]

  // An EntityDecoder instance instructs http4s how to decode a request body
  // into our model (which is User here).
  // We are using our JSON decoding logic above. jsonOf takes an Decoder instance
  // and make a EntityDecoder which:
  // - Checks the HTTP Content-Type header is "application/json"
  // - Attempts to decode the body as JSON using the provided Decoder
  implicit val entityDecoder: EntityDecoder[IO, User] = jsonOf[IO, User]

  // EntityEncoder tells http4s how we can take a model (User in this case)
  // and make a HTTP response body.
  // Similar to above, we are building an EntityEncoder based off our JSON encoder
  // that we already have. The resulting EntityEncoder will
  // - Yield a body which is the JSON representation of our model, using the provided Encoder
  // - Set the HTTP header "Content-Type" to "application/json"
  implicit val entityEncoder: EntityEncoder[IO, User] = jsonEncoderOf[IO, User]
}

