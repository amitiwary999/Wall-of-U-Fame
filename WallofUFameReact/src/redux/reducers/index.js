import homeScreenReducer from './HomeScreenReducer';
import { combineReducers } from 'redux';

const reducers = combineReducers({
    homeReducer: homeScreenReducer
})

export default reducers;
