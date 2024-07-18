package furhatos.app.cardgame.flow.outro

import furhatos.app.cardgame.*
import furhatos.app.cardgame.flow.Playing
import furhatos.app.cardgame.game.Game
import furhatos.app.cardgame.game.GameTable
import furhatos.flow.kotlin.*

val CheckSolution: State = state(Playing) {
    onEntry {
        send(ActionHideButton())
        furhat.say(output.lets_check_solution)
        furhat.attend(GameTable.centerLocation)
        send(ActionShowSolutionIndicators(Game.cardSet.cards.map {
            Game.cardSet.correctOrder.indexOf(it) == Game.cardSet.currentOrder.indexOf(it)
        }))
        goto(CommentScore)
    }
}





