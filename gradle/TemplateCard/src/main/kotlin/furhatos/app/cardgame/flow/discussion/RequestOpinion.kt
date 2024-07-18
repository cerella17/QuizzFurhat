package furhatos.app.cardgame.flow.discussion

import furhatos.app.cardgame.*
import furhatos.app.cardgame.nlu.AgreeIntent
import furhatos.app.cardgame.nlu.DisagreeIntent
import furhatos.flow.kotlin.*
import furhatos.nlu.common.DontKnow
import furhatos.nlu.common.Maybe
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import kotlin.random.Random

/**
 * An open question about opinions from the users
 */
val RequestOpinion: State = state(Discussing) {
    onEntry {
        when (Random.nextInt(3)) {
            0 -> {
                random(
                    {
                        furhat.attend(users.random)
                        furhat.ask(output.request_opinion_name)
                    },
                    {
                        furhat.attendOneOrAll()
                        furhat.ask(output.request_opinion)
                    }
                )
            }
            else -> {
                goto(Discussing)
            }
        }
    }

    onResponse<AgreeIntent> {
        raise(it, Yes())
    }

    onResponse<DisagreeIntent> {
        raise(it, No())
    }

    onResponse<DontKnow> {
        furhat.say(output.request_opinion_unsure)
        goto(TakeInitiative())
    }

    onResponse<Maybe> {
        furhat.say(output.request_opinion_unsure)
        goto(TakeInitiative())
    }

    onResponse<Yes> {
        discuss(output.request_opinion_accept)
    }

    onResponse<No> {
        if (users.count > 1) {
            furhat.attend(users.other)
            discuss(output.request_opinion_other)
        } else {
            goto(Discussing)
        }
    }

}