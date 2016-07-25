package testgrp

import co.paralleluniverse.actors.ActorRef
import co.paralleluniverse.actors.behaviors.ServerActor
import co.paralleluniverse.fibers.SuspendExecution
import co.paralleluniverse.strands.Strand

class SimpleServerActor : ServerActor<String, String, String>() {
    private var counter: Int = 0

    @Throws(Exception::class, SuspendExecution::class)
    override fun handleCall(from: ActorRef<*>, id: Any, m: String): String {
        try {
            Strand.sleep(20)
            return "Response from a simple server: " + m + " " + counter++
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }

    }
}


fun main(args: Array<String>) {
    var actor = SimpleServerActor().spawn()
    println(actor.call("foo"))
}
