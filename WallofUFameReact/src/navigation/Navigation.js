import React, { Component } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import {
    createStackNavigator,
} from '@react-navigation/stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import Icon from 'native-base'
import LoginScreen from '../screens/LoginScreen';
import HomeScreen from '../screens/HomeScreen';
import ProfileScreen from '../screens/ProfileScreen'
import TabScreen from '../screens/TabScreen';
import AddPost from '../screens/AddPost';

const Stack = createStackNavigator();
const Tab = createBottomTabNavigator();

const Navigation = () => {
    const startUpStack = () => (
        <Stack.Navigator
            headerMode="none"
            // screenOptions={{
            //     cardStyleInterpolator: CardStyleInterpolators.forHorizontalIOS,
            // }}
            initialRouteName="TabScreen">
            <Stack.Screen name="HomeScreen" component={HomeScreen} />
            <Stack.Screen name="LoginScreen" component = {LoginScreen}/>
            <Stack.Screen name="TabScreen" component = {TabScreen} />
            <Stack.Screen name="ProfileScreen" component = {ProfileScreen} />
            <Stack.Screen name="AddPost" component = {AddPost} />
        </Stack.Navigator>
    );

    const bottomTab = () => {
        <Tab.Navigator
            screenOptions={({ route }) => ({
                tabBarIcon: ({ focused, color, size }) => {
                    let iconName, iconType;

                    if (route.name === 'HomeScreen') {
                        iconType = 'MaterialCommunityIcons'
                        iconName = focused
                            ? 'home'
                            : 'home-variant-outline';
                    } else if (route.name === 'ProfileScreen') {
                        iconType = 'MaterialCommunityIcons'
                        iconName = focused ? 'account' : 'account-outline';
                    }else if(route.name === 'AddPostScreen') {
                        iconName = focused ? 'aadd-circle' : 'aadd-circle-outline';
                        iconType = 'MaterialIcons'
                    }

                    // You can return any component that you like here!
                    return <Icon name={iconName} size={size} type = {iconType} color={color} />;
                },
            })}
            tabBarOptions={{
                activeTintColor: 'black',
                inactiveTintColor: 'gray',
            }}>
            <Tab.Screen name="HomeScreen" component={HomeScreen} />
            <Tab.Screen name="ProfileScreen" component={ProfileScreen} />
            <Tab.Screen name=  "AddPostScreen" component={AddPost} />
        </Tab.Navigator>
    }

    function HomeStack() {
        return (
            <Stack.Navigator
                initialRouteName="HomeScreen"
                screenOptions={{
                    headerStyle: { backgroundColor: '#42f44b' },
                    headerTintColor: '#fff',
                    headerTitleStyle: { fontWeight: 'bold' }
                }}>
                <Stack.Screen name="HomeScreen" component={HomeScreen} options={{ title: 'Home' }} />
            </Stack.Navigator>
        );
    }

    function ProfileStack() {
        return (
            <Stack.Navigator
                initialRouteName="ProfileScreen"
                screenOptions={{
                    headerStyle: { backgroundColor: '#42f44b' },
                    headerTintColor: '#fff',
                    headerTitleStyle: { fontWeight: 'bold' }
                }}>
                <Stack.Screen name="ProfileScreen" component={ProfileScreen} options={{ title: 'Profile' }} />
            </Stack.Navigator>
        );
    }

    return <NavigationContainer>{startUpStack()}</NavigationContainer>;
};

export default Navigation