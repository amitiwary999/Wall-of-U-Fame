import React, { Component } from 'react';
import {
    StyleSheet,
    View,
    Text,
    Image,
    StatusBar,
    TouchableOpacity,
    Animated,
    ActivityIndicator,
    ProgressBarAndroid,
    ImageBackground,
    Dimensions,
} from 'react-native';
import {
    GoogleSignin,
    GoogleSigninButton,
    statusCodes,
} from 'react-native-google-signin';
import auth from '@react-native-firebase/auth';

const LoginScreen = ({navigation}) => {

    GoogleSignin.configure({
        scopes: [],
        androidClientId:
            '538184241590-gelgo0d1492gns699pji45k02cc1psn7.apps.googleusercontent.com',
        webClientId:
            '538184241590-gelgo0d1492gns699pji45k02cc1psn7.apps.googleusercontent.com',
    });

    if (auth().currentUser) {
        navigation.navigate('HomeScreen')
    }

    const signIn = async () => {
        console.log("signin")
        try {
            var k = await GoogleSignin.hasPlayServices();
            try {
                const data = await GoogleSignin.signIn();
                try {
                    const credential = auth.GoogleAuthProvider.credential(
                        data.idToken,
                        data.accessToken,
                    );
                    const firebaseUserCredential = await auth().signInWithCredential(credential);
                    navigation.navigate('HomeScreen')
                } catch (error) {
                    console.log(error)
                }
            } catch (error) {
                console.log(error)
            }
        } catch (error) {
            console.log(error)
        }
    }

    return (
        <View style={styles.container}>
            <StatusBar hidden />
            <View style={styles.content}>
                <GoogleSigninButton
                    style={styles.googleButton}
                    size={GoogleSigninButton.Size.Wide}
                    color={GoogleSigninButton.Color.Light}
                    onPress={signIn}
                />
            </View>
        </View>
    )
}

export default LoginScreen

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: 'rgba(12, 8, 16, 1)',
    },
    content: {
        marginLeft: Dimensions.get('window').width * 0.021,
        marginRight: Dimensions.get('window').width * 0.021,
        marginTop: Dimensions.get('window').height * 0.5,
    },
    googleButton: {
        width: Dimensions.get('window').width * 0.9,
        height: 55,
        marginTop: Dimensions.get('window').height * 0.03,
        marginLeft: Dimensions.get('window').width * 0.021,
        marginRight: Dimensions.get('window').width * 0.021,
    },
})