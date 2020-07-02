import * as React from 'react';
import { View, StyleSheet, Dimensions, AppState } from 'react-native';
import { TabView, SceneMap, TabBar } from 'react-native-tab-view';
import { Icon, Container, Header, Right } from 'native-base';
import ProfileScreenLikedTab from './ProfileScreenLikedTab'
import ProfileScreenBookmarkedTab from './ProfileScreenBookmarkedTab'
import AddPost from './AddPost'
import auth from '@react-native-firebase/auth';
import { setVideoPlayPause } from '../redux/actions';
import { useDispatch } from 'react-redux';

const ProfileScreenTab = ({ navigation }) => {
    const [index, setIndex] = React.useState(0);

    const [routes] = React.useState([
        { key: 'first', title: 'Like' },
        { key: 'second', title: 'Bookmark' }
    ]);

    const renderScene = SceneMap({
        first: ProfileScreenLikedTab,
        second: ProfileScreenBookmarkedTab
    });

    const getTabBarIcon = ({ route, focused }) => {
        let iconName, iconType, iconColor = "#ffffff";
        console.log("route in home " + JSON.stringify(route) + " " + route.title + " " + (route.title === 'Home'))
        if (route.title === 'Like') {
            iconType = 'MaterialCommunityIcons'
            iconName = focused ? 'home' : 'home-variant-outline';
        } else if (route.title === 'Bookmark') {
            iconType = 'MaterialCommunityIcons'
            iconName = focused ? 'account' : 'account-outline';
        }
        return <Icon name={iconName} size={24} type={iconType} style={{ color: 'white' }} />;
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
            <Container style={{ flex: 1 }}>
                <TabView
                    renderTabBar={renderTabBar}
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
        backgroundColor: 'black'
    },
    tabStyle: {
        backgroundColor: 'black',
        color: 'black'
    },
    plusIconView: {
        alignItems: 'flex-end',
        margin: 8
    }
})

export default ProfileScreenTab
