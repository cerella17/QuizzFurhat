package furhatos.app.cardgame

import furhatos.app.cardgame.game.Card
import furhatos.app.cardgame.game.CardOrder
import furhatos.event.Event
import furhatos.records.Record

// Gameplay events
class SenseCardsMovedDone() : Event()
class SenseCardsReordered(val lastOrder: CardOrder, val newOrder: CardOrder) : Event()
class ActionShowDecks(val decks: List<EventDeck>) : Event()

// Events passed from flow
class ActionAttendCard(val cardId: String) : Event()
class ActionPlayersUpdate(val left: Boolean, val right: Boolean) : Event()
class ActionDealCards(val deck: DeckWithCards) : Event()
class ActionGameState(
        val deck: DeckWithCards? = null,
        val decks: List<EventDeck>? = null,
        val left: Boolean? = null,
        val right: Boolean? = null,
        val button: String? = null,
        val indicators: List<Boolean>? = null,
        val solution: List<Truth>? = null
) : Event()
class ActionClearTable : Event()
//class ActionStartGame : Event()
class ActionShowButton(val label: String) : Event()
class ActionHideButton : Event()
class ActionShowSolutionIndicators(val indicators: List<Boolean>) : Event()
class ActionRevealSolution(val solution: List<Truth>): Event()

// Events passed from frontend
//const val SenseGameNewGame = "SenseGameNewGame"
const val SenseGameReset = "SenseGameReset"
const val SenseGamePause = "SenseGamePause"
const val SenseButtonClick = "SenseButtonClick"
const val SenseSelectDeck = "SenseSelectDeck"
const val SenseCardsMoved = "SenseCardsMoved"

// Data classes used for events
data class GameRound(
        var deck: DeckWithCards? = null,
        var playing: Boolean = false,
        var showButton: Boolean = false,
        var buttonLabel: String = "",
        var showSolutionIndicators: Boolean = false
) : Record()

data class DeckWithCards (
        val name: String,
        val instruction: String,
        val cards: List<EventCard>
) : Record()

data class EventDeck (
        val name: String,
        val imageSrc: String
) : Record()

data class EventCard (
        val name: String,
        val imageSrc: String,
        var position: Position = Position()
) : Record()

data class Position(
        val x: Double = 0.0,
        val y: Double = 0.0
) : Record()

data class Truth(
        val truth: String,
        val order: Int
) : Record()