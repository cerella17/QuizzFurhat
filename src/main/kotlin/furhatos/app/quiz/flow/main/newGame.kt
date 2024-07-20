package furhatos.app.quiz.flow.main

import furhatos.app.quiz.flow.Parent
import furhatos.app.quiz.questions.QuestionSet
import furhatos.app.quiz.setting.playing
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users

val NewGame = state(parent = Parent) {
    onEntry {
        playing = true
        rounds = 0

        // Spiega le regole del gioco al plurale
        furhat.say("Benvenuti al quiz! Vi farò $maxRounds domande a scelta multipla.")
        furhat.say("Ogni domanda avrà diverse opzioni di risposta. Dovrete scegliere quella corretta per guadagnare punti.")
        furhat.say("Se rispondete correttamente, guadagnerete un punto. Se sbagliate, la domanda passerà alla squadra avversaria.")

        // Se ci sono più giocatori, spiega la regola per le risposte errate
        if (users.count > 1) {
            furhat.say("Dato che ci sono più giocatori, se rispondete in modo errato, la domanda passerà al prossimo giocatore.")
        }

        // Annuncia l'inizio del gioco
        furhat.say("Bene, iniziamo! Preparatevi per la prima domanda.")

        // Poni la prima domanda
        QuestionSet.next()
        goto(AskQuestion)
    }
}
