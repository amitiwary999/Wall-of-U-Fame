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

        case Actions.UPDATE_LIKE:
            updateLike(state, payload)    

        default:
            return state;    
    }
}

const updateLike = (state, pos) => {
    let post = state.posts[pos]
    post.isLiked = (post.isLiked == 0?1:0)
    let updatedPosts = state.posts
    updatedPosts[pos] = post
    // let updatedPosts = [state.posts.slice(0, pos), post, state.posts.slice(pos+1)]
    for(let i=0; i<updatedPosts.length; i++){
        console.log("post updated "+JSON.stringify(updatedPosts[i]))
    }
    return {...state, posts: updatedPosts}
}

export default homeScreenReducer