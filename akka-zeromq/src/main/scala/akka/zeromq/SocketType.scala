/**
 * Copyright (C) 2009-2011 Typesafe Inc. <http://www.typesafe.com>
 */
package akka.zeromq

import org.zeromq.{ZMQ => ZeroMQ}

object SocketType extends Enumeration {
  type SocketType = Value
  val Pub = Value(ZeroMQ.PUB)
  val Sub = Value(ZeroMQ.SUB)
  val Dealer = Value(ZeroMQ.DEALER)
  val Router = Value(ZeroMQ.ROUTER)
}
