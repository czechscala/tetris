package com.czechscala.tetris.render

import java.awt.event.{KeyEvent, KeyListener}
import java.awt.{Color, Dimension, Graphics}
import javax.swing.{JFrame, JPanel, SwingUtilities}

import akka.actor.ActorRef
import com.czechscala.tetris.model._

class Swing(width: Int, height: Int, keyListener: ActorRef) extends Renderer {

  private val frame = new JFrame("Tetris")
  private val canvas = new Canvas

  swing {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.addKeyListener(Keyboard)
    frame.add(canvas)
    frame.pack()
    frame.setVisible(true)
  }

  override def render(state: State): Unit = swing {
    canvas.refresh(state)
  }

  private class Canvas extends JPanel {

    private final val RectSizePx = 30

    var currentState: Option[State] = None

    def refresh(state: State): Unit = {
      currentState = Some(state)
      this.repaint()
    }

    override def paintComponent(g: Graphics) = {
      super.paintComponent(g)

      currentState match {
        case Some(state) =>
          clear(g)
          drawGrid(state.board.grid, g)

          state.shape match {
            case Some(((offsetX, offsetY), shape)) => drawGrid(shape.grid, g, offsetX, offsetY)
            case None => // ignore
          }
        case None => // ignore
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

    private def drawGrid(grid: Grid, g: Graphics, offsetX: Int = 0, offsetY: Int = 0) = {
      grid.foreach { case (x, y) =>
        drawRectangle(offsetX + x, offsetY + y, g)
      }
    }

    override def getPreferredSize() = new Dimension(widthPx, heightPx)

    private def widthPx: Int = width * RectSizePx

    private def heightPx: Int = height * RectSizePx
  }

  private object Keyboard extends KeyListener {
    override def keyPressed(e: KeyEvent): Unit = {
      val keyPress = e.getExtendedKeyCode match {
        case KeyEvent.VK_LEFT => Some(LeftArrow)
        case KeyEvent.VK_RIGHT => Some(RightArrow)
        case KeyEvent.VK_DOWN => Some(DownArrow)
        case KeyEvent.VK_SPACE => Some(Space)
        case _ => None
      }

      keyPress match {
        case Some(ev) => keyListener ! ev
        case None => // ignore
      }
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
