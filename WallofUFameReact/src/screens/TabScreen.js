import * as React from 'react';
import {View, StyleSheet, Dimensions} from 'react-native';
import {TabView, SceneMap} from 'react-native-tab-view';
import EntypoIcon from 'react-native-vector-icons/Entypo';
import Icon from 'react-native-vector-icons/MaterialIcons';
import ProfileScreen from './ProfileScreen'
import HomeScreen from './HomeScreen'

const TabScreen = (navigation) => {
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

    return (
      <View style={{flex: 1}}>
        <View style={styles.header}>
          <View style={styles.plusIconView}>
            <Icon name={'add'} size={24} color={'white'} />
          </View>
        </View>
        <TabView
          navigationState={{index, routes}}
          renderScene={renderScene}
          onIndexChange={setIndex}
        />
      </View>
    );
}

const styles = StyleSheet.create({
    header: {
        height: 54,
        color: 'black',
        backgroundColor:'black'
    },
    plusIconView: {
        alignItems: 'flex-end',
        margin:8
    }
})

export default TabScreen
