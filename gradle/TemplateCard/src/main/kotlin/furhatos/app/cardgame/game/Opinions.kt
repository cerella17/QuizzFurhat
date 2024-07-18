package furhatos.app.cardgame.game

import furhatos.app.cardgame.output
import furhatos.flow.kotlin.UtteranceDefinition

/**
 * Collects all opinions that Furhat has expressed
 */
object Opinions {

    private val all = mutableMapOf<String, Opinion>()

    /**
     * The latest opinions Furhat has expressed
     */
    var latest: Opinion? = null

    /**
     * Clears the memory of Furhat's opinions (for a new game)
     */
    fun forget() {
        all.clear()
    }

    /**
     * Returns a strong opinion about the card that Furhat hasn't expressed enough
     */
    fun getStrongOpinion(card: Card): Opinion? {
        val highest = getExtremePosition(true)
        if (highest.cards.size == 1 && highest.cards[0] == card && highest.needChange && highest.count < 2) {
            return highest
        }
        val lowest = getExtremePosition(false)
        if (lowest.cards.size == 1 && lowest.cards[0] == card && lowest.needChange && lowest.count < 2) {
            return lowest
        }
        for (cardNext in Game.cardSet.getNeighbours(card)) {
            val comp = getComparison(card, cardNext)
            if (comp.needChange && comp.count < 2) {
                return comp
            }
        }
        return null
    }

    /**
     * Returns a comparison of two cards
     */
    fun getComparison(card1: Card, card2: Card) : Comparison {
        val comp = Comparison(card1, card2)
        if (all.contains(comp.id)) {
            return all[comp.id] as Comparison
        } else {
            all[comp.id] = comp
            return comp
        }
    }

    /**
     * Returns an opinion about which card(s) should be lowest/highest
     */
    fun getExtremePosition(highest: Boolean) : ExtremePosition {
        val comp = ExtremePosition(highest)
        if (all.contains(comp.id)) {
            return all[comp.id] as ExtremePosition
        } else {
            all[comp.id] = comp
            return comp
        }
    }

    /**
     * Returns a list of strong opinions where Furhat was correct but the cards are not sorted like that
     */
    fun getCorrect(): List<Opinion> {
        return all.values.filter { it.needChange && it.correct }
    }

    /**
     * Returns a list of strong opinions where Furhat was incorrect and the cards are not sorted like that
     */
    fun getIncorrect(): List<Opinion> {
        return all.values.filter { it.needChange && !it.correct }
    }

}

abstract class Opinion {

    // The users that agrees with this opinion
    val agreement = mutableSetOf<String>()
    // The users that disagrees with this opinion
    val disagreement = mutableSetOf<String>()

    /**
     * Whether Furhat is confident that the cards should be moved
     */
    abstract val needChange: Boolean

    /**
     * Whether Furhat is correct in his opinion
     */
    abstract val correct: Boolean

    /**
     * A motivation for Furhat's opinion (same as argument, but repetition allowed)
     * Only used when user explicitly asks for it
     */
    abstract val motivation: UtteranceDefinition

    /**
     * The opinion in text form (for TTS), e.g. "the horse is slower than the lion"
     */
    abstract val expression: UtteranceDefinition

    abstract val confident: Boolean

    /**
     * The number of times the opinion has been expressed
     */
    var count = 0

    /**
     * Call this each time the opinion is expressed
     */
    fun expressed() {
        count++
        Opinions.latest = this
    }

}

/**
 * A comparison Furhat makes between two cards
 */
class Comparison(val card1: Card, val card2: Card) : Opinion() {

    /**
     * greaterThan > 0 if Furhat thinks card1 is higher than card2
     */
    val greaterThan = card1.greaterThan(card2)

    /**
     * Furhat's confidence in his belief
     */
    val conf = Math.abs(greaterThan)

    // By sorting the cards we make sure comparisons of cards are the same regardless of order
    val id: String
        get() = listOf(card1.id, card2.id).sorted().joinToString(separator = "--")

    fun has(cardId: String): Boolean {
        return card1.id == cardId || card2.id == cardId
    }

