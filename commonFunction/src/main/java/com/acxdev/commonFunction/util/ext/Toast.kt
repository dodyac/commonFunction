package com.acxdev.commonFunction.util.ext

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.acxdev.commonFunction.common.Toast
import es.dmoral.toasty.Toasty

fun Context.toasty(toast: Toast, string: String) {
    when (toast) {
        Toast.INFO -> Toasty.info(this, string, android.widget.Toast.LENGTH_LONG, true).show()
        Toast.SUCCESS -> Toasty.success(this, string, android.widget.Toast.LENGTH_LONG, true).show()
        Toast.WARNING -> Toasty.warning(this, string, android.widget.Toast.LENGTH_LONG, true).show()
        Toast.ERROR -> Toasty.error(this, string, android.widget.Toast.LENGTH_LONG, true).show()
    }
}

fun Context.toasty(toast: Toast, @StringRes string: Int) {
    when (toast) {
        Toast.INFO -> Toasty.info(this, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
        Toast.SUCCESS -> Toasty.success(this, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
        Toast.WARNING -> Toasty.warning(this, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
        Toast.ERROR -> Toasty.error(this, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
    }
}

fun Fragment.toasty(toast: Toast, string: String) {
    when (toast) {
        Toast.INFO -> Toasty.info(requireContext(), string, android.widget.Toast.LENGTH_LONG, true).show()
        Toast.SUCCESS -> Toasty.success(requireContext(), string, android.widget.Toast.LENGTH_LONG, true).show()
        Toast.WARNING -> Toasty.warning(requireContext(), string, android.widget.Toast.LENGTH_LONG, true).show()
        Toast.ERROR -> Toasty.error(context!!, string, android.widget.Toast.LENGTH_LONG, true).show()
    }
}

fun Fragment.toasty(toast: Toast, @StringRes string: Int) {
    when (toast) {
        Toast.INFO -> Toasty.info(requireContext(), getString(string), android.widget.Toast.LENGTH_LONG, true).show()
        Toast.SUCCESS -> Toasty.success(requireContext(), getString(string), android.widget.Toast.LENGTH_LONG, true).show()
        Toast.WARNING -> Toasty.warning(requireContext(), getString(string), android.widget.Toast.LENGTH_LONG, true).show()
        Toast.ERROR -> Toasty.error(requireContext(), getString(string), android.widget.Toast.LENGTH_LONG, true).show()
    }
}

fun singleToast() {
    Toasty.Config.getInstance().allowQueue(false).apply()
}

fun Fragment.toast(@StringRes string: Int) {
    android.widget.Toast.makeText(requireContext(), getString(string), android.widget.Toast.LENGTH_LONG).show()
}

fun Fragment.toast(string: String) {
    android.widget.Toast.makeText(requireContext(), string, android.widget.Toast.LENGTH_LONG).show()
}

fun Context.toast(string: String) {
    android.widget.Toast.makeText(this, string, android.widget.Toast.LENGTH_LONG).show()
}

fun Context.toast(@StringRes string: Int) {
    android.widget.Toast.makeText(this, getString(string), android.widget.Toast.LENGTH_LONG).show()
}