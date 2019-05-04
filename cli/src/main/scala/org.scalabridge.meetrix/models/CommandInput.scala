package org.scalabridge.meetrix.models

/** Possible user commands that can be parsed from command line arguments */
sealed trait CommandInput
final case class SearchCmdInput(keyword: String, category: Option[Category]) extends CommandInput
final case class ListCmdInput(location: Option[LatLng], radius: Option[Int]) extends CommandInput
