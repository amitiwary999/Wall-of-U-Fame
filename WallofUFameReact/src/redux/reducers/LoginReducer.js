import * as Actions from '../type';

const initialState = {
    loginAndSendingData: false,
    sendingDataSuccess: false,
    sendingDataFailure: false
}

const LoginReducer = (state = initialState, action) => {
    const {type, payload} = action;

    switch(type){
        case Actions.LOGIN_PENDING:{
            return{
                ...state,
                loginAndSendingData: true
            }
        }

        case Actions.USER_DATA_UPLOAD_SUCCESS: {
            return{
                ...state,
                loginAndSendingData: false,
                sendingDataSuccess: true
            }
        }

        case Actions.USER_DATA_UPLOAD_FAILURE: {
            return{
                ...state,
                loginAndSendingData: false,
                sendingDataFailure: true
            }
        }

        default: {
            return state
        }
    }
}

export default LoginReducer