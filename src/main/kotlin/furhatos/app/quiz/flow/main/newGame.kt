package furhatos.app.quiz.flow.main

import furhatos.app.quiz.flow.Parent
import furhatos.app.quiz.questions.QuestionSet
import furhatos.app.quiz.setting.blueTeam
import furhatos.app.quiz.setting.playing
import furhatos.app.quiz.setting.redTeam
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
        furhat.say("Avrete 30 secondi per discutere con la vostra squadra prima di dare la risposta. Per dare la risposta, dite 'pronto'. Se il tempo scade, dovrete dare subito una risposta altrimenti la domanda passerà alla squadra avversaria.")


        // Annuncia l'inizio del gioco
        furhat.say("Bene, iniziamo! Preparatevi per la prima domanda.")

        // Imposta l'attenzione sul capo della squadra rossa
        val redLeader = users.redTeam().firstOrNull()
        if (redLeader != null) {
            println(users.redTeam())
            furhat.attend(redLeader)
        }
        // Poni la prima domanda
        QuestionSet.next()
        goto(AskQuestion)
    }
}
