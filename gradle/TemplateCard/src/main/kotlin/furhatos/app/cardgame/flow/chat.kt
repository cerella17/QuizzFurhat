package furhatos.app.cardgame.flow

import furhatos.flow.kotlin.*
import furhatos.nlu.*
import furhatos.util.Language
import kotlin.reflect.full.createInstance

typealias RobotTurnDefinition = RobotTurn.(IntentInstance) -> Unit

typealias Nomatch = NullIntent

object Silent : Intent() {

    override fun getExamples(lang: Language): List<String> {
        return listOf()
    }

}

fun StateBuilder.user(vararg intents: IntentCandidate, proceed: State? = null, robotTurnDef: RobotTurnDefinition) {
    createOnResponse(UserTurn(intents.toList(), proceed, robotTurnDef))
}

inline fun <reified T : IntentCandidate> StateBuilder.user(proceed: State? = null, noinline robotTurnDef: RobotTurn.(T) -> Unit) where T : IntentInstance {
    createOnResponse(UserTurn(listOf(T::class.createInstance()), proceed, { robotTurnDef(it as T) }))
}

class UserTurn(val intents: List<IntentCandidate>, val proceed: State?, val robotTurnDef: RobotTurnDefinition)

class RobotTurn(val proceed: State?, val parent: State, val intentInstance: IntentInstance, def: RobotTurnDefinition) {

    lateinit var robotUtterance: Utterance
    val userTurns = mutableListOf<UserTurn>()

    init {
        def(intentInstance)
    }

    fun robot(utteranceDef: UtteranceDefinition) {
        robotUtterance = utterance(utteranceDef)
    }

    fun user(vararg intents: IntentCandidate, proceed: State? = null, robotTurnDef: RobotTurnDefinition) {
        if (intents.isEmpty()) {
            userTurns.add(UserTurn(listOf(NullIntent), proceed ?: this.proceed, robotTurnDef))
        } else {
            userTurns.add(UserTurn(intents.toList(), proceed ?: this.proceed, robotTurnDef))
        }
    }

    fun toState(): State = state(parent) {
        onEntry {
            furhat.say(robotUtterance)
            if (userTurns.isEmpty()) {
                goto(proceed ?: parent)
            } else {
                furhat.listen()
            }
        }
        if (userTurns.isNotEmpty()) {
            userTurns.forEach { userTurn ->
                createOnResponse(userTurn, parent)
            }
        }
    }
}

fun StateBuilder.createOnResponse(userTurn: UserTurn, parent: State? = null) {
    for (intent in userTurn.intents) {
        if (intent is Silent) {
            onNoResponse {
                goto(RobotTurn(userTurn.proceed, parent ?: thisState, Silent, userTurn.robotTurnDef).toState())
            }
        } else if (intent is NullIntent) {
            onResponse(cond = { it.intent is NullIntent }) {
                goto(RobotTurn(userTurn.proceed, parent ?: thisState, it.intent, userTurn.robotTurnDef).toState())
            }
        } else {
            onResponse(intent) {
                goto(RobotTurn(userTurn.proceed, parent ?: thisState, it.intent, userTurn.robotTurnDef).toState())
            }
        }
    }
}