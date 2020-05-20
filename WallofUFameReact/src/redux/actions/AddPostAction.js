import { API, PATH } from '../../common/network';
import * as Actions from '../type';
import storage from '@react-native-firebase/storage';

const savePost = (token, data) => {
    return (dispatch) => {
        dispatch(uploadPostPending())
        API.post(PATH.setPostSql, data, token).then(res => {
            dispatch(uploadPostSuccess())
        }).catch((error) => {
            console.log(error);
            dispatch(uploadPostFail())
        })
    }
}

const uploadMediaStart = () => {
    return{
        type: Actions.UPLOAD_MEDIA_SUCCESS
    }
}

const uploadPostPending = () => {
    return{
        type: Actions.ADD_POST_PENDING
    }
}

const uploadPostFail = () => {
    return{
        type: Actions.ADD_POST_FAILUE
    }
}

const uploadPostSuccess = (mediaUrl) => {
    return{
        type: Actions.ADD_POST_SUCCESS,
        payload: mediaUrl
    }
}

export {savePost}