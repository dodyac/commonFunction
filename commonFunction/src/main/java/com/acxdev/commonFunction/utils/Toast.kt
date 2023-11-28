package com.acxdev.commonFunction.utils

import android.content.Context
import androidx.annotation.StringRes
import com.acxdev.commonFunction.model.Toast
import es.dmoral.toasty.Toasty

fun Context.toasty(toast: Toast, string: String, isLengthLong: Boolean = true) {
    val length = if(isLengthLong) {
        android.widget.Toast.LENGTH_LONG
    } else {
        android.widget.Toast.LENGTH_SHORT
    }
    when (toast) {
        Toast.Info -> Toasty.info(this, string, length, true).show()
        Toast.Success -> Toasty.success(this, string, length, true).show()
        Toast.Warning -> Toasty.warning(this, string, length, true).show()
        Toast.Error -> Toasty.error(this, string, length, true).show()
    }
}

fun Context.toasty(toast: Toast, @StringRes string: Int, isLengthLong: Boolean = true) {
    val length = if(isLengthLong) {
        android.widget.Toast.LENGTH_LONG
    } else {
        android.widget.Toast.LENGTH_SHORT
    }
    when (toast) {
        Toast.Info -> Toasty.info(this, getString(string), length, true).show()
        Toast.Success -> Toasty.success(this, getString(string), length, true).show()
        Toast.Warning -> Toasty.warning(this, getString(string), length, true).show()
        Toast.Error -> Toasty.error(this, getString(string), length, true).show()
    }
}

fun singleToast() {
    Toasty.Config.getInstance().allowQueue(false).apply()
}

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