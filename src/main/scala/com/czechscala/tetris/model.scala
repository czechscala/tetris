package com.czechscala.tetris

package object model {
  type Grid = Set[(Int, Int)]

  case class Board(width: Int, height: Int, grid: Grid) {
    require {
      grid forall { case (x, y) => x >= 0 && x < width && y >= 0 && y < height }
    }
  }
  
  sealed trait Direction

  case object Down extends Direction
  case object Left extends Direction
  case object Right extends Direction

  case class Shape(grid: Grid) {
    
    require {
      !grid.filter{ case (x, y) => x == 0 || y == 0 }.isEmpty
    }
    
    def width: Int = {
      grid.map{ case (x,y) => x }.max + 1
    }
    
    def height: Int = {
      grid.map{ case (x,y) => y }.max + 1
    }
  }
  
  case class State(board: Board, shape: Option[((Int, Int), Shape)]) {
    def putShape(x: Int, shape: Shape): State = {
      require {
        shape.width + x <= board.width 
      }
      State(board, Some(((x, 1 - shape.height), shape)))
    }
  }
}

