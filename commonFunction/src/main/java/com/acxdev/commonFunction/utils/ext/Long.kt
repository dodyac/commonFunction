package com.acxdev.commonFunction.utils.ext

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

fun Long.toDate(format: String, locale: Locale = Locale.getDefault()): String {
    val date = Date(this)
    val sdf = SimpleDateFormat(format, locale)
    return sdf.format(date)
}

fun Long.toDateEpoch(format: String, locale: Locale = Locale.getDefault()): String {
    val netDate = Date(this * 1000)
    val sdf = SimpleDateFormat(format, locale)
    return sdf.format(netDate)
}

fun Long.asReadableDataSize(): String {
    if (this < 1024) return "$this B"
    val z = (63 - java.lang.Long.numberOfLeadingZeros(this)) / 10
    return String.format("%.1f %sB", toDouble() / (1L shl z * 10), " KMGTPE"[z])
}

fun Long.divideToPercent(divideTo: Long): Int {
    return if (divideTo == 0L) 0
    else ((toFloat() / divideTo.toFloat()) * 100).toInt()
}

fun Double.toReadable(): String {
    if(this == 0.0)  {
        return "0"
    }

    val prefix = if(this < 0) {
        "-"
    } else {
        ""
    }

    val num = kotlin.math.abs(this)

    // figure out what group of suffixes we are in and scale the number
    val pow = kotlin.math.floor(kotlin.math.log10(num) / 3).roundToInt()
    val base = num / 10.0.pow(pow * 3)

    // Using consistent rounding behavior, always rounding down since you want
    // 999999999 to show as 999.99M and not 1B
    val roundedDown = kotlin.math.floor(base * 100) /100.0

    // Convert the number to a string with up to 2 decimal places
    var baseStr = BigDecimal(roundedDown)
        .setScale(2, RoundingMode.HALF_EVEN)
        .toString()

    // Drop trailing zeros, then drop any trailing '.' if present
    baseStr = baseStr.dropLastWhile {
        it == '0'
    }.dropLastWhile {
        it == '.'
    }

    val suffixes = listOf("","K","M","B","T")

    return when {
        pow < suffixes.size -> "$prefix$baseStr${suffixes[pow]}"
        else -> "${prefix}infty"
    }
}