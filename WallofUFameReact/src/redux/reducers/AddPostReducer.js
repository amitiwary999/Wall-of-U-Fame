import * as Actions from '../type'

const initialState = {
    postAddResponseSuccess: false,
    postAddResponseFailure: false,
    loading: false,
    loadingMedia: false,
    mediaUrl: ''
}

const addPostReducer = (state = initialState, action) => {
    const {type, payload} = action

    switch(type){
        case Actions.ADD_POST_PENDING:
            return {
                ...state,
                loading: true
            }

        case Actions.ADD_POST_SUCCESS:
            return {
                ...state,
                loading: false,
                postAddResponse: true,
                postAddResponseFailure: false
            }  
            
        case Actions.ADD_POST_FAILUE:
            return {
                ...state,
                loading: false,
                postAddResponseFailure: true,
                postAddResponseSuccess: false
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