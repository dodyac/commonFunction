package com.acdev.commonFunction.util

import android.content.Context
import com.acdev.commonFunction.common.Constantx.Companion.LOGGED
import com.acdev.commonFunction.common.Constantx.Companion.PREFERENCE
import com.acdev.commonFunction.common.Constantx.Companion.TOKEN

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

        fun Context.readPrefs(path: String): String? {
            return getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getString(path, "")
        }

        fun Context.readPrefsBoolean(path: String): Boolean {
            return getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getBoolean(path, false)
        }

        fun Context.readPrefsCustom(name: String, path: String): String? {
            return getSharedPreferences(name, 0).getString(path, "")
        }

        fun Context.readPrefsCustomBoolean(name: String, path: String): Boolean {
            return getSharedPreferences(name, 0).getBoolean(path, false)
        }

        fun Context.deletePrefs() {
            val prefs = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.remove(LOGGED).apply()
            editor.remove(TOKEN).commit()
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

        fun Context.insertToken(data: String) {
            val prefs = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(TOKEN, data)
            editor.apply()
        }

        fun Context.readToken(): String { return "Bearer " + readPrefs(TOKEN) }
    }
}