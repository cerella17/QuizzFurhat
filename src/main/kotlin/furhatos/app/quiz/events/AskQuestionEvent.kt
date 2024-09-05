package furhatos.app.quiz.events

import furhatos.app.quiz.setting.TeamEnum
import furhatos.event.Event

class AskQuestionEvent(
    val question: String,
    val options: List<String>,
    val team: String,
    val timeInMs: Int
) : Event()