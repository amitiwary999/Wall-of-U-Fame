import * as Actions from '../types'

const initialState = {
    posts = []
}

const homeScreenReducer = (state = initialState, action) => {
    const { type, payload } = action;

    switch(type){
        case Actions.FETCH_POSTS:
            return{
                ...state,
                posts: payload
            }
    }
}

export default homeScreenReducer