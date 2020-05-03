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

export { getPosts }