package com.example.amit.uniconnexample.utils

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.amit.uniconnexample.App

/**
 * Created by Meera on 10,November,2019
 */
class PrefManager {
    companion object {
        fun getSharedPreference(): SharedPreferences {
            //TODO : vg make it private
            return PreferenceManager.getDefaultSharedPreferences(App.instance)
        }

        fun clearPrefs() {
            getSharedPreference().edit().clear().apply()
        }

        fun getString(preferenceKey: String, defaultValue: String): String? {
            return getSharedPreference().getString(preferenceKey, defaultValue)
        }

        fun putString(preferenceKey: String, defaultValue: String) {
            PrefManager.getSharedPreference().edit().putString(preferenceKey, defaultValue).apply()
        }

        fun getInt(preferenceKey: String, defaultValue: Int): Int {
            return getSharedPreference().getInt(preferenceKey, defaultValue)
        }

        fun putInt(preferenceKey: String, defaultKey: Int) {
            PrefManager.getSharedPreference().edit().putInt(preferenceKey, defaultKey).apply()
        }

        fun getBoolean(preferenceKey: String, defaultValue: Boolean): Boolean {
            return getSharedPreference().getBoolean(preferenceKey, defaultValue)
        }

        fun putBoolean(preferenceKey: String, defaultValue: Boolean) {
            getSharedPreference().edit().putBoolean(preferenceKey, defaultValue).apply()
        }

        fun removePref(key: String) {
            getSharedPreference().edit().remove(key).apply()
        }
    }
}