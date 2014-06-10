package com.czechscala.tetris

import org.scalatest.{FlatSpec, Matchers}
import com.czechscala.tetris.model._

class StateSuite extends FlatSpec with Matchers {

  "State" should "move shape right" in {
    val shape = Shape(Set((0,0)))
    val board = Board(2, 1, Set())
    val state = State(board, Some((0, 0), shape))

    state.moveShape(Right) should be (State(board, Some((1, 0), shape)))
  }

  "State" should "not move shape left while at the edge of the board" in {
    val shape = Shape(Set((0,0)))
    val board = Board(2, 1, Set())
    val state = State(board, Some((0, 0), shape))

    state.moveShape(Left) should be (state)
  }

  "State" should "not move shape right while at the edge of the board" in {
    val shape = Shape(Set((0,0), (1, 0)))
    val board = Board(2, 1, Set())
    val state = State(board, Some((0, 0), shape))

    state.moveShape(Right) should be (state)
  }

  "State" should "not move shape while it is adjacent to already played shape" in {
    val shape = Shape(Set((0,0)))
    val board = Board(2, 1, Set((1, 0)))
    val state = State(board, Some((0, 0), shape))

    state.moveShape(Right) should be (state)
  }
}
