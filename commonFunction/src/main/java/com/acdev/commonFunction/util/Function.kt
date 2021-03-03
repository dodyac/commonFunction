package com.acdev.commonFunction.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.text.Html
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.setMargins
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acdev.commonFunction.model.BankRegion
import com.acdev.commonFunction.common.Constant.Companion.PATTERN_CURRENCY
import com.acdev.commonFunction.common.Constant.Companion.PATTERN_CURRENCY_END
import com.acdev.commonFunction.common.LibQue.Companion.libque
import com.acdev.commonFunction.R
import com.acdev.commonFunction.common.*
import com.acdev.commonFunction.common.Toast
import com.acdev.commonFunction.widget.ShimmerImage
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.play.core.review.ReviewManagerFactory
import com.sanojpunchihewa.updatemanager.UpdateManager
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.smarteist.autoimageslider.SliderViewAdapter
import com.thefinestartist.finestwebview.FinestWebView
import com.yalantis.ucrop.UCrop
import es.dmoral.toasty.Toasty
import org.joda.time.DateMidnight
import org.joda.time.Days
import retrofit2.Call
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class Function {
    @Suppress("DEPRECATION")
    companion object {

        private const val gone: Int = View.GONE
        private const val visible: Int = View.VISIBLE
        private const val invisible: Int = View.INVISIBLE

        fun Context.setLayoutManager(adapter: RecyclerView.Adapter<*>?, recyclerView: RecyclerView?, spanCount: Int) {
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, spanCount)
            recyclerView?.layoutManager = layoutManager
            recyclerView?.adapter = adapter
            adapter?.notifyDataSetChanged()
        }

        fun daysBetween(from: String?, to: String?):Int{
            return Days.daysBetween(DateMidnight(from!!), DateMidnight(to!!)).days
        }

        fun Context.toast(toast: Toast, string: String) {
            when (toast) {
                Toast.INFO -> Toasty.info(this, string, android.widget.Toast.LENGTH_LONG, true).show()
                Toast.SUCCESS -> Toasty.success(this, string, android.widget.Toast.LENGTH_LONG, true).show()
                Toast.WARNING -> Toasty.warning(this, string, android.widget.Toast.LENGTH_LONG, true).show()
                Toast.ERROR -> Toasty.error(this, string, android.widget.Toast.LENGTH_LONG, true).show()
            }
        }

        fun Context.toast(toast: Toast, @StringRes string: Int) {
            when (toast) {
                Toast.INFO -> Toasty.info(this, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
                Toast.SUCCESS -> Toasty.success(this, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
                Toast.WARNING -> Toasty.warning(this, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
                Toast.ERROR -> Toasty.error(this, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
            }
        }

        fun singleToast(){ Toasty.Config.getInstance().allowQueue(false).apply() }

        fun CharSequence.isEmailValid(): Boolean { return Patterns.EMAIL_ADDRESS.matcher(this).matches() }

        @SuppressLint("ResourceType")
        fun Context.setImage64(imageView: ImageView, base64: String?) {
            Glide.with(this.applicationContext).load(Base64.decode(base64, Base64.DEFAULT))
                .transform(RoundedCorners(8)).into(imageView)
        }

        fun Context.setImageUrl(imageView: ImageView, url: String){ Glide.with(this.applicationContext).load(url).into(imageView) }

        fun Context.setImageUrl(imageView: ShimmerImage, url: String){ Glide.with(this.applicationContext).load(url).into(imageView) }

        @Suppress("UNCHECKED_CAST")
        fun Context.stringArrayToAutoComplete(stringArray: Array<String?>, autoComplete: MaterialAutoCompleteTextView?) {
            val lst: List<String> = listOf(*stringArray) as List<String>
            val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lst)
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            autoComplete?.setAdapter(dataAdapter)
        }

        fun Context.getWidth(percent: Int): Int {
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

        fun ImageView?.default(name: String?, @ColorRes color: Int) {
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
            call?.libque {
                response = {
                    if (!it.isSuccessful) toast(Toast.ERROR, getString(R.string.error, it.code(), it.message()))
                    else {
                        if (it.body()!!.success) {
                            val modelDataArrayList = it.body()!!.data
                            val array = arrayOfNulls<String>(modelDataArrayList.size)
                            for ((index, value) in modelDataArrayList.withIndex()) array[index] = value.nama
                            stringArrayToAutoComplete(array, autoComplete)
                        }
                    }
                }
                failure = { toast(Toast.ERROR, R.string.cannotConnect) }
            }
        }

        fun Context.getRegion(call: Call<BankRegion?>?, autoComplete: MaterialAutoCompleteTextView?, region: Region) {
            call?.libque {
                response = {
                    if (!it.isSuccessful) toast(Toast.ERROR, getString(R.string.error, it.code(), it.message()))
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
                failure = { toast(Toast.ERROR, R.string.cannotConnect) }
            }
        }

        private fun String.add62(): String{ return "+62${this.substring(1)}" }
        
        fun Context.socialMediaOnclick(view: View, data: String, socialMedia: SocialMedia){
            view.setOnClickListener {
                when(socialMedia){
                    SocialMedia.FACEBOOK -> {
                        try {
                            this.packageManager.getPackageInfo("com.facebook.katana", 0)
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=https://$data")).setPackage("com.facebook.katana"))
                        } catch (e: java.lang.Exception) {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://$data")).setPackage("com.facebook.katana"))
                        }
                    }
                    SocialMedia.INSTAGRAM -> {
                        val uri = Uri.parse("http://instagram.com/_u/${data.replace("www.instagram.com/","")}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage("com.instagram.android")
                        try { startActivity(intent) } catch (e: ActivityNotFoundException) {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://$data")))
                        }
                    }
                    SocialMedia.WHATSAPP -> {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse("https://api.whatsapp.com/send?phone=${data.add62()}")
                        startActivity(i)
                    }
                    SocialMedia.GMAIL -> {
                        try { startActivity(Intent(Intent.ACTION_VIEW , Uri.parse("mailto:$data"))) }
                        catch(e: ActivityNotFoundException){}
                    }
                }
            }
        }

        fun Context.emptyAuth(mail: TextInputLayout, password: TextInputLayout): Boolean {
            when {
                mail.editText!!.text.isEmpty() -> {
                    mail.isErrorEnabled = true
                    mail.error = getString(R.string.emptyMail)
                    mail.requestFocus()
                    return false
                }
                !mail.editText!!.text.isEmailValid() -> {
                    mail.isErrorEnabled = true
                    mail.error = getString(R.string.notMail)
                    mail.clearFocus()
                    mail.requestFocus()
                    return false
                }
                password.editText!!.text.isEmpty() -> {
                    password.isErrorEnabled = true
                    password.error = getString(R.string.emptyPassword)
                    password.requestFocus()
                    return false
                }
                password.editText!!.text.length < 8 -> {
                    password.isErrorEnabled = true
                    password.error = getString(R.string.shortPassword)
                    password.clearFocus()
                    password.requestFocus()
                    return false
                }
                else -> {
                    mail.isErrorEnabled = false
                    password.isErrorEnabled = false
                    return true
                }
            }
        }

        fun Context.emptyMail(mail: TextInputLayout): Boolean {
            when {
                mail.editText!!.text.isEmpty() -> {
                    mail.isErrorEnabled = true
                    mail.error = getString(R.string.emptyMail)
                    mail.requestFocus()
                    return false
                }
                !mail.editText!!.text.isEmailValid() -> {
                    mail.isErrorEnabled = true
                    mail.error = getString(R.string.notMail)
                    mail.clearFocus()
                    mail.requestFocus()
                    return false
                }
                else -> {
                    mail.isErrorEnabled = false
                    return true
                }
            }
        }

        fun Context.emptyTil(textInputLayout: TextInputLayout, @StringRes alert: Int): Boolean {
            return if(textInputLayout.editText!!.text.isEmpty()) {
                textInputLayout.isErrorEnabled = true
                textInputLayout.error = getString(alert)
                textInputLayout.requestFocus()
                false
            } else {
                textInputLayout.isErrorEnabled = false
                textInputLayout.clearFocus()
                true
            }
        }

        private fun Context.numOfColumns(columnWidthDp: Float): Int {
            val displayMetrics = this.resources.displayMetrics
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
            return (screenWidthDp / columnWidthDp + 0.5).toInt()
        }

        fun Context.setLayoutManagerGrid(adapter: RecyclerView.Adapter<*>?, recyclerView: RecyclerView?, numOfColumns: Float) {
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, numOfColumns(numOfColumns))
            recyclerView?.layoutManager = layoutManager
            recyclerView?.adapter = adapter
            adapter?.notifyDataSetChanged()
        }

        fun Context.openPDFDocument(filename: String) {
            val pdfIntent = Intent(Intent.ACTION_VIEW)
            pdfIntent.setDataAndType(Uri.parse(filename), "application/pdf")
            pdfIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(Intent.createChooser(pdfIntent, "Open PDF"))
        }

        @RequiresApi(Build.VERSION_CODES.N)
        @WorkerThread
        fun String.urlFileLength(): Long {
            var conn: HttpURLConnection? = null
            return try {
                conn = URL(this).openConnection() as HttpURLConnection?
                conn!!.requestMethod = "HEAD"
                conn.contentLengthLong
            } catch (e: IOException) { return 0L } finally { conn?.disconnect() }
        }

        fun Long.formatSize(): String {
            if (this < 1024) return "$this B"
            val z = (63 - java.lang.Long.numberOfLeadingZeros(this)) / 10
            return String.format("%.1f %sB", this.toDouble() / (1L shl z * 10), " KMGTPE"[z])
        }

        fun String.dateFormat(pattern: String): String? {
            val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale("id", "ID"))
            val date = dateFormat.parse(this)
            val output = SimpleDateFormat(pattern, Locale("id", "ID"))
            return output.format(date)
        }

        fun ImageView.tint(@ColorRes colorRes: Int) {
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
        }

        fun View.layoutTint(@ColorRes colorRes: Int) {
            ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
        }

        fun View.setMargin(margin: Int) {
            if (this.layoutParams is ViewGroup.MarginLayoutParams) {
                val p = this.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(margin)
                this.requestLayout()
            }
        }

        fun SliderView.adapter(sliderViewAdapter: SliderViewAdapter<*>){
            this.setSliderAdapter(sliderViewAdapter)
            this.setIndicatorAnimation(IndicatorAnimationType.WORM)
            this.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            this.startAutoCycle()
        }

        fun Context.setPattern(imageView: ImageView, @DrawableRes drawableRes: Int){
            imageView.setImageDrawable(TileDrawable(
                ContextCompat.getDrawable(this, drawableRes)!!, Shader.TileMode.REPEAT))
        }

        fun getToday(pattern: String): String? {
            val date = System.currentTimeMillis()
            val sdf = SimpleDateFormat(pattern, Locale("id", "ID"))
            return sdf.format(date)
        }

        fun MaterialCardView.nestedClick(view: View) {
            this.setOnClickListener { if (view.visibility == gone) view.visibility = visible else view.visibility = gone }
        }

        fun Context.cropError(data: Intent?) {
            val cropError = data?.let { UCrop.getError(it) }
            if (cropError != null) toast(Toast.ERROR, cropError.message!!)
        }

        fun Activity.startCrop(uri: Uri?) {
            val destination = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()
            UCrop.of(uri!!, Uri.fromFile(File(cacheDir, destination))).withAspectRatio(1f, 1f)
                .withMaxResultSize(512, 512).start(this)
        }

        fun Fragment.startCrop(uri: Uri?) {
            val destination = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()
            UCrop.of(uri!!, Uri.fromFile(File(context?.cacheDir, destination))).withAspectRatio(1f, 1f)
                .withMaxResultSize(512, 512).start(context!!, this)
        }

        @SuppressLint("SimpleDateFormat")
        fun Context.datePicker(textInputLayout: TextInputEditText, pattern: String) {
            textInputLayout.setOnClickListener {
                val calendar = Calendar.getInstance()
                val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calendar[Calendar.YEAR] = year
                    calendar[Calendar.MONTH] = monthOfYear
                    calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val sdf = SimpleDateFormat(pattern)
                    textInputLayout.setText(sdf.format(calendar.time))
                }
                DatePickerDialog(this@datePicker, date, calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                    calendar[Calendar.DAY_OF_MONTH]).show()
            }
        }

        fun TextView.html(foo: String){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) this.text = Html.fromHtml(foo, Html.FROM_HTML_MODE_COMPACT)
            else this.text = Html.fromHtml(foo)
        }

        fun Fragment.instance(bundle: String): Fragment {
            val wd = this
            val args = Bundle()
            args.putString("data", bundle)
            wd.arguments = args
            return this
        }

        fun Context.instanceSheet(bottomSheet: BottomSheetDialogFragment, bundle: String?) {
            val args = Bundle()
            args.putString("data", bundle)
            bottomSheet.arguments = args
            bottomSheet.show((this as FragmentActivity).supportFragmentManager, bottomSheet.tag)
        }

        fun setThreadPolicy(){
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        fun AppCompatActivity.checkUpdateImmediately(){
            val updateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.IMMEDIATE)
            updateManager.start()
        }

        fun Context.showRate(){
            val manager = ReviewManagerFactory.create(this)
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener {
                if (it.isSuccessful) {
                    val reviewInfo = it.result
                    val flow = manager.launchReviewFlow(this as Activity, reviewInfo)
                    flow.addOnCompleteListener {
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                    }
                } else {
                    // There was some problem, continue regardless of the result.
                    // you can show your own rate dialog alert and redirect user to your app page
                    // on play store.
                }
            }
        }

        fun Activity.shareVia(view: View, appName: String, packageName: String){
            view.setOnClickListener { ShareCompat.IntentBuilder.from(this).setType("text/plain")
                .setChooserTitle("Bagikan Menggunakan").setText("Unduh aplikasi $appName secara gratis! Silahkan unduh di " +
                        "https://play.google.com/store/apps/details?id=$packageName").startChooser()
            }
        }
    }
}
