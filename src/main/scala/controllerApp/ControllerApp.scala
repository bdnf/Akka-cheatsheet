package controllerApp

import akka.actor.{Actor, ActorSystem, Props}
import controllerApp.ControllerTier.{Get, Post}
import controllerApp.FrontendTier.{ShowData, UpdateData}

object ControllerTier {
  sealed trait ControllerMsg
  case object Get extends ControllerMsg
  case object Post extends ControllerMsg

  def props = Props[ControllerApp]
}

class ControllerApp extends Actor {
  override def receive: Receive = {
    case Get => println("Backend: Fetching data from a DB")
    case Post => println("Backend: Submitted new data to the DB")
    case _ => println("Request not supported")
  }
}

object FrontendTier {
  sealed trait FrontendMsg
  case object ShowData extends FrontendMsg
  case object UpdateData extends FrontendMsg
}

class FrontendApp extends Actor {
  // val controller = context.actorOf(Props[ControllerApp], "controller") // BAD
  val controller = context.actorOf(ControllerTier.props, "controller") // GOOD

  override def receive: Receive = {
    case ShowData => {
      println(" Showing some data on the screen")
      controller ! Get
    }
    case UpdateData => {
      println(" Submitting new data to the app")
      controller ! Post
    }
    case _ => println("Unsupported action encountered")
  }
}

object ControllerAppDemo extends App {

  val system = ActorSystem("WebAppMockup")

  val webapp = system.actorOf(Props[FrontendApp], "simple-web-app")

  webapp ! ShowData
  Thread.sleep(2000)
  webapp ! UpdateData

  system.terminate()

}
