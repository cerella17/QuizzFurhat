package furhatos.app.cardgame

import furhatos.app.cardgame.flow.*
import furhatos.app.cardgame.game.*
import furhatos.app.cardgame.locales.english.englishLocale
import furhatos.skills.Skill
import furhatos.flow.kotlin.*
import furhatos.nlu.LogisticMultiIntentClassifier
import furhatos.skills.HostedGUI

class CardgameSkill : Skill() {

    val gui = HostedGUI("Game GUI", "assets/gui", port=12345)

    override fun start() {
        LogisticMultiIntentClassifier.setAsDefault()
        Game.locale = englishLocale
        Flow().run(Init)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
