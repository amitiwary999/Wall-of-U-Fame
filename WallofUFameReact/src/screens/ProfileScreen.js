import React, {useState, useEffect} from 'react';
import {
  StyleSheet,
  View,
  Text,
  Image,
  FlatList,
  Dimensions,
  StatusBar,
} from 'react-native';
import {Avatar} from 'react-native-elements';
import auth from '@react-native-firebase/auth';
import { deviceWidth } from '../common/utils';

const ProfileScreen = ({navigation}) => {

    return (
      <View style={styles.container}>
        <Avatar 
          style={styles.avatarContainer}
          rounded source={{uri: auth().currentUser.photoURL}} />
      </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex:1
    },
    avatarContainer: {
      alignSelf: 'center',
        height: 128,
        width: 128
    }
})

export default ProfileScreen;