    /**
     * Whether the cards are sorted in the correct order (according to Furhat's belief)
     */
    val ordered
        get() = if (greaterThan < 0)
            Game.cardSet.isLeftOf(card1, card2)
        else
            Game.cardSet.isLeftOf(card2, card1)

    /**
     * Whether this comparison was actually correct
     */
    override val correct
        get() = (greaterThan > 0 && card1.truth.toDouble() > card2.truth.toDouble()) ||
                (greaterThan < 0 && card1.truth.toDouble() < card2.truth.toDouble())

    /**
     * A textual output of Furhat's belief
     * e.g. "the zebra is slower than the lion"
     */
    override val expression: UtteranceDefinition
        get() = {
            +card1.def
            if (greaterThan > 0)
                include(Game.deck.output.is_more_than)
            else
                include(Game.deck.output.is_less_than)
            +card2.def
        }

    override val needChange get() = confident && !ordered

    override val confident get() = conf > 0.7

    fun getArgument(onlyCard1: Boolean): UtteranceDefinition? {
        // If we should only provide an argument for card1
        if (onlyCard1) {
            return if (greaterThan > 0) {
                card1.argument_high.randomNoRepeat(Game.cardSet)
            } else {
                card1.argument_low.randomNoRepeat(Game.cardSet)
            }
        }
        val (card1arg, card2arg) =
                if (greaterThan > 0) {
                    card1.argument_high.randomNoRepeat(Game.cardSet) to card2.argument_low.randomNoRepeat(Game.cardSet)
                } else {
                    card1.argument_low.randomNoRepeat(Game.cardSet) to card2.argument_high.randomNoRepeat(Game.cardSet)
                }
        if (card1arg != null && card2arg != null) {
            return {
                include(card1arg)
                +","
                +output.and
                include(card2arg)
            }
        } else if (card1arg != null) {
            return card1arg
        } else {
            return card2arg
        }
    }

    override val motivation: UtteranceDefinition
        get() = {
            val (first,second) =
                    if (greaterThan > 0) {
                        card1.argument_high.randomAvoidRepeat() to card2.argument_low.randomAvoidRepeat()
                    } else {
                        card2.argument_high.randomAvoidRepeat() to card1.argument_low.randomAvoidRepeat()
                    }
            if (first != null && second != null) {
                include(first)
                +output.and
                include(second)
            } else if (first != null) {
                include(first)
            } else if (second != null) {
                include(second)
            } else  {
                include(output.generic_motivation)
            }
        }

}

/**
 * An opinion about which card(s) should be lowest/highest
 */
class ExtremePosition(val highest: Boolean) : Opinion() {

    val cards = if (highest) Game.cardSet.getHighestBelief() else Game.cardSet.getLowestBelief()

    val id: String
        get() = "Extreme, highest:" + highest

    override val correct
        get() = cards.size == 1 && Game.cardSet.correctOrder[if (highest) 4 else 0] == cards[0]

    override val needChange
        get() = cards.size == 1 &&
                Game.cardSet.currentOrder[if (highest) 4 else 0] != cards[0]

    override val confident get() = cards.size == 1

    fun getArgument(): UtteranceDefinition? {
        if (cards.size == 1) {
            if (highest) {
                return cards[0].argument_high.randomNoRepeat(Game.cardSet)
            } else {
                return cards[0].argument_low.randomNoRepeat(Game.cardSet)
            }
        }
        return null
    }

    override val motivation: UtteranceDefinition
        get() = {
            val motiv =
                    if (cards.size == 1) {
                        if (highest) {
                            cards[0].argument_high.randomAvoidRepeat()
                        } else {
                            cards[0].argument_low.randomAvoidRepeat()
                        }
                    } else null
            include(motiv?: output.generic_motivation)
        }

    override val expression: UtteranceDefinition
        get() = {
            if (cards.size > 1) {
                +output.either
                +cards.joinToString(separator = " ${output.or} ") { it.def }
            } else {
                +cards[0].def
            }
            if (highest)
                include(Game.deck.output.is_max)
            else
                include(Game.deck.output.is_min)
        }


}
