package com.acxdev.commonFunction.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Preference(private val context: Context) {

    companion object {
        private const val PREFERENCE = "prefs"
        private const val LOGGED = "logged"
        private const val IS_DARK_MODE = "is_dark_mode"
    }

    fun put(
        path: String,
        data: Any?,
        prefsName: String = PREFERENCE
    ) {
        val prefs = get(prefsName)
        prefs.edit {
            when (data) {
                is Boolean -> putBoolean(path, data)
                is String -> putString(path, data)
                is Int -> putInt(path, data)
                is Long -> putLong(path, data)
                is Float -> putFloat(path, data)
            }
        }
    }

    fun get(prefsName: String = PREFERENCE): SharedPreferences {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    fun clear(prefsName: String = PREFERENCE) {
        get(prefsName)
            .edit {
                clear()
            }
    }

    fun signIn() {
        get()
            .edit {
                putBoolean(LOGGED, true)
            }
    }

    fun signOut() {
        get()
            .edit {
                remove(LOGGED)
            }
    }

    fun isSigned(): Boolean {
        return get()
            .getBoolean(LOGGED, false)
    }

    fun isDarkMode(): Boolean {
        return get().getBoolean(IS_DARK_MODE, false)
    }

    fun setTheme(isDark: Boolean) {
        put(IS_DARK_MODE, isDark)
    }
}