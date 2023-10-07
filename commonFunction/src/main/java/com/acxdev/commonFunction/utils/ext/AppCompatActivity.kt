package com.acxdev.commonFunction.utils.ext

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.*

fun AppCompatActivity.forceSetLocale(langCode: String) {
    val locale = Locale(langCode)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    createConfigurationContext(config)
}

fun AppCompatActivity.resultLauncher(result: ActivityResult.() -> Unit): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result.invoke(it)
    }
}
