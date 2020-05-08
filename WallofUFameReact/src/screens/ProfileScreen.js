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
        <View style={styles.avatarContainer}>
          <Avatar rounded source={{uri: auth().currentUser.photoURL}} />
        </View>
      </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex:1
    },
    avatarContainer: {
        justifyContent: 'center',
        height: 64,
        width: 64
    }
})

export default ProfileScreen;