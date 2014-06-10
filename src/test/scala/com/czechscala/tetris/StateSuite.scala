package com.czechscala.tetris

import org.scalatest.{FlatSpec, Matchers}
import com.czechscala.tetris.model._

class StateSuite extends FlatSpec with Matchers {

  "State" should "move shape right" in {
    val shape = Shape(Set((0,0)))
    val board = Board(4, 1, Set())
    val state = State(board, Some((2, 0), shape))

    state.moveShape(Right) should be (State(board, Some((3, 0), shape)))
  }

  it should "not move shape left while at the edge of the board" in {
    val shape = Shape(Set((0,0)))
    val board = Board(4, 1, Set())
    val state = State(board, Some((0, 0), shape))

    state.moveShape(Left) should be (state)
  }

  it should "not move shape right while at the edge of the board" in {
    val shape = Shape(Set((0,0), (1, 0)))
    val board = Board(4, 1, Set())
    val state = State(board, Some((2, 0), shape))

    state.moveShape(Right) should be (state)
  }

  it should "not move shape while it is adjacent to already played shape" in {
    val shape = Shape(Set((0,0)))
    val board = Board(2, 1, Set((1, 0)))
    val state = State(board, Some((0, 0), shape))

    state.moveShape(Right) should be (state)
  }

  it should "move a shape down" in {
    val shape = Shape(Set((0, 0)))
    val board = Board(2, 4, Set((0, 1), (0, 2), (0, 3)))
    val startState = State(board, Some((1, 2), shape))
    val endState = startState.moveShape(Down)

    endState should be(State(board, Some((1, 3), shape)))
  }

  it should "put the shape to the board" in {
    val shape = Shape(Set((0, 0)))
    val board = Board(2, 4, Set((1, 3)))
    val startState = State(board, Some((1, 2), shape))

    startState.moveShape(Down) should be(State(Board(2, 4, Set((1, 2), (1, 3))), None))
  }
}
