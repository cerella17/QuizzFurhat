package furhatos.app.quiz.events

import furhatos.event.Event

class NewGameEvent(
    val redLeaderName: String,
    val blueLeaderName: String,
    val maxRounds: Int
) : Event()