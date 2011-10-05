/**
 * Copyright (C) 2009-2011 Typesafe Inc. <http://www.typesafe.com>
 */
package akka.zeromq

sealed trait Response
case object Connected extends Response
case object Closed extends Response
