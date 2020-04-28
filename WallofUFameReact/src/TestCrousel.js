import React from 'react';
import {
 StyleSheet,
 View,
 Text,
 ImageBackground,
 ActivityIndicator,
 Dimensions
} from 'react-native';
import Video from 'react-native-video';
 
 
import Carousel from 'react-native-snap-carousel';
 
class TestCrousel extends React.Component {
 
 
 
 constructor(props) {
   super(props)
   this.state = {
     paused: true,
     index: 0,
     loading: true,
     entries: [
       "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
       "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
       "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
       "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
       "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
       "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
       "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
       "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.jpg",
       "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
       "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
 
     ]
   }
 }
 
 _renderItem = ({ item, index }) => {
 
   let url = item
   return (
     <View style={{
       height: "100%",
       width: "100%",
       backgroundColor: 'black'
     }}>
 
       {this.state.index == index ?
         <Video
           source={{ uri: url }}
           ref={(ref) => {
             this.player = ref
           }}
           repeat={true}
           paused={this.state.index == index ? false : true}
           resizeMode="cover"
           //style={StyleSheet.absoluteFill}
           onReadyForDisplay={() => {
             console.log('onReadyForDisplay')
             this.setState({
               loading: false
             })
           }}
           onBuffer={() => {
             console.log('onBuffer')
             this.setState({
               loading: true
             })
           }}
           onLoad={() => {
             this.setState({
               paused: false
             })
             //console.log('onLoad')
 
           }}
           onProgress={() => {
             console.log('onProgress ==' + index)
           }}
           onError={() => {
             alert('Error occured')
           }}
           style={[styles.backgroundVideo, StyleSheet.absoluteFill]} />
 
         :
         null
       }
 
       {this.state.loading &&
         <ImageBackground source={{ uri: 'https://embedwistia-a.akamaihd.net/deliveries/f7edc17cbbb7a3657caea4cf3b64cc6df818c1b3.webp?image_crop_resized=720x1280' }}
           resizeMode="cover"
           style={{
             marginTop: 0,
             width: "100%",
             height: "100%",
             alignSelf: 'center',
             position: 'absolute',
             justifyContent: 'center',
             alignItems: 'center',
           }}>
           <Text style={{
             color: 'white'
           }}>Index {index}</Text>
         </ImageBackground>}
 
     </View>
 
   );
 }
 
 
 
 render() {
 
   let sliderWidth = Dimensions.get('window').width
   let itemWidth = Dimensions.get('window').width
   let sliderHeight = Dimensions.get('window').height
 
 
   return (
 
     <View>
 
       <Carousel
         onSnapToItem={(index) => {
           this.setState({
             paused: false
           })
           console.log("onSnapToItem:: ==" + index)
           this.setState({
             loading: true,
             index: index
           })
         }}
         style={{
           backgroundColor: 'black'
         }}
         inactiveSlideOpacity={1}
         inactiveSlideScale={1}
         vertical={true}
         ref={(c) => { this._carousel = c; }}
         data={this.state.entries}
         renderItem={this._renderItem}
         sliderWidth={sliderWidth}
         sliderHeight={sliderHeight}
         itemHeight={sliderHeight}
         itemWidth={itemWidth}
         loopClonesPerSide={0}
         loop={false}
       />
 
       {this.state.loading &&
 
 
         <View
           style={{
             marginTop: 16,
             //width: "100%",
             height: "100%",
             alignSelf: 'center',
             position: 'absolute',
             justifyContent: 'center',
             alignItems: 'center',
           }}>
 
           <ActivityIndicator size="large" color="#00ff00" />
         </View>
 
       }
 
     </View>
 
   );
 }
};
var styles = StyleSheet.create({
 backgroundVideo: {
   backgroundColor: 'black',
   position: 'absolute',
   top: 8,
   left: 8,
   bottom: 8,
   right: 8,
 },
});
 
 
export default TestCrousel;