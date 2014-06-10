package com.czechscala.tetris

import scala.util.Random
import com.czechscala.tetris.model.Shape

object ShapeGenerator {

  val shapes = List(
    Shape(Set((0, 0))),
    Shape(Set((0, 0), (1, 0), (2, 0))),
    Shape(Set((0, 0), (1, 0), (2, 0), (3, 0))),
    Shape(Set((0, 0), (1, 0), (0, 1), (1, 1))),
    Shape(Set((0, 1), (1, 0), (1, 1))),
    Shape(Set((0, 1), (1, 1), (2, 1), (2, 0))),
    Shape(Set((0, 1), (1, 1), (1, 0), (2, 1), (1, 2)))
  )

  def next = shapes(Random.nextInt(shapes.size))
}
