
import React, { Component, useEffect, useState } from 'react';
import {
    StyleSheet,
    View,
    Text,
    Image,
    FlatList,
    Dimensions,
    StatusBar,
} from 'react-native';
import * as urls from '../Constants';
import auth from '@react-native-firebase/auth';
import {getPosts} from '../redux/actions';
import {useSelector, shallowEqual, useDispatch} from 'react-redux';
import {imageMime, videoMime} from '../common/constant'
import Video from 'react-native-video'
import deviceWidth from '../common/utils'

const HomeScreen = ({navigation}) => {

    const [posts, setPosts] = useState([])
    const dispatch = useDispatch()

    const {postData} =  useSelector(state => ({
        postData: state.homeReducer.posts
    }), shallowEqual)

    useEffect(() => {
        async function getPost(){
            if (auth().currentUser != null) {
                let data = JSON.stringify({limit: 10, nextKey: ''})
               let tokenResult = await auth().currentUser.getIdTokenResult();
               let token = tokenResult.token
               dispatch(getPosts(token, data))
            }
        }
        getPost();
    },[])

    useEffect(() => {
        if (postData != []) {
            setPosts(postData);
        }
    }, [postData])

    return (
        <View style={styles.container}>
            <StatusBar hidden />
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
                        <View style={styles.mediaContainerStyle}>
                            {
                                (item.mimeType.includes(imageMime))?(
                                    <Image style = {{flex: 1}}
                                        source = {{uri: item.mediaUrl}}
                                    />
                                ):(
                                    <Video 
                                    source = {{uri: item.mediaUrl}}
                                    resizeMode = "cover"
                                    style={StyleSheet.absoluteFill}
                                    />
                                )
                            }
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
        flex: 1,
        backgroundColor: 'white'
    },
    container: {
        flex: 1,
        backgroundColor: '#DFDFDF',
    },
    authorDetailStyle: {
        flex: 1,
        flexDirection: 'row',
        padding: 8
    },
    dpViewStyle: {
        width: 48,
        height: 48,
        borderRadius: 24,
    },
    authorNameStyle: {
        flex: 1,
        fontSize: 14,
        color: 'black',
        marginLeft: 8,
        alignItems: 'center',
        justifyContent: 'center', 
        textAlignVertical: 'center'
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

    mediaContainerStyle: {
        width: Dimensions.get('window').width - 16,
        height: Dimensions.get('window').width - 16,
        marginLeft: 8,
        marginRight: 8
    }
});