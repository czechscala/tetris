package com.czechscala.tetris.render

import java.awt.{Color, Dimension, Graphics}
import javax.swing.{JFrame, JPanel, SwingUtilities}

import com.czechscala.tetris.model.State

class Swing extends Renderer {

  private val frame = new JFrame("Tetris")
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  private var initialized = false
  private var canvas: Canvas = _

  override def render(state: State): Unit = swing {
    if (!initialized) {
      canvas = new Canvas
      canvas.refresh(state)
      frame.add(canvas)
      frame.pack()
      frame.setVisible(true)

      initialized = true
    }

    canvas.refresh(state)
  }

  private def swing(block: => Unit): Unit = {
    SwingUtilities.invokeLater(
      new Runnable() {
        def run(): Unit = {
          block
        }
      }
    )
  }

  private class Canvas extends JPanel {

    private final val RectSizePx = 30

    var currentState: State = _

    def refresh(state: State): Unit = {
      currentState = state
      this.repaint()
    }

    override def paintComponent(g: Graphics) = {
      super.paintComponent(g)

      g.setColor(Color.WHITE)
      g.clearRect(0, 0, widthPx, heightPx)

      currentState.board.grid.foreach { case (x, y) =>
        drawRectangle(x, y, g)
      }
    }

    override def getPreferredSize() = new Dimension(widthPx, heightPx)

    private def drawRectangle(x: Int, y: Int, g: Graphics) = {
      g.setColor(Color.BLACK)
      g.fillRect(x * RectSizePx, y * RectSizePx, RectSizePx, RectSizePx)
    }

    private def widthPx: Int = currentState.board.width * RectSizePx

    private def heightPx: Int = currentState.board.height * RectSizePx
  }

}
