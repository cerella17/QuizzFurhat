package furhatos.app.quiz.flows.main

import furhatos.app.quiz.core.QuizGameManager
import furhatos.app.quiz.core.UserData
import furhatos.app.quiz.events.*
import furhatos.app.quiz.intents.AnswerOption
import furhatos.app.quiz.intents.ImReady
import furhatos.app.quiz.setting.EngagementDistanceInGame
import furhatos.app.quiz.setting.EngagementMaxUsersInGame
import furhatos.app.quiz.setting.TeamEnum
import furhatos.app.quiz.setting.initEngagementDistance
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.Yes
import furhatos.records.User
import kotlin.random.Random

// Stato iniziale
val Idle: State = state {
    onEntry {
        if (users.count >= 1) {
            furhat.attend(users.random)
            furhat.say("Ciao!")
        }

        // If there are more than 2 users, say that Furhat is ready to start the quiz
        if (users.count >= 2) {
            furhat.say("Ciao a tutti! Sono pronto per iniziare il quiz.")
            furhat.say("Ditemi quando siete pronti.")
            furhat.listen()
        }
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
        if (users.count >= 2)
            furhat.listen()
    }
}

// Spiega le regole
val ExplainRules: State = state {
    onEntry {
        users.setSimpleEngagementPolicy(EngagementDistanceInGame, EngagementDistanceInGame, EngagementMaxUsersInGame)

        // TODO: Tell them the rules of the quiz
        furhat.say {
            +glance(users.random, duration = 20000)
            +"Benvenuti al quiz! Vi farò ${QuizGameManager.maxRounds} domande a scelta multipla."
            +glance(users.random, duration = 20000)
            +"Ogni domanda avrà diverse opzioni di risposta. Dovrete scegliere quella corretta."
            +glance(users.random, duration = 20000)
            +"In caso di risposta corretta, la rispettiva squadra guadagnerà un punto."
            +glance(users.random, duration = 20000)
            +"Avrete ${QuizGameManager.timeForQuestionTimeout / 1000} secondi per discutere con la vostra squadra prima di dare la risposta."
            +glance(users.random, duration = 20000)
        }
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
    var currentTeam = TeamEnum.RED
    onEntry {
        // Guarda user1 e chiedi il nome
        furhat.attend(user1)
        furhat.say("Ciao, come ti chiami?")
        furhat.listen()
    }
    onResponse {
        when (currentTeam) {
            TeamEnum.RED -> {
                furhat.say("Ciao ${it.text}, tu sarai il capo gruppo della squadra rossa.")
                QuizGameManager.redLeader = UserData(user = user1, name = it.text, score = 0)

                // Guarda user2 e chiedi il nome
                currentTeam = TeamEnum.BLUE
                furhat.attend(user2)
                furhat.say("Ciao, tu invece, come ti chiami?")
                furhat.listen()
            }

            TeamEnum.BLUE -> {
                furhat.say("Ciao ${it.text}, tu sarai il capo gruppo della squadra blu.")
                QuizGameManager.blueLeader = UserData(user = user2, name = it.text, score = 0)
                goto(QuizGameInit)
            }
        }
    }
    onNoResponse {
        furhat.say("Non ho capito.")
        furhat.listen()
    }
}

val QuizGameInit: State = state {
    onEntry {
        parallel {
            send(
                NewGameEvent(
                    redLeaderName = QuizGameManager.redLeader!!.name,
                    blueLeaderName = QuizGameManager.blueLeader!!.name,
                    maxRounds = QuizGameManager.maxRounds
                )
            )
        }
        // Aumento la distanza di engagement per il quiz
        users.setSimpleEngagementPolicy(initEngagementDistance, initEngagementDistance, EngagementMaxUsersInGame)
        furhat.say("Perfetto, iniziamo!")
        goto(QuizGameNewQuestion)
    }
}

val QuizGameNewQuestion: State = state {
    onEntry {
        QuizGameManager.QuestionSet.next()
        QuizGameManager.round++
        QuizGameManager.nextTurn()
        // send event to GUI
        parallel {
            send(
                SyncInformationEvent(
                    round = QuizGameManager.round,
                    redScore = QuizGameManager.redLeader!!.score,
                    blueScore = QuizGameManager.blueLeader!!.score,
                )
            )
        }

        delay(2000)
        val currentTeamTurnString: String = when (QuizGameManager.currentTurnTeam) {
            TeamEnum.RED -> "rossa"
            TeamEnum.BLUE -> "blu"
        }
        furhat.attend(
            when (QuizGameManager.currentTurnTeam) {
                TeamEnum.RED -> QuizGameManager.redLeader!!.user
                TeamEnum.BLUE -> QuizGameManager.blueLeader!!.user
            }
        )
        furhat.say("Domanda per la squadra $currentTeamTurnString.")
        goto(QuizGameAskQuestion)
    }
}

val QuizGameAskQuestion: State = state {
    onEntry {
        val currentQ = QuizGameManager.QuestionSet.current
        val questionText = currentQ.toQuestionTextSpeech()
        // send event to GUI
        parallel {
            send(
                AskQuestionEvent(
                    currentQ.question,
                    currentQ.options,
                    QuizGameManager.currentTurnTeam.toString(),
                    QuizGameManager.timeForQuestionTimeout
                )
            )
        }

        // Ask the question
        furhat.say(questionText)
        furhat.say("Avvisatemi quando siete pronti.")

        furhat.attendNobody()

        furhat.listen(timeout = QuizGameManager.timeForQuestionTimeout)
    }
    onTime(delay = QuizGameManager.timeForQuestionTimeout) {
        furhat.say("Tempo scaduto!")
        furhat.stopListening()
        goto(QuizGameListenForAnswer)
    }
    onResponse<ImReady> {
        goto(QuizGameListenForAnswer)
    }
    onResponse { furhat.listen(timeout = 30000) }
    onNoResponse { furhat.listen(timeout = 30000) }
}

val QuizGameListenForAnswer: State = state {
    onEntry {
        // guardare il capo gruppo della squadra che deve rispondere
        furhat.attend(
            when (QuizGameManager.currentTurnTeam) {
                TeamEnum.RED -> QuizGameManager.redLeader!!.user
                TeamEnum.BLUE -> QuizGameManager.blueLeader!!.user
            }
        )
        furhat.say("Qual è la risposta?")
        furhat.listen()
    }
    onResponse<AnswerOption> {
        if (QuizGameManager.QuestionSet.current.isCorrect(it.text)) {
            parallel {
                send(
                    QuestionAnswerEvent(
                        it.text,
                        QuizGameManager.QuestionSet.current.correctAnswer(),
                        true
                    )
                )
            }
            random(
                { furhat.gesture(Gestures.Surprise, async = true) },
                { furhat.gesture(Gestures.BigSmile, async = true) }
            )
            furhat.say("Risposta corretta!")
            if (QuizGameManager.currentTurnTeam == TeamEnum.RED) {
                QuizGameManager.redLeader!!.score++
            } else {
                QuizGameManager.blueLeader!!.score++
            }
        } else {
            parallel {
                send(
                    QuestionAnswerEvent(
                        it.text,
                        QuizGameManager.QuestionSet.current.correctAnswer(),
                        false
                    )
                )
            }
            random(
                { furhat.gesture(Gestures.Shake, async = true) },
                { furhat.gesture(Gestures.ExpressFear, async = true) },
                { furhat.gesture(Gestures.ExpressSad, async = true) }
            )
            // frasi di risposta sbagliata
            furhat.say(frasiRispostaSbagliata[Random.nextInt(frasiRispostaSbagliata.size - 1)])
        }
        if (QuizGameManager.round < QuizGameManager.maxRounds) {
            furhat.attendNobody()
            delay(1000)
            goto(QuizGameNewQuestion)
        } else {
            goto(QuizGameEnd)
        }
    }
    onResponse {
        furhat.say("Non ho capito.")
        // guardare il capo gruppo della squadra che deve rispondere
        furhat.attend(
            when (QuizGameManager.currentTurnTeam) {
                TeamEnum.RED -> QuizGameManager.redLeader!!.user
                TeamEnum.BLUE -> QuizGameManager.blueLeader!!.user
            }
        )
        furhat.listen()
        reentry()
    }
    onNoResponse {
        furhat.say(frasiTroppoLento[Random.nextInt(frasiTroppoLento.size - 1)])
        if (QuizGameManager.round < QuizGameManager.maxRounds) {
            delay(1000)
            goto(QuizGameNewQuestion)
        } else {
            goto(QuizGameEnd)
        }
    }
}

val QuizGameEnd: State = state {
    onEntry {
        furhat.attendNobody()
        parallel {
            send(
                EndGameEvent(
                    redScore = QuizGameManager.redLeader!!.score,
                    blueScore = QuizGameManager.blueLeader!!.score,
                )
            )
        }
        furhat.say("Il gioco è finito!")
        if (QuizGameManager.redLeader!!.score > QuizGameManager.blueLeader!!.score) {
            furhat.say("La squadra rossa ha vinto!")
        } else if (QuizGameManager.redLeader!!.score < QuizGameManager.blueLeader!!.score) {
            furhat.say("La squadra blu ha vinto!")
        } else {
            furhat.say("Pareggio!")
        }

        // re-game
        furhat.say("Volete giocare ancora?")
        furhat.listen()
    }
    onResponse<Yes> {
        QuizGameManager.resetGame()
        goto(QuizGameInit)
    }
    onResponse {
        furhat.say("Arrivederci!")
    }
    onNoResponse {
        furhat.say("Arrivederci!")
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

// frasi di risposta sbagliata
val frasiRispostaSbagliata = listOf(
    "Oh no, risposta sbagliata! Ma non ti preoccupare, succede!",
    "Purtroppo no, questa non è la risposta giusta. Ritenta!",
    "Ops! Non è corretto. Ma sei sempre un campione per averci provato!",
    "Oh oh, risposta sbagliata! Ma va bene così, continua a giocare!",
    "Quasi! Ma non ci siamo. Non arrenderti!",
    "Non è corretto! Ma chi non prova non sbaglia!",
    "Eh no, non è giusto. Ma sei sulla strada giusta, continua così!",
    "Niente da fare, risposta sbagliata. Ma sei qui per divertirti, giusto?",
    "Ahimè, questa non è la risposta giusta. Ma va bene, la prossima volta andrà meglio!",
    "Nope, non è corretto. Ma il divertimento è nel giocare!",
    "Sbagliato! Hai per caso bisogno di un caffè?",
    "Oh no, hai sbagliato! Stai giocando con la testa o con i piedi?",
    "Ops, risposta sbagliata. Forse dovresti chiedere aiuto a Google!",
    "Non è giusto! Hai studiato su Wikipedia?",
    "Ahia, risposta sbagliata. Hai dormito durante le lezioni?"
)

// frasi per quando il capo gruppo è troppo lento a rispondere
val frasiTroppoLento = listOf(
    "Troppo lento! Ecco la prossima domanda",
    "Un po' troppo lento! Preparati per la prossima domanda",
    "Devi essere più veloce! Passiamo alla prossima domanda",
    "Risposta non pervenuta. Passiamo oltre",
    "Nessuna risposta! Prossima domanda in arrivo",
    "Troppo tempo per rispondere. Passiamo alla prossima domanda",
    "Non hai risposto in tempo. Ecco la prossima domanda",
    "Sei stato troppo lento! Prossima domanda",
    "Risposta non ricevuta. Prossima domanda",
    "Nessuna risposta registrata. Passiamo alla prossima domanda"
)
