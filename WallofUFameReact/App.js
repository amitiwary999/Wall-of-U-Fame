/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {Component} from 'react';
import {createAppContainer} from 'react-navigation';
import {createStackNavigator} from 'react-navigation-stack';
import LoginScreen from './src/LoginScreen';
import HomeScreen from './src/HomeScreen';

const RootStack = createStackNavigator(
  {
    LoginScreen : LoginScreen,
    HomeScreen : HomeScreen
  })
const RootContainer = createAppContainer(RootStack);

export default class App extends React.Component {
  render(){
    return <RootContainer/>
 }
}
