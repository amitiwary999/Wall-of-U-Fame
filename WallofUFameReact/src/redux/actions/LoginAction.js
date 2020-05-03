import {API, PATH} from '../../common/network';
import * as Actions from '../type';

const uploadDataSuccess = () =>{
    return{type: Actions.USER_DATA_UPLOAD_SUCCESS}
}

const uploadDataFailed = () => {
    return{type: Actions.USER_DATA_UPLOAD_FAILURE}
}

const uploadDataPending = () => {
    return {type: Actions.LOGIN_PENDING}
}

const uploadUserData = (token, data) => {
  return (dispatch) => {
      dispatch(uploadDataPending())
    API.post(PATH.setUserSql, data, token).then(res => {
        console.log("login data res "+res)
      if (res != null && res != undefined) {
        dispatch(uploadDataSuccess());
      }else{
          dispatch(uploadDataFailed())
      }
    });
  };
};

export {uploadUserData}