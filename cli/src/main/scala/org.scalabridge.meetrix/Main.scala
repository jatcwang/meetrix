package org.scalabridge.meetrix
import InputParsing.parseInput

object Main {

  def main(argsArgs: Array[String]): Unit = {

    val args = argsArgs.toList

    println(parseInput(args))

  }

}
