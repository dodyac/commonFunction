package com.acxdev.commonFunction.util.ext

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.Preference.Companion.getPrefs
import com.acxdev.commonFunction.util.toast
import com.acxdev.commonFunction.util.toasty
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.play.core.review.ReviewManagerFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.thefinestartist.finestwebview.FinestWebView
import com.yalantis.ucrop.UCrop
import kotlin.math.roundToInt

fun Context.isNetworkAvailable(): Boolean {
    var result = false
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return result
}

fun Context.webView(url: String, @ColorRes color: Int) {
    FinestWebView.Builder(this as Activity).toolbarColorRes(color)
        .swipeRefreshColorRes(color).show(url)
}

fun Context.webView(@StringRes url: Int, @ColorRes color: Int) {
    FinestWebView.Builder(this as Activity).toolbarColorRes(color)
        .swipeRefreshColorRes(color).show(getString(url))
}

fun Context.openPDFDocument(filename: String) {
    val pdfIntent = Intent(Intent.ACTION_VIEW)
    pdfIntent.setDataAndType(Uri.parse(filename), "application/pdf")
    pdfIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    startActivity(Intent.createChooser(pdfIntent, "Open PDF"))
}

fun Context.cropError(data: Intent?) {
    val cropError = data?.let { UCrop.getError(it) }
    cropError?.let {
        it.message?.let { it1 -> toasty(Toast.ERROR, it1) }
    }
}

@ColorInt
fun Context.getResourceColor(@AttrRes resource: Int, alphaFactor: Float = 1f): Int {
    val typedArray = obtainStyledAttributes(intArrayOf(resource))
    val color = typedArray.getColor(0, 0)
    typedArray.recycle()

    if (alphaFactor < 1f) {
        val alpha = (color.alpha * alphaFactor).roundToInt()
        return Color.argb(alpha, color.red, color.green, color.blue)
    }

    return color
}

fun Context.getCompatActivity(): AppCompatActivity {
    return when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.getCompatActivity()
        is Application -> baseContext.getCompatActivity()
        else -> (this as AppCompatActivity)
    }
}

fun Context.showPlayStoreRate() {
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

fun Context.showSheetWithExtra(
    bottomSheet: BottomSheetDialogFragment,
    data: String? = null,
    isFullScreen: Boolean = false
) {
    val args = Bundle()
    args.putString(ConstantLib.DATA, data)
    args.putString(ConstantLib.IS_SHEET_FULL_SCREEN, isFullScreen.toString())
    bottomSheet.arguments = args
    bottomSheet.show(getCompatActivity().supportFragmentManager, bottomSheet.tag)
}

fun Context.getVersionName(): String {
    return try {
        getCompatActivity().packageManager.getPackageInfo(
            getCompatActivity().packageName, 0
        ).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        emptyString()
    }
}

fun Context.openSettings(applicationID: String) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", applicationID, null)
    intent.data = uri
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

fun Context.useCurrentTheme() {
    if (getPrefs().getBoolean(ConstantLib.IS_DARK_MODE, false)) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
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

fun Context.getCacheSize(): Long {
    var size: Long = 0
    size += getDirSize(cacheDir)
    size += getDirSize(externalCacheDir!!)
    return size
}

fun Context.getView(): View {
    return getCompatActivity().window.decorView.rootView
}

fun Context.whenPermissionGranted(permissions: String, permissionGranted: (() -> Unit)) {
    Dexter.withContext(this)
        .withPermission(permissions)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                permissionGranted.invoke()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                toast("Permission Denied")
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }

        }).check()
}

fun Context.whenPermissionsGranted(vararg permissions: String, permissionGranted: (() -> Unit)) {
    Dexter.withContext(this)
        .withPermissions(permissions.toMutableList())
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                permissionGranted.invoke()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }
        }).check()
}