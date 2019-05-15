package org.scalabridge.meetrix.models

/** Possible user commands that can be parsed from command line arguments */
sealed trait CommandInput

object CommandInput {

  /**
    *
    * @param desc If true, show newest past events first
    */
  final case class ListPastEvents(desc: Option[Boolean]) extends CommandInput

  final case class ListMyGroups() extends CommandInput

  final case class FindGroups(
    radius: Option[Int],
    category: Option[Category],
    location: Option[Location],
    keyword: Option[String]
  ) extends CommandInput

  final case class FindEvents(
    radius: Option[Int],
    category: Option[Category],
    location: Option[Location],
    keyword: Option[String]
  ) extends CommandInput

}
