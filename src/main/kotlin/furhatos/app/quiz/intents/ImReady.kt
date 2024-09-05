package furhatos.app.quiz.intents

import furhatos.nlu.Intent
import furhatos.util.Language

class ImReady : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "pronto",
            "pronta",
            "sono pronto",
            "sono pronta",
            "siamo pronti",
        )
    }
}