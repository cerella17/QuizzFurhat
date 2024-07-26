package furhatos.app.quiz.flow.main

import furhatos.app.quiz.AnswerOption
import furhatos.app.quiz.DontKnow
import furhatos.app.quiz.RequestRepeatOptions
import furhatos.app.quiz.RequestRepeatQuestion
import furhatos.app.quiz.nlu.ReadyIntent
import furhatos.app.quiz.flow.Parent
import furhatos.app.quiz.questions.QuestionSet
import furhatos.app.quiz.setting.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.RequestRepeat

var currentTeam = "red" // Traccia la squadra corrente per alternare le domande
var teamAnnounced = false // Traccia se l'annuncio della squadra è stato fatto
var scoreTeamRed = 0
var scoreTeamBlue = 0

var responseTimeout = 10000 // Durata del timeout per le risposte in millisecondi
var consultTime = 30000 // Tempo per la consultazione prima di ascoltare in millisecondi

val NewQuestion: State = state(parent = Parent) {
    onEntry {
        /*
            Se c'è più di un giocatore, determiniamo quale utente deve rispondere successivamente qui, basato sul booleano shouldChangeUser
         */
        val nextUser = if (currentTeam == "red") {
            users.redTeam().firstOrNull()
        } else {
            users.blueTeam().firstOrNull()
        }

        // Poni una nuova domanda
        QuestionSet.next()
        goto(AskQuestion) // Stato esplicito
    }
}

