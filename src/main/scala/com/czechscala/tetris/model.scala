package com.czechscala.tetris

package object model {

  type Grid = Set[(Int, Int)]

  case class Board(width: Int, height: Int, grid: Grid) {
    require {
      grid forall { case (x, y) => x >= 0 && x < width && y >= 0 && y < height}
    }
  }

  sealed trait Direction

  case object Down extends Direction

  case object Left extends Direction

  case object Right extends Direction

  case class Shape(grid: Grid) {

    require {
      !grid.filter { case (x, y) => x == 0 || y == 0}.isEmpty
    }

    def width: Int = {
      grid.map { case (x, y) => x}.max + 1
    }

    def height: Int = {
      grid.map { case (x, y) => y}.max + 1
    }
  }

  case class State(board: Board, shape: Option[((Int, Int), Shape)]) {
    def putShape(x: Int, shape: Shape): State = {
      require {
        shape.width + x <= board.width
      }
      State(board, Some(((x, 1 - shape.height), shape)))
    }

    def moveShape(direction: Direction) = {
      require(this.shape.isDefined)
      val Some(((x, y), shape)) = this.shape

      val newX = direction match {
        case Right => x + 1
        case Left => x - 1
      }
      val shapeGrid = shape.grid map { case (gx, gy) => (gx + x + newX, gy + y) }
      val isAdjacent = board.grid.intersect(shapeGrid).nonEmpty

      if (isAdjacent || newX < 0 || newX + shape.width > board.width) this
      else State(board, Some((newX, y), shape))
    }
  }

  sealed trait KeyPress

  case object LeftArrow extends KeyPress

  case object RightArrow extends KeyPress

  case object DownArrow extends KeyPress

  case object Space extends KeyPress

}
