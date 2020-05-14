import { API, PATH } from '../../common/network';
import * as Actions from '../type';
import storage from '@react-native-firebase/storage';

savePost = (token, data) => {
    return (dispatch) => {
        
    }
}

uploadMediaStart = () => {
    return{
        type: Actions.UPLOAD_MEDIA_SUCCESS
    }
}

uploadMediaFail = () => {
    return{
        type: Actions.UPLOAD_MEDIA_FAILURE
    }
}

uploadMediaSuccess = (mediaUrl) => {
    return{
        type: Actions.UPLOAD_MEDIA_SUCCESS,
        payload: mediaUrl
    }
}

uploadMedia = (uri) => {
    let mediaUrl = ""
    try {
        uploadMediaStart()
        let storageRef = getStorageLocation()
        await storageRef.putFile(uri)
        console.log('upload done')
        let uploadedOfferMediaUrl = await storageRef.getDownloadURL()
        if (uploadedOfferMediaUrl != null && uploadedOfferMediaUrl != undefined) {
            mediaUrl = uploadedOfferMediaUrl;
        }
        console.log("media url " + mediaUrl)
        uploadMediaSuccess(mediaUrl)
    } catch (error) {
        uploadMediaFail()
    }
}

const getStorageLocation = () => {
    let key = getFirebaseStorageUploadKey();
    return storage().ref().child(key)
}

const getFirebaseStorageUploadKey = () => {
    console.log("storage location key")
    let currentTime = new Date().getTime();
    let userId = auth().currentUser.uid;
    let key = currentTime + "_" + userId + ".mp4";
    return key;
}

export {uploadMedia, savePost}