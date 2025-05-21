package com.acxdev.commonFunction.utils

import android.content.Context
import androidx.annotation.StringRes

fun Context.toast(string: String, isLengthLong: Boolean = true) {
    val length = if(isLengthLong) {
        android.widget.Toast.LENGTH_LONG
    } else {
        android.widget.Toast.LENGTH_SHORT
    }
    android.widget.Toast.makeText(this, string, length).show()
}

fun Context.toast(@StringRes string: Int, isLengthLong: Boolean = true) {
    val length = if(isLengthLong) {
        android.widget.Toast.LENGTH_LONG
    } else {
        android.widget.Toast.LENGTH_SHORT
    }
    android.widget.Toast.makeText(this, getString(string), length).show()
}