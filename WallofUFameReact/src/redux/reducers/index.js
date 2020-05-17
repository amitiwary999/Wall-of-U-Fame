import homeScreenReducer from './HomeScreenReducer';
import loginReducers from './LoginReducer'
import addPostReducer from './AddPostReducer'
import { combineReducers } from 'redux';

const reducers = combineReducers({
    homeReducer: homeScreenReducer,
    loginReducers: loginReducers,
    addPostReducer: addPostReducer
})

export default reducers;
