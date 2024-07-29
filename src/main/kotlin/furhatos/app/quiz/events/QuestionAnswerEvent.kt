package furhatos.app.quiz.events

import furhatos.event.Event

class QuestionAnswerEvent(
    val answer: String,
    val correctAnswer: String,
    val correct: Boolean
) : Event()