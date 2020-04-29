import { createStore, combineReducers, compose, applyMiddleware } from 'redux';
import { createLogger } from 'redux-logger';
import thunk from 'redux-thunk';
import reducers from '../reducers';

const middlewares = [thunk];
if (process.env.NODE_ENV !== 'production') {
    middlewares.push(createLogger());
}

export default createStore(reducers, compose(applyMiddleware(...middlewares)));
