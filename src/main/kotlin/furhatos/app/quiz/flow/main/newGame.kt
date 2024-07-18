package furhatos.app.quiz.flow.main

import furhatos.app.quiz.flow.Parent
import furhatos.app.quiz.questions.QuestionSet
import furhatos.app.quiz.setting.playing
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users

val NewGame = state(parent = Parent) {
    onEntry {
        playing = true
        rounds = 0

        furhat.say("Ti farò $maxRounds domande a scelta multipla. E vedremo quanti punti riuscirai a ottenere.")
        if (users.count > 1) {
            furhat.say("Se rispondi in modo errato, la domanda passerà alla persona successiva.")
        }

        furhat.say("Bene, iniziamo!")
        QuestionSet.next()
        goto(AskQuestion)
    }
}
