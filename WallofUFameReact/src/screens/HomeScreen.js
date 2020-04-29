
import React, { Component, useEffect, useState } from 'react';
import {
    StyleSheet,
    View,
    Text,
    Image,
    FlatList,
    Dimensions,
} from 'react-native';
import * as urls from '../Constants';
import auth from '@react-native-firebase/auth';

const HomeScreen = ({navigation}) => {

    const [posts, setPosts] = useState([])
    useEffect(() => {
        getPostData()
    })

    getData = (token) => {
        fetch(urls.fetchPostUrl, {
            method: 'POST',
            headers: {
                Authorization: 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                nextKey: '',
                limit: 10
            })
        })
            .catch(error => {
                console.log("fetch error " + error)
            })
            .then(response => {
                if (response.ok) {
                    let responseJson = response.json()
                    console.log("response " + JSON.stringify(responseJson))
                    return responseJson
                } else {
                    throw new Error("bad response ")
                }
            })
            .catch(error => {
                console.log("response parse error " + error);
            })
            .then(responseJson => {
                console.log("response json " + responseJson)
                if (responseJson != undefined) {
                    for (let i = 0; i < responseJson.length; i++) {
                        console.log(responseJson[i].postId + " " + responseJson[i].userName)
                    }
                }
                setPosts(responseJson)
            })
            .catch(error => console.log("final error " + error))
    }

    getToken = async () => {
        let token = await auth().currentUser.getIdTokenResult();
        console.log('token got ' + token.token);
        return token.token;
    };

    getPostData = () => {
        getToken()
            .then(token => {
                getData(token)
            })
            .catch(error => {
                console.log(error)
            })
    }

    return (
        <View style={styles.container}>
            <FlatList
                keyExtractor={item => item.postId}
                contentContainerStyle={[{ width: Dimensions.get('window').width }]}
                data={posts}
                renderItem={({ item }) =>
                    <View style={styles.itemContainer}>
                        <View style={styles.authorDetailStyle}>
                            <Image style={styles.dpViewStyle}
                                source={{ uri: item.userDp }} />
                            <Text style={styles.authorNameStyle}>
                                {item.userName}
                            </Text>
                        </View>
                    </View>

                }
            />
        </View>
    )
}

export default HomeScreen

const styles = StyleSheet.create({
    listStyle: {
        margin: 8
    },
    listItemStyle: {
        flex: 1,
        margin: 8
    },
    itemContainer: {
        flex: 1
    },
    container: {
        flex: 1,
        backgroundColor: 'rgba(12, 8, 16, 1)',
    },
    authorDetailStyle: {
        flex: 1,
        flexDirection: 'row'
    },
    dpViewStyle: {
        width: 48,
        height: 48,
        borderRadius: 24
    },
    authorNameStyle: {
        height: 24,
        color: 'white',
        textAlign: 'center',
    },
    item: {
        backgroundColor: '#f9c2ff',
        padding: 20,
        marginVertical: 8,
        marginHorizontal: 16,
    },
    title: {
        fontSize: 32,
    },
});