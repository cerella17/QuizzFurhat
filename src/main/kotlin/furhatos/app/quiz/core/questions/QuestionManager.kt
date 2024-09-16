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
        val jsonText = """
            [
               {
                  "question":"In CSS, quale delle seguenti proprietà di un tag ancora implica un collegamento ipertestuale ancora non visitato? ",
                  "answers":[
                  "Link",
                     "link",
                     "la prima",
                     "la A",
                     "la risposta uno",
                     "prima",
                     "A",
                     "uno"
                  ],
                  "options":[
                     "Link",
                     "Visited",
                     "Hover",
                     "Active"
                  ]
               },
               {
                  "question":"Quale proprietà CSS controlla la dimensione del testo?",
                  "answers":[
                     "Font-size",
                     "font size",
                     "la terza",
                     "font Sites",
                     "font Site",
                     "la tre",
                     "la C",
                     "C",
                     "tre",
                     "terza",
                     "la numero tre"
                  ],
                  "options":[
                     "Text-size",
                     "Text-style",
                     "Font-size",
                     "Font-style"
                  ]
               },
               {
                  "question":"In JavaScript, come si invoca una funzione denominata “ciao()”?",
                  "answers":[
                     "ciao()",
                     "ciao parentesi tonde",
                     "ciao",
                     "la seconda",
                     "la due",
                     "la b",
                     "b",
                     "due",
                     "seconda",
                     "la numero 2"
                  ],
                  "options":[
                     "Tutte le opzioni",
                     "ciao()",
                     "call function ciao()",
                     "call ciao()"
                  ]
               },
               {
                  "question":"In JavaScript, quale evento si verifica quando l’utente clicca su un element HTML?",
                  "answers":[
                     "ho un click",
                     "Onclick",
                     "onclick",
                     "un click",
                     "on click",
                     "la prima",
                     "la A",
                     "la numero uno",
                     "A",
                     "la risposta A",
                     "prima",
                     "uno"
                  ],
                  "options":[
                     "Onclick",
                     "Onchange",
                     "Onmouseclick",
                     "Onmouseover"
                  ]
               },
               {
                  "question":"Quale delle seguenti tecnologie consente di ottenere i dati da un server senza ricaricare l'intera pagine?",
                  "answers":[
                     "AJAX",
                     "agiax",
                     "aiax",
                     "la prima",
                     "la numero uno",
                     "la risposta A",
                     "la a",
                     "A",
                     "prima",
                     "uno"
                  ],
                  "options":[
                     "AJAX",
                     "JSON",
                     "Javascript",
                     "MVC"
                  ]
               },
               {
                  "question":"Come si seleziona l’elemento con id “demo”?",
                  "answers":[
                     "#demo",
                     "hashtag demo",
                     "astag demo",
                     "ashtag demo",
                     "cancelletto demo",
                     "la seconda",
                     "la due",
                     "la b",
                     "la risposta due",
                     "due",
                     "b",
                     "la seconda risposta",
                     "la risposta b"
                  ],
                  "options":[
                     ".demo",
                     "#demo",
                     "Demo",
                     "*demo"
                  ]
               },
               {
                  "question":"In CSS, quale è lo statement che consente di non visualizzare un particolare elemento (tag)?",
                  "answers":[
                     "il display non",
                     "il Display none",
                     "il display non è",
                     "Display: none",
                     "Display none",
                     "Display non",
                     "display non è",
                     "la terza",
                     "la risposta tre",
                     "la risposta c",
                     "la c",
                     "la tre",
                     "c",
                     "tre"
                  ],
                  "options":[
                     "Display: initial",
                     "Display: hidden",
                     "Display: none",
                     "Nessuna delle opzioni"
                  ]
               },
               {
                  "question":"La sessione tra più richieste di uno stesso client può essere mantenuta:",
                  "answers":[
                     "utilizzando http session",
                     "utilizzando HttpSession",
                     "HttpSession",
                     "http session",
                     "la terza",
                     "la risposta tre",
                     "la risposta c",
                     "la c",
                     "la tre",
                     "c",
                     "tre"
                  ],
                  "options":[
                     "Utilizzando PageContext",
                     "Tutte le opzioni",
                     "Utilizzando HttpSession",
                     "Utilizzando SessionManager"
                  ]
               }
            ]
        """.trimIndent()
        val questionsList = Json.decodeFromString<List<Question>>(jsonText)
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
        return answers.find { it -> it.lowercase() in options.map { it.lowercase() } } ?: ""
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