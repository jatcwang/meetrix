package org.scalabridge.meetrix

import org.scalabridge.meetrix.models.CommandInput

/** Functions which are used to parse user input (command line arguments) */
object InputParsing {

  def parseInput(args: List[String]): Either[ParseError, CommandInput] = {

    args match {

      case Nil => Left(ParseError("Command not provided"))

      case firstArg :: others =>
        firstArg match {
          case "myevents" => parsePastEventsCommand(others)
          case "mygroups" => parseListMyGroupsCommand(others)
          /* TODO: match other commands like findgroups and findevents! */
          case unrecognizedCmd => Left(ParseError(s"Unrecognized command $unrecognizedCmd"))
        }
    }
  }

  def parsePastEventsCommand(args: List[String]): Either[ParseError, CommandInput.ListPastEvents] =
    ???

  def parseListMyGroupsCommand(args: List[String]): Either[ParseError, CommandInput.ListMyGroups] =
    ???

  def parseFindGroupsCommand(args: List[String]): Either[ParseError, CommandInput.FindGroups] =
    ???

  def parseFindEvents(args: List[String]): Either[ParseError, CommandInput.FindEvents] =
    ???

}
