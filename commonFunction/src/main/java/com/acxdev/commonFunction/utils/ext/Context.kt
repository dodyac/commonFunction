package com.acxdev.commonFunction.utils.ext

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.utils.Preference
import com.acxdev.commonFunction.utils.toast
import com.acxdev.commonFunction.utils.toasty
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

fun Context.webView(url: String, @ColorInt color: Int) {
    FinestWebView.Builder(this as Activity).toolbarColor(color)
        .swipeRefreshColor(color).show(url)
}

fun Context.webView(@StringRes url: Int, @ColorInt color: Int) {
    FinestWebView.Builder(this as Activity).toolbarColor(color)
        .swipeRefreshColor(color).show(getString(url))
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

@ColorInt
fun Context.getColorPrimary(): Int  {
  return getResourceColor(android.R.attr.colorPrimary)
}

fun Context.getActivity(): AppCompatActivity {
    return when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.getActivity()
        is Application -> baseContext.getActivity()
        is android.view.ContextThemeWrapper -> baseContext.getActivity()
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
    data: String? = null
) {
    val args = Bundle()
    args.putString(ConstantLib.DATA, data)
    bottomSheet.arguments = args
    bottomSheet.show(getActivity().supportFragmentManager, bottomSheet.tag)
}

fun Context.getVersionName(): String {
    return try {
        packageManager.getPackageInfo(packageName, 0).versionName
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
    if (Preference(this).get().getBoolean(ConstantLib.IS_DARK_MODE, false)) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

fun Context.getCacheSize(): Long {
    var size: Long = 0
    size += cacheDir.getDirSize()
    externalCacheDir?.getDirSize()?.let {
        size += it
    }
    return size
}

fun Context.getView(): View {
    return getActivity().window.decorView.rootView
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
                p0?.let {
                    if(it.areAllPermissionsGranted()) {
                        permissionGranted.invoke()
                    } else {
                        toast("Permissions Denied")
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }
        }).check()
}

fun <T>Context.startActivityExtra(cls: Class<T>, data: String = emptyString(), intent: (Intent.() -> Unit)? = null) {
    val intents = Intent(this, cls)
    intents.putExtra(ConstantLib.DATA, data)

    if(intent != null) {
        intent.invoke(intents)
    } else {
        intents.also {
            startActivity(it)
        }
    }
}