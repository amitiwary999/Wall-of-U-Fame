import { API, PATH } from '../../common/network';
import * as Actions from '../type';
import storage from '@react-native-firebase/storage';

const savePost = (token, data) => {
    return (dispatch) => {
        
    }
}

const uploadMediaStart = () => {
    return{
        type: Actions.UPLOAD_MEDIA_SUCCESS
    }
}

const uploadPostFail = () => {
    return{
        type: Actions.UPLOAD_MEDIA_FAILURE
    }
}

const uploadPostSuccess = (mediaUrl) => {
    return{
        type: Actions.UPLOAD_MEDIA_SUCCESS,
        payload: mediaUrl
    }
}

export {savePost}