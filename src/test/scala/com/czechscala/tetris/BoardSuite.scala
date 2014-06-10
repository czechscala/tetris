package com.czechscala.tetris.model

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class BoardSuite extends FlatSpec with ShouldMatchers {

  "Board with illegal parameters" should "throw exception" in {

    intercept[IllegalArgumentException] {
      Board(1, 2, Set((-1, -1)))
    }

  }

  "Board with legal parameters" should "have filled all members" in {

    val board = Board(1, 2, Set((0, 0)))
    board.width should be(1)
    board.height should be(2)
    board.grid.size should be(1)

  }

  "Shape" should " throw exception when created with bad points" in {
    intercept[IllegalArgumentException] {
      Shape(Set((1, 1)))
    }
  }

  it should " not throw exception" in {
    val s = Shape(Set((1, 0), (0, 1), (1, 1)))
    s.height should be(2)
    s.width should be(2)
  }

  "Method putShape" should "create new state" in {
    val shape = Shape(Set((0, 0), (0, 1)))
    val board = Board(2, 2, Set())
    val state = State(board, None)
    state.putShape(1, shape) should be(State(board, Some((1, -1), shape)))
  }

  it should "throw exception when shape dimension exceed board" in {
    val shape = Shape(Set((0, 0), (1, 0)))
    val board = Board(2, 2, Set())
    val state = State(board, None)
    intercept[IllegalArgumentException] {
    	state.putShape(1, shape)
    }
  }

}