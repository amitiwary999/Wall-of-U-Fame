import * as React from 'react';
import {View, StyleSheet, Dimensions} from 'react-native';
import {TabView, SceneMap} from 'react-native-tab-view';
import ProfileScreen from './ProfileScreen'
import HomeScreen from './HomeScreen'

const TabScreen = () => {
    const [index, setIndex] = React.useState(0);

    const [routes] = React.useState([
        {key: 'first', title: 'Home'},
        {key: 'second', title: 'Profile'},
    ]);
    
    const renderScene = SceneMap({
        first: HomeScreen,
        second: ProfileScreen,
    });

    return (
        <TabView
          navigationState={{index, routes}}
          renderScene={renderScene}
          onIndexChange={setIndex}
        />
    );
}

export default TabScreen
