package furhatos.app.cardgame.flow.discussion

import furhatos.app.cardgame.game.Card
import furhatos.app.cardgame.game.Opinions
import furhatos.app.cardgame.output
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state

/**
 * Express opinion about the comparison of two cards
 * If focusCard1 == true, a potential argument will only concern card1
 */
fun CompareCards(card1: Card, card2: Card, focusCard1: Boolean = false): State = state(Discussing) {

    onEntry {
        furhat.attend(card1.location)
        // Make a comparison of the two cards
        val comparison = Opinions.getComparison(card1,card2)
        // Have we made this comparison before?
        val repeat = comparison.count > 0
        // Remember that we have expressed this opinion
        comparison.expressed()
        // If we have any card-specific arguments, not already used
        val argument = if (comparison.confident) comparison.getArgument(focusCard1) else null
        if (argument != null)
            furhat.say(argument)
        furhat.say {
            if (comparison.confident && !comparison.ordered) {
                if (argument != null)
                    include(output.so)
                if (repeat && comparison.conf > 0.9 && !comparison.ordered) {
                    include(output.i_still_think_90)
                } else if (repeat && comparison.conf > 0.8 && !comparison.ordered) {
                    include(output.i_still_think_80)
                } else if (comparison.conf > 0.95 && !comparison.ordered) {
                    include(output.i_think_95)
                } else if (comparison.conf > 0.9 && !comparison.ordered) {
                    include(output.i_think_90)
                } else if (comparison.conf > 0.8 && !comparison.ordered) {
                    include(output.i_think_80)
                } else {
                    include(output.i_think_70)
                }
            } else if (comparison.confident) {
                // The two cards are already in order
                if (argument != null)
                    include(output.so)
                include(output.i_think_correct)
            } else {
                include(output.i_dont_know_whether)
            }
            // "the zebra is slower than the lion"
            include(comparison.expression)
        }
        if (comparison.confident)
            goto(SeekAgreement(comparison))
        else
            goto(RequestOpinion)
    }

}