package furhatos.app.cardgame.flow.intro

import furhatos.app.cardgame.*
import furhatos.app.cardgame.flow.discussion.StartDiscussing
import furhatos.app.cardgame.game.*
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.nlu.Entity
import furhatos.nlu.Intent

fun SetupGame(deck: Deck) = state(Sleeping) {

    onEntry {
        send(ActionClearTable())
        Game.selectDeck(deck)
        Entity.forgetAll()
        Intent.forgetAll()
        Opinions.forget()
        Card.forgetInitiatives()
        send(ActionDealCards(Game.deckWithCards))
        furhat.attend(GameTable.centerLocation)
        furhat.say(Game.deck.output.purpose)
        furhat.say(output.explain_order)
        furhat.attendOneOrAll()
        goto(StartDiscussing)
    }

}