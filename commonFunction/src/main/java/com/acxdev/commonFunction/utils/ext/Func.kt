package com.acxdev.commonFunction.utils.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.StrictMode
import android.util.Base64
import android.util.Patterns
import com.acxdev.commonFunction.common.ConstantLib
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.graphics.scale

fun getToday(pattern: String, locale: Locale = Locale.getDefault()): String {
    val date = System.currentTimeMillis()
    val sdf = SimpleDateFormat(pattern, locale)
    return sdf.format(date)
}

fun emptyString() = ConstantLib.EMPTY

fun setThreadPolicy() {
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
}

fun Any?.toPercent(): String {
    val format = DecimalFormat("0.##")
    return "${format.format(toString().removePrefix("-").toBigDecimal())}%"
}

fun Any?.toZero(): String {
    return if (toString().isEmpty() || toString() == ConstantLib.NULL) ConstantLib.ZERO else toString()
}

fun CharSequence.isEmailValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun File?.getDirSize(): Long {
    var size: Long = 0
    this?.listFiles()?.forEach {
        if (it.isDirectory) {
            size += it.getDirSize()
        } else if (it.isFile) {
            size += it.length()
        }
    }
    return size
}

fun Uri?.toBase64(context: Context): String {
    val imageStream = this?.let { it1 -> context.contentResolver.openInputStream(it1) }
    val bitmap = BitmapFactory.decodeStream(imageStream)

    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

    val byteArray = byteArrayOutputStream.toByteArray()

    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

val Bitmap.base64: String?
    get() {
        val outputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

fun Bitmap.resizeBitmap(maxSize: Int): Bitmap {
    var bitmapWidth = width
    var bitmapHeight = height
    val bitmapRatio = bitmapWidth.toFloat() / bitmapHeight.toFloat()
    if (bitmapRatio > 1) {
        bitmapWidth = maxSize
        bitmapHeight = (bitmapWidth / bitmapRatio).toInt()
    } else {
        bitmapHeight = maxSize
        bitmapWidth = (bitmapHeight * bitmapRatio).toInt()
    }
    return this.scale(bitmapWidth, bitmapHeight, false)
}

fun Bitmap.rotateImage(angle: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle.toFloat())
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}