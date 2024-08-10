package furhatos.app.quiz.events

import furhatos.event.Event

class GreetingLeaderEvent(
    val team: String,
    val name: String
) : Event()