import {
    CARDS_LOADED,
    CARD_MOVED,
    DECK_SYNCED, SHOW_SOLUTION_INDICATORS, DECKS_RECEIVED, DECK_LOADED, REVEAL_SOLUTION, CLEAR_TABLE
} from "../actions";

const initialState = {
    decks: [],
    deck: {
        cards: [],
        synced: false,
    },
    truths: []
}

export default function(state = initialState, action) {
    switch (action.type) {
        case DECKS_RECEIVED:
            return {
                ...state,
                decks: action.decks
            }
        case DECK_LOADED:
            return {
                ...state,
                deck: {
                    ...state.deck,
                    ...action.deck,
                    synced: true
                }
            }
        case CLEAR_TABLE:
            return {
                ...state,
                ...initialState
            }
        case DECK_SYNCED:
            return {
                ...state,
                deck: {
                    ...state.deck,
                    synced: true
                }
            }
        case CARD_MOVED:
            const { index, position } = action.data
            const updatedCards = state.deck.cards.map((card, i) => {
                if (index == i) {
                    return {
                        ...card,
                        position: {
                            x: position.x,
                            y: position.y
                        }
                    }
                }
                else {
                    return card
                }
            })
            return {
                ...state,
                deck: {
                    ...state.deck,
                    synced: false,
                    cards: updatedCards
                }
            }
        case SHOW_SOLUTION_INDICATORS:
            const { indicators } = action
            return {
                ...state,
                deck: {
                    ...state.deck,
                    cards: state.deck.cards.map((card, i) => {
                        return {
                            ...card,
                            solutionIndicator: indicators[i]
                        }
                    })
                }
            }
        case REVEAL_SOLUTION:
            return {
                ...state,
                truths: action.truths
            }
        default: return state
    }
}
