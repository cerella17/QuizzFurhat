package furhatos.app.quiz.events

import furhatos.event.Event

class SyncInformationEvent(
    val round: Int,
    val redScore: Int,
    val blueScore: Int,
) : Event()