package furhatos.app.cardgame.flow.outro

import furhatos.app.cardgame.*
import furhatos.app.cardgame.flow.Playing
import furhatos.app.cardgame.game.Game
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state

val CommentScore: State = state(Playing) {
    onEntry {
        furhat.say(output.comment_score)
        send(ActionRevealSolution(Game.cardSet.cards.map { card ->
            Truth(Game.deck.displayTruth(card), Game.cardSet.correctOrder.indexOf(card))
        }))
        goto(CommentSolution)
    }

}