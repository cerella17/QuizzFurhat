import {
    BUTTON_TOGGLE,
    CARDS_REVEALED, CLEAR_TABLE,
    DECK_SELECTED,
    GAME_ENDED,
    GAMEROUND_UPDATED,
    NEW_GAME_BUTTON_TOGGLE,
    SHOW_SOLUTION_INDICATORS,
    SOLVER_BUTTON_TOGGLE,
    START_GAME
} from "../actions";

const initialState = {
    revealed: false,
    draggable: true,
    playing: true,
    buttonVisible: false,
    buttonLabel: "",
    showSolutionIndicators: false
}

export default function(state = initialState, action) {
    switch (action.type) {
        case START_GAME:
            return {
                ...state,
                playing: true,
                draggable: true
            }
        case CLEAR_TABLE:
            return {
                ...state,
                ...initialState
            }
        case CARDS_REVEALED:
            return {
                ...state,
                revealed: true
            }
        case BUTTON_TOGGLE:
            return {
                ...state,
                buttonVisible: action.show,
                buttonLabel: action.show ? action.label : ""
            }
        case SHOW_SOLUTION_INDICATORS:
            return {
                ...state,
                playing: false,
                draggable: false,
                showSolutionIndicators: true
            }
        default: return state
    }
}
