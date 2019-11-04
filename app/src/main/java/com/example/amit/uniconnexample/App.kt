package com.example.amit.uniconnexample

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.example.amit.uniconnexample.Others.Foreground
import com.google.firebase.database.FirebaseDatabase

import timber.log.Timber

/**
 * Created by amit on 30/10/16.
 */

class App : Application() {
    internal var flag: Boolean? = null
    internal var vib: Boolean? = null
    internal var logincheck: Boolean = false
    internal var myPrefs: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        myPrefs = getSharedPreferences("com.example.amit.uniconnexample", Context.MODE_PRIVATE)
        flag = myPrefs.getBoolean("isChecked1", true)
        vib = myPrefs.getBoolean("isChecked2", true)
        logincheck = myPrefs.getBoolean("isLoggedin", false)
        Foreground.init(this)
        Timber.plant(Timber.DebugTree())
    }

    fun setFlag(flag: Boolean?) {
        this.flag = flag
    }

    fun setVib(vib: Boolean?) {
        this.vib = vib
    }

    fun getFlag(): Boolean? {
        //flag=settings.isEnabledswitch();
        return myPrefs.getBoolean("isChecked1", true)
    }

    fun getVib(): Boolean? {
        return myPrefs.getBoolean("isChecked2", true)
    }

    fun getLogincheck(): Boolean {
        return myPrefs.getBoolean("isLoggedin", false)
    }

    companion object {

        fun putPref(key: String, value: String, context: Context) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            editor.putString(key, value)
            editor.commit()
        }

        fun getPref(key: String, context: Context): String? {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(key, null)
        }
    }
}
