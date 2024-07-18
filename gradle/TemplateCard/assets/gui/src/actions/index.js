export const DECKS_RECEIVED = "DECKS_RECEIVED"
export const PLAYERS_UPDATED = "PLAYERS_UPDATED"
export const DECK_SELECTED = "DECK_SELECTED"
export const GAMEROUND_UPDATED = "GAMEROUND_UPDATED"
export const DECK_LOADED = "DECK_LOADED"
export const CARD_MOVED = "CARD_MOVED"
export const BUTTON_TOGGLE = "BUTTON_TOGGLE"
export const UPDATE_WINDOW_DIMENSIONS = "UPDATE_WINDOW_DIMENSIONS"
export const DECK_SYNCED = "DECK_SYNCED"
export const CLEAR_TABLE = "CLEAR_TABLE"
export const SHOW_SOLUTION_INDICATORS = "SHOW_SOLUTION_INDICATORS"
export const CARDS_LOADED = "CARDS_LOADED"
export const CARDS_REVEALED = "CARDS_REVEALED"
export const REVEAL_SOLUTION = "REVEAL_SOLUTION"

function getWindow() {
    return {
        width: window.innerWidth,
        height: window.innerHeight
    }
}

export function decksReceived(decks = []) {
    return {
        type: DECKS_RECEIVED,
        decks
    }
}

export function dealCards() {
    return {
        type: CARDS_REVEALED
    }
}

export function playersUpdated(players = {}) {
    return {
        type: PLAYERS_UPDATED,
        players
    }
}

export function gameRoundUpdated(gameRound) {
    return {
        type: GAMEROUND_UPDATED,
        data: {
            round : gameRound,
            window: getWindow()
        }
    }
}

export function handleNewPosition(index, position) {
    return {
        type: CARD_MOVED,
        data: {
            index,
            position,
            window: getWindow()
        }
    }
}
export function deckLoaded(deck) {
    // Since we received the cards from the backend, we need to convert to pixels instead of percentage
    const window = getWindow()
    return {
        type: DECK_LOADED,
        deck: {
            ...deck,
            cards: deck.cards.map((card) => {
                return {
                    ...card,
                    position: {
                        x: card.position.x * window.width,
                        y: card.position.y * window.height
                    }
                }
            })
        },
        header: deck.header
    }
}


export function deckSynced() {
    return {
        type: DECK_SYNCED,
    }
}

export function showButton(label, show = true) {
    return {
        type: BUTTON_TOGGLE,
        label,
        show
    }
}

export function hideButton() {
    return showButton({}, false)
}

export function showSolutionIndicators(indicators) {
    return {
        type: SHOW_SOLUTION_INDICATORS,
        indicators
    }
}

export function revealSolution(truths) {
    return {
        type: REVEAL_SOLUTION,
        truths
    }
}


export function updateWindowDimensions() {
    return {
        type: UPDATE_WINDOW_DIMENSIONS,
        window: getWindow()
    }
}

export function settingsUpdated(settings) {
    return {
        type: SETTINGS_UPDATE,
        settings
    }
}

export function clearTable() {
    return {
        type: CLEAR_TABLE
    }
}
