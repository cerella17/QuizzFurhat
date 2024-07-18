import { combineReducers } from 'redux'
import PlayersReducer from './players'
import ContentReducer from './content'
import RoundReducer from './round'
import WindowReducer from './window'
import SettingsReducer from './settings'

const rootReducer = combineReducers({
    players: PlayersReducer,
    content: ContentReducer,
    round: RoundReducer,
    window: WindowReducer,
    settings: SettingsReducer
});

export default rootReducer;
