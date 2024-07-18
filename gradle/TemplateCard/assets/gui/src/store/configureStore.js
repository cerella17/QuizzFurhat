import { createStore } from 'redux'
import rootReducer from "../reducers";

export default function configureStore(initialState) {
    const store = createStore(
        rootReducer,
        initialState,
        window.__REDUX_DEVTOOLS_EXTENSION__  ? window.__REDUX_DEVTOOLS_EXTENSION__ () : undefined
    )

    if (module.hot) {
        // Enable Webpack hot module replacement for reducers
        module.hot.accept('../reducers', () => {
            const nextRootReducer = require('../reducers').default;
            store.replaceReducer(nextRootReducer);
        })
    }

    return store
}
