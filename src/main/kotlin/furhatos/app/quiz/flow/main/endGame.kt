package furhatos.app.quiz.flow.main

import furhatos.app.quiz.flow.Parent
import furhatos.app.quiz.setting.playing
import furhatos.app.quiz.setting.quiz
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users

val EndGame = state(parent = Parent) {
    onEntry {
        playing = false

        // Separate users by team
        val redTeam = users.playing().filter { it.quiz.team == "red" }
        val blueTeam = users.playing().filter { it.quiz.team == "blue" }

        // Calculate total scores for each team
        val totalRedScore = redTeam.sumBy { it.quiz.scoreTeamRed ?: 0 }
        val totalBlueScore = blueTeam.sumBy { it.quiz.scoreTeamBlue ?: 0 }

        // Announce total scores
        furhat.say("Ora annunciamo i risultati finali.")
        furhat.say("La squadra rossa ha totalizzato $totalRedScore punti.")
        furhat.say("La squadra blu ha totalizzato $totalBlueScore punti.")

        // Announce the winning team
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

        // Resetting user state variables
        users.playing().forEach {
            it.quiz.playing = false
            it.quiz.played = true
            it.quiz.lastScoreTeamRed = it.quiz.scoreTeamRed
            it.quiz.lastScoreTeamBlue = it.quiz.scoreTeamBlue
            it.quiz.scoreTeamRed = 0
            it.quiz.scoreTeamBlue = 0
        }

        delay(1000)

        goto(Idle)
    }
}
