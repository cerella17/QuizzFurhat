package furhatos.app.cardgame.flow.discussion

import furhatos.app.cardgame.game.Opinions
import furhatos.app.cardgame.discuss
import furhatos.app.cardgame.output
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.state

/**
 * Motivate Furhat's opinion
 */
val MotivateOpinion: State = state(Discussing) {
    onEntry {
        val opinion = Opinions.latest
        if (opinion != null) {
            discuss(opinion.motivation)
        } else {
            // This happens if Furhat has not expressed any opinions yet
            discuss(output.dont_understand)
        }
    }

}