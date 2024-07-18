package furhatos.app.cardgame.flow.discussion

import furhatos.app.cardgame.SenseCardsReordered
import furhatos.app.cardgame.output
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state


fun CommentReordered(event: SenseCardsReordered): State = state(Discussing) {

    onEntry {
        event.newOrder.getChanges(event.lastOrder).getOrNull(0)?.let {
            furhat.attend(it.location)
        }
        val improvement = event.newOrder.evaluate() - event.lastOrder.evaluate()
        if (improvement > 0) {
            furhat.say(output.comment_order_better)
        } else {
            furhat.say(output.comment_order_worse)
        }
        goto(Discussing)
    }
}