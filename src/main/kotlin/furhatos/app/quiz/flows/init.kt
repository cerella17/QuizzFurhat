package furhatos.app.quiz.flows

import furhatos.app.quiz.flows.main.PreQuiz
import furhatos.app.quiz.setting.initEngagementDistance
import furhatos.app.quiz.setting.initEngagementMaxUsers
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users
import furhatos.flow.kotlin.voice.Voice
import furhatos.skills.RemoteGUI
import furhatos.util.Language

val Init: State = state() {
    init {
        // Imposto la politica di engagement per l'interazione
        users.setSimpleEngagementPolicy(distance = initEngagementDistance, maxUsers = initEngagementMaxUsers)
        // Imposta la lingua in italiano
        furhat.setInputLanguage(Language.ITALIAN)
        // Inizializza la voce Adriano-Neural
        furhat.voice = Voice("Adriano-Neural")

        // Host the GUI
        RemoteGUI("WEBAPP", port = 3000)

        goto(PreQuiz)
    }
}
