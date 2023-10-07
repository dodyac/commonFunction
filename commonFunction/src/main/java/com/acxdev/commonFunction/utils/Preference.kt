package com.acxdev.commonFunction.utils

import android.content.Context
import android.content.SharedPreferences

class Preference(private val context: Context) {

    companion object {
        private const val PREFERENCE = "prefs"
        private const val LOGGED = "logged"
        private const val IS_DARK_MODE = "is_dark_mode"
    }

    fun put(path: String, data: Any?, prefsName: String = PREFERENCE) {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
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

    fun get(prefsName: String = PREFERENCE): SharedPreferences {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    fun clear(prefsName: String = PREFERENCE) {
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    fun signIn() {
        context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(LOGGED, true)
            .apply()
    }

    fun signOut() {
        context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            .edit()
            .remove(LOGGED)
            .apply()
    }

    fun isSigned(): Boolean {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            .getBoolean(LOGGED, false)
    }

    fun isDarkMode(): Boolean {
        return get().getBoolean(IS_DARK_MODE, false)
    }

    fun setTheme(isDark: Boolean) {
        put(IS_DARK_MODE, isDark)
    }
}