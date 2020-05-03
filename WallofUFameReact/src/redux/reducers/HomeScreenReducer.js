import * as Actions from '../type'

const initialState = {
    posts : []
}

const homeScreenReducer = (state = initialState, action) => {
    const { type, payload } = action;

    switch(type){
        case Actions.FETCH_POSTS:
            return{
                ...state,
                posts: payload
            }

        default:
            return state;    
    }
}

export default homeScreenReducer