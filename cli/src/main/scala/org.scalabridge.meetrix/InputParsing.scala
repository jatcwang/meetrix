package org.scalabridge.meetrix

import org.scalabridge.meetrix.models.{CommandInput, ListCmdInput, SearchCmdInput}

/** Functions which are used to parse user input (command line arguments) */
object InputParsing {

  def parseInput(args: List[String]): Either[ParseError, CommandInput] = {

    args match {

      case Nil => Left(ParseError("Command not provided"))

      case firstArg :: others =>
        firstArg match {
          case "list"          => parseListCmdInput(others)
          case "search"        => parseSearchCmdInput(others)
          case unrecognizedCmd => Left(ParseError(s"Unrecognized command $unrecognizedCmd"))
        }
    }
  }

  def parseListCmdInput(args: List[String]): Either[ParseError, ListCmdInput] =
    ???

  def parseSearchCmdInput(args: List[String]): Either[ParseError, SearchCmdInput] =
    ???

}
