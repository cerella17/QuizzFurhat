package furhatos.app.cardgame.game

import furhatos.app.cardgame.cumulativeStandardNormalDistribution
import furhatos.app.cardgame.flow.RobotTurn
import furhatos.app.cardgame.flow.RobotTurnDefinition
import furhatos.app.cardgame.options
import furhatos.app.cardgame.utteranceOptions
import furhatos.flow.kotlin.State
import furhatos.nlu.NullIntent
import furhatos.records.Location

class Card(val deck: Deck) {

    val imagePath get() =  "/tasks/${deck.imgFolder}/${image}"

    /**
     * A computer-friendly ID for the card
     */
    var id = ""
    /**
     * The true value for the card
     */
    var truth: Number = 0
    /**
     * The image file for the card
     */
    var image = ""
    /**
     * A name for the card shown on the screen
     */
    var name = ""
    /**
     * The card in definite form (like "the zebra")
     */
    var def = ""
    /**
     * The card in indefinite form (like "a zebra")
     */
    var indef = ""
    /**
     * Words or phrases the user might say to name the card
     */
    val input = options()

    val argument_high = utteranceOptions()
    val argument_low = utteranceOptions()

    var beliefMean: Double = 0.0
    var beliefDev: Double = 0.0

    var location = Location(0,0,0)

    private var initiative: Initiative? = null

    fun initiative(proceed: State? = null, definition: RobotTurnDefinition) {
        initiative = Initiative(definition, proceed)
    }

    fun hasInitiative() = initiative != null && !usedInitiatives.contains(this)

    fun getInitiativeState(currentState: State): State {
        usedInitiatives.add(this)
        return RobotTurn(initiative!!.proceed ?: currentState, currentState, NullIntent, initiative!!.robotTurnDef).toState()
    }

    class Initiative(val robotTurnDef: RobotTurnDefinition, val proceed: State?)

    /**
     * Returns a score from -1 to 1, where 1 means that this card is definitely greater than other, and 0 that there is no difference
     */
    fun greaterThan(other: Card): Double {
        var p = 1.0
        if (beliefDev > 0 || other.beliefDev > 0) {
            val z = Math.abs(beliefMean - other.beliefMean) / Math.sqrt(Math.pow(beliefDev, 2.0) + Math.pow(other.beliefDev, 2.0))
            p = cumulativeStandardNormalDistribution(z)
        }
        return if (beliefMean > other.beliefMean)
            p
        else
            -p
    }

    /**
     * Returns a score from -1 to 1, where 1 means that this card is definitely less than other, and 0 that there is no difference
     */
    fun lessThan(other: Card): Double {
        return -greaterThan(other)
    }

    override fun toString(): String {
        return id
    }

    /**
     * Sets the location of the card relative to the screen (x,y=0 is top left, x,y=1 is bottom right)
     */
    fun setScreenLocation(x: Double, y: Double) {
        location = GameTable.topLeftLocation.add(Location(x * GameTable.width, 0.0, y * GameTable.height))
    }

    companion object {
        private val usedInitiatives = mutableSetOf<Card>()

        fun forgetInitiatives() {
            usedInitiatives.clear()
        }
    }

}