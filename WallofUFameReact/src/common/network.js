import axios from 'axios';

const BASE_URL = 'https://expinf.firebaseapp.com/';

const http = token =>
  axios.create({
    baseURL: BASE_URL,
    responseType: 'json',
    headers: {
      Accept: 'application/json',
      Authorization: token ? 'Bearer ' + token : {},
      'Content-Type': 'application/json',
    },
  });

const PATH = {
  getPost : 'getBlogSql',
  setUserSql: 'setUserSql',
  setPostSql: 'setPostSql',
  updateLike: 'setPostLikeSql',
  updateBookmark: 'setBookmarkSql',
  getLikedPost: 'getLikedPost',
  getBookmarkedList: 'getBookmarkedList'
};

const API = {
  post: (url, data = {}, token = null) => {
    console.log(url);
    return http(token)
      .post(url, data)
      .then(res => {
        console.log("response "+res);
        return res.data;
      })
      .catch(error => {
        console.log(" API error "+error);
      });
  },
};

export {API, PATH}
