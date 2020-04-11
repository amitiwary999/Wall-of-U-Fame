/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

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
import urls from 'src/Constants';
import auth from '@react-native-firebase/auth';

import {
  Header,
  LearnMoreLinks,
  Colors,
  DebugInstructions,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

class App extends React.Component {

constructor(){
   this.state = {
    posts: []
   }
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
    console.log(error)
  })
  .then(response => {
    if(response.ok){
      return response.json()
    }else{
      throw new Error("bad response ")
    }
  })
  .then(responseJson => {
    if(responseJson !== undefined){
      posts = responseJson
    }
  })
  .catch(error => {
    console.log(error);
  })
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

    })
    .then( response => {
      if(response.ok){
        return response.json()
      }else{
        throw new Error()
      }
    })
    .catch( error => {

    })
    then(responseJson => {
        this.setState({posts: responseJson})
    })
  }

  render(){
   <View style = {styles.container}>
     <FlatList 
     data = {posts}
     renderItem = {({item}) => {
      <View style = {styles.itemContainer}>
        <View style = {styles.authorDetailStyle}>
          <Image style = {styles.dpViewStyle}
             source = {}/>
          <Text style = {styles.authorNameStyle}>
              
          </Text>
        <View>
      </View>
     }
   }
   />
   </View>
 }
}

const styles = StyleSheet.create{
  container:{
    flex:1
  },

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
}

export default App;
