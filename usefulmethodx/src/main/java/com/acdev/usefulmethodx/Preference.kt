package com.acdev.usefulmethodx

import android.content.Context

class Preference {
    companion object {

        fun Context.isLogged(defaultValue: String?): String? {
            val sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            return sharedPref.getString("PASS", defaultValue)
        }

        fun Context.logged(defaultValue: String?) {
            val sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("PASS", defaultValue)
            editor.apply()
        }

        fun Context.save(path: String?, data: String?) {
            val prefs = getSharedPreferences("prefs", 0)
            val editor = prefs.edit()
            editor.putString(path, data)
            editor.apply()
        }

        fun Context.get(path: String?): String? {
            return getSharedPreferences("prefs", 0).getString(path, "")
        }

        fun Context.delete() {
            val prefs = getSharedPreferences("prefs", 0)
            val editor = prefs.edit()
            editor.remove("PASS").apply()
            editor.remove("token").commit()
        }
    }
}