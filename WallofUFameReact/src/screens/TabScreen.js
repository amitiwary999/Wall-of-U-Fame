import * as React from 'react';
import {View, StyleSheet, Dimensions} from 'react-native';
import {TabView, SceneMap, TabBar} from 'react-native-tab-view';
import EntypoIcon from 'react-native-vector-icons/Entypo';
import Icon from 'react-native-vector-icons/MaterialIcons';
import ProfileScreen from './ProfileScreen'
import HomeScreen from './HomeScreen'
import auth from '@react-native-firebase/auth';

const TabScreen = ({navigation}) => {
  if(auth().currentUser == null){
    navigation.reset({ routes: [{ name: 'LoginScreen' }]})
  }
    const [index, setIndex] = React.useState(0);

    const [routes] = React.useState([
        {key: 'first', title: 'Home'},
        {key: 'second', title: 'Profile'},
    ]);
    
    const renderScene = SceneMap({
        first: HomeScreen,
        second: ProfileScreen,
    });

    const openAddPostScreen= () => {
      navigation.navigate('AddPost', { });
    }

  const renderTabBar = (props) => {
    return (<TabBar
      style={{ backgroundColor: 'black', elevation: 0, borderColor: '#000000', borderBottomWidth: 1, height: 50 }}
      labelStyle={{ color: 'white', fontSize: 18, fontWeight: 'bold' }}
      {...props}
      indicatorStyle={{ backgroundColor: 'white', height: 2.5 }}
    />
    );
  }

    return (
      auth().currentUser && (
        <View style={{ flex: 1 }}>
          <View style={styles.header}>
            <View style={styles.plusIconView}>
              <Icon name={'add'} size={24} color={'white'} onPress={openAddPostScreen} />
            </View>
          </View>
          <TabView
            renderTabBar = {renderTabBar}
            navigationState={{ index, routes }}
            renderScene={renderScene}
            onIndexChange={setIndex}
          />
        </View>
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
