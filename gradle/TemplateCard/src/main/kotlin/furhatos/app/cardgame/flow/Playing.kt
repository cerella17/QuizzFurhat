package furhatos.app.cardgame.flow

import furhatos.app.cardgame.*
import furhatos.app.cardgame.flow.intro.Sleeping
import furhatos.app.cardgame.flow.outro.CheckSolution
import furhatos.app.cardgame.game.Game
import furhatos.flow.kotlin.*
import furhatos.records.Record

val Playing: State = state(Parent) {
    onEvent(SenseCardsMoved, instant = true) { event ->
        //cardsMoving = true

        val record = event.getRecord("params")
        val cards = record.getList<Record>("cards")
        if (oldOrder == null)
            oldOrder = Game.cardSet.currentOrder

        cards.forEachIndexed { index, cardRecord ->
            val position = cardRecord.getRecord("position")
            // We have to compensate for the fact that the coordinates from the GUI refers to the top-left corner, and not the center of the card
            val x = position.getDouble("x") + 0.075
            val y = position.getDouble("y") + 0.2
            val card = Game.cardSet.cards[index]
            val oldLocation = card.location
            card.setScreenLocation(x, y)
            val newLocation = card.location
            if (oldLocation != newLocation) {
                println("Card moved: " + card.id)
                if (!furhat.isSpeaking) {
                    // Only attend to cards when Furhat is not speaking
                    cardsAttentionLimiter.limitRepeat(500) {
                        raise(ActionAttendCard(card.id))
                    }
                }
            }
        }

        val newOrder = Game.cardSet.currentOrder

        if (newOrder.hasChanged(oldOrder!!)) {
            //println("CARDS REORDERED")
            cardsMoving = true
            cardsMovingDone.run(3000) {
                cardsMoving = false
                //println("CARD ORDER CHANGED: " + newOrder)
                raise(SenseCardsReordered(Game.cardSet.lastKnownOrder, newOrder))
                Game.cardSet.lastKnownOrder = newOrder
            }
            oldOrder = newOrder
        }

    }

    onEvent(SenseButtonClick) {
        goto(CheckSolution)
    }

    onUserLeave(instant = true) {
        if (users.count == 0) {
            goto(Sleeping)
        }
    }
}