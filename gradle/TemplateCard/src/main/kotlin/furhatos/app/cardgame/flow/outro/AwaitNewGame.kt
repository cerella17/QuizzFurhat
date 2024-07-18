package furhatos.app.cardgame.flow.outro

import furhatos.app.cardgame.*
import furhatos.app.cardgame.flow.Playing
import furhatos.app.cardgame.flow.intro.SelectDeck
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures.BigSmile
import furhatos.nlu.common.Goodbye
import furhatos.nlu.common.No
import furhatos.nlu.common.Thanks
import furhatos.nlu.common.Yes

val AwaitNewGame: State = state(Playing) {
    onEntry {
        send(ActionShowButton(output.button_play_again))
        furhat.say(output.ask_play_again)
        furhat.listen()
    }

    val playAgain = behavior {
        furhat.say { +BigSmile; include(output.lets_play_again) }
        goto(SelectDeck)
    }

    onResponse(input.play_again) {
        playAgain(this)
    }

    onResponse<Yes> {
        playAgain(this)
    }

    onResponse<No> {
        goto(Goodbye)
    }

    onResponse<Thanks> {
        goto(Goodbye)
    }

    onResponse<Goodbye> {
        goto(Goodbye)
    }

    onResponse {
        furhat.listen()
    }

    onNoResponse {
        furhat.listen()
    }

    onEvent(SenseButtonClick) {
        playAgain(this)
    }
}


