package furhatos.app.cardgame.flow.discussion

import furhatos.app.cardgame.*
import furhatos.app.cardgame.flow.outro.CheckSolution
import furhatos.app.cardgame.flow.Playing
import furhatos.app.cardgame.game.Game
import furhatos.app.cardgame.nlu.*
import furhatos.event.senses.SenseSpeechDirection
import furhatos.flow.kotlin.*

val Discussing = state(Playing) {

    onEntry {
        furhat.listen()
    }

    onEvent<ActionAttendCard>(instant = true) {
        println("Attending " + it.cardId)
        val card = Game.cardSet.getCard(it.cardId)
        if (card != null) {
            focusStack.prime(card)
            furhat.attend(card.location)
        }
    }

    onEvent<SenseSpeechDirection>(instant=true) {
        gazeShiftLimiter.limitRepeat(1000) {
            furhat.attend(users.getUser(it.userId))
        }
    }

    onResponse(priority = true) {
        // keep count of users turns so far
        ++onResponseCounterInDiscussingState

        furhat.attend(users.getUser(it.userId))
        // Centering theory says that the first item is more likely to be in focus
        it.findAll(CardEntity()).reversed().forEach {
            focusStack.prime(it.card!!)
        }
        if (cardsMoving) {
            // Cards are still being moved, wait with the response
            currentResponse = it
        } else {
            propagate()
        }
    }

    // "We are done"
    onResponse<GameDoneIntent> {
        if(!hasCheckIfDoneBeenTriggered) {
            hasCheckIfDoneBeenTriggered = true
            goto(CheckIfDone)
        } else {
            goto(CheckSolution)
        }
    }

    // "Do you think the zebra is faster than the lion?"
    // "I think the zebra is faster than the lion"
    onResponse<CompareCardsIntent>(cond={it.isTowardsFurhat }) {
        knowsToAskFurhatAboutCards = true
        val card2 = it.intent.card2?.card?:focusStack[0]
        var card1 = it.intent.card1?.card
        var i = 0
        while (card1 == null || card1.id == card2.id) {
            card1 = focusStack[i++]
        }
        goto(CompareCards(card1, card2))
    }

    // "Which one do you think is the fastest?"
    // "Do you think the zebra is the fastest?"
    // "I think the zebra is the fastest animal"
    onResponse<HighestCardIntent>(cond={it.isTowardsFurhat }) {
        knowsToAskFurhatAboutCards = true
        goto(ExpressExtreme(true))
    }

    onResponse<LowestCardIntent>(cond={it.isTowardsFurhat }) {
        knowsToAskFurhatAboutCards = true
        goto(ExpressExtreme(false))
    }

    // "What do you think about the zebra?"
    onResponse<CommentCardIntent>(cond={it.isTowardsFurhat }) {
        knowsToAskFurhatAboutCards = true
        val card = it.intent.card?.card?:focusStack[0]
        goto(ExpressOpinion(card))
    }

    // "what do you think?"
    onResponse<RequestOpinionIntent>(cond={it.isTowardsFurhat }) {
        goto(TakeInitiative())
    }

    // "why do you think so?"
    onResponse<RequestWhyIntent>(cond={it.isTowardsFurhat }) {
        goto(MotivateOpinion)
    }

    onEvent<SenseCardsReordered>(instant = true) {
        knowsHowToMoveCards = true
        cardsReordered = it
        if (currentResponse != null) {
            raise(currentResponse!!)
        } else if (!furhat.isSpeaking && !users.areSpeaking) {
            goto(CommentReordered(it))
        }
    }

    // My name is Peter
    //onResponse<TellNameIntent> {
     //   users.current.name = it.intent.name?.value
     //   furhat.say(output.acknowledge_name_late)
     //   goto(thisState)
    //}

    // General questions
    Game.locale.questions?.invoke(this)

    // Questions regarding the deck
    Game.deck.questions?.invoke(this)

    onResponse {
        if (cardsReordered != null) {
            goto(CommentReordered(cardsReordered!!))
        } else if (it.isTowardsFurhat) {
            // take the initative
            goto(TakeInitiative(it))
        } else {
            // make a backchannel and continue listening
            backchannelLimiter.limitRepeat(10000) {
                furhat.say(output.backchannel)
            }
            goto(thisState)
        }
    }

    onNoResponse {
        if (cardsMoving) {
            // Cards are moving, wait a bit more
            usersSilent = true
            furhat.listen()
        } else {
            // Users are silent and cards have not been moved recently, take the initative
            furhat.say(output.hold_floor)
            goto(TakeInitiative())

        }
    }

    onExit(inherit = true) {
        cardsReordered = null
        currentResponse = null
        usersSilent = false
    }
}