val AskQuestion: State = state(parent = Parent) {
    var failedAttempts = 0
    var readyToAnswer = false // Traccia se l'utente ha detto "pronto"

    onEntry {
        failedAttempts = 0
        readyToAnswer = false

        // Imposta le frasi di riconoscimento vocale in base alle risposte della domanda corrente
        furhat.setSpeechRecPhrases(QuestionSet.current.speechPhrases)

        // Annuncia per quale squadra è la domanda e aggiorna l'attenzione del robot
        val nextUser = if (currentTeam == "red") {
            users.redTeam().firstOrNull()
        } else {
            users.blueTeam().firstOrNull()
        }

        if (nextUser != null) {
            furhat.attend(nextUser)
        }

        furhat.say(
            when (currentTeam) {
                "red" -> "Tocca alla squadra rossa."
                "blue" -> "Tocca alla squadra blu."
                else -> throw IllegalStateException("La squadra corrente deve essere 'red' o 'blue'")
            }
        )
        teamAnnounced = true

        // Annuncia la domanda
        furhat.say("${QuestionSet.current.text} ${QuestionSet.current.getOptionsString()}")

        // Inizia ad ascoltare per "pronto" o "sono pronto" durante il tempo di consultazione
        furhat.listen()
    }

    onResponse<ReadyIntent> {
        readyToAnswer = true
        furhat.say("Dimmi la risposta.")
        goto(ListenForAnswer)
    }

    onResponse {
        // Ignora tutte le altre risposte durante il tempo di consultazione
        if (!readyToAnswer) {
            furhat.listen()
        }
    }

    onTime(delay = consultTime) {
        if (!readyToAnswer) {
            furhat.say("Adesso dovete rispondere.")
            goto(ListenForAnswer)
        }
    }

    // L'utente risponde con una delle alternative
    onResponse<AnswerOption> {
        val answer = it.intent

        // Se l'utente risponde correttamente, incrementa il punteggio della squadra e congratulati con la squadra
        if (answer.correct) {
            furhat.gesture(Gestures.Smile)
            val user = users.current
            if (currentTeam == "red") {
                user.quiz.scoreTeamRed += 1
                furhat.say("Ottimo! Questa era la risposta ${furhat.voice.emphasis("giusta")} per la squadra rossa, ora avete un punteggio di ${user.quiz.scoreTeamRed}")
            } else {
                user.quiz.scoreTeamBlue += 1
                furhat.say("Ottimo! Questa era la risposta ${furhat.voice.emphasis("giusta")} per la squadra blu, ora avete un punteggio di ${user.quiz.scoreTeamBlue}")
            }
            // Alterna squadra
            currentTeam = if (currentTeam == "red") "blue" else "red"
            teamAnnounced = false // Resetta il flag di annuncio per la prossima squadra
            // Controlla se il gioco è finito e in caso contrario, passa a una nuova domanda
            if (++rounds >= maxRounds) {
                furhat.say("Questa era l'ultima domanda")
                goto(EndGame) // Stato esplicito
            } else {
                goto(NewQuestion) // Stato esplicito
            }
        } else {
            furhat.gesture(Gestures.BrowFrown)
            random(
                { furhat.say("Oh no, risposta ${furhat.voice.emphasis("sbagliata")}! Ma non ti preoccupare, succede!") },
                { furhat.say("Purtroppo no, questa non è la risposta giusta. Ritenta!") },
                { furhat.say("Ops! Non è corretto. Ma sei sempre un campione per averci provato!") },
                { furhat.say("Oh oh, risposta sbagliata! Ma va bene così, continua a giocare!") },
                { furhat.say("Quasi! Ma non ci siamo. Non arrenderti!") },
                { furhat.say("Non è corretto! Ma chi non prova non sbaglia!") },
                { furhat.say("Eh no, non è giusto. Ma sei sulla strada giusta, continua così!") },
                { furhat.say("Niente da fare, risposta sbagliata. Ma sei qui per divertirti, giusto?") },
                { furhat.say("Ahimè, questa non è la risposta giusta. Ma va bene, la prossima volta andrà meglio!") },
                { furhat.say("Nope, non è corretto. Ma il divertimento è nel giocare!") },
                { furhat.say("Sbagliato! Hai per caso bisogno di un caffè?") },
                { furhat.say("Oh no, hai sbagliato! Stai giocando con la testa o con i piedi?") },
                { furhat.say("Ops, risposta sbagliata. Forse dovresti chiedere aiuto a Google!") },
                { furhat.say("Non è giusto! Hai studiato su Wikipedia?") },
                { furhat.say("Ahia, risposta sbagliata. Hai dormito durante le lezioni?") }
            )

            // Alterna squadra e passa a una nuova domanda
            currentTeam = if (currentTeam == "red") "blue" else "red"
            teamAnnounced = false // Resetta il flag di annuncio per la prossima squadra

            // Seleziona una nuova domanda per la squadra opposta
            goto(NewQuestion)
        }
    }

    // Gli utenti rispondono che non sanno
    onResponse<DontKnow> {
        furhat.say("Peccato. Ecco la prossima domanda")
        currentTeam = if (currentTeam == "red") "blue" else "red" // Alterna squadra
        teamAnnounced = false // Resetta il flag di annuncio
        // Seleziona una nuova domanda
        goto(NewQuestion) // Stato esplicito
    }

    onResponse<RequestRepeat> {
        if (readyToAnswer) {
            furhat.ask("Adesso dovete rispondere.", timeout = responseTimeout)
        } else {
            reentry()
        }
    }

    onResponse<RequestRepeatQuestion> {
        furhat.gesture(Gestures.BrowRaise)
        furhat.ask(QuestionSet.current.text, timeout = responseTimeout)
    }

    // L'utente vuole sentire di nuovo le opzioni
    onResponse<RequestRepeatOptions> {
        furhat.gesture(Gestures.Surprise)
        random(
            { furhat.ask("Le opzioni sono ${QuestionSet.current.getOptionsString()}", timeout = responseTimeout) },
            { furhat.ask(QuestionSet.current.getOptionsString(), timeout = responseTimeout) }
        )
    }

    // Se non riceviamo risposta, assumiamo che l'utente sia stato troppo lento
    onNoResponse {
        if (readyToAnswer) {
            random(
                { furhat.say("Troppo lento! Ecco la prossima domanda") },
                { furhat.say("Un po' troppo lento! Preparati per la prossima domanda") },
                { furhat.say("Devi essere più veloce! Passiamo alla prossima domanda") },
                { furhat.say("Risposta non pervenuta. Passiamo oltre") },
                { furhat.say("Nessuna risposta! Prossima domanda in arrivo") },
                { furhat.say("Troppo tempo per rispondere. Passiamo alla prossima domanda") },
                { furhat.say("Non hai risposto in tempo. Ecco la prossima domanda") },
                { furhat.say("Sei stato troppo lento! Prossima domanda") },
                { furhat.say("Risposta non ricevuta. Prossima domanda") },
                { furhat.say("Nessuna risposta registrata. Passiamo alla prossima domanda") }
            )
            currentTeam = if (currentTeam == "red") "blue" else "red" // Alterna squadra
            teamAnnounced = false // Resetta il flag di annuncio
            // Seleziona una nuova domanda
            goto(NewQuestion)
        } else {
            furhat.listen()
        }
    }

    /* Se riceviamo una risposta che non corrisponde a nessuna alternativa o a nessuno degli handler sopra,
       tracciamo quante volte è successo di seguito e diamo loro due tentativi in più e alla fine andiamo avanti se ancora non capiamo.
     */
    onResponse {
        if (readyToAnswer) {
            failedAttempts++
            when (failedAttempts) {
                1 -> furhat.ask("Non ho capito, scusa. Riprova!", timeout = responseTimeout)
                2 -> {
                    furhat.say("Mi dispiace, ancora non ho capito")
                    furhat.ask("Le opzioni sono ${QuestionSet.current.getOptionsString()}", timeout = responseTimeout)
                }
                else -> {
                    furhat.say("Non riesco ancora a capire. Proviamo una nuova domanda")
                    currentTeam = if (currentTeam == "red") "blue" else "red" // Alterna squadra
                    teamAnnounced = false // Resetta il flag di annuncio
                    // Seleziona una nuova domanda
                    goto(NewQuestion) // Stato esplicito
                }
            }
        } else {
            furhat.listen()
        }
    }
}

