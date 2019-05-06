package org.scalabridge.meetrix.models

sealed trait Location

case object London extends Location
case object NewYork extends Location
