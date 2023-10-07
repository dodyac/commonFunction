package com.acxdev.commonFunction.utils

import android.content.Context
import android.content.SharedPreferences
import com.acxdev.commonFunction.common.ConstantLib

class Preference(private val context: Context) {

    fun put(path: String, data: Any?, prefsName: String = ConstantLib.PREFERENCE) {
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

    fun get(prefsName: String = ConstantLib.PREFERENCE): SharedPreferences {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    fun clear(prefsName: String = ConstantLib.PREFERENCE) {
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    fun signIn() {
        context.getSharedPreferences(ConstantLib.PREFERENCE, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(ConstantLib.LOGGED, true)
            .apply()
    }

    fun signOut() {
        context.getSharedPreferences(ConstantLib.PREFERENCE, Context.MODE_PRIVATE)
            .edit()
            .remove(ConstantLib.LOGGED)
            .apply()
    }

    fun isSigned(): Boolean {
        return context.getSharedPreferences(ConstantLib.PREFERENCE, Context.MODE_PRIVATE)
            .getBoolean(ConstantLib.LOGGED, false)
    }
}