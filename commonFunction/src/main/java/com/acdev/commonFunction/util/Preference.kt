package com.acdev.commonFunction.util

import android.content.Context

class Preference {
    companion object {

        fun Context.isLogged(): Boolean {
            return getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("PASS", "") == "hihi"
        }

        fun Context.logged() {
            val sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("PASS", "hihi")
            editor.apply()
        }

        fun Context.insertPrefs(path: String?, data: String?) {
            val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(path, data)
            editor.apply()
        }

        fun Context.readPrefs(path: String?): String? {
            return getSharedPreferences("prefs", Context.MODE_PRIVATE).getString(path, "")
        }

        fun Context.deletePrefs() {
            val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.remove("PASS").apply()
            editor.remove("token").commit()
        }
    }
}