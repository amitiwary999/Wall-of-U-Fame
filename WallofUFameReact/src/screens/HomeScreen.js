
import React, { Component, useEffect, useState, useRef, useImperativeHandle } from 'react';
import {
    StyleSheet,
    View,
    Text,
    Image,
    FlatList,
    Dimensions,
    StatusBar,
    AppState,
    TouchableOpacity
} from 'react-native';
import * as urls from '../Constants';
import auth from '@react-native-firebase/auth';
import {getPosts, likePost, addBookmark} from '../redux/actions';
import {useSelector, shallowEqual, useDispatch} from 'react-redux';
import {imageMime, videoMime} from '../common/constant'
import Video from 'react-native-video'
import {Icon, Spinner} from 'native-base';
import deviceWidth from '../common/utils'
import FastImage from 'react-native-fast-image'

const HomeScreen = ({navigation}) => {

    const [posts, setPosts] = useState([])
    const [viewIndex, setViewIndex] = useState(-1)
    const dispatch = useDispatch()

    const {postData, playAndPause} =  useSelector(state => ({
        postData: state.homeReducer.posts,
        playAndPause: state.homeReducer.playPause
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
        console.log("post data story "+postData)
        if (postData != []) {
            setPosts(postData);
        }
    }, [postData])

    const onViewRef = useRef((viewableItems)=> {
        console.log("viewable "+JSON.stringify(viewableItems))
        let items = viewableItems.viewableItems
        if(items){
            setViewIndex(items[0].index)
        }
        
        // Use viewable items in state or as intended
    })
    const viewConfigRef = useRef({ viewAreaCoveragePercentThreshold: 50 })

    const updateLike = async(pos, id, increment) => {
        if (auth().currentUser != null) {
            let data = JSON.stringify({postId: id, increment: increment})
               let tokenResult = await auth().currentUser.getIdTokenResult();
               let token = tokenResult.token
               dispatch(likePost(token, data, pos))
        }
    }

    const updateBookmark = async(pos, id, addBookmarkOrNot) => {
        if(auth().currentUser != null){
            let data = JSON.stringify({ uid: useImperativeHandle, postId: id, addBookmark: addBookmarkOrNot })
            let tokenResult = await auth().currentUser.getIdTokenResult();
            let token = tokenResult.token
            dispatch(addBookmark(token, data, pos))
        }
    }

    return (
        <View style={styles.container}>
            {console.log("play and pause "+playAndPause)}
            <StatusBar hidden />
            {posts && posts.length>0 ?
            <FlatList
            onViewableItemsChanged={onViewRef.current}
                keyExtractor={item => item.postId}
                viewabilityConfig={viewConfigRef.current}
                contentContainerStyle={[{ width: Dimensions.get('window').width }]}
                data={posts}
                renderItem={({ item, index }) =>
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
                                    <FastImage style = {{flex: 1}}
                                        source = {{uri: item.mediaUrl}}
                                        resizeMode={FastImage.resizeMode.contain}
                                    />
                                ):(
                                    <Video 
                                    repeat
                                    source = {{uri: item.mediaUrl}}
                                    resizeMode = "cover"
                                    // onBuffer= {(data) => {
                                    //   //  console.log("buffer "+JSON.stringify(data))
                                    // }}
                                    // onReadyForDisplay = {() => {
                                    //    // console.log("ready for display")
                                    // }}
                                    // onLoadStart = {
                                    //    // console.log("load start")
                                    // }
                                    // onLoad = {
                                    //   //  console.log("on laod")
                                    // }
                                    // onProgress={(data) => {
                                    //     //console.log("progress "+JSON.stringify(data))
                                    // }}
                                    // onPlaybackStalled = {console.log("palyback stalled")}
                                    // onPlaybackResume={console.log("play resume")}
                                    paused={(!(index == viewIndex) && !AppState.currentState.match(/inactive|background/)) ||(!playAndPause)}
                                    style={StyleSheet.absoluteFill}
                                    />
                                )
                            }
                        </View>
                        <View style={{flexDirection: 'row', justifyContent: 'space-between', backgroundColor: 'white', marginLeft: 8, marginRight: 8}}>
                            <TouchableOpacity style={{flex:1}} onPress = {() => {
                                // console.log("pressed like")
                                updateLike(index, item.postId, item.isLiked == 1 ? 0 : 1)
                            }}>
                                {item.isLiked ? <Icon name="like1" type="AntDesign" style={{color: 'black', fontSize: 36}}/> :
                                 <Icon name="like2" type ="AntDesign" style={{color: 'black', fontSize: 36}}/> } 
                            </TouchableOpacity>

                            <TouchableOpacity style={{ flex: 1 }} onPress={() => {
                                updateBookmark(index, item.postId, item.isBookmarked == 1 ? 0 : 1)
                            }}>
                                {item.isBookmarked ? <Icon name="bookmark" type="FontAwesome" style={{color: 'black', fontSize: 36}}/> : 
                                 <Icon name="bookmark-o" type="FontAwesome" style={{color: 'black', fontSize: 36}}/> }
                            </TouchableOpacity>        
                        </View>    
                    </View>
                }
            /> : 
                <Spinner
                    color="black"
                    size={40}
                    animating={true}
                />
            }
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