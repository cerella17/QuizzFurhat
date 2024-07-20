package furhatos.app.quiz.flow.main

import furhatos.app.quiz.flow.Parent
import furhatos.app.quiz.questions.QuestionSet
import furhatos.app.quiz.setting.playing
import furhatos.app.quiz.setting.quiz
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users

val StartNewGameDirectly = state(parent = Parent) {
    onEntry {
        playing = true
        rounds = 0

        // Resettare i punteggi degli utenti per la nuova partita
        users.playing().forEach {
            it.quiz.scoreTeamRed = 0
            it.quiz.scoreTeamBlue = 0
        }

        furhat.say("Iniziamo una nuova partita! Preparati per la prima domanda.")
        QuestionSet.next()
        goto(AskQuestion)
    }
}
