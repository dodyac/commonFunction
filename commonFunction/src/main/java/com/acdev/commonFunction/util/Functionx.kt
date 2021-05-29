package com.acdev.commonFunction.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
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
import android.provider.Settings
import android.text.Html
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.setMargins
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.ViewPager
import com.acdev.commonFunction.model.BankRegion
import com.acdev.commonFunction.common.Constantx.Companion.PATTERN_CURRENCY
import com.acdev.commonFunction.common.Constantx.Companion.PATTERN_CURRENCY_END
import com.acdev.commonFunction.util.LibQue.Companion.libque
import com.acdev.commonFunction.R
import com.acdev.commonFunction.common.*
import com.acdev.commonFunction.common.Toast
import com.acdev.commonFunction.util.Preference.Companion.readPrefsBoolean
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.play.core.review.ReviewManagerFactory
import com.h6ah4i.android.tablayouthelper.TabLayoutHelper
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

class Functionx {
    @Suppress("DEPRECATION")
    companion object {

        private const val gone: Int = View.GONE
        private const val visible: Int = View.VISIBLE
        private const val invisible: Int = View.INVISIBLE

        fun RecyclerView.adapter(adapter: RecyclerView.Adapter<*>?, spanCount: Int) {
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this.context.getCompatActivity(), spanCount)
            this.layoutManager = layoutManager
            this.adapter = adapter
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

        fun Fragment.toast(toast: Toast, string: String) {
            when (toast) {
                Toast.INFO -> Toasty.info(this.context!!, string, android.widget.Toast.LENGTH_LONG, true).show()
                Toast.SUCCESS -> Toasty.success(this.context!!, string, android.widget.Toast.LENGTH_LONG, true).show()
                Toast.WARNING -> Toasty.warning(this.context!!, string, android.widget.Toast.LENGTH_LONG, true).show()
                Toast.ERROR -> Toasty.error(this.context!!, string, android.widget.Toast.LENGTH_LONG, true).show()
            }
        }

        fun Fragment.toast(toast: Toast, @StringRes string: Int) {
            when (toast) {
                Toast.INFO -> Toasty.info(this.context!!, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
                Toast.SUCCESS -> Toasty.success(this.context!!, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
                Toast.WARNING -> Toasty.warning(this.context!!, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
                Toast.ERROR -> Toasty.error(this.context!!, getString(string), android.widget.Toast.LENGTH_LONG, true).show()
            }
        }

        fun singleToast(){ Toasty.Config.getInstance().allowQueue(false).apply() }

        fun CharSequence.isEmailValid(): Boolean { return Patterns.EMAIL_ADDRESS.matcher(this).matches() }

        @SuppressLint("ResourceType")
        fun ImageView.setImage64(base64: String?) {
            Glide.with(this.context.getCompatActivity()!!).load(Base64.decode(base64, Base64.DEFAULT))
                .transform(RoundedCorners(8)).into(this)
        }

        fun ImageView.setImageUrl(url: String){
            val circularProgressDrawable = CircularProgressDrawable(this.context)
            circularProgressDrawable.strokeWidth = 2f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(this.context.getCompatActivity()!!).load(url).placeholder(circularProgressDrawable).into(this) }

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

        @Suppress("UNCHECKED_CAST")
        fun MaterialAutoCompleteTextView.setStringArray(stringArray: Array<String?>) {
            val lst: List<String> = listOf(*stringArray) as List<String>
            val dataAdapter = ArrayAdapter(this.context.getCompatActivity()!!, android.R.layout.simple_spinner_dropdown_item, lst)
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            this.setAdapter(dataAdapter)
        }

        fun MaterialAutoCompleteTextView.getBank(call: Call<BankRegion?>?) {
            call?.libque {
                response = {
                    if (!it.isSuccessful) this@getBank.context.getCompatActivity()!!.toast(Toast.ERROR, this@getBank.context.getString(R.string.error, it.code(), it.message()))
                    else {
                        if (it.body()!!.success) {
                            val modelDataArrayList = it.body()!!.data
                            val array = arrayOfNulls<String>(modelDataArrayList.size)
                            for ((index, value) in modelDataArrayList.withIndex()) array[index] = value.nama
                            this@getBank.setStringArray(array)
                        }
                    }
                }
                failure = { this@getBank.context.getCompatActivity()!!.toast(Toast.ERROR, R.string.cannotConnect) }
            }
        }

        fun MaterialAutoCompleteTextView.getRegion(call: Call<BankRegion?>?, region: Region) {
            call?.libque {
                response = {
                    if (!it.isSuccessful) this@getRegion.context.getCompatActivity()!!.toast(Toast.ERROR, this@getRegion.context.getString(R.string.error, it.code(), it.message()))
                    else {
                        if (it.body()!!.success) {
                            when (region) {
                                Region.PROVINCE -> Constantx.PROVINCE = it.body()!!.data
                                Region.CITY -> Constantx.CITY = it.body()!!.data
                                Region.DISTRICT -> Constantx.DISTRICT = it.body()!!.data
                                Region.VILLAGE -> Constantx.VILLAGE = it.body()!!.data
                            }
                            val array = arrayOfNulls<String>(it.body()!!.data.size)
                            for ((index, value) in it.body()!!.data.withIndex()) array[index] = value.nama
                            this@getRegion.setStringArray(array)
                        }
                    }
                }
                failure = { this@getRegion.context.getCompatActivity()!!.toast(Toast.ERROR, R.string.cannotConnect) }
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

        fun Context.alertAuth(mail: TextInputLayout, password: TextInputLayout): Boolean {
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

        fun TextInputLayout.alertMail(): Boolean {
            when {
                this.editText!!.text.isEmpty() -> {
                    this.isErrorEnabled = true
                    this.error = this.context.getCompatActivity()!!.getString(R.string.emptyMail)
                    this.requestFocus()
                    return false
                }
                !this.editText!!.text.isEmailValid() -> {
                    this.isErrorEnabled = true
                    this.error = this.context.getCompatActivity()!!.getString(R.string.notMail)
                    this.clearFocus()
                    this.requestFocus()
                    return false
                }
                else -> {
                    this.isErrorEnabled = false
                    return true
                }
            }
        }

        fun TextInputLayout.alertPassword(): Boolean {
            when {
                this.editText!!.text.isEmpty() -> {
                    this.isErrorEnabled = true
                    this.error = this.context.getCompatActivity()!!.getString(R.string.emptyPassword)
                    this.requestFocus()
                    return false
                }
                this.editText!!.text.length < 8 -> {
                    this.isErrorEnabled = true
                    this.error = this.context.getCompatActivity()!!.getString(R.string.shortPassword)
                    this.clearFocus()
                    this.requestFocus()
                    return false
                }
                else -> {
                    this.isErrorEnabled = false
                    return true
                }
            }
        }

        fun TextInputLayout.alertEmpty(@StringRes alert: Int): Boolean {
            return if(this.editText!!.text.isEmpty()) {
                this.isErrorEnabled = true
                this.error = this.context.getCompatActivity()!!.getString(alert)
                this.requestFocus()
                false
            } else {
                this.isErrorEnabled = false
                this.clearFocus()
                true
            }
        }

        private fun Context.numOfColumns(columnWidthDp: Float): Int {
            val displayMetrics = this.resources.displayMetrics
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
            return (screenWidthDp / columnWidthDp + 0.5).toInt()
        }

        fun RecyclerView.adapterGrid(adapter: RecyclerView.Adapter<*>?, numOfColumns: Float) {
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this.context.getCompatActivity()!!, this.context.getCompatActivity()!!.numOfColumns(numOfColumns))
            this.layoutManager = layoutManager
            this.adapter = adapter
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

        fun ImageView.setPattern(@DrawableRes drawableRes: Int){
            this.setImageDrawable(TileDrawable(ContextCompat.getDrawable(this.context.getCompatActivity()!!, drawableRes)!!, Shader.TileMode.REPEAT))
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun View.backgroundTint(@ColorRes colorRes: Int) {
            this.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
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

        @Deprecated("Use TexinputLayout.datePicker")
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

        fun TextInputLayout.datePicker(title: String, format: String){
            this.setOnClickListener {
                val builder = MaterialDatePicker.Builder.datePicker()
                builder.setTitleText(title)
                val datePicker = builder.build()
                datePicker.show((this.context.getCompatActivity())!!.supportFragmentManager, "DATE_PICKER")
                datePicker.addOnPositiveButtonClickListener { this.editText!!.setText(it.toDate(format)) }
            }
        }

        fun Context.getCompatActivity(): AppCompatActivity? {
            return when (this) {
                is AppCompatActivity -> this
                is ContextWrapper -> this.baseContext.getCompatActivity()
                else -> null
            }
        }

        fun Long.toDate(format: String): String {
            val date = Date(this)
            val sdf = SimpleDateFormat(format)
            return sdf.format(date)
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

        fun BottomSheetDialogFragment.instanceSheet(bundle: String?) {
            val args = Bundle()
            args.putString("data", bundle)
            this.arguments = args
            this.show((this.context!!.getCompatActivity()!! as FragmentActivity).supportFragmentManager, this.tag)
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

        fun AppCompatActivity.lockSize(configuration: Configuration?, smallestWidth: Int) {
            if (configuration != null) {
                Log.d("TAG", "adjustDisplayScale: " + configuration.densityDpi)
                configuration.densityDpi = smallestWidth
                val metrics = resources.displayMetrics
                val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
                wm.defaultDisplay.getMetrics(metrics)
                metrics.scaledDensity = configuration.densityDpi * metrics.density
                this.resources.updateConfiguration(configuration, metrics)
            }
        }

        fun TabLayout.setupWithViewPagerHelper(viewPager: ViewPager){
            this.setupWithViewPager(viewPager)
            TabLayoutHelper(this, viewPager).isAutoAdjustTabModeEnabled = true
        }

        fun Context.openSettings(applicationID: String) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", applicationID, null)
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        fun TextView.showVersion(){
            try { this.text = "Versi ${this.context.getCompatActivity()!!.packageManager.getPackageInfo(this.context.getCompatActivity()!!.packageName, 0).versionName}" }
            catch (e: PackageManager.NameNotFoundException) { this.context.getCompatActivity()!!.toast(Toast.WARNING, e.message.toString()) }
        }

        fun Context.useCurrentTheme(){
            AppCompatDelegate.setDefaultNightMode(if(readPrefsBoolean(Constantx.DARK_MODE))
                AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }

        fun Context.getScreenResolution(): Int {
            val wm = getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            val width = metrics.widthPixels
            val height = metrics.heightPixels
            println("Screen Resolution- width: $width, height: $height")
            return width
        }

        fun AppCompatActivity.scaleScreen(configuration: Configuration, sizeHD: Int, sizeFHD: Int){
            Log.d("TAG", "adjustDisplayScaleBefore: ${configuration.densityDpi}")
            if(getScreenResolution() >= 1080) lockSize(configuration, sizeFHD)
            else lockSize(configuration, sizeHD)
        }

        fun String.formatDate(before: String, after: String): String? {
            val dateFormat = SimpleDateFormat(before, Locale("id", "ID"))
            val date = dateFormat.parse(this)
            val output = SimpleDateFormat(after, Locale("id", "ID"))
            return output.format(date!!)
        }
    }
}
