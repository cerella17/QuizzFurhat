package furhatos.app.cardgame.flow.discussion

import furhatos.app.cardgame.*
import furhatos.app.cardgame.game.Game
import furhatos.app.cardgame.game.Opinions
import furhatos.app.cardgame.nlu.CardEntity
import furhatos.flow.kotlin.*
import furhatos.nlu.Response
import kotlin.random.Random

fun TakeInitiative(response: Response<*>? = null): State = state(Discussing) {

    onEntry {
        val cardsInResponse = response?.findAll(CardEntity())?.map { it.card }?.filterNotNull()

        // Has the user mentioned any card in the latest response?
        if (cardsInResponse?.isNotEmpty() == true) {
            if (cardsInResponse.size == 2 && cardsInResponse[0].id != cardsInResponse[1].id) {
                goto(CompareCards(cardsInResponse[0], cardsInResponse[1]))
            } else {
                goto(ExpressOpinion(cardsInResponse[0]))
            }
        }

        // if enough turns have passed, we can inform a person on how to end the game
        else if (!knowsHowToMoveCards && onResponseCounterInDiscussingState > 2) {
            knowsHowToMoveCards = true
            furhat.say(output.by_the_way)
            pauseToLookForAttendingUser(500)
            furhat.say(output.explain_you_can_move_cards)
            goto(Discussing)
        }

        // after a couple of turns, tell person that they can ask furhat about cards
        else if (!knowsToAskFurhatAboutCards && onResponseCounterInDiscussingState > 2) {
            knowsToAskFurhatAboutCards = true
            furhat.say(output.grab_attention)
            pauseToLookForAttendingUser(500)
            furhat.say(output.explain_you_can_ask_about_cards)
            goto(Discussing)
        }

        // if enough turns have passed, we can inform a person on how to end the game
        else if (!knowsHowToEndGame && onResponseCounterInDiscussingState > 4) {
            knowsHowToEndGame = true
            furhat.say(output.by_the_way)
            pauseToLookForAttendingUser(500)
            furhat.say(output.explain_you_can_ask_to_end_game)
            goto(Discussing)
        }

        else {
            for (card in focusStack) {
                if (card.hasInitiative()) {
                    focusStack.prime(card)
                    furhat.attend(card.location)
                    goto(card.getInitiativeState(thisState))
                }
                val opinion = Opinions.getStrongOpinion(card)
                if (opinion != null) {
                    focusStack.prime(card)
                    goto(ExpressOpinion(card))
                }
            }
            // We don't have anything more to say about the cards
            when (counter("NothingMoreToSay", Game.cardSet)) {
                2 -> { goto(CheckIfDone) }
                else -> {
                    furhat.say(output.no_more_ideas)
                    goto(RequestOpinion)
                }
            }
        }
    }

}