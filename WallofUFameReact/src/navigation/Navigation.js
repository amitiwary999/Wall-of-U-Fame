import React, { Component } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import {
    createStackNavigator,
} from '@react-navigation/stack';
import LoginScreen from '../screens/LoginScreen';
import HomeScreen from '../screens/HomeScreen';
import TabScreen from '../screens/TabScreen';
import AddPost from '../screens/AddPost';

const Stack = createStackNavigator();

const Navigation = () => {
    const startUpStack = () => (
        <Stack.Navigator
            headerMode="none"
            // screenOptions={{
            //     cardStyleInterpolator: CardStyleInterpolators.forHorizontalIOS,
            // }}
            initialRouteName="AddPost">
            <Stack.Screen name="HomeScreen" component={HomeScreen} />
            <Stack.Screen name="LoginScreen" component = {LoginScreen}/>
            <Stack.Screen name="TabScreen" component = {TabScreen} />
            <Stack.Screen name="AddPost" component = {AddPost} />
        </Stack.Navigator>
    );

    return <NavigationContainer>{startUpStack()}</NavigationContainer>;
};

export default Navigation