package furhatos.app.quiz.events

import furhatos.event.Event

class EndGameEvent(
    val redScore: Int,
    val blueScore: Int,
) : Event()