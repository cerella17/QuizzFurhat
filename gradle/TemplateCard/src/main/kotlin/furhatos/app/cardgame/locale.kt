package furhatos.app.cardgame

import furhatos.app.cardgame.game.Deck
import furhatos.app.cardgame.game.Game
import furhatos.flow.kotlin.StateDefinition
import furhatos.util.Language

fun locale(language: Language, definition: Locale.()->Unit) : Locale {
    return Locale(language, definition)
}

class Locale(val language: Language, val definition: Locale.()->Unit) {

    var decks = listOf<Deck>()
    var deck = Deck()
        private set
    var input = Input()
    var output = Output()
    var questions: StateDefinition? = null

    init {
        definition()
    }

    fun setDeck(deck: Deck) {
        this.deck = deck
        definition()
    }

    fun questions(def: StateDefinition) {
        this.questions = def
    }

    fun input(definition: Input.()->Unit) {
        input = Input().apply(definition)
    }

    class Input {
        val ask_favorite_game = options()
        val ask_about_deck = options()
        val ask_compare_cards = options()
        val assert_compare_cards = options()
        val ask_highest_card = options()
        val ask_lowest_card = options()
        val comment_card = options()
        val game_done = options()
        val entity_finished_game = options()
        val entity_game_solution = options()
        val game_not_done = options()
        val request_why = options()
        val request_opinion = options()
        val agree = options()
        val disagree = options()
        val tell_name = options()
        val play_again = options()
    }

    fun output(definition: Output.()->Unit) {
        output = Output().apply(definition)
    }

    class Output {

        var button_play_again = ""
        var button_check_answer = ""
        var button_tap_to_play = ""

        var greeting = utteranceDef()
        var ask_name_1 = utteranceDef()
        var ask_name_2 = utteranceDef()
        var acknowledge_name = utteranceDef()

        var my_name_is_furhat = utteranceDef()

        var select_game = utteranceDef()
        var select_new_game = utteranceDef()
        var confirm_same_game = utteranceArgDef()
        var confirm_game = utteranceArgDef()
        var propose_game = utteranceArgDef()
        var propose_game_again = utteranceArgDef()
        var answer_favorite_game = utteranceDef()

        var back_to_the_game_then = utteranceDef()
        var explain_order = utteranceDef()
        var by_the_way = utteranceDef()
        var grab_attention = utteranceDef()
        var explain_you_can_move_cards = utteranceDef()
        var explain_you_can_ask_about_cards = utteranceDef()
        var explain_you_can_ask_to_end_game = utteranceDef()
        var ask_where_to_start = utteranceDef()
        var generic_motivation = utteranceDef()
        var lets_continue_discussion = utteranceDef()
        var ask_if_done = utteranceDef()
        var lets_check_solution = utteranceDef()
        var comment_order_better = utteranceDef()
        var comment_order_worse = utteranceDef()
        var dont_understand = utteranceDef()
        var no_more_ideas = utteranceDef()
        var hold_floor = utteranceDef()
        var backchannel = utteranceDef()

        var i_still_think_90 = utteranceDef()
        var i_still_think_80 = utteranceDef()
        var i_think_95 = utteranceDef()
        var i_think_90 = utteranceDef()
        var i_think_80 = utteranceDef()
        var i_think_70 = utteranceDef()
        var i_think_correct = utteranceDef()
        var i_dont_know_whether = utteranceDef()

        var suggest_move_card = utteranceDef()
        var i_dont_know_which = utteranceDef()
        var seek_agreement_accepted = utteranceDef()
        var seek_agreement_rejected = utteranceDef()
        var request_move_card = utteranceDef()
        var seek_agreement_name = utteranceDef()
        var seek_agreement = utteranceDef()
        var request_opinion_name = utteranceDef()
        var request_opinion = utteranceDef()
        var request_opinion_accept = utteranceDef()
        var request_opinion_other = utteranceDef()
        var request_opinion_unsure = utteranceDef()

        var and = ""
        var or = ""
        var either = ""
        var alright = utteranceDef()
        var so = utteranceDef()

        var comment_score = utteranceDef()

        var i_told_you_that = utteranceDef()
        var why_did_you_not_listen = utteranceDef()
        var i_thought_that = utteranceDef()
        var i_should_have_listened = utteranceDef()
        var start_new_game = utteranceDef()
        var ask_play_again = utteranceDef()
        var lets_play_again = utteranceDef()

        var comment_sleep = utteranceDef()
        var goodbye = utteranceDef()

    }
}
