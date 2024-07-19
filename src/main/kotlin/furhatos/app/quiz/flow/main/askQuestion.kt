package furhatos.app.quiz.flow.main

import furhatos.app.quiz.AnswerOption
import furhatos.app.quiz.DontKnow
import furhatos.app.quiz.RequestRepeatOptions
import furhatos.app.quiz.RequestRepeatQuestion
import furhatos.app.quiz.flow.Parent
import furhatos.app.quiz.questions.QuestionSet
import furhatos.app.quiz.setting.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.RequestRepeat

var currentTeam = "red" // Track the current team to alternate questions
var teamAnnounced = false // Track if the team announcement has been made
var questionUnanswered = false // Track if the question is unanswered
var scoreTeamRed = 0
var scoreTeamBlue = 0

var responseTimeout = 30000 // Timeout duration for responses in milliseconds

val AskQuestion: State = state(parent = Parent) {
    var failedAttempts = 0

    onEntry {
        failedAttempts = 0

        // Set speech rec phrases based on the current question's answers
        furhat.setSpeechRecPhrases(QuestionSet.current.speechPhrases)

        // Announce which team the question is for, if not already announced
        if (!teamAnnounced) {
            furhat.say(
                when (currentTeam) {
                    "red" -> "Tocca alla squadra rossa."
                    "blue" -> "Tocca alla squadra blu."
                    else -> throw IllegalStateException("Current team must be either 'red' or 'blue'")
                }
            )
            teamAnnounced = true
        }

        // Ask the question followed by the options
        if (questionUnanswered) {
            furhat.ask("La domanda era, ${QuestionSet.current.text} ${QuestionSet.current.getOptionsString()}", timeout = responseTimeout)
        } else {
            furhat.ask("${QuestionSet.current.text} ${QuestionSet.current.getOptionsString()}", timeout = responseTimeout)
        }
    }

    // Here we re-state the question
    onReentry {
        failedAttempts = 0

        // Announce which team the question is for, if not already announced
        if (!teamAnnounced) {
            furhat.say(
                when (currentTeam) {
                    "red" -> "Tocca alla squadra rossa."
                    "blue" -> "Tocca alla squadra blu."
                    else -> throw IllegalStateException("Current team must be either 'red' or 'blue'")
                }
            )
            teamAnnounced = true
        }

        furhat.ask("La domanda era, ${QuestionSet.current.text} ${QuestionSet.current.getOptionsString()}", timeout = responseTimeout)
    }

    // User is answering with any of the alternatives
    onResponse<AnswerOption> {
        val answer = it.intent

        // If the user answers correct, we up the team's score and congratulate the team
        if (answer.correct) {
            furhat.gesture(Gestures.Smile)
            if (currentTeam == "red") {
                scoreTeamRed += 1
                furhat.say("Ottimo! Questa era la risposta ${furhat.voice.emphasis("giusta")} per la squadra rossa, ora avete un punteggio di $scoreTeamRed")
            } else {
                scoreTeamBlue += 1
                furhat.say("Ottimo! Questa era la risposta ${furhat.voice.emphasis("giusta")} per la squadra blu, ora avete un punteggio di $scoreTeamBlue")
            }
            // Alternate team
            currentTeam = if (currentTeam == "red") "blue" else "red"
            teamAnnounced = false // Reset the announcement flag for the next team
            questionUnanswered = false // Reset the unanswered question flag
            // Check if the game has ended and if not, goes to a new question
            if (++rounds >= maxRounds) {
                furhat.say("Questa era l'ultima domanda")
                goto(EndGame) // Explicit state type
            } else {
                goto(NewQuestion) // Explicit state type
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

            // Alternate team
            currentTeam = if (currentTeam == "red") "blue" else "red"
            teamAnnounced = false // Reset the announcement flag for the next team
            questionUnanswered = true // Set the unanswered question flag

            // Re-ask the same question to the other team
            reentry()
        }
    }

    // The users answers that they don't know
    onResponse<DontKnow> {
        furhat.say("Peccato. Ecco la prossima domanda")
        currentTeam = if (currentTeam == "red") "blue" else "red" // Alternate team
        teamAnnounced = false // Reset the announcement flag
        questionUnanswered = false // Reset the unanswered question flag
        goto(NewQuestion) // Explicit state type
    }

    onResponse<RequestRepeat> {
        reentry()
    }

    onResponse<RequestRepeatQuestion> {
        furhat.gesture(Gestures.BrowRaise)
        furhat.ask(QuestionSet.current.text, timeout = responseTimeout)
    }

    // The user wants to hear the options again
    onResponse<RequestRepeatOptions> {
        furhat.gesture(Gestures.Surprise)
        random(
            { furhat.ask("Le opzioni sono ${QuestionSet.current.getOptionsString()}", timeout = responseTimeout) },
            { furhat.ask(QuestionSet.current.getOptionsString(), timeout = responseTimeout) }
        )
    }

    // If we don't get any response, we assume the user was too slow
    onNoResponse {
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
        currentTeam = if (currentTeam == "red") "blue" else "red" // Alternate team
        teamAnnounced = false // Reset the announcement flag
        questionUnanswered = true // Set the unanswered question flag
        reentry()
    }

    /* If we get a response that doesn't map to any alternative or any of the above handlers,
        we track how many times this has happened in a row and give them two more attempts and
        finally moving on if we still don't get it.
     */
    onResponse {
        failedAttempts++
        when (failedAttempts) {
            1 -> furhat.ask("Non ho capito, scusa. Riprova!", timeout = responseTimeout)
            2 -> {
                furhat.say("Mi dispiace, ancora non ho capito")
                furhat.ask("Le opzioni sono ${QuestionSet.current.getOptionsString()}", timeout = responseTimeout)
            }
            else -> {
                furhat.say("Non riesco ancora a capire. Proviamo una nuova domanda")
                currentTeam = if (currentTeam == "red") "blue" else "red" // Alternate team
                teamAnnounced = false // Reset the announcement flag
                questionUnanswered = false // Reset the unanswered question flag
                goto(NewQuestion) // Explicit state type
            }
        }
    }
}

val NewQuestion: State = state(parent = Parent) {
    onEntry {
        /*
            If more than one player, we determine what user to target next here, based on the shouldChangeUser boolean
         */
        val nextUser = if (currentTeam == "red") {
            users.redTeam().firstOrNull()
        } else {
            users.blueTeam().firstOrNull()
        }

        if (nextUser != null) {
            furhat.attend(nextUser)
            furhat.say(
                when (currentTeam) {
                    "red" -> "Ora tocca alla squadra rossa."
                    "blue" -> "Ora tocca alla squadra blu."
                    else -> throw IllegalStateException("Current team must be either 'red' or 'blue'")
                }
            )
            teamAnnounced = true // Set the announcement flag
        }

        // Ask new question
        QuestionSet.next()
        goto(AskQuestion) // Explicit state type
    }
}
