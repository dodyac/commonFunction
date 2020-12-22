package com.acdev.commonFunction.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acdev.commonFunction.model.BankRegion
import com.acdev.commonFunction.common.Constant.Companion.PATTERN_CURRENCY
import com.acdev.commonFunction.common.Constant.Companion.PATTERN_CURRENCY_END
import com.acdev.commonFunction.common.Enqueue.Companion.queue
import com.acdev.commonFunction.common.GlideApp
import com.acdev.commonFunction.util.Preference.Companion.get
import com.acdev.commonFunction.R
import com.acdev.commonFunction.common.Region
import com.acdev.commonFunction.common.Constant
import com.acdev.commonFunction.common.Toastx
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.thefinestartist.finestwebview.FinestWebView
import es.dmoral.toasty.Toasty
import org.joda.time.DateMidnight
import org.joda.time.Days
import retrofit2.Call
import java.io.ByteArrayOutputStream

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

        fun Context.toastx(toastx: Toastx, string: String) {
            when (toastx) {
                Toastx.INFO -> Toasty.info(this, string, Toast.LENGTH_LONG, true).show()
                Toastx.SUCCESS -> Toasty.success(this, string, Toast.LENGTH_LONG, true).show()
                Toastx.WARNING -> Toasty.warning(this, string, Toast.LENGTH_LONG, true).show()
                Toastx.ERROR -> Toasty.error(this, string, Toast.LENGTH_LONG, true).show()
            }
        }

        fun Context.toastx(toastx: Toastx, @StringRes string: Int) {
            when (toastx) {
                Toastx.INFO -> Toasty.info(this, getString(string), Toast.LENGTH_LONG, true).show()
                Toastx.SUCCESS -> Toasty.success(this, getString(string), Toast.LENGTH_LONG, true).show()
                Toastx.WARNING -> Toasty.warning(this, getString(string), Toast.LENGTH_LONG, true).show()
                Toastx.ERROR -> Toasty.error(this, getString(string), Toast.LENGTH_LONG, true).show()
            }
        }

        fun CharSequence.isEmailValid(): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(this).matches()
        }

        @SuppressLint("ResourceType")
        fun Context.setImage64(imageView: ImageView, base64: String?) {
            GlideApp.with(this.applicationContext).load(Base64.decode(base64, Base64.DEFAULT))
                .transform(RoundedCorners(5)).into(imageView)
        }

        fun Context.token(): String {
            return "Bearer " + get("token")
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

        fun Activity.webView(url: String, @ColorRes color: Int) {
            FinestWebView.Builder(this).toolbarColorRes(color).swipeRefreshColorRes(color).show(url)
        }

        fun Activity.webView(@StringRes url: Int, @ColorRes color: Int) {
            FinestWebView.Builder(this).toolbarColorRes(color).swipeRefreshColorRes(color).show(getString(url))
        }

        fun ImageView?.default(name: String?, @ColorRes color: Int, ) {
            val textDrawable = TextDrawable.builder()
                .beginConfig()
                .width(120)
                .height(120)
                .bold()
                .toUpperCase()
                .textColor(color)
                .endConfig()
                .buildRound(name?.substring(0, 1), Color.WHITE)
            this?.setImageDrawable(textDrawable)
        }

        fun ImageView?.defaultMaterial(name: String?) {
            val textDrawable = TextDrawable.builder()
                .beginConfig()
                .width(120)
                .height(120)
                .bold()
                .toUpperCase()
                .textColor(Color.WHITE)
                .useFont(Typeface.SANS_SERIF)
                .endConfig()
                .buildRect(name?.substring(0, 1), ColorGenerator.MATERIAL.randomColor)
            this?.setImageDrawable(textDrawable)
        }

        fun String.toCurrency(): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append(this)
            var three = 0
            for (i in this.length downTo 1) {
                three++
                while (three > 3) {
                    stringBuilder.insert(i - 0, ".")
                    three = +1
                }
            }
            return PATTERN_CURRENCY + stringBuilder.toString() + PATTERN_CURRENCY_END
        }

        fun Long.toCurrency(): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append(this.toString())
            var three = 0
            for (i in this.toString().length downTo 1) {
                three++
                while (three > 3) {
                    stringBuilder.insert(i - 0, ".")
                    three = +1
                }
            }
            return PATTERN_CURRENCY + stringBuilder.toString() + PATTERN_CURRENCY_END
        }

        fun String.removeCurrency(): String {
            return this.replace(PATTERN_CURRENCY, "").replace(PATTERN_CURRENCY_END, "")
                .replace(".", "")
        }

        fun ImageView.toBase64(): String? {
            return try {
                val byteArrayOutputStream = ByteArrayOutputStream()
                (this.drawable as BitmapDrawable).bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                Base64.encodeToString(byteArray, Base64.DEFAULT)
            } catch (e: Exception) { "" }
        }

        fun Context.getBank(call: Call<BankRegion?>?, autoComplete: MaterialAutoCompleteTextView?) {
            call?.queue {
                response = {
                    if (!it.isSuccessful) toastx(Toastx.ERROR, getString(R.string.error, it.code(), it.message()))
                    else {
                        if (it.body()!!.success) {
                            val modelDataArrayList = it.body()!!.data
                            val array = arrayOfNulls<String>(modelDataArrayList.size)
                            for ((index, value) in modelDataArrayList.withIndex()) array[index] = value.nama
                            stringArrayToAutoComplete(array, autoComplete)
                        }
                    }
                }
                failure = { toastx(Toastx.ERROR, R.string.cannotConnect) }
            }
        }

        fun Context.getRegion(call: Call<BankRegion?>?, autoComplete: MaterialAutoCompleteTextView?, region: Region) {
            call?.queue {
                response = {
                    if (!it.isSuccessful) toastx(Toastx.ERROR, getString(R.string.error, it.code(), it.message()))
                    else {
                        if (it.body()!!.success) {
                            when (region) {
                                Region.PROVINCE -> Constant.PROVINCE = it.body()!!.data
                                Region.CITY -> Constant.CITY = it.body()!!.data
                                Region.DISTRICT -> Constant.DISTRICT = it.body()!!.data
                                Region.VILLAGE -> Constant.VILLAGE = it.body()!!.data
                            }
                            val array = arrayOfNulls<String>(it.body()!!.data.size)
                            for ((index, value) in it.body()!!.data.withIndex()) array[index] = value.nama
                            stringArrayToAutoComplete(array, autoComplete)
                        }
                    }
                }
                failure = { toastx(Toastx.ERROR, R.string.cannotConnect) }
            }
        }
    }
}
