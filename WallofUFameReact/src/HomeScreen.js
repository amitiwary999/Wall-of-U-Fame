import React from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  Image,
  StatusBar,
  FlatList
} from 'react-native';
import * as urls from './Constants';
import auth from '@react-native-firebase/auth';

import {
  Header,
  LearnMoreLinks,
  Colors,
  DebugInstructions,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

class HomeScreen extends React.Component {

constructor(){
  super()
   this.state = {
    posts: []
   }
}

componentDidMount(){
  this.getPostData()
}

 getData = (token) => {
  fetch( urls.fetchPostUrl, {
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + token,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        nextKey : '',
        limit : 10
      })
    })
  .catch(error => {
    console.log("fetch error "+error)
  })
  .then(response => {
    if(response.ok){
      let responseJson = response.json()
       console.log("response "+JSON.stringify(responseJson))
      return responseJson
    }else{
      throw new Error("bad response ")
    }
  })
  .catch(error => {
    console.log("response parse error "+error);
  })
  .then(responseJson => {
    console.log("response json "+responseJson)
    this.setState({posts: responseJson})
  })
  .catch(error => console.log("final error "+error))
 }

   getToken = async () => {
    let token = await auth().currentUser.getIdTokenResult();
    console.log('token got ' + token.token);
    return token.token;
  };

  getPostData = () => {
    this.getToken()
    .then(token => {
      this.getData(token)
    })
    .catch( error => {
      console.log(error)
    })
  }

  render(){
    return(
   <View style = {styles.container}>
     <FlatList 
     keyExtractor={item => item.postId}
     data = {this.state.posts}
     renderItem = {({item}) => {
      <View style = {styles.itemContainer}>
        <View style = {styles.authorDetailStyle}>
          <Image style = {styles.dpViewStyle}
             source = {item.userDp}/>
          <Text style = {styles.authorNameStyle}>
              item.userName
          </Text>
        </View>
      </View>
     }
   }
   />
   </View>
   )
 }
}

const styles = StyleSheet.create({
   listStyle:{
    margin:8
  },
  listItemStyle:{
    flex:1,
    margin:8
  },
  itemContainer:{
    flex: 1
  },
  container: {
    flex: 1,
    backgroundColor: 'rgba(12, 8, 16, 1)',
  },
  authorDetailStyle:{
    flex:1,
    flexDirection:'row'
  },
  dpViewStyle: {
     width:48,
     height:48,
     borderRadius:24
  },
  authorNameStyle: {
    height:24,
    color: '#fff',
    textAlign: 'center',
  }
});

export default HomeScreen