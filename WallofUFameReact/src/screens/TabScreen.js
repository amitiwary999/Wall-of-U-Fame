import * as React from 'react';
import {View, StyleSheet, Dimensions, AppState} from 'react-native';
import {TabView, SceneMap, TabBar} from 'react-native-tab-view';
import EntypoIcon from 'react-native-vector-icons/Entypo';
import {Icon, Container, Header, Right} from 'native-base';
import ProfileScreen from './ProfileScreen'
import HomeScreen from './HomeScreen'
import AddPost from './AddPost'
import auth from '@react-native-firebase/auth';
import { setVideoPlayPause } from '../redux/actions';
import { useDispatch } from 'react-redux';

const TabScreen = ({navigation}) => {
  if(auth().currentUser == null){
    navigation.reset({ routes: [{ name: 'LoginScreen' }]})
  }
    const [index, setIndex] = React.useState(0);
    let active = 1;
    let dispatch = useDispatch()

    const [routes] = React.useState([
        {key: 'first', title: 'Home'},
        {key: 'second', title: 'Profile'},
        {key:'third', title: 'AddPost'}
    ]);
    
    const renderScene = SceneMap({
        first: HomeScreen,
        second: ProfileScreen,
        third: AddPost
    });

    const openAddPostScreen= () => {
      navigation.navigate('AddPost', { });
    }

  React.useEffect(() => {
    const unsubscribe = navigation.addListener('focus', () => {
      console.log("focus")
     active = 1
      if (index == 0) {
        dispatch(setVideoPlayPause(1))
      } else {
        dispatch(setVideoPlayPause(0))
      }
    });
    return unsubscribe;
  }, [navigation]);

    React.useEffect(() => {
      console.log("index selected "+index)
      if(index == 0 && active){
        dispatch(setVideoPlayPause(1))
      }else{
        dispatch(setVideoPlayPause(0))
      }
    },[index])

  const getTabBarIcon = ({ route, focused }) => {
    let iconName, iconType, iconColor="#ffffff";
console.log("route in home "+JSON.stringify(route)+" "+route.title+" "+(route.title === 'Home'))
    if (route.title === 'Home') {
      iconType = 'MaterialCommunityIcons'
      iconName = focused? 'home' : 'home-variant-outline';
    } else if (route.title === 'Profile') {
      iconType = 'MaterialCommunityIcons'
      iconName = focused ? 'account' : 'account-outline';
    } else if (route.title === 'AddPost') {
      iconName = focused ? 'add-circle' : 'add-circle-outline';
      iconType = 'MaterialIcons'
    }
    return <Icon name={iconName} size={24} type={iconType} style={{color: 'white'}} />;
  }

  const renderTabBar = (props) => {
    return (<TabBar
      renderIcon={getTabBarIcon}
      renderLabel={() => null}
      style={{ backgroundColor: 'black', elevation: 0, borderColor: '#000000', borderBottomWidth: 1, height: 50 }}
      labelStyle={{ color: 'white', fontSize: 18, fontWeight: 'bold' }}
      {...props}
      indicatorStyle={{ backgroundColor: 'white', height: 2.5 }}
    />
    );
  }

    return (
      auth().currentUser && (
        <Container style={{flex: 1}}>
          <TabView
            renderTabBar = {renderTabBar}
            navigationState={{ index, routes }}
            renderScene={renderScene}
            onIndexChange={setIndex}
            tabBarPosition='bottom'
            swipeEnabled={false}
          />
        </Container>
      )
    );
}

const styles = StyleSheet.create({
    header: {
        height: 54,
        color: 'black',
        backgroundColor:'black'
    },
    tabStyle: {
      backgroundColor: 'black',
      color: 'black'
    },
    plusIconView: {
        alignItems: 'flex-end',
        margin:8
    }
})

export default TabScreen
