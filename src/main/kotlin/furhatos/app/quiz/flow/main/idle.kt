package furhatos.app.quiz.flow.main

import furhatos.app.quiz.flow.Parent
import furhatos.app.quiz.setting.*
import furhatos.flow.kotlin.*
import furhatos.records.User

val Idle: State = state {
    onEntry {
        /*
            Loop through all (potentially) interested users.
            Goto calls are used since users may enter and leave
            while we are querying other users and we want to
            ask all users before moving on. I.e we want to update the
            users.interested() list of users.
          */
        users.interested().forEach {
            furhat.say("welcome")
            furhat.attend(it)
            goto(QueryPerson(it))
        }

    }
    onUserEnter {
        countpeople++
        furhat.say("ciao")
        if(countpeople > 1)
            goto(QueryPerson(it))
    }
    onUserLeave(instant = true) {
        countpeople--
        if (users.count < 0) {
            furhat.attendAll()
        }
    }
    onResponse{
        reentry()
    }
    onNoResponse {
        reentry()
    }
}

// Variables
val maxRounds = 5
var rounds = 0
var shouldChangeUser = true
var playing = false
var countpeople = 0

fun QueryPerson(user: User) = state(parent = Parent) {
    onEntry {
        if (users.count > 0) {
            furhat.say("benvenuti ragazzi")
            val longtime: Long = 4000
            delay(longtime)
            furhat.say("I capi gruppi scelti facciano due passi in avanti")

            users.setSimpleEngagementPolicy(distanceToEngageForQuiz, maxNumberOfUsers)

            // Wait for two users to step forward
            var leaders: List<User> = listOf()
            while (leaders.size < 2) {
                leaders = users.list.filter { it.head.location.z < 0.5 }
                if (leaders.size >= 2) break
                furhat.say("sto aspettando")
                delay(2000) // Wait a bit before checking again
            }

            //quando ci sono più persone nel range
            while (leaders.size > 2) {
                furhat.say("Tutte le persone al di fuori dei capi gruppi si allontanino")
                delay(3000)
                if(leaders.size <= 2) break

            }

            // Assign teams based on position
            if (isUserLeft(leaders[0])) {
                leaders[0].quiz.team = "red"
                leaders[1].quiz.team = "blue"
            } else {
                leaders[1].quiz.team = "red"
                leaders[0].quiz.team = "blue"
            }

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
        furhat.say("Piacere di conoscerti $nameBlue, capo squadra blu")

        // Set both leaders to playing
        listOf(leaderRed, leaderBlue).forEach {
            it.quiz.playing = true
        }

        // Proceed to the new game state
        goto(NewGame)
    }
}


fun isUserLeft(user: User): Boolean {
    return user.head.location.x > 0
}


