package com.czechscala.tetris

import org.scalatest.{FlatSpecLike, Matchers}
import akka.testkit.{TestActorRef, TestKit}
import akka.actor.ActorSystem
import Tetris._

class TetrisEngineSuite extends TestKit(ActorSystem()) with FlatSpecLike with Matchers {

  "Engine" should "update ticks number" in {
    val engine = TestActorRef[Engine]
    engine.underlyingActor.getTicks should be (0)
    engine.tell(Tick, testActor)
    engine.underlyingActor.getTicks should be (1)
  }

  "Engine" should "decrease delay after 100 ticks" in {
    val engine = TestActorRef[Engine]
    engine.underlyingActor.getTicks should be (0)
    engine.underlyingActor.getDelay should be (1000)
    1 to 100 foreach { _ => engine.tell(Tick, testActor) }
    engine.underlyingActor.getTicks should be (100)
    engine.underlyingActor.getDelay should be (900)
  }
}