val ListenForAnswer: State = state(parent = Parent) {
    onEntry {
        furhat.listen(timeout = responseTimeout)
    }

    onResponse<AnswerOption> {
        val answer = it.intent

        // Se l'utente risponde correttamente, incrementa il punteggio della squadra e congratulati con la squadra
        if (answer.correct) {
            furhat.gesture(Gestures.Smile)
            val user = users.current
            if (currentTeam == "red") {
                user.quiz.scoreTeamRed += 1
                furhat.say("Ottimo! Questa era la risposta ${furhat.voice.emphasis("giusta")} per la squadra rossa, ora avete un punteggio di ${user.quiz.scoreTeamRed}")
            } else {
                user.quiz.scoreTeamBlue += 1
                furhat.say("Ottimo! Questa era la risposta ${furhat.voice.emphasis("giusta")} per la squadra blu, ora avete un punteggio di ${user.quiz.scoreTeamBlue}")
            }
            // Alterna squadra
            currentTeam = if (currentTeam == "red") "blue" else "red"
            teamAnnounced = false // Resetta il flag di annuncio per la prossima squadra
            // Controlla se il gioco è finito e in caso contrario, passa a una nuova domanda
            if (++rounds >= maxRounds) {
                furhat.say("Questa era l'ultima domanda")
                goto(EndGame) // Stato esplicito
            } else {
                goto(NewQuestion) // Stato esplicito
            }
        } else {
            furhat.gesture(Gestures.BrowFrown)
            random(
                { furhat.say("Oh no, risposta ${furhat.voice.emphasis("sbagliata")}! Ma non ti preoccupare, succede!") },
                { furhat.say("Purtroppo no, questa non è la risposta giusta. Ritenta!") },
                { furhat.say("Ops! Non è corretto. Ma sei sempre un campione per averci provato!") },
                { furhat.say("Oh oh, risposta sbagliata! Ma va bene così, continua a giocare!") },
                { furhat.say("Quasi! Ma non ci siamo. Non arrenderti!") },
                { furhat.say("Non è corretto! Ma chi non prova non sbaglia!") },
                { furhat.say("Eh no, non è giusto. Ma sei sulla strada giusta, continua così!") },
                { furhat.say("Niente da fare, risposta sbagliata. Ma sei qui per divertirti, giusto?") },
                { furhat.say("Ahimè, questa non è la risposta giusta. Ma va bene, la prossima volta andrà meglio!") },
                { furhat.say("Nope, non è corretto. Ma il divertimento è nel giocare!") },
                { furhat.say("Sbagliato! Hai per caso bisogno di un caffè?") },
                { furhat.say("Oh no, hai sbagliato! Stai giocando con la testa o con i piedi?") },
                { furhat.say("Ops, risposta sbagliata. Forse dovresti chiedere aiuto a Google!") },
                { furhat.say("Non è giusto! Hai studiato su Wikipedia?") },
                { furhat.say("Ahia, risposta sbagliata. Hai dormito durante le lezioni?") }
            )

            // Alterna squadra e passa a una nuova domanda
            currentTeam = if (currentTeam == "red") "blue" else "red"
            teamAnnounced = false // Resetta il flag di annuncio per la prossima squadra

            // Seleziona una nuova domanda per la squadra opposta
            goto(NewQuestion)
        }
    }

    // Gli utenti rispondono che non sanno
    onResponse<DontKnow> {
        furhat.say("Peccato. Ecco la prossima domanda")
        currentTeam = if (currentTeam == "red") "blue" else "red" // Alterna squadra
        teamAnnounced = false // Resetta il flag di annuncio
        // Seleziona una nuova domanda
        goto(NewQuestion) // Stato esplicito
    }

    onResponse<RequestRepeat> {
        furhat.ask("Adesso dovete rispondere.", timeout = responseTimeout)
    }

    onResponse<RequestRepeatQuestion> {
        furhat.gesture(Gestures.BrowRaise)
        furhat.ask(QuestionSet.current.text, timeout = responseTimeout)
    }

    // L'utente vuole sentire di nuovo le opzioni
    onResponse<RequestRepeatOptions> {
        furhat.gesture(Gestures.Surprise)
        random(
            { furhat.ask("Le opzioni sono ${QuestionSet.current.getOptionsString()}", timeout = responseTimeout) },
            { furhat.ask(QuestionSet.current.getOptionsString(), timeout = responseTimeout) }
        )
    }

    onNoResponse {
        furhat.say("Non ho sentito nessuna risposta. Prossima domanda.")
        currentTeam = if (currentTeam == "red") "blue" else "red" // Alterna squadra
        teamAnnounced = false // Resetta il flag di annuncio
        // Seleziona una nuova domanda
        goto(NewQuestion) // Stato esplicito
    }

    onResponse {
        // Ignora tutte le altre risposte
        furhat.listen(timeout = responseTimeout)
    }
}


