package furhatos.app.cardgame.flow.outro

import furhatos.app.cardgame.flow.intro.Sleeping
import furhatos.app.cardgame.game.Game
import furhatos.app.cardgame.output
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.gestures.Gestures

val Goodbye =
    state(AwaitNewGame) {
        onEntry {
            furhat.say {
                +Gestures.Smile
                +delay(500)
                include(output.alright)
                +delay(100)
                include(output.comment_sleep)
                +delay(250)
                include(output.goodbye)
            }
            goto(Sleeping)
        }
    }