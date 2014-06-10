package com.czechscala.tetris

import model._
import akka.actor.{Props, ActorSystem, Actor}
import scala.annotation.tailrec
import scala.concurrent.duration._

object Tetris extends App {

  case class Key(code: Int)

  class Engine extends Actor {
    private var state = State(Board(10, 20, Set()), None)
    private var ticks = 0
    private var speed = 2000

    override def preStart = context.system.scheduler.scheduleOnce(speed millis, self, "tick")
	
    def receive = {
      case "tick" =>
        ticks += 1
        if (ticks == 100) speed *= 0.9

      case Key(code) => println("key pressed: " + code);
    }
  }

  val system = ActorSystem("tetris")
  val engine = system.actorOf(Props[Engine])
  
  @tailrec
  def waitForKey: Unit = {
    val code = System.in.read
    if (code != 10) {
      engine ! Key(code)
      waitForKey
    }
  }
  
  waitForKey
  system.shutdown()
}