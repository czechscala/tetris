package com.czechscala.tetris.render

import com.czechscala.tetris.model.State

trait Renderer {
  
  def render(state: State): Unit

}
