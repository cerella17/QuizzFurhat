package furhatos.app.quiz.nlu

import furhatos.nlu.Intent
import furhatos.util.Language

class ReadyIntent : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("pronto", "sono pronto", "siamo pronti", "siamo pronto","sono pronta","pronta")
    }
}
