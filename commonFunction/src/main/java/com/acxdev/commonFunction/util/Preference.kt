package com.acxdev.commonFunction.util

import android.content.Context
import android.content.SharedPreferences
import com.acxdev.commonFunction.common.ConstantX.LOGGED
import com.acxdev.commonFunction.common.ConstantX.PREFERENCE

class Preference {
    companion object {
        fun Context.insertPrefs(path: String, data: Any?) {
            val prefs = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            when(data){
                is Boolean -> editor.putBoolean(path, data)
                is String -> editor.putString(path, data)
                is Int -> editor.putInt(path, data)
                is Long -> editor.putLong(path, data)
                is Float -> editor.putFloat(path, data)
            }
            editor.apply()
        }

        fun Context.insertPrefsCustom(name: String, path: String, data: Any?) {
            val prefs = getSharedPreferences(name, 0)
            val editor = prefs.edit()
            when(data){
                is Boolean -> editor.putBoolean(path, data)
                is String -> editor.putString(path, data)
                is Int -> editor.putInt(path, data)
                is Long -> editor.putLong(path, data)
                is Float -> editor.putFloat(path, data)
            }
            editor.apply()
        }

        fun Context.readPrefs(): SharedPreferences { return getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE) }

        fun Context.readPrefsCustom(name: String): SharedPreferences { return getSharedPreferences(name,0) }

        fun Context.logout() {
            val prefs = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            prefs.edit().remove(LOGGED).apply()
        }

        fun Context.deletePrefs() {
            val prefs = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
        }

        fun Context.deletePrefsCustom(name: String) {
            val prefs = getSharedPreferences(name, 0)
            prefs.edit().clear().apply()
        }

        fun Context.logged() {
            val sharedPref = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(LOGGED, LOGGED)
            editor.apply()
        }

        fun Context.isLogged(): Boolean {
            return getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getString(LOGGED,"") == LOGGED
        }
    }
}