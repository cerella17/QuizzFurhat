package furhatos.app.quiz.questions

import furhatos.app.quiz.AnswerOption
import furhatos.nlu.EnumItem
import furhatos.nlu.TextBuilder
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException
import java.util.*

object QuestionSet {

    var count: Int = 0
    lateinit var current: Question
    private lateinit var questions: MutableList<Question>

    init {
        try {
            loadQuestions()
            questions.shuffle()
            current = questions[Random().nextInt(questions.size)]
        } catch (e: FileNotFoundException) {
            println("Errore: Il file questions.json non è stato trovato. Assicurati che il file sia nella directory src/main/resources/")
        }
    }

    fun next() {
        count++
        if (count >= questions.size)
            count = 0
        current = questions[count]
        AnswerOption().forget()
    }

    private fun loadQuestions() {
        val filePath = "/Users/cerella17/Desktop/furhat-open-ai-main/TemplateQuizz/src/main/kotlin/furhatos/app/quiz/questions/domandeArchitettura.json"
        val file = File(filePath)
        if (!file.exists()) {
            throw FileNotFoundException("Il file $filePath non è stato trovato.")
        }
        val fileContent = file.readText()
        val questionList: List<QuestionIntermediate> = Json.decodeFromString(fileContent)
        questions = questionList.map { it.toQuestion() }.toMutableList()
    }
}

@Serializable
data class QuestionIntermediate(
    val question: String, // Cambia 'text' a 'question'
    val answer: List<String>,
    val alternatives: List<List<String>>
) {
    fun toQuestion(): Question {
        return Question(question, answer, alternatives)
    }
}

class Question(val text: String, answer: List<String>, alternatives: List<List<String>>) {
    //All options, used to prime the NLU
    @Contextual
    var options: MutableList<EnumItem> = mutableListOf()
    //Only the first option of the answers, these are correctly spelled, and not alternative.
    @Contextual
    var primeoptions: MutableList<EnumItem> = mutableListOf()

    //init loads the first item of the list into primeoptions
    //And loads everything into options
    init {
        primeoptions.add(EnumItem(AnswerOption(true, answer.first()), answer.first()))
        answer.forEach {
            options.add(EnumItem(AnswerOption(true, it), it))
        }

        alternatives.forEach {
            primeoptions.add(EnumItem(AnswerOption(false, it.first()), it.first()))
            it.forEach {
                options.add(EnumItem(AnswerOption(false, it), it))
            }
        }

        options.shuffle()
        primeoptions.shuffle()
    }

    //Returns the well formatted answer options
    fun getOptionsString(): String {
        val text = TextBuilder()
        text.appendList(primeoptions.map { it.wordString }, "o")
        return text.toString()
    }

    //Returns the well formatted answer options
    val speechPhrases: List<String>
        get() = primeoptions.map { it.wordString ?: "" }
}
