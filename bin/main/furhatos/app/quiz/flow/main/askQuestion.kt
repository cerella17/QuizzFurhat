package furhatos.app.quiz.flow.main

import furhatos.app.quiz.AnswerOption
import furhatos.app.quiz.DontKnow
import furhatos.app.quiz.RequestRepeatOptions
import furhatos.app.quiz.RequestRepeatQuestion
import furhatos.app.quiz.flow.Parent
import furhatos.app.quiz.questions.QuestionSet
import furhatos.app.quiz.setting.nextPlaying
import furhatos.app.quiz.setting.notQuestioned
import furhatos.app.quiz.setting.playing
import furhatos.app.quiz.setting.quiz
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.RequestRepeat

val AskQuestion: State = state(parent = Parent) {
    var failedAttempts = 0

    onEntry {
        failedAttempts = 0

        // Imposta le frasi di riconoscimento vocale in base alle risposte della domanda corrente
        furhat.setSpeechRecPhrases(QuestionSet.current.speechPhrases)

        // Poni la domanda seguita dalle opzioni
        furhat.ask(QuestionSet.current.text + " " + QuestionSet.current.getOptionsString())
    }

    // Qui ripetiamo la domanda
    onReentry {
        failedAttempts = 0
        furhat.ask("La domanda era, ${QuestionSet.current.text} ${QuestionSet.current.getOptionsString()}")
    }

    // L'utente risponde con una delle alternative
    onResponse<AnswerOption> {
        val answer = it.intent

        // Se l'utente risponde correttamente, incrementa il punteggio della squadra e congratula la squadra
        if (answer.correct) {
            furhat.gesture(Gestures.Smile)
            users.current.quiz.score++
            random(
                { furhat.say("Ottimo! Quella era la risposta ${furhat.voice.emphasis("giusta")}, ora hai un punteggio di ${users.current.quiz.score}") },
                { furhat.say("Quella era ${furhat.voice.emphasis("corretta")}, ora hai un punteggio di ${users.current.quiz.score}") }
            )
            /*
            Se l'utente risponde in modo errato, diamo un'altra possibilità a un altro utente presente nel gioco.
            Se effettivamente chiediamo a un altro giocatore, la furhat.ask() interrompe il resto dell'handler.
             */
        } else {
            furhat.gesture(Gestures.BrowFrown)
            furhat.say("Mi dispiace, quella era ${furhat.voice.emphasis("sbagliata")}")

            // Teniamo traccia di quale utente ha risposto a quale domanda per non chiedere la stessa domanda allo stesso utente
            users.current.quiz.questionsAsked.add(QuestionSet.current.text)

            /* Trova un altro utente che non ha risposto a questa domanda e, se esiste, gliela chiede.
             Per il flusso dell'abilità, continueremo a chiedere al nuovo utente la prossima domanda attraverso il
             flag shouldChangeUser = false.
             */
            val availableUsers = users.notQuestioned(QuestionSet.current.text)
            if (availableUsers.isNotEmpty()) {
                furhat.attend(availableUsers.first())
                shouldChangeUser = false
                furhat.ask("Forse tu conosci la risposta?")
            }
        }

        // Controlla se il gioco è finito e, in caso contrario, passa a una nuova domanda
        if (++rounds >= maxRounds) {
            furhat.say("Quella era l'ultima domanda")
            goto(EndGame)
        } else {
            goto(NewQuestion)
        }
    }

    // Gli utenti rispondono che non sanno
    onResponse<DontKnow> {
        furhat.say("Peccato. Ecco la prossima domanda")
        goto(NewQuestion)
    }

    onResponse<RequestRepeat> {
        reentry()
    }

    onResponse<RequestRepeatQuestion> {
        furhat.gesture(Gestures.BrowRaise)
        furhat.ask(QuestionSet.current.text)
    }

    // L'utente vuole sentire di nuovo le opzioni
    onResponse<RequestRepeatOptions> {
        furhat.gesture(Gestures.Surprise)
        random(
            { furhat.ask("Le opzioni sono ${QuestionSet.current.getOptionsString()}") },
            { furhat.ask(QuestionSet.current.getOptionsString()) }
        )
    }

    // Se non riceviamo risposta, assumiamo che l'utente sia stato troppo lento
    onNoResponse {
        random(
            { furhat.say("Troppo lento! Ecco la prossima domanda") },
            { furhat.say("Un po' troppo lento amico! Preparati per la prossima domanda") }
        )
        goto(NewQuestion)
    }

    /* Se riceviamo una risposta che non corrisponde a nessuna alternativa o a nessuno degli handler sopra,
       tracciamo quante volte è successo di seguito e diamo loro due tentativi in più e alla fine andiamo avanti se ancora non capiamo.
     */
    onResponse {
        failedAttempts++
        when (failedAttempts) {
            1 -> furhat.ask("Non ho capito, scusa. Riprova!")
            2 -> {
                furhat.say("Scusa, ancora non ho capito")
                furhat.ask("Le opzioni sono ${QuestionSet.current.getOptionsString()}")
            }
            else -> {
                furhat.say("Non riesco ancora a capire. Proviamo una nuova domanda")
                shouldChangeUser = false
                goto(NewQuestion)
            }
        }
    }
}

val NewQuestion = state(parent = Parent) {
    onEntry {
        /*
            Se ci sono più di un giocatore, determiniamo a quale utente rivolgerci successivamente qui, basandoci sul booleano shouldChangeUser
         */
        if (users.playing().count() > 1) {
            if (shouldChangeUser) {
                val nextUser = users.nextPlaying()
                furhat.attend(nextUser)
                random(
                    { furhat.say("La prossima è per te") },
                    { furhat.say("Adesso tocca a te") },
                    { furhat.say("Ora per te") }
                )
            } else {
                shouldChangeUser = true
                random(
                    { furhat.say("Continui tu") },
                    { furhat.say("Ecco la prossima") },
                    { furhat.say("Eccone un'altra") }
                )
            }
        }
        if (!users.current.isAttendingFurhat) {
            furhat.say {
                random {
                    block {
                        +"Ma allora voglio che tu presti attenzione"
                        +Gestures.BigSmile
                    }
                    +"Guardami, ora sono io il capitano"
                    +"Potresti prestare attenzione a me"
                }
            }
        }
        // Poni una nuova domanda
        QuestionSet.next()
        goto(AskQuestion)
    }
}

