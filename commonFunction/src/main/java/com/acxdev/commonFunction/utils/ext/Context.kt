package com.acxdev.commonFunction.utils.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.provider.Settings
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.model.Toast
import com.acxdev.commonFunction.utils.Preference
import com.acxdev.commonFunction.utils.toasty
import com.google.android.play.core.review.ReviewManagerFactory
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
        it.message?.let { it1 -> toasty(Toast.Error, it1) }
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

val Context.colorPrimary: Int
    @ColorInt
    get() = getResourceColor(android.R.attr.colorPrimary)

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

//fun Context.showSheetWithExtra(
//    bottomSheet: BottomSheetDialogFragment,
//    data: String? = null
//) {
//    val args = Bundle()
//    args.putString(ConstantLib.DATA, data)
//    bottomSheet.arguments = args
//    bottomSheet.show(getActivity().supportFragmentManager, bottomSheet.tag)
//}

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
    if (Preference(this).isDarkMode()) {
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

//fun Context.getView(): View {
//    return getActivity().window.decorView.rootView
//}

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