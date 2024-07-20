package furhatos.app.quiz.flow.main

import furhatos.app.quiz.flow.Parent
import furhatos.app.quiz.setting.playing
import furhatos.app.quiz.setting.quiz
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val EndGame = state(parent = Parent) {
    onEntry {
        playing = false

        // Separare gli utenti per squadra
        val redTeam = users.playing().filter { it.quiz.team == "red" }
        val blueTeam = users.playing().filter { it.quiz.team == "blue" }

        // Calcolare i punteggi totali per ogni squadra
        val totalRedScore = redTeam.sumBy { it.quiz.scoreTeamRed }
        val totalBlueScore = blueTeam.sumBy { it.quiz.scoreTeamBlue }

        // Annunciare i punteggi totali
        furhat.say("Ora annunciamo i risultati finali.")
        furhat.say("La squadra rossa ha totalizzato $totalRedScore punti.")
        furhat.say("La squadra blu ha totalizzato $totalBlueScore punti.")

        // Annunciare la squadra vincitrice
        when {
            totalRedScore > totalBlueScore -> {
                furhat.say("Congratulazioni alla squadra rossa per aver vinto con $totalRedScore punti!")
            }
            totalBlueScore > totalRedScore -> {
                furhat.say("Congratulazioni alla squadra blu per aver vinto con $totalBlueScore punti!")
            }
            else -> {
                furhat.say("È un pareggio! Entrambe le squadre hanno totalizzato $totalRedScore punti.")
            }
        }

        furhat.say("Grazie per aver giocato!")

        // Chiedere se vogliono iniziare una nuova partita
        furhat.ask("Volete iniziare una nuova partita?")
    }

    onResponse<Yes> {
        // Passare allo stato di nuova partita senza resettare i punteggi
        goto(StartNewGameDirectly)
    }

    onResponse<No> {
        furhat.say("Grazie ancora per aver giocato! Alla prossima!")
        goto(Idle)
    }

    onNoResponse {
        furhat.say("Grazie ancora per aver giocato! Alla prossima!")
        goto(Idle)
    }
}
