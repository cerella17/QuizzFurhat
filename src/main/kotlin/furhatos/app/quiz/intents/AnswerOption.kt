package furhatos.app.quiz.intents

import furhatos.app.quiz.core.QuizGameManager
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
        // merge QuizGameManager.QuestionSet.current.options and QuizGameManager.QuestionSet.current.answers
        val all = listOf(
            QuizGameManager.QuestionSet.current.options,
            QuizGameManager.QuestionSet.current.answers
        ).flatten().distinct()
        return all.map {
            EnumItem(
                AnswerOption(
                    QuizGameManager.QuestionSet.current.isCorrect(it), it
                ), it
            )
        }
    }
}