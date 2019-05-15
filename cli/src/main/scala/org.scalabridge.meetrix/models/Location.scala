package org.scalabridge.meetrix.models

sealed trait Location

object Location {

  case class London() extends Location

  case class NewYork() extends Location

}
