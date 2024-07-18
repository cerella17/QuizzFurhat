import {SETTINGS_UPDATE} from "../actions";

const initialState = {
    revealDelay: 2000,
    updateInterval: 1000
}

export default function(state = initialState, action) {
    switch (action.type) {
        case SETTINGS_UPDATE:
            return action.settings
        default: return state
    }
}
