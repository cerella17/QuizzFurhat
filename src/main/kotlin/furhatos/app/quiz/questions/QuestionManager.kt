package furhatos.app.quiz.questions

import furhatos.app.quiz.intents.AnswerOption
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

object QuestionManager {
    private var count: Int = 0
    private lateinit var questions: MutableList<Question>
    lateinit var current: Question

    init {
        loadQuestions()
        questions.shuffle()
    }

    fun next() {
        count++
        if (count >= questions.size)
            count = 0
        current = questions[count]
        AnswerOption().forget()
    }

    private fun loadQuestions() {
        val file = File("./questions.json")
        if (!file.exists()) {
            throw FileNotFoundException("Il file non è stato trovato.")
        }
        val jsonString = file.readText()
        val questionsList = Json.decodeFromString<List<Question>>(jsonString)
        questions = questionsList.toMutableList()
    }
}

class Question(
    val question: String,
    val answers: List<String>,
    val correctAnswers: List<String>
) {
    fun isCorrect(answer: String): Boolean {
        return correctAnswers.contains(answer)
    }
}