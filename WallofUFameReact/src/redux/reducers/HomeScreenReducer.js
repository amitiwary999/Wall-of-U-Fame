import * as Actions from '../type'
import { Item } from 'native-base';

const initialState = {
    posts : [],
    playPause: 1,
    homeScreenFocus: 1
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
          //  updateLike(state, payload)
            let item = state.posts[payload]
            item.isLiked = (item.isLiked == 0?1:0)
            let newPost = [...state.posts.slice(0, payload), item, ...state.posts.slice(payload+1)]
            return {...state, posts: newPost}

        case Actions.UPDATE_BOOKMARK:
            let updateItem = state.posts[payload]
            updateItem.isBookmarked = (updateItem.isBookmarked == 0 ? 1 : 0)
            let updatedPost = [...state.posts.slice(0, payload), updateItem, ...state.posts.slice(payload + 1)]
            return { ...state, posts: updatedPost }
            
        case Actions.MEDIA_PLAY_PAUSE:    
             return{
                 ...state,
                 playPause: payload
             }
             
        case Actions.HOME_SCREEN_FOCUS:
            return {
                ...state, 
                homeScreenFocus: payload
            }     

        default:
            return state;    
    }
}

const updateLike = (state, pos) => {
    let post = state.posts[pos]
    post.isLiked = (post.isLiked == 0?1:0)
    let updatedPosts = {...state.posts}
    updatedPosts[pos] = post
    // let updatedPosts = [state.posts.slice(0, pos), post, state.posts.slice(pos+1)]
    for(let i=0; i<updatedPosts.length; i++){
        console.log("post updated "+JSON.stringify(updatedPosts[i]))
    }
    return {...state, posts: updatedPosts}
}

export default homeScreenReducer