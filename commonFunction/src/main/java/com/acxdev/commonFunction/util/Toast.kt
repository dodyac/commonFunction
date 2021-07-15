package com.acxdev.commonFunction.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.acxdev.commonFunction.common.Toast
import es.dmoral.toasty.Toasty

class Toast {
    companion object{

        fun Context.toasty(toast: Toast, string: String) {
            when (toast) {
                Toast.INFO -> Toasty.info(applicationContext, string, android.widget.Toast.LENGTH_LONG, true).show()
                Toast.SUCCESS -> Toasty.success(applicationContext, string, android.widget.Toast.LENGTH_LONG, true).show()
                Toast.WARNING -> Toasty.warning(applicationContext, string, android.widget.Toast.LENGTH_LONG, true).show()
                Toast.ERROR -> Toasty.error(applicationContext, string, android.widget.Toast.LENGTH_LONG, true).show()
            }
        }

        fun Context.toasty(toast: Toast, @StringRes string: Int) {
            when (toast) {
                Toast.INFO -> Toasty.info(applicationContext, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
                Toast.SUCCESS -> Toasty.success(applicationContext, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
                Toast.WARNING -> Toasty.warning(applicationContext, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
                Toast.ERROR -> Toasty.error(applicationContext, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
            }
        }

        fun Fragment.toasty(toast: Toast, string: String) {
            when (toast) {
                Toast.INFO -> Toasty.info(context!!.applicationContext, string, android.widget.Toast.LENGTH_LONG, true).show()
                Toast.SUCCESS -> Toasty.success(context!!.applicationContext, string, android.widget.Toast.LENGTH_LONG, true).show()
                Toast.WARNING -> Toasty.warning(context!!.applicationContext, string, android.widget.Toast.LENGTH_LONG, true).show()
                Toast.ERROR -> Toasty.error(context!!.applicationContext, string, android.widget.Toast.LENGTH_LONG, true).show()
            }
        }

        fun Fragment.toasty(toast: Toast, @StringRes string: Int) {
            when (toast) {
                Toast.INFO -> Toasty.info(context!!.applicationContext, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
                Toast.SUCCESS -> Toasty.success(context!!.applicationContext, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
                Toast.WARNING -> Toasty.warning(context!!.applicationContext, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
                Toast.ERROR -> Toasty.error(context!!.applicationContext, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
            }
        }

        fun singleToast(){ Toasty.Config.getInstance().allowQueue(false).apply() }

        fun Fragment.toast(@StringRes string: Int) {
            android.widget.Toast.makeText(context!!.applicationContext, getString(string), android.widget.Toast.LENGTH_LONG).show()
        }

        fun Context.toast(string: String) {
            android.widget.Toast.makeText(applicationContext, string, android.widget.Toast.LENGTH_LONG).show()
        }
    }
}