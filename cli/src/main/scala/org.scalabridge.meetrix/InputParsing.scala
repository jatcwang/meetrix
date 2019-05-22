package org.scalabridge.meetrix

import org.scalabridge.meetrix.models.{Category, CommandInput, Location}
import org.scalabridge.meetrix.models.CommandInput.{FindEvents, FindGroups, ListEvents, ListMyGroups}

/** Functions which are used to parse user input (command line arguments) */
object InputParsing {

  def parseInput(args: List[String]): Either[ParseError, CommandInput] = {

    args match {

      case Nil => Left(ParseError("Command not provided"))

      case firstArg :: otherArguments =>
        firstArg match {
          case "myevents"      => parseListEventsCommand(otherArguments)
          case "mygroups"      => Right(ListMyGroups()) // No arguments required
          case "findgroups"    => parseFindGroupsCommand(otherArguments)
          case "findevents"    => parseFindEventsCommand(otherArguments)
          case unrecognizedCmd => Left(ParseError(s"Unrecognized command $unrecognizedCmd"))
        }
    }
  }

  def parseListEventsCommand(args: List[String]): Either[ParseError, CommandInput.ListEvents] = {
    for {
      argsLookup: Map[String, String] <- parseIntoArgumentLookup(args)
      _ = println(argsLookup)

      description <- optionalParse(argsLookup.get("desc"), parseBoolean)
      past        <- optionalParse(argsLookup.get("past"), parseBoolean)
    } yield ListEvents(past = past, desc = description)
  }

  def parseFindGroupsCommand(args: List[String]): Either[ParseError, CommandInput.FindGroups] = {
    for {
      argsLookup: Map[String, String] <- parseIntoArgumentLookup(args)

      radius   <- optionalParse(argsLookup.get("radius"), parseInt)
      category <- optionalParse(argsLookup.get("category"), parseCategory)
      location <- optionalParse(argsLookup.get("location"), parseLocation)
      keyword  <- Right(argsLookup.get("keyword"))
    } yield FindGroups(radius = radius, category = category, location = location, keyword = keyword)
  }

  def parseFindEventsCommand(args: List[String]): Either[ParseError, CommandInput.FindEvents] =
    for {
      argsLookup: Map[String, String] <- parseIntoArgumentLookup(args)

      radius   <- optionalParse(argsLookup.get("radius"), parseInt)
      category <- optionalParse(argsLookup.get("category"), parseCategory)
      location <- optionalParse(argsLookup.get("location"), parseLocation)
      keyword  <- Right(argsLookup.get("keyword"))
    } yield FindEvents(radius = radius, category = category, location = location, keyword = keyword)

  /**
    * Given a list of arguments of the form 'key=value', parse it into a Map (key-value lookup).
    * This allows us to lookup a value using a key going forward, instead of needing to go through
    * the list of arguments every time!
    */
  def parseIntoArgumentLookup(args: List[String]): Either[ParseError, Map[String, String]] = {
    // Parse each argument and split by the '=' symbol in the middle
    val parsedKeyAndValues: List[Either[ParseError, (String, String)]] = args.map { argument =>
      // each argument must be of the form 'key=value' in order to be parsed correctly
      argument.split("=").toList match {
        case key :: value :: Nil if key.startsWith("--") => Right(key.drop(2) -> value)
        case _                   => Left(ParseError(s"Invalid argument '$argument'. Expecting argument of form --key=value"))
      }
    }

    // Check every argument is parsed correctly
    // If any argument failed to parse, then the final result is a Left (i.e. failure) with the first ParseError
    // it encounters. If all argument parsed correctly, we'll have a Right containing a List of key values
    // (String, String) tuple
    val collectedKeyValues: Either[ParseError, List[(String, String)]] =
      parsedKeyAndValues.foldLeft[Either[ParseError, List[(String, String)]]](Right(List.empty)) {
        (accumulatedResult, thisArgumentParseResult) =>
          accumulatedResult match {
            case Right(accumulatedKeyValues) =>
              thisArgumentParseResult match {
                case Right(keyValue) => Right(accumulatedKeyValues :+ keyValue)
                case Left(error)     => Left(error)
              }
            case Left(error) => Left(error)
          }
      }

    // Finally, convert the key values
    collectedKeyValues match {
      case Right(keyValues) => Right(keyValues.toMap)
      case Left(error)      => Left(error)
    }

    // Alternatively, the same logic can be expressed in just a few lines using common abstractions
    // from the library 'cats'. It is a library focused on enabling functional programming in Scala.
    // This is a good demonstration of the power of FP abstractions :)
//    import cats.implicits._
//    args.traverse { argument =>
//      argument.split("=").toList match {
//        case key :: value :: Nil => Right(key -> value)
//        case _                   => Left(ParseError(s"Invalid argument '$argument'. Expecting argument of form key=value"))
//      }
//    }.map(_.toMap)
  }

  /** Attempt to parse a single string into a boolean */
  def parseBoolean(inputStr: String): Either[ParseError, Boolean] = {
    inputStr match {
      case "true"     => Right(true)
      case "false"    => Right(false)
      case invalidStr => Left(ParseError(s"Not a valid boolean value: '$invalidStr'"))
    }
  }

  def parseInt(inputStr: String): Either[ParseError, Int] = {
    try {
      Right(inputStr.toInt)
    } catch {
      case _: NumberFormatException => Left(ParseError(s"Not an integer: $inputStr"))
    }
  }

  def parseCategory(inputStr: String): Either[ParseError, Category] = {
    import Category._
    inputStr match {
      case "tech"     => Right(Tech())
      case "art"      => Right(Art())
      case invalidStr => Left(ParseError(s"Not a known category: $invalidStr"))
    }
  }

  def parseLocation(inputStr: String): Either[ParseError, Location] = {
    import Location._
    inputStr.toLowerCase match {
      case "london"   => Right(London())
      case "newyork"  => Right(NewYork())
      case invalidStr => Left(ParseError(s"Not a known location: $invalidStr"))
    }
  }

  /**
    * Parse string that may or may not be provided
    * 1. If string isn't provided, simply succeed with None (i.e. not provided but is a valid input)
    * 2. If a string is provided, try to parse it using the provided parseFunction.
    *    Depending on whether it fails or not, return either the failure, or the result wrapped in Some
    */
  def optionalParse[A](
    inputStr: Option[String],
    parseFunction: String => Either[ParseError, A]
  ): Either[ParseError, Option[A]] = {
    inputStr match {
      case Some(value) =>
        parseFunction(value) match {
          case Right(parsedResult) => Right(Some(parsedResult))
          case Left(parseError)    => Left(parseError)
        }
      case None => Right(None)
    }
  }

}
