package furhatos.app.cardgame.flow.discussion

import furhatos.app.cardgame.*
import furhatos.app.cardgame.flow.outro.CheckSolution
import furhatos.app.cardgame.nlu.GameDoneIntent
import furhatos.app.cardgame.nlu.GameNotDoneIntent
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val CheckIfDone: State = state(Discussing) {
    onEntry {
        knowsHowToEndGame = true
        send(ActionShowButton(output.button_check_answer))
        furhat.ask(output.ask_if_done)
    }

    onResponse<Yes> {
        raise(it, GameDoneIntent())
    }

    onResponse<GameDoneIntent> {
        goto(CheckSolution)
    }

    onResponse<No> {
        raise(it, GameNotDoneIntent())
    }

    onResponse<GameNotDoneIntent> {
        discuss(output.lets_continue_discussion)
    }

}