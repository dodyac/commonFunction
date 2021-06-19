package com.acxdev.commonFunction.util

import android.content.Context
import com.acxdev.commonFunction.common.Constantx.Companion.LOGGED
import com.acxdev.commonFunction.common.Constantx.Companion.PREFERENCE
class Preference {
    companion object {

        fun Context.insertPrefs(path: String, data: String) {
            val prefs = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(path, data)
            editor.apply()
        }

        fun Context.insertPrefs(path: String, data: Boolean) {
            val prefs = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putBoolean(path, data)
            editor.apply()
        }

        fun Context.insertPrefsCustom(name: String, path: String, data: String) {
            val prefs = getSharedPreferences(name, 0)
            val editor = prefs.edit()
            editor.putString(path, data)
            editor.apply()
        }

        fun Context.insertPrefsCustom(name: String, path: String, data: Boolean) {
            val prefs = getSharedPreferences(name, 0)
            val editor = prefs.edit()
            editor.putBoolean(path, data)
            editor.apply()
        }

        fun Context.readPrefs(path: String, defValue: String? = null): String? {
            return getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getString(path, defValue ?: "")
        }

        fun Context.readPrefsBoolean(path: String, defValue: Boolean? = null): Boolean {
            return getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getBoolean(path, defValue ?: false)
        }

        fun Context.readPrefsCustom(name: String, path: String, defValue: String? = null): String? {
            return getSharedPreferences(name, 0).getString(path, defValue ?: "")
        }

        fun Context.readPrefsCustomBoolean(name: String, path: String, defValue: Boolean? = null): Boolean {
            return getSharedPreferences(name, 0).getBoolean(path, defValue ?: false)
        }

        fun Context.logout() {
            val prefs = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.remove(LOGGED).apply()
        }

        fun Context.deletePrefs() {
            val prefs = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
        }

        fun Context.deletePrefsCustom(name: String) {
            val prefs = getSharedPreferences(name, 0)
            val editor = prefs.edit()
            editor.clear().apply()
        }

        fun Context.logged() {
            val sharedPref = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(LOGGED, LOGGED)
            editor.apply()
        }

        fun Context.isLogged(): Boolean {
            return getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getString(LOGGED, "") == LOGGED
        }
    }
}