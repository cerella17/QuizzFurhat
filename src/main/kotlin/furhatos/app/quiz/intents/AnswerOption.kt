package furhatos.app.quiz.intents

import furhatos.app.quiz.questions.QuestionManager
import furhatos.nlu.EnumEntity
import furhatos.nlu.EnumItem
import furhatos.util.Language

class AnswerOption : EnumEntity {
    private var correct: Boolean = false

    // Ogni entità e intento necessita di un costruttore vuoto.
    constructor() {
    }

    // Poiché stiamo sovrascrivendo il valore, dobbiamo usare questo costruttore personalizzato
    constructor(correct: Boolean, value: String) {
        this.correct = correct
        this.value = value
    }

    override fun getEnumItems(lang: Language): List<EnumItem> {
        return QuestionManager.current.answers.map {
            EnumItem(
                AnswerOption(
                    QuestionManager.current.correctAnswers.contains(it), it
                ), it
            )
        }
    }
}