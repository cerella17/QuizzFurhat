package furhatos.app.quiz.flow.main

import furhatos.app.quiz.flow.Parent
import furhatos.app.quiz.questions.QuestionSet
import furhatos.app.quiz.setting.*
import furhatos.flow.kotlin.*
import furhatos.records.User

val Idle: State = state {
    onEntry {
        users.interested().forEach {
            furhat.say("Benvenuti!")
            furhat.attend(it)
            goto(QueryPerson(it))
        }
    }

    onUserEnter {
        countpeople++
        furhat.say("Ciao!")
        if (countpeople > 1)
            goto(QueryPerson(it))
    }

    onUserLeave(instant = true) {
        countpeople--
        if (users.count < 0) {
            furhat.attendAll()
        }
    }

    onResponse {
        reentry()
    }

    onNoResponse {
        reentry()
    }
}

// Variabili
val maxRounds = 2
var rounds = 0
var shouldChangeUser = true
var playing = false
var countpeople = 0

fun QueryPerson(user: User) = state(parent = Parent) {
    onEntry {
        if (users.count > 0) {
            furhat.say("Benvenuti ragazzi!")
            val longtime: Long = 4000
            delay(longtime)
            furhat.say("I capi gruppi scelti facciano due passi in avanti")

            users.setSimpleEngagementPolicy(distanceToEngageForQuiz, maxNumberOfUsers)

            // Aspetta che due utenti si facciano avanti
            var leaders: List<User> = listOf()
            while (leaders.size < 2) {
                leaders = users.list.filter { it.head.location.z < 0.5 }
                if (leaders.size >= 2) break
                furhat.say("Sto aspettando")
                delay(2000) // Aspetta un po' prima di controllare di nuovo
            }

            // Gestisci il caso in cui ci sono più persone nel range
            while (leaders.size > 2) {
                furhat.say("Tutte le persone al di fuori dei capi gruppi si allontanino")
                delay(3000)
                if (leaders.size <= 2) break
            }

            // Assegna le squadre in base alla posizione
            if (isUserLeft(leaders[0])) {
                leaders[0].quiz.team = "red"
                leaders[1].quiz.team = "blue"
            } else {
                leaders[1].quiz.team = "red"
                leaders[0].quiz.team = "blue"
            }

            println("Leader rossa: ${leaders[0].quiz.team}, Leader blu: ${leaders[1].quiz.team}") // Log delle squadre assegnate

            goto(askNameRed(leaders[0], leaders[1]))
        }
    }
}


fun askNameRed(leaderRed: User, leaderBlue: User) = state {
    onEntry {
        furhat.attend(leaderRed)
        furhat.ask("Capo gruppo squadra rossa, come ti chiami?")
    }

    onResponse {
        val nameRed = it.text
        leaderRed.quiz.namePlayerRed = nameRed
        println("Nome del capo squadra rossa: $nameRed") // Log del nome del capo squadra rossa
        furhat.say("Piacere di conoscerti $nameRed, capo squadra rossa")
        goto(askNameBlue(leaderBlue, leaderRed))
    }
}

fun askNameBlue(leaderBlue: User, leaderRed: User) = state {
    onEntry {
        furhat.attend(leaderBlue)
        furhat.ask("Capo gruppo squadra blu, come ti chiami?")
    }

    onResponse {
        val nameBlue = it.text
        leaderBlue.quiz.namePlayerBlue = nameBlue
        println("Nome del capo squadra blu: $nameBlue") // Log del nome del capo squadra blu
        furhat.say("Piacere di conoscerti $nameBlue, capo squadra blu")

        // Imposta entrambi i capi squadra come giocatori attivi
        listOf(leaderRed, leaderBlue).forEach {
            it.quiz.playing = true
        }

        // Procedi allo stato di nuova partita
        goto(NewGame)
    }
}

fun isUserLeft(user: User): Boolean {
    // Supponiamo che una posizione x negativa significhi che l'utente è a sinistra.
    return user.head.location.x < 0
}


