package helloAkka

import akka.actor.{Actor, ActorSystem, Props}
import supervision.MasterWorker.system

case class Greeting(who: String)

class SimpleApp extends Actor {
  override def receive: Receive = {
    case Greeting(who) => println(s"Hello $who")
  }
}

object SimpleAppDemo extends App {

  val system = ActorSystem("Hello-World")

  val greeter = system.actorOf(Props[SimpleApp], "simple-app")

  greeter ! Greeting("Akka")

  system.terminate()
}
