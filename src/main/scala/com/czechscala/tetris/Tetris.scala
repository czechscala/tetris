package com.czechscala.tetris

import akka.actor.{Actor, ActorSystem, Props}
import com.czechscala.tetris.model._
import com.czechscala.tetris.render.Swing

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._
import scala.util.Random

object Tetris extends App {

  case object Tick
  case object NewGame
  case class Key(code: Int)

  class Engine extends Actor {
    private val renderer = new Swing(20, 30, self)
    private var state = State(Board(20, 30, Set()), None)
    private var ticks = 0
    private var delay = 1000

    override def preStart = {
      newShape; render; tick
    }

    private def tick = context.system.scheduler.scheduleOnce(delay millis, self, Tick)

    def receive = {
      case NewGame => state = State(Board(20, 30, Set()), None)

      case Tick =>
        moveDown
        render
        ticks += 1
        if (ticks % 100 == 0) delay = (delay * 0.9).toInt
        tick

      case key: KeyPress =>
        key match {
          case LeftArrow => state = state.moveShape(Left)
          case RightArrow => state = state.moveShape(Right)
          case DownArrow => moveDown
          case Space =>
            while (state.shape.nonEmpty) state = state.moveShape(Down)
            newShape
        }
        render
    }

    private def render = renderer render state

    private def moveDown = {
      state = state.moveShape(Down)
      if (state.shape.isEmpty) newShape
    }

    private def newShape = {

      val shape = ShapeGenerator.next
      val x = Random.nextInt(state.board.width - shape.width)
      state = state.putShape(x, shape)
    }

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