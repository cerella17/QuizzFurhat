package furhatos.app.cardgame.flow

import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.voice.PollyNeuralVoice
import furhatos.flow.kotlin.voice.PollyVoice

object MatthewNeuralVoice: PollyNeuralVoice.Matthew() {
    const val minSpeakingRate: Int = 100
    const val maxSpeakingRate: Int = 105

    override fun transform(text: String): String {
        val rate = (minSpeakingRate..maxSpeakingRate).random() / 100.0
        return super.transform(prosody(text, rate = rate))
    }
}

val Init = state(Parent) {
    onEntry {
        //flowLogger.start(File("flow.txt"))
        furhat.setTexture("default")
        furhat.voice = MatthewNeuralVoice
        parallel(abortOnExit = false) {
            goto(TrackUsers)
        }
    }
}