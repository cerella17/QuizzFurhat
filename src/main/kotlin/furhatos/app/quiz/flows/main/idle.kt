package furhatos.app.quiz.flows.main

import furhatos.app.quiz.intents.ImReady
import furhatos.app.quiz.setting.*
import furhatos.flow.kotlin.*
import furhatos.records.User
import kotlin.random.Random

// Stato iniziale
val Idle: State = state {
    onEntry {
        // TODO: OnEntry if someone is visible, greet them
    }
    onUserEnter {
        // TODO: OnUserEnter, glance and greet them

        // If there are more than 2 users, say that Furhat is ready to start the quiz
        if (users.count >= 2) {
            furhat.say("Ciao a tutti! Sono pronto per iniziare il quiz.")
            furhat.say("Ditemi quando siete pronti.")
            furhat.listen()
        }
    }
    onResponse<ImReady> {
        // If the user says they are ready, start the quiz
        goto(ExplainRules)
    }
    onResponse {
        // If the user says something else, listen again
        furhat.listen()
    }
    onNoResponse {
        // If the user doesn't say anything, listen again
        random(
            { furhat.say("Dai su, ditemi quando siete pronti!", async = true) },
            { furhat.say("Sono pronto, ditemi quando siete pronti!", async = true) },
            { furhat.say("Sono qui, ditemi quando siete pronti!", async = true) }
        )
        furhat.listen()
    }
}

// Spiega le regole
val ExplainRules: State = state {
    onEntry {
        users.setSimpleEngagementPolicy(EngagementDistanceInGame, EngagementDistanceInGame, EngagementMaxUsersInGame)

        // TODO: Tell them the rules of the quiz

        furhat.say("Ok, iniziamo!")

        goto(PreQuiz)
    }
}

// Ha il compito di assegnare le squadre
val PreQuiz: State = state {
    onEntry {
        // Aspetta che i capi gruppi si facciano avanti
        while (users.count < 2) {
            furhat.say(frasiAttesaCapoGruppi[Random.nextInt(frasiAttesaCapoGruppi.size - 1)])
            delay(2000) // Aspetta un po' prima di controllare di nuovo
        }

        // Gestisci il caso in cui ci sono più persone nel range
        while (users.count > 2) {
            furhat.say(frasiTroppePersone[Random.nextInt(frasiTroppePersone.size)])
            delay(2000)
        }

        // Assegna le squadre
        if (users.list.size != 2)
            reentry()

        goto(assignTeamToLeaders(users.list[0], users.list[1]))
    }
}

fun assignTeamToLeaders(user1: User, user2: User) = state {
    onEntry {
        // Assegna red al primo e blue al secondo
        user1.gameData.team = TeamEnum.RED
        user2.gameData.team = TeamEnum.BLUE

        // Guarda user1 e chiedi il nome
        furhat.attend(user1)
        furhat.say("Ciao, tu sarai il capo gruppo della squadra rossa, come ti chiami?")
        furhat.listen()
    }
    onResponse {
        when (users.current.gameData.team) {
            TeamEnum.RED -> {
                furhat.say("Ciao ${it.text}, tu sarai il capo gruppo della squadra rossa.")
                user2.gameData.name = it.text

                // Guarda user2 e chiedi il nome
                furhat.attend(user2)
                furhat.say("Ciao, tu sarai il capo gruppo della squadra blue, come ti chiami?")
                furhat.listen()
            }

            TeamEnum.BLUE -> {
                furhat.say("Ciao ${it.text}, tu sarai il capo gruppo della squadra blue.")
                user2.gameData.name = it.text
                goto(QuizGame)
            }

            else -> {
                furhat.say("Mi dispiace, c'è stato un errore.")
                goto(Idle)
            }
        }
    }
}

val QuizGame: State = state {
    onEntry {
        furhat.say("QUIZ STATE")
    }
}


// frasi in attesa che i capo gruppi entrino
val frasiAttesaCapoGruppi = listOf(
    "Sto aspettando che i capi gruppi facciano un passo in avanti.",
    "Per favore, i capi gruppi si avvicinino.",
    "Aspetto che i capi gruppi si facciano avanti.",
    "Capi gruppi, potete fare un passo avanti per favore?",
    "Attendo che i capi gruppi si presentino.",
    "I capi gruppi possono venire avanti, per favore?",
    "Sto aspettando che i capi gruppi vengano avanti.",
    "Per favore, i capi gruppi facciano un passo avanti."
)

// frasi per allontanare le persone che non sono capo gruppo
val frasiTroppePersone = listOf(
    "Tutte le persone al di fuori dei capi gruppi si allontanino.",
    "Per favore, solo i capi gruppi restino vicini.",
    "Chiedo a chi non è capo gruppo di fare un passo indietro.",
    "Solo i capi gruppi dovrebbero essere qui, per favore allontanatevi.",
    "Chi non è capo gruppo, per favore si allontani.",
    "Solo i capi gruppi devono rimanere, gli altri per favore si allontanino.",
    "Per favore, chi non è capo gruppo faccia un passo indietro.",
    "Solo i capi gruppi possono rimanere vicini, gli altri si allontanino."
)