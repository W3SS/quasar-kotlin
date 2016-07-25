package testgrp

import co.paralleluniverse.actors.Actor
import co.paralleluniverse.actors.OnUpgrade
import co.paralleluniverse.fibers.SuspendExecution
import java.util.concurrent.TimeUnit

class SimpleActor(name: String) : Actor<Message, Void>(name, null) {
    private var count: Int = 0
    private var count2: Int = 0
    private val foo = Foo()
    private val task = MyTask()

    private inner class MyTask : Runnable {
        private var ra: Int = 0

        override fun run() {
            println("An actor's simple runnable: " + (count + 100) + " " + ra++)
        }
    }

    @Throws(InterruptedException::class, SuspendExecution::class)
    override fun doRun(): Void {
        while (true) {
            val m = receive(500, TimeUnit.MILLISECONDS)
            if (m != null)
                System.out.println("Got message: " + m.num)
            println(NAME + " I am a simple actor - " + count++ + ", " + count2++ + ", foo:" + foo.a++)
            task.run()
            checkCodeSwap()
        }
    }

    @OnUpgrade
    private fun foo() {
        count2 = -10
    }

    override fun onCodeChange() {
        count2 = count * 100
    }

    internal class Foo {
        var a: Int = 0
    }

    companion object {
        internal var NAME = "GOGO"
    }
}

fun main(args: Array<String>) {
    var actor = SimpleActor("simple").spawn()
    actor.send(Message(123))
}
