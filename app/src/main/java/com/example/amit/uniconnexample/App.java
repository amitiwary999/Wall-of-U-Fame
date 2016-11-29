package com.example.amit.uniconnexample;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by amit on 30/10/16.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
