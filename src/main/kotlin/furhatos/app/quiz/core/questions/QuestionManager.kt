package furhatos.app.quiz.core.questions

import furhatos.app.quiz.intents.AnswerOption
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class QuestionSet {
    private var count: Int = -1
    private lateinit var questions: MutableList<Question>
    lateinit var current: Question

    init {
        loadAndShuffleQuestions()
    }

    fun next(): Question {
        count++
        if (count >= questions.size)
            count = 0
        AnswerOption().forget()
        current = questions[count]
        return current
    }

    private fun loadQuestions() {
        val file = File("src/main/kotlin/furhatos/app/quiz/core/questions/questions.json")
        if (!file.exists()) {
            throw FileNotFoundException("Il file non è stato trovato.")
        }
        val jsonString = file.readText()
        val questionsList = Json.decodeFromString<List<Question>>(jsonString)
        questions = questionsList.toMutableList()
    }

    private fun loadAndShuffleQuestions() {
        loadQuestions()
        questions.shuffle()
    }
}

@Serializable
class Question(
    val question: String,
    val answers: List<String>,
    val options: List<String>
) {
    fun isCorrect(answer: String): Boolean {
        return answers.map { it.lowercase() }.contains(answer.lowercase())
    }

    fun correctAnswer(): String {
        return answers.find { it in options } ?: ""
    }

    fun toQuestionTextSpeech(): String {
        var optionsJoinedString = ""
        for (i in options.indices) {
            optionsJoinedString += options[i]
            optionsJoinedString += if (i < options.size - 2) {
                ". "
            } else if (i < options.size - 1) {
                " oppure "
            } else {
                "."
            }
        }
        return "$question $optionsJoinedString"
    }
}