package furhatos.app.cardgame.nlu

import furhatos.app.cardgame.game.Card
import furhatos.app.cardgame.game.Game
import furhatos.app.cardgame.game.Deck
import furhatos.app.cardgame.input
import furhatos.nlu.*
import furhatos.nlu.common.PersonName
import furhatos.util.CommonUtils
import furhatos.util.Language
import org.apache.commons.csv.CSVFormat
import java.io.InputStreamReader
import java.net.URL
import kotlin.concurrent.thread

class RequestOpinionIntent : SimpleIntent(input.request_opinion)

class RequestWhyIntent : SimpleIntent(input.request_why)

class GameDoneIntent : Intent() {
    var finished: EntityFinished? = null
    var answer: EntityGameSolution? = null
    override fun getExamples(lang: Language): List<String> {
        return input.game_done
    }
}

class EntityFinished : EnumEntity(speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return input.entity_finished_game
    }
}

class EntityGameSolution : EnumEntity(speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return input.entity_game_solution
    }
}

class GameNotDoneIntent : SimpleIntent(input.game_not_done)

class AgreeIntent : SimpleIntent(input.agree)

class DisagreeIntent : SimpleIntent(input.disagree)

class TellNameIntent : Intent() {
    var name: PersonName? = null

    override fun getExamples(lang: Language): List<String> {
        return Game.locale.input.tell_name
    }
}

class DeckEntity(var deck: Deck? = null) : Entity() {
    override fun getInterpreterImpl(lang: Language): EntityInterpreter {
        return EnumInterpreter(getEnumItems(), lang, true, true)
    }
    private fun getEnumItems(): List<EnumItem> {
        return Game.decks.map { EnumItem(DeckEntity(it), it.name) }
    }
    override fun getSpeechRecPhrases(lang:Language): Collection<String> {
        return (getInterpreter(lang) as EnumInterpreter).getSpeechRecPhrases()
    }
}

class AskAboutDeckIntent: Intent() {
    var deck: DeckEntity? = null

    override fun getExamples(lang: Language): List<String> {
        return Game.locale.input.ask_about_deck
    }
}

/**
 *  "do you think the zebra is slower than the elk"
 *  "do you think it is slower than the elk"
 */
class CompareCardsIntent : Intent() {
    var card1: CardEntity? = null
    var card2: CardEntity? = null

    override fun getExamples(lang: Language): List<String> {
        return Game.locale.input.ask_compare_cards + Game.locale.input.assert_compare_cards
    }
}

/**
 *  "which one do you think is the fastest"
 *  "do you think the zebra is the fastest one"
 */
class HighestCardIntent : Intent() {
    var card: CardEntity? = null

    override fun getExamples(lang: Language): List<String> {
        return Game.locale.input.ask_highest_card
    }
}

/**
 *  "which one do you think is the slowest"
 *  "do you think the zebra is the slowest one"
 */
class LowestCardIntent : Intent() {
    var card: CardEntity? = null

    override fun getExamples(lang: Language): List<String> {
        return Game.locale.input.ask_lowest_card
    }
}

/**
 *  "what do you think about the zebra"
 */
class CommentCardIntent : Intent() {
    var card: CardEntity? = null

    override fun getConfidenceThreshold(): Double {
        return 0.0
    }

    override fun getExamples(lang: Language): List<String> {
        return Game.locale.input.comment_card
    }
}

class CardEntity(var card: Card? = null) : Entity() {
    override fun getInterpreterImpl(lang: Language): EntityInterpreter {
        return EnumInterpreter(getEnumItems(), lang, true, true)
    }
    private fun getEnumItems(): List<EnumItem> {
        return Game.cardSet.cards.flatMap { card -> card.input.map { keyword -> EnumItem(CardEntity(card), keyword) }}
    }
    override fun getSpeechRecPhrases(lang:Language): Collection<String> {
        return (getInterpreter(lang) as EnumInterpreter).getSpeechRecPhrases()
    }
}
