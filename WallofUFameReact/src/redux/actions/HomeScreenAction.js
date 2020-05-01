import {API, PATH} from '../../common/network';
import * as Actions from '../types';

const dispatchGetPostAction = (posts) => {
    return{
        type: Actions.FETCH_POSTS,
        payload: posts
    }
}

const getPosts =(token) => {
    return (dispatch) => {
         API.post(PATH.getPost, null, token).then(res => {
             if(res != null && res != undefined){
                 dispatch(dispatchGetPostAction(res))
             }
         });
    }
   
}

export { getPosts }