package com.acxdev.commonFunction.utils.ext

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.provider.Settings
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.acxdev.commonFunction.utils.Preference
import kotlin.math.roundToInt

fun Context.openPDFDocument(filename: String) {
    val pdfIntent = Intent(Intent.ACTION_VIEW)
    pdfIntent.setDataAndType(Uri.parse(filename), "application/pdf")
    pdfIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    startActivity(Intent.createChooser(pdfIntent, "Open PDF"))
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

fun Context.getVersionName(): String {
    return try {
        packageManager.getPackageInfo(packageName, 0).versionName.orEmpty()
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

fun Context.getBitmapFromUri(uri: Uri): Bitmap? {
    return try {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

//fun Context.getView(): View {
//    return getActivity().window.decorView.rootView
//}