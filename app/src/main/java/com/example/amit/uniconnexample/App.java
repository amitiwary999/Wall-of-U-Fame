package com.example.amit.uniconnexample;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.example.amit.uniconnexample.Others.Foreground;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

/**
 * Created by amit on 30/10/16.
 */

public class App extends MultiDexApplication {
    Boolean flag,vib,logincheck;
    SharedPreferences myPrefs;
    public static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        myPrefs=getSharedPreferences("com.example.amit.uniconnexample",MODE_PRIVATE);
        flag=myPrefs.getBoolean("isChecked1",true);
        vib=myPrefs.getBoolean("isChecked2",true);
        logincheck=myPrefs.getBoolean("isLoggedin",false);
        Foreground.init(this);
        Timber.plant(new Timber.DebugTree());
    }



    public static void putPref(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public void setVib(Boolean vib) {
        this.vib = vib;
    }

    public Boolean getFlag(){
        //flag=settings.isEnabledswitch();
        return (myPrefs.getBoolean("isChecked1",true));
    }

    public Boolean getVib() {
        return (myPrefs.getBoolean("isChecked2",true));
    }

    public Boolean getLogincheck() {
        return myPrefs.getBoolean("isLoggedin",false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
