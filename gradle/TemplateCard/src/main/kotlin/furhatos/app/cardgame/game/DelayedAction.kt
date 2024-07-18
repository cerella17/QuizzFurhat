package furhatos.app.cardgame.game

import kotlin.concurrent.thread

class DelayedAction() {
    val lock = Any()
    var currentThread: Thread? = null
    var action : (()->Unit)? = null

    val running get() =
        synchronized(lock) {
            currentThread?.isAlive()?:false
        }

    private fun startThread(time: Long) {
        if (currentThread != null) {
            currentThread!!.interrupt()
        }
        currentThread = thread(start = true) {
            try {
                Thread.sleep(time)
                synchronized(lock) {
                    action!!.invoke()
                    currentThread = null
                }
            } catch (_: InterruptedException) {
            }
        }
    }

    fun postpone(time: Long) {
        synchronized(lock) {
            if (running && action != null) {
                startThread(time)
            }
        }
    }

    fun run(time: Long, action:()->Unit) {
        synchronized(lock) {
            this.action = action
            startThread(time)
        }
    }
}