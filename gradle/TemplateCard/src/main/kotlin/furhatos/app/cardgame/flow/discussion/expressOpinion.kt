package furhatos.app.cardgame.flow.discussion

import furhatos.app.cardgame.game.*
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.state

/**
 * Express opinion about a card
 */
fun ExpressOpinion(card: Card): State = state(Discussing) {

    onEntry {
        val opinion = Opinions.getStrongOpinion(card)
        if (opinion != null) {
            if (opinion is Comparison) {
                goto(CompareCards(opinion.card1, opinion.card2, true))
            } else if (opinion is ExtremePosition) {
                goto(ExpressExtreme(opinion.highest))
            }
        }
        // No strong opinion found, just do a comparison anyway
        goto(CompareCards(card, Game.cardSet.getNeighbours(card)[0], true))
    }

}