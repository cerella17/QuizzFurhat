package furhatos.app.quiz

import furhatos.app.quiz.flows.Init
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill

class Quiz : Skill() {
    override fun start() {
        Flow().run(Init)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
