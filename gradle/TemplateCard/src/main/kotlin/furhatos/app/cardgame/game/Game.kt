package furhatos.app.cardgame.game

import furhatos.app.cardgame.DeckWithCards
import furhatos.app.cardgame.EventCard
import furhatos.app.cardgame.Locale
import java.util.Random

var Incorrectness = 1.0
var Uncertainty = 1.0

/**
 * The game state
 */
object Game {
    val random = Random()

    // The current locale
    lateinit var locale: Locale

    // All available decks
    val decks get() = locale.decks

    // The current deck
    val deck get() = locale.deck

    // The current card set (of five cards)
    var cardSet = CardSet(listOf<Card>())

    fun selectDeck(deck: Deck) {
        locale.setDeck(deck)
        cardSet = CardSet(deck.chooseCards())
    }

    val deckWithCards get() = DeckWithCards(deck.name, deck.instruction, cardSet.cards.map { EventCard(it.name, it.imagePath) })
}
