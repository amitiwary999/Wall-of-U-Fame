import * as Actions from '../type'
import {SUCCESS, FAILURE, PENDING} from '../../common'

const initialState = {
    postAddStatus: '',
    loadingMedia: false,
    mediaUrl: ''
}

const addPostReducer = (state = initialState, action) => {
    const {type, payload} = action

    switch(type){
        case Actions.ADD_POST_PENDING:
            return {
                ...state,
                postAddStatus: PENDING
            }

        case Actions.ADD_POST_SUCCESS:
            return {
                ...state,
                loading: false,
                postAddStatus: SUCCESS
            }  
            
        case Actions.ADD_POST_FAILUE:
            return {
                ...state,
                loading: false,
                postAddStatus: FAILURE
            } 
            
        case Actions.UPLOAD_MEDIA_PENDING:
            return{
                ...state,
                loadingMedia: true
            }
            
        case Actions.UPLOAD_MEDIA_FAILURE:
            return{
                ...state,
                loadingMedia: false
            }
            
        case Actions.UPLOAD_MEDIA_SUCCESS:
            return{
                ...state,
                loadingMedia: false,
                mediaUrl: payload
            }   
            
        default:
            return state    
    }
}

export default addPostReducer