package com.acxdev.commonFunction.util.ext

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Long.toDate(format: String, locale: Locale? = null): String {
    val date = Date(this)
    val sdf = SimpleDateFormat(format, locale)
    return sdf.format(date)
}

@SuppressLint("SimpleDateFormat")
fun Long.toDateEpoch(format: String, locale: Locale? = null): String {
    val netDate = Date(this * 1000)
    val sdf = SimpleDateFormat(format, locale)
    return sdf.format(netDate)
}

fun Long.asReadableDataSize(): String {
    if (this < 1024) return "$this B"
    val z = (63 - java.lang.Long.numberOfLeadingZeros(this)) / 10
    return String.format("%.1f %sB", toDouble() / (1L shl z * 10), " KMGTPE"[z])
}