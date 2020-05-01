
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
import {getPosts} from '../redux/actions';
import {useSelector, shallowEqual, useDispatch} from 'react-redux';

const HomeScreen = ({navigation}) => {

    const [posts, setPosts] = useState([])
    const dispatch = useDispatch()

    const {postData} =  useSelector(state => ({
        postData: state.homeReducer.posts
    }), shallowEqual)

    useEffect(() => {
        async function getPost(){
            if (auth().currentUser != null) {
               let tokenResult = await auth().currentUser.getIdTokenResult();
               let token = tokenResult.token
               dispatch(getPosts(token))
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