import {PLAYERS_UPDATED} from "../actions";

const initialState = {
    left: false,
    right: false
}

export default function(state = initialState, action) {
    switch (action.type) {
        case PLAYERS_UPDATED:
            return {
                ...state,
                ...action.players
            }
        default: return state
    }
}
