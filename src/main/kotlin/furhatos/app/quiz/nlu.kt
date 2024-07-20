package furhatos.app.quiz

import furhatos.app.quiz.questions.QuestionSet
import furhatos.nlu.EnumEntity
import furhatos.nlu.EnumItem
import furhatos.nlu.Intent
import furhatos.util.Language

class DontKnow : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "Non lo so",
            "Non so",
            "Nessuna idea",
            "Non ho idea"
        )
    }
}

class RequestRules : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "Quali sono le regole",
            "Come funziona"
        )
    }
}

class RequestRepeatQuestion : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "Qual era la domanda",
            "Puoi ripetere la domanda",
            "Qual era la domanda di nuovo"
        )
    }
}

class RequestRepeatOptions : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "Quali sono le opzioni",
            "Puoi ripetere le opzioni",
            "Quali erano le opzioni"
        )
    }
}

class AnswerOption : EnumEntity {

    var correct : Boolean = false

    // Ogni entità e intento necessita di un costruttore vuoto.
    constructor() {
    }

    // Poiché stiamo sovrascrivendo il valore, dobbiamo usare questo costruttore personalizzato
    constructor(correct : Boolean, value : String) {
        this.correct = correct
        this.value = value
    }

    override fun getEnumItems(lang: Language): List<EnumItem> {
        return QuestionSet.current.options;
    }

}
