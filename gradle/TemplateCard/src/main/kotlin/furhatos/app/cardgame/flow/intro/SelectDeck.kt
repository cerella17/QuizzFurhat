package furhatos.app.cardgame.flow.intro

import furhatos.app.cardgame.*
import furhatos.app.cardgame.flow.Playing
import furhatos.app.cardgame.game.Game
import furhatos.app.cardgame.game.GameTable
import furhatos.app.cardgame.nlu.AskAboutDeckIntent
import furhatos.flow.kotlin.*
import furhatos.nlu.SimpleIntent

val SelectDeck: State = state(Playing) {

    onEntry {
        roundCounter++
        send(ActionClearTable())
        furhat.attendOneOrAll()
        if (roundCounter == 1) {
            furhat.say(output.select_game)
        } else {
            furhat.say(output.select_new_game)
        }
        furhat.attend(GameTable.centerLocation)
        send(ActionShowDecks(decks = Game.decks.map { EventDeck(it.name, it.cards.first().imagePath) }))
        furhat.listen()
    }

    onEvent(SenseSelectDeck) { event ->
        val deck = Game.decks.find { it.name == event.getString("params") }
        if (deck != null) {
            if (roundCounter > 1 && Game.deck.name == deck.name) {
                furhat.say(output.confirm_same_game(deck.name))
            } else {
                furhat.say(output.confirm_game(deck.name))
            }
            goto(SetupGame(deck = deck))
        }
    }

    var remindToTap = 0

    onResponse<AskAboutDeckIntent> {
        val deck = it.intent.deck?.deck
        if (deck != null) {
            if (remindToTap++ == 0) {
                furhat.say(output.propose_game(deck.name))
            } else {
                furhat.say(output.propose_game_again(deck.name))
            }
        }
        furhat.listen()
    }

    onResponse(SimpleIntent(input.ask_favorite_game)) {
        furhat.say(output.answer_favorite_game)
        furhat.listen()
    }

    onResponse {
        furhat.listen()
    }

    onNoResponse {
        furhat.listen()
    }

}

