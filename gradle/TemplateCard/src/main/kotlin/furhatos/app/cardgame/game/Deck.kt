package furhatos.app.cardgame.game

import furhatos.app.cardgame.Locale
import furhatos.app.cardgame.options
import furhatos.app.cardgame.utteranceDef
import furhatos.flow.kotlin.StateDefinition
import furhatos.util.Language

fun deck(definition: Deck.()->Unit) : Deck {
    val deck = Deck()
    deck.definition()
    return deck
}

class Deck {
    /*
    private var _locale = { furhatos.app.cardgame.locale(Language.ENGLISH_US) {} }

    fun locale(value: ()-> Locale) {
        _locale = value
    }

    val locale by lazy {
        _locale()
    }
    */

    var imgFolder = ""
    var name = ""
    var instruction = ""
    var unitLabel = ""

    var output: Output = Output()
    var input: Input = Input()
    var cards = mutableListOf<Card>()

    var displayTruth: (Card)->String = {card -> "${card.truth} ${unitLabel}"}
    var questions: StateDefinition? = null

    fun questions(def: StateDefinition) {
        this.questions = def
    }

    fun output(definition: Output.()->Unit) {
        output.definition()
    }

    fun input(definition: Input.()->Unit) {
        input.definition()
    }

    fun card(definition: Card.()->Unit) {
        cards.add(Card(this).apply(definition))
    }

    /**
     * Choose 5 random cards that have distinct truth values
     */
    fun chooseCards() : List<Card> = cards.shuffled().distinctBy { it.truth }.take(5)

    class Input {
        val singular = options()

        val min_def = options()
        val max_def = options()

        val is_min_def = options()
        val is_max_def = options()

        val is_less_than = options()
        val is_more_than = options()
    }

    class Output {
        var singular = utteranceDef()

        var purpose = utteranceDef()

        var min_def = utteranceDef()
        var max_def = utteranceDef()

        var is_min = utteranceDef()
        var is_max = utteranceDef()

        var is_less_than = utteranceDef()
        var is_more_than = utteranceDef()
    }
}
