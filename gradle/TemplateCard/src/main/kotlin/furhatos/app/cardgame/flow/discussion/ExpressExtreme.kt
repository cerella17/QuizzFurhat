package furhatos.app.cardgame.flow.discussion

import furhatos.app.cardgame.game.Game
import furhatos.app.cardgame.game.GameTable
import furhatos.app.cardgame.game.Opinions
import furhatos.app.cardgame.output
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state

/**
 * Express opinion about which card is the lowest/highest
 */
fun ExpressExtreme(highest: Boolean): State = state(Discussing) {

    onEntry {
        val opinion = Opinions.getExtremePosition(highest)
        // Have we expressed this opinion before?
        val repeat = opinion.count > 0
        // Remember that we have expressed this opinion
        opinion.expressed()
        // If we have any card-specific arguments, not already used
        val argument = if (opinion.confident) opinion.getArgument() else null
        if (argument != null)
            furhat.say(argument)
        if (opinion.cards.size <= 2) {
            furhat.attend(opinion.cards[0].location)
            furhat.say {
                if (argument != null)
                    include(output.so)
                if (repeat)
                    include(output.i_still_think_80)
                else
                    include(output.i_think_80)
                // "The zebra is the fastest animal"
                include(opinion.expression)
            }
            if (opinion.cards.size == 1 && opinion.needChange) {
                if (highest)
                    furhat.attend(GameTable.highestLocation)
                else
                    furhat.attend(GameTable.lowestLocation)
                furhat.say(output.suggest_move_card)
            }
            goto(SeekAgreement(opinion))
        } else {
            furhat.attend(GameTable.centerLocation)
            furhat.say {
                include(output.i_dont_know_which)
                if (highest)
                    include(Game.deck.output.is_max)
                else
                    include(Game.deck.output.is_min)
            }
            goto(RequestOpinion)
        }
    }

}