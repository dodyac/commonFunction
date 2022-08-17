package com.acxdev.commonFunction.util.ext

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*

fun AppCompatActivity.startCrop(uri: Uri?) {
    val destination = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()
    UCrop.of(uri!!, Uri.fromFile(File(cacheDir, destination))).withAspectRatio(1f, 1f)
        .withMaxResultSize(512, 512).start(this)
}

fun AppCompatActivity.lockSize(configuration: Configuration?, smallestWidth: Int) {
    if (configuration != null) {
        Log.d("SCALE SIZE", "adjustDisplayScale: " + configuration.densityDpi)
        configuration.densityDpi = smallestWidth
        val metrics = resources.displayMetrics
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)

        metrics.scaledDensity = configuration.densityDpi * metrics.density
        resources.updateConfiguration(configuration, metrics)
    }
}
fun AppCompatActivity.scaleScreen(configuration: Configuration, sizeHD: Int, sizeFHD: Int) {
    Log.d("SCALE SIZE", "adjustDisplayScaleBefore: ${configuration.densityDpi}")
    if (getScreenResolution() >= 1080) lockSize(configuration, sizeFHD)
    else lockSize(configuration, sizeHD)
}

fun AppCompatActivity.forceSetLocal(langCode: String) {
    val locale = Locale(langCode)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    createConfigurationContext(config)
}
