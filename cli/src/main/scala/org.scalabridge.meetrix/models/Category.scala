package org.scalabridge.meetrix.models

sealed trait Category

object Category {

  final case class Tech() extends Category

  final case class Art() extends Category

}
