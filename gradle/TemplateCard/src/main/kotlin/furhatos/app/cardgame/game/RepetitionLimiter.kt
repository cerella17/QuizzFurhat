package furhatos.app.cardgame.game

class RepetitionLimiter {
    private var lastRepeat: Long = 0

    fun limitRepeat(msec: Int, action: () -> Unit) {
        if (System.currentTimeMillis() - lastRepeat > msec) {
            action()
        }
        lastRepeat = System.currentTimeMillis()
    }
}