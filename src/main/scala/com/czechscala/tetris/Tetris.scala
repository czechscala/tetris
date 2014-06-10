package com.czechscala.tetris

import akka.actor.{Actor, ActorSystem, Props}
import com.czechscala.tetris.model._
import com.czechscala.tetris.render.Swing

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._

object Tetris extends App {

  case object Tick

  case class Key(code: Int)

  class Engine extends Actor {
    private val renderer = new Swing(20, 30, self)
    private var state = State(Board(20, 30, Set()), None).putShape(0, ShapeGenerator.next)
    private var ticks = 0
    private var delay = 2000

    override def preStart = { render; tick }

    private def tick = context.system.scheduler.scheduleOnce(delay millis, self, Tick)

    def receive = {
      case Tick =>
        render
        ticks += 1
        if (ticks % 100 == 0) delay = (delay * 0.9).toInt
        tick

      case key: KeyPress =>
        key match {
          case LeftArrow => state = state.moveShape(Left)
          case RightArrow => state = state.moveShape(Right)
        }
        render
    }

    private def render = renderer render state

    def getTicks = ticks

    def getDelay = delay
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