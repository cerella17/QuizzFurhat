package furhatos.app.cardgame.flow.intro

import furhatos.app.cardgame.*
import furhatos.app.cardgame.flow.Playing
import furhatos.app.cardgame.nlu.TellNameIntent
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.PersonName
import furhatos.nlu.common.Yes

fun AskName(player: Int = 1): State = state(Playing) {

    val proceed = behavior {
        furhat.sayOnce(output.my_name_is_furhat)
        if (users.count > 1 && player == 1) {
            furhat.attend(users.other)
            goto(AskName(2))
        } else {
            goto(SelectDeck)
        }
    }

    onEntry {
        if (player == 1)
            // "What is your name?"
            furhat.ask(output.ask_name_1)
        else
            // "And what is your name?"
            furhat.ask(output.ask_name_2)
    }

    onResponse<TellNameIntent> {
        users.current.name = it.intent.name?.value
        furhat.say(output.acknowledge_name)
        proceed()
    }

    onResponse {
        furhat.say(output.acknowledge_name)
        proceed()
    }

    onNoResponse {
        furhat.say(output.acknowledge_name)
        proceed()
    }
}
