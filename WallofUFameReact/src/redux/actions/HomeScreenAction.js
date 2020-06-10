import {API, PATH} from '../../common/network';
import * as Actions from '../type';

const dispatchGetPostAction = (posts) => {
    return{
        type: Actions.FETCH_POSTS,
        payload: posts
    }
}

const getPosts =(token, data) => {
    return (dispatch) => {
         API.post(PATH.getPost, data, token).then(res => {
             if(res != null && res != undefined){
                 dispatch(dispatchGetPostAction(res))
             }
         });
    }
   
}

const updateLike = (pos) => {
    return {
        type: Actions.UPDATE_LIKE,
        payload: pos
    }
}

const likePost = (token, data, pos) => {
    return (dispatch) => {
        dispatch(updateLike(pos))
        API.post(PATH.updateLike, data, token).then(res => {
            console.log("like api response ")
            if(res != null && res != undefined){
                
            }
        }).catch(error => {
            console.log("like api response error "+error)
        })
    }
}

export { getPosts, likePost }