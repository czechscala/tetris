package com.czechscala.tetris 

package object model {
  type Grid = Set[(Int, Int)]

  case class Board(width: Int, height: Int, grid: Grid)

  case class Shape(grid: Grid)

  case class State(board: Board, shape: Option[((Int, Int), Shape)])

}
