package furhatos.app.cardgame

import furhatos.app.cardgame.flow.discussion.Discussing
import furhatos.app.cardgame.game.*
import furhatos.event.EventSystem
import furhatos.event.actions.ActionAttend
import furhatos.event.actions.ActionGaze
import furhatos.flow.kotlin.*
import furhatos.nlu.Response
import furhatos.records.Location
import furhatos.records.User
import furhatos.skills.UserManager
import java.time.LocalDateTime

// Global variables used in flow
// ===========================================================================

val sayFurhat = "<phoneme alphabet=\"ipa\" ph=\"fɜrr.hætt\">furhat</phoneme>" // pronounce "furhat" correctly ; works well with amazon-polly-voice, matthew-neural
val furhatInventionYear:Int = 2012
val currentYear:Int = LocalDateTime.now().year
val output get() = Game.locale.output
val input get() = Game.locale.input
val focusStack get() = Game.cardSet.focusStack
val cardsMovingDone = DelayedAction()
val cardsAttentionLimiter = RepetitionLimiter()
val backchannelLimiter = RepetitionLimiter()
val gazeShiftLimiter = RepetitionLimiter()
var oldOrder: CardOrder? = null
var cardsReordered: SenseCardsReordered? = null
var currentResponse: Response<*>? = null

// Flags and Counters

private val counterMap = mutableMapOf<Int,Int>()
var cardsMoving = false
var usersSilent = false
var hasCheckIfDoneBeenTriggered = false
var knowsHowToEndGame = false
var knowsToAskFurhatAboutCards = false
var knowsHowToMoveCards = false
var roundCounter = 0
var onResponseCounterInDiscussingState = 0

fun resetFlagsAndCounters() {
    counterMap.clear()
    cardsMoving = false
    usersSilent = false
    hasCheckIfDoneBeenTriggered = false
    knowsHowToEndGame = false
    knowsToAskFurhatAboutCards = false
    knowsHowToMoveCards = false
    roundCounter = 0
    onResponseCounterInDiscussingState = 0
}

// Util functions
// ===========================================================================

/**
 * Increments an integer bound to the provided keys.
 * Returns 1 the first time it is called for the provided keys.
 */
fun counter(vararg keys: Any) : Int {
    val key = keys.sumBy { it.hashCode() }
    counterMap[key] = counterMap.getOrDefault(key, 0) + 1
    return counterMap[key]!!
}


/**
 * Util method to ask and goto(By inheritance not goto()) Discussing state for response
 */
fun FlowControlRunner.discuss(utteranceBuilder: UtteranceDefinition) {
    goto(state(Discussing) {
        onEntry {
            furhat.ask(utteranceBuilder)
        }
    })
}

/**
 * Checks whether the user addressed Furhat
 */
val Response<*>.isTowardsFurhat : Boolean get() {
    return UserManager.count < 2 || "system" == UserManager.getUser(userId).attending
}

fun Furhat.attendOneOrAll() {
    if (users.count > 1)
        attendAll()
    else
        attend(users.random)
}

fun utteranceDef() : UtteranceDefinition =  {}
fun utteranceArgDef() : (String)->UtteranceDefinition = {{}}
fun utteranceOptions() = GenericOptionList<UtteranceDefinition>()

fun options() : OptionList {
    return OptionList()
}

fun options(vararg item: String) : OptionList {
    return OptionList(item.toList())
}

fun List<Double>.stdev() : Double {
    val mean = this.average()
    var variance = 0.0
    for (pd in this) {
        variance += Math.pow(pd - mean, 2.0) / this.count()
    }
    return Math.sqrt(variance)
}

fun cumulativeStandardNormalDistribution(zInput: Double): Double {
    var z = zInput
    val neg = if (z < 0.0) 1 else 0
    if (neg == 1)
        z *= -1.0

    val k = 1.0 / (1.0 + 0.2316419 * z)
    var y = ((((1.330274429 * k - 1.821255978) * k + 1.781477937) * k - 0.356563782) * k + 0.319381530) * k
    y = 1.0 - 0.398942280401 * Math.exp(-0.5 * z * z) * y

    return (1.0 - neg) * y + neg * (1.0 - y)
}

fun FlowControlRunner.pauseToLookForAttendingUser(shortWait: Long = 300) {
    when {
        users.current.isAttendingFurhat() -> {
            furhat.attend(users.current)
        }
        users.other.isAttendingFurhat() -> {
            furhat.attend(users.other)
        }
        else -> {
            furhat.attendOneOrAll()
            delay(shortWait)
        }
    }
}

fun Furhat.sayOnce(utteranceDefinition: UtteranceDefinition) {
    if (counter(utteranceDefinition) == 1) {
        say(utteranceDefinition)
    }
}