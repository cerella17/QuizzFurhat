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

        // Funzione per gestire il singolare e il plurale dei punti
        fun puntiToString(punti: Int): String {
            return if (punti == 1) "un punto" else "$punti punti"
        }

        // Annunciare i punteggi totali
        furhat.say("Ora annunciamo i risultati finali.")
        furhat.say("La squadra rossa ha totalizzato ${puntiToString(totalRedScore)}.")
        furhat.say("La squadra blu ha totalizzato ${puntiToString(totalBlueScore)}.")

        // Annunciare la squadra vincitrice
        when {
            totalRedScore > totalBlueScore -> {
                furhat.say("Congratulazioni alla squadra rossa per aver vinto con ${puntiToString(totalRedScore)}!")
            }
            totalBlueScore > totalRedScore -> {
                furhat.say("Congratulazioni alla squadra blu per aver vinto con ${puntiToString(totalBlueScore)}!")
            }
            else -> {
                furhat.say("È un pareggio! Entrambe le squadre hanno totalizzato ${puntiToString(totalRedScore)}.")
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
