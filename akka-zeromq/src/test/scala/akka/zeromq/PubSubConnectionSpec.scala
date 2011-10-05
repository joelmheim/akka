/**
 * Copyright (C) 2009-2011 Typesafe Inc. <http://www.typesafe.com>
 */
package akka.zeromq

import akka.actor.{Actor, ActorRef}
import akka.testkit.TestKit
import akka.util.Duration
import akka.util.duration._
import java.util.Arrays
import org.scalatest.matchers.MustMatchers
import org.scalatest.WordSpec

class PubSubConnectionSpec extends WordSpec with MustMatchers with TestKit {
  "Pub-sub connection" should {
    "send / receive messages" in {
      val message = ZMQMessage("hello".getBytes)
      var publisher: Option[ActorRef] = None
      var subscriber: Option[ActorRef] = None
      try {
        publisher = newPublisher
        subscriber = newSubscriber
        within (5000 millis) {
          expectMsg(Connected)
        }
        within (5000 millis) {
          publisher ! message
          expectMsg(message)
        }
      } finally {
        subscriber ! Close
        publisher ! Close
        within (5000 millis) {
          expectMsg(Closed)
        }
        subscriber.foreach(_.stop)
        publisher.foreach(_.stop)
      }
    }
    def newPublisher = {
      val publisher = ZeroMQ.newSocket(context, SocketType.Pub)
      publisher ! Bind(endpoint)
      Some(publisher)
    }
    def newSubscriber = {
      val subscriber = ZeroMQ.newSocket(context, SocketType.Sub, Some(testActor))
      subscriber ! Connect(endpoint)
      subscriber ! Subscribe(Seq())
      Some(subscriber)
    }
    lazy val context = ZeroMQ.newContext
    lazy val endpoint = "inproc://PubSubConnectionSpec"
  }
}
