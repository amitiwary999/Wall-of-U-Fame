import React, {useEffect, useState} from 'react'
import { View, StyleSheet, TouchableOpacity, Image, Text} from 'react-native';
import { Card } from 'react-native-elements';
import {deviceWidth, deviceHeight} from '../common/utils'
import ImagePicker from 'react-native-image-crop-picker';
import Entypo from 'react-native-vector-icons/Entypo';
import { add } from 'react-native-reanimated';
import { TextInput } from 'react-native-gesture-handler';
import {savePost} from '../redux/actions'
import auth from '@react-native-firebase/auth';

let uploadMediaUrl = ''

const AddPost = ()=> {
   const [mediaPicked, setMediaPicked] = useState(false)
   const [mediaUri, setMediaUri] = useState('')
   cosnt [postDesc, setPostDesc] = useState('')
   const [uploadingMediaProgress, setUploadingMediaProgress] = useState(false)
   const [uploadingPost, setUploadingPost] = useState(false)
   const dispatch = useDispatch()

   const {mediaUrl, loading, loadingMedia} =  useSelector(state => ({
       mediaUrl : state.addPostReducer.mediaUrl,
       loading : state.addPostReducer.loading,
       loadingMedia :state.addPostReducer.loadingMedia
   }), shallowEqual)

   useEffect(() => {
       setUploadingPost(loading)
   }, [loading])

    const options = {
        title: 'Select Media',
        storageOptions: {
            skipBackup: true,
            path: 'videos',
        },
    }

    const addMedia = () => {
        console.log("add media")
        ImagePicker.openPicker({
            mediaType: "video",
        }).then((video) => {
            console.log(video);
            setMediaUri(video.path)
            setMediaPicked(true)
        });
    }

    const removeMedia = () => {
        setMediaUri('')
        setMediaPicked(false)
    }

    const addPost = ()=> {
        if(mediaUri){
            uploadMedia(mediaUri)
        }
    }

    uploadMedia = async(uri) => {
        let mediaUrl = ""
        setUploadingMediaProgress(true)
        try {
            let storageRef = getStorageLocation()
            await storageRef.putFile(uri)
            console.log('upload done')
            let uploadedOfferMediaUrl = await storageRef.getDownloadURL()
            if (uploadedOfferMediaUrl != null && uploadedOfferMediaUrl != undefined) {
                 uploadMediaUrl = uploadedOfferMediaUrl;
            }
            console.log("media url " + mediaUrl)
            setUploadingMediaProgress(false)
            sendPost()
            } catch (error) {
                setUploadingMediaProgress(false)
            }
    }

    const sendPost = async() => {
        let postId = generatePostId()
        let tokenResult = await auth().currentUser.getIdTokenResult();
        let token = tokenResult.token
        let data = JSON.stringify({
            description: '',
            mediaUrl: uploadMediaUrl,
            mediaThumbUrl: '',
            postId: postId,
            mimeType: mimeType
        })
        dispatch(savePost(token, data))
    }

    const generatePostId = async() => {
        let currentTime = new Date().getTime();
        let userId = auth().currentUser.uid;
        let key = currentTime + "_" + userId
        return key;
    }

    const getStorageLocation = () => {
        let key = getFirebaseStorageUploadKey();
        return storage().ref().child(key)
    }
    
    const getFirebaseStorageUploadKey = () => {
        console.log("storage location key")
        let currentTime = new Date().getTime();
        let userId = auth().currentUser.uid;
        let key = currentTime + "_" + userId + ".mp4";
        return key;
    }

    return(
        <View style={styles.container}>
                {mediaPicked &&( <View style = {styles.mediaParentStyle}>
                    <Image style={styles.selectedMediaStyle} source={{uri: mediaUri}} />
                    <View style={styles.closeMedia}>
                    <Entypo name={'cross'} color='white' size={24} onPress={removeMedia} />
                    </View>
                </View>)}
                {!mediaPicked && (<TouchableOpacity onPress={ () =>{
                    console.log("media clicked")
                    addMedia()}
                    } style={styles.addMediaButtonStyle}>
                    <Text style = {styles.addMediaButtonText}>Add media</Text>
                </TouchableOpacity>)}
                <TextInput 
                style = {styles.textBoxStyle}
                placeholder="Express your love"
                placeholderTextColor="#FFFFFF"
                onChangeText={postDesc => setPostDesc(postDesc)}
                value={postDesc}/>
                <TouchableOpacity 
                onPress = {addPost}
                style={styles.addMediaButtonStyle}>
                <Text style={styles.addMediaButtonText}>Done</Text>
                </TouchableOpacity>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex:1
    },
    addMediaStyle:{
        height: deviceWidth - 10,
        width: deviceWidth - 10,
        borderRadius: 6, 
        backgroundColor: 'black',
        marginLeft: 5,
        marginRight: 5
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
    selectedMediaStyle: {
        height: deviceWidth - 10,
        width: deviceWidth - 10,
        borderRadius: 6,
        backgroundColor: 'black',
        marginLeft: 5,
        marginRight: 5,
        marginTop:5
    },
    addMediaButtonStyle: {
        height: 50,
        marginBottom: 20,
        backgroundColor: 'white',
        alignSelf: 'center',
        justifyContent: 'center',
        paddingLeft: 12,
        paddingRight: 12,
        marginLeft: 16,
        marginRight: 16,
        marginTop: 8,
        elevation: 2,
        borderRadius: 4
    },
    addMediaButtonText: {
        color: 'black',
        fontSize: 16,
        alignItems: 'center',
        justifyContent: 'center'
    },
    buttonParentStyle: {
        position: 'absolute',
        alignItems: 'center',
        justifyContent:'center'
    },
    mediaParentStyle: {
        borderRadius: 6,
    }, 
    closeMedia: {
        position: 'absolute',
        alignSelf: 'flex-end',
        height: 36,
        width: 36
    }
})

export default AddPost
