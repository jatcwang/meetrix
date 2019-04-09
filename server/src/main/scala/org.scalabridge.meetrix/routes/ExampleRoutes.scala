package org.scalabridge.meetrix.routes

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.scalabridge.meetrix.models.User

object ExampleRoutes {

  def makeRoute(): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {

      // Match a "GET /hello" request
      case GET -> Root / "hello" => Ok("Hello world!")

      case request @ POST -> Root / "add_age" => {
        request.as[User].flatMap { user =>
          val olderUser = user.copy(age = user.age + 1)
          Ok(olderUser)
        }
      }

    }
  }

}
