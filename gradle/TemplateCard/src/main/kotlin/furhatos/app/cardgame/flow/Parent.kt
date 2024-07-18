package furhatos.app.cardgame.flow

import furhatos.app.cardgame.SenseGameReset
import furhatos.app.cardgame.*
import furhatos.app.cardgame.flow.intro.Sleeping
import furhatos.event.senses.SenseSkillGUIConnected
import furhatos.flow.kotlin.*
import furhatos.nlu.Response
import furhatos.records.Record

val Parent: State = state {

    onEvent<SenseSkillGUIConnected> {
        goto(Sleeping)
    }

    onButton("Reset Game") {
        goto(Sleeping)
    }

    onEvent(SenseGameReset) {
        goto(Sleeping)
    }

}