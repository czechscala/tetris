package com.czechscala.tetris.render

import java.awt.event.{KeyEvent, KeyListener}
import java.awt.{Color, Dimension, Graphics}
import javax.swing.{JFrame, JPanel, SwingUtilities}

import akka.actor.ActorRef
import com.czechscala.tetris.model.{Down, Space, State}

class Swing(keyListener: ActorRef) extends Renderer {

  private val frame = new JFrame("Tetris")
  private val canvas = new Canvas
  private var canvasInitialized = false

  swing {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.addKeyListener(Keyboard)
  }

  override def render(state: State): Unit = swing {
    if (!canvasInitialized) {
      frame.add(canvas)
      frame.pack()
      frame.setVisible(true)

      canvasInitialized = true
    }

    canvas.refresh(state)
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

      clear(g)

      currentState.board.grid.foreach { case (x, y) =>
        drawRectangle(x, y, g)
      }
    }

    private def clear(g: Graphics) = {
      g.setColor(Color.WHITE)
      g.clearRect(0, 0, widthPx, heightPx)
    }

    private def drawRectangle(x: Int, y: Int, g: Graphics) = {
      g.setColor(Color.BLACK)
      g.fillRect(x * RectSizePx, y * RectSizePx, RectSizePx, RectSizePx)
    }

    override def getPreferredSize() = new Dimension(widthPx, heightPx)

    private def widthPx: Int = currentState.board.width * RectSizePx

    private def heightPx: Int = currentState.board.height * RectSizePx
  }

  private object Keyboard extends KeyListener {
    override def keyPressed(e: KeyEvent): Unit = keyListener ! e.getExtendedKeyCode match {
      case KeyEvent.VK_LEFT => Left
      case KeyEvent.VK_RIGHT => Right
      case KeyEvent.VK_DOWN => Down
      case KeyEvent.VK_SPACE => Space
      case _ => // ignore
    }

    override def keyTyped(e: KeyEvent): Unit = ()

    override def keyReleased(e: KeyEvent): Unit = ()
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

}
