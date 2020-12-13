package com.acdev.commonFunction

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acdev.commonFunction.Preference.Companion.get
import com.amulyakhare.textdrawable.TextDrawable
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.thefinestartist.finestwebview.FinestWebView
import es.dmoral.toasty.Toasty
import org.joda.time.DateMidnight
import org.joda.time.Days

class Function {

    @Suppress("DEPRECATION")
    companion object {
        fun Context.setLayoutManager(adapter: RecyclerView.Adapter<*>?, recyclerView: RecyclerView?, spanCount: Int) {
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, spanCount)
            recyclerView?.layoutManager = layoutManager
            recyclerView?.adapter = adapter
            adapter?.notifyDataSetChanged()
        }

        fun daysBetween(from: String?, to: String?):Int{
            return Days.daysBetween(DateMidnight(from!!), DateMidnight(to!!)).days
        }

        fun Context.toasty(isError: Boolean?, string: String) {
            when (isError) {
                null -> Toasty.info(this, string, Toast.LENGTH_LONG, true).show()
                true -> Toasty.error(this, string, Toast.LENGTH_LONG, true).show()
                else -> Toasty.success(this, string, Toast.LENGTH_LONG, true).show()
            }
        }

        fun Context.toasty(isError: Boolean?, string: Int) {
            when (isError) {
                null -> Toasty.info(this, getString(string), Toast.LENGTH_LONG, true).show()
                true -> Toasty.error(this, getString(string), Toast.LENGTH_LONG, true).show()
                else -> Toasty.success(this, getString(string), Toast.LENGTH_LONG, true).show()
            }
        }

        fun isEmailValid(email: CharSequence): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        @SuppressLint("ResourceType")
        fun Context.setImage64(imageView: ImageView, base64: String?) {
            GlideApp.with(this.applicationContext).load(Base64.decode(base64, Base64.DEFAULT))
                .transform(RoundedCorners(5)).into(imageView)
        }

        fun Context.token(): String {
            return "Bearer " + get("token")
        }

        fun isNullOrEmpty(str: String?): Boolean {
            return str == null || str.isEmpty()
        }

        @Suppress("UNCHECKED_CAST")
        fun Context.stringArrayToAutoComplete(stringArray: Array<String?>, autoComplete: MaterialAutoCompleteTextView?) {
            val lst: List<String> = listOf(*stringArray) as List<String>
            val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lst)
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            autoComplete?.setAdapter(dataAdapter)
        }

        fun Context.width(percent: Int): Int {
            val displayMetrics = DisplayMetrics()
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            return (displayMetrics.widthPixels / 100) * percent
        }

        fun Context.isNetworkAvailable(): Boolean {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
        }

        fun Activity.webView(url: String, color: Int) {
            FinestWebView.Builder(this).toolbarColorRes(color).swipeRefreshColorRes(color).show(url)
        }

        fun Activity.webView(url: Int, color: Int) {
            FinestWebView.Builder(this).toolbarColorRes(color).swipeRefreshColorRes(color).show(getString(url))
        }

        fun defaultPhoto(imageView: ImageView?, name: String?, color: Int) {
            val textDrawable = TextDrawable.builder()
                .beginConfig()
                .width(60)
                .height(60)
                .textColor(color)
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(name?.substring(0, 1), Color.WHITE)
            imageView?.setImageDrawable(textDrawable)
        }
    }
}
