package com.acxdev.commonFunction.util

import android.content.Context
import android.content.SharedPreferences

class Preference {
    companion object {
        private const val PREFERENCE = "prefs"
        private const val LOGGED = "logged"

        fun Context.putPrefs(path: String, data: Any?) {
            val prefs = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            when (data) {
                is Boolean -> editor.putBoolean(path, data)
                is String -> editor.putString(path, data)
                is Int -> editor.putInt(path, data)
                is Long -> editor.putLong(path, data)
                is Float -> editor.putFloat(path, data)
            }
            editor.apply()
        }

        fun Context.putPrefsCustom(name: String, path: String, data: Any?) {
            val prefs = getSharedPreferences(name, 0)
            val editor = prefs.edit()
            when (data) {
                is Boolean -> editor.putBoolean(path, data)
                is String -> editor.putString(path, data)
                is Int -> editor.putInt(path, data)
                is Long -> editor.putLong(path, data)
                is Float -> editor.putFloat(path, data)
            }
            editor.apply()
        }

        fun Context.getPrefs(): SharedPreferences {
            return getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        }

        fun Context.getPrefsCustom(name: String): SharedPreferences {
            return getSharedPreferences(name, 0)
        }

        fun Context.clearPrefs() {
            val prefs = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
        }

        fun Context.clearPrefsCustom(name: String) {
            val prefs = getSharedPreferences(name, 0)
            prefs.edit().clear().apply()
        }

        fun Context.signInPrefs() {
            val sharedPref = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean(LOGGED, true)
            editor.apply()
        }

        fun Context.signOutPrefs() {
            val prefs = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            prefs.edit().remove(LOGGED).apply()
        }

        fun Context.isSignedPrefs(): Boolean {
            return getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getBoolean(LOGGED, false)
        }
    }
}