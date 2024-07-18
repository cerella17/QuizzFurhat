package furhatos.app.cardgame.flow.discussion

import furhatos.app.cardgame.*
import furhatos.app.cardgame.game.GameTable
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users

val StartDiscussing: State = state(Discussing) {
    onEntry {
        /*
        if(!isThisRoundTwo) {
            // Grounding: "By the way, You can touch the screen to move the cards. I don't have hands, you see."
            furhat.attend(users.other)
            furhat.say(output.by_the_way_name)
            furhat.attend(GameTable.centerLocation)
            furhat.say(output.explain_you_can_move_cards)
            furhat.attend(users.current)
            furhat.say (output.no_hands_you_see)
        }
        */
        furhat.attend(users.current)
        // "Alright. Which animal do you think we should start with?"
        furhat.ask(output.ask_where_to_start)
    }
}