/**
 * Copyright (C) 2009-2011 Typesafe Inc. <http://www.typesafe.com>
 */
package akka.zeromq

import akka.actor.{Actor, ActorRef}
import akka.testkit.{TestKit, TestProbe}
import akka.util.Duration
import akka.util.duration._
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers

class AsyncClientServerConnectionSpec extends WordSpec with MustMatchers with TestKit {
  "Asynchronous client-server connection" should {
    "send / receive requests and responses" in {
      val (clientProbe, serverProbe) = (TestProbe(), TestProbe())
      val request = ZMQMessage("request".getBytes)
      val response = ZMQMessage("response".getBytes)
      var client: Option[ActorRef] = None
      var server: Option[ActorRef] = None
      try {
        server = newServer(serverProbe.ref)
        client = newClient(clientProbe.ref)
        clientProbe.within (5000 millis) {
          clientProbe.expectMsg(Connected)
        }
        serverProbe.within (5000 millis) {
          client ! request
          serverProbe.expectMsg(request)
        }
        clientProbe.within (5000 millis) {
          server ! response 
          clientProbe.expectMsg(response)
        }
      } finally {
        client ! Close
        server ! Close
        clientProbe.within (5000 millis) {
          clientProbe.expectMsg(Closed)
        }
        serverProbe.within (5000 millis) {
          serverProbe.expectMsg(Closed)
        }
        client.foreach(_.stop)
        server.foreach(_.stop)
      }
    }
    def newClient(listener: ActorRef) = {
      val client = ZeroMQ.newSocket(context, SocketType.Dealer, Some(listener))
      client ! Connect(endpoint)
      Some(client)
    }
    def newServer(listener: ActorRef) = {
      val server = ZeroMQ.newSocket(context, SocketType.Dealer, Some(listener))
      server ! Bind(endpoint)
      Some(server)
    }
    lazy val context = ZeroMQ.newContext
    lazy val endpoint = "inproc://AsyncClientServerConnectionSpec"
  }
}
