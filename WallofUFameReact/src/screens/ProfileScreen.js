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
    const [name, setName] = useState(auth().currentUser.displayName)
    return (
      <View style={styles.container}>
        <Avatar 
          style={styles.avatarContainer}
          rounded source={{uri: auth().currentUser.photoURL}} />

        <Text style={styles.textBoxStyle}>{auth().currentUser.displayName}</Text>
        <Text style={styles.textBoxStyle}>{auth().currentUser.email}</Text>
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
    },
   textBoxStyle: {
    color: '#FFFFFF',
    height: 50,
    borderWidth: 1,
    borderColor: '#FFFFFF',
    borderRadius: 10,
    backgroundColor: '#000000',
    padding: 10,
    marginLeft: 16,
    marginRight: 16,
    marginTop: 4,
    marginBottom: 4,
  },
})

export default ProfileScreen;