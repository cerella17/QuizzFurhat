import {UPDATE_WINDOW_DIMENSIONS} from "../actions";

export default function(state = {}, action) {
    switch (action.type) {
        case UPDATE_WINDOW_DIMENSIONS:
            return action.window
        default: return state
    }
}
