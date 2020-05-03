import homeScreenReducer from './HomeScreenReducer';
import loginReducers from './LoginReducer'
import { combineReducers } from 'redux';

const reducers = combineReducers({
    homeReducer: homeScreenReducer,
    loginReducers: loginReducers
})

export default reducers;
