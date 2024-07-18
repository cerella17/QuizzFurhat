package furhatos.app.cardgame.game

import furhatos.app.cardgame.stdev
import furhatos.util.Language
import java.util.*

class CardSet(val cards : List<Card>) {

    private var lang: Language? = null
    var focusStack: FocusStack
    var lastKnownOrder: CardOrder

    init {
        var x = 0.2
        cards.forEach {
            it.setScreenLocation(x, 0.5)
            x+= 0.15
        }
        focusStack = FocusStack(cards)
        if (cards.isNotEmpty())
            setupBelief()
        lastKnownOrder = CardOrder(cards)
    }

    val id
        get() = cards.map{it.id}.joinToString(",")

    /**
     * The current order of the cards, as seen from the user's perspective
      */
    val currentOrder
        get() = CardOrder(cards.sortedBy { it.location.x })

    /**
     * The cards in the correct order (from the user's perspective)
     */
    val correctOrder
        get() = CardOrder(cards.sortedBy { it.truth.toDouble() })

    private fun setupBelief() {
        val values = cards.map { it.truth.toDouble() }
        val min = values.min()!!
        val stdev = values.stdev()
        for (card in cards) {
            var beliefMean: Double
            do {
                val r = Game.random.nextFloat()
                beliefMean = card.truth.toDouble() + (r * 2 - 1) * stdev * Incorrectness
            } while (beliefMean < min / 2.0)
            card.beliefMean = beliefMean
            val r = Game.random.nextFloat()
            val beliefDev = r * stdev * Uncertainty
            card.beliefDev = beliefDev
        }
    }

    /**
     * Whether card1 is currently left of card2, from the user's perspective
     */
    fun isLeftOf(card1: Card, card2: Card) = getTableOrder(card1) < getTableOrder(card2)

    /**
     * A list of the (one or two) cards that are currently next to the card
     */
    fun getNeighbours(card: Card) : List<Card> {
        val index = getTableOrder(card)
        return listOf(getCard(index - 1), getCard(index + 1)).filterNotNull()
    }

    /**
     * Return the card with a certain order index (0-4), from the user's perspective
     */
    private fun getCard(index: Int) : Card? {
        return currentOrder.getOrNull(index)
    }

    fun getCard(id: String) : Card? {
        return cards.find {it.id == id}
    }

    /**
     * The current order index of the card (0-4), from the user's perspective
     */
    private fun getTableOrder(card: Card) = currentOrder.indexOf(card)

    /**
     * Returns a list of cards which are deemed to be lowest
     */
    fun getLowestBelief() : List<Card> {
        val result = mutableListOf(cards.minBy { it.beliefMean }!!)
        cards.forEach {
            if (it != result[0] && result[0].lessThan(it) < 0.7)
                result.add(it)
        }
        return result
    }

    /**
     * Returns a list of cards which are deemed to be highest
     */
    fun getHighestBelief() : List<Card> {
        val result = mutableListOf(cards.maxBy { it.beliefMean }!!)
        cards.forEach {
            if (it != result[0] && result[0].greaterThan(it) < 0.7)
                result.add(it)
        }
        return result
    }

    val score: Int get () {
        val correctOrder = correctOrder
        var corr = 0
        currentOrder.forEachIndexed { i, id -> if (correctOrder[i] == id) corr++ }
        return corr
    }

}
