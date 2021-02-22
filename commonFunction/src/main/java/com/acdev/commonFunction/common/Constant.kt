package com.acdev.commonFunction.common

import android.graphics.Color
import com.acdev.commonFunction.model.Datum

class Constant {
    companion object{
        val COLOR_DEFAULT_GRADIENT = Color.rgb(245, 245, 245)
        const val MIN_WEIGHT = 0.0f
        const val MAX_WEIGHT = 1.0f
        const val CORNER_DEFAULT = 16
        const val USE_GRADIENT_DEFAULT = false
        const val ANGLE = 20
        const val CHILD_COUNT = 3
        const val PATTERN_CURRENCY = "Rp "
        const val PATTERN_CURRENCY_END = ",-"
        const val PREFERENCE = "prefs"
        const val TOKEN = "token"
        const val LOGGED = "logged"
        var PROVINCE: List<Datum>? = null
        var CITY: List<Datum>? = null
        var DISTRICT: List<Datum>? = null
        var VILLAGE: List<Datum>? = null
    }
}