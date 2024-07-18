package furhatos.app.cardgame.game

import furhatos.app.cardgame.focusStack

class FocusStack(list: List<Card>) : ArrayList<Card>(list) {

    fun prime(card: Card) {
        remove(card)
        add(0, card)
        println("Focus stack: " + focusStack)
    }

}