package com.acxdev.commonFunction.util.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import com.google.gson.Gson
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

fun String.formatDate(before: String, after: String, locale: Locale = Locale.getDefault()): String {
    val dateFormat =  SimpleDateFormat(before, locale)
    val date = dateFormat.parse(this)
    val output = SimpleDateFormat(after, locale)
    return date?.let { output.format(it) }.toString()
}

@RequiresApi(Build.VERSION_CODES.N)
@WorkerThread
fun String.urlFileLength(): Long {
    var conn: HttpURLConnection? = null
    return try {
        conn = URL(this).openConnection() as HttpURLConnection?
        conn!!.requestMethod = "HEAD"
        conn.contentLengthLong
    } catch (e: IOException) {
        return 0L
    } finally {
        conn?.disconnect()
    }
}

fun String.isNumber(): Boolean {
    return try {
        Integer.parseInt(this)
        true
    } catch (e: Exception) {
        false
    }
}

fun String.withBearer(): String {
    return "Bearer $this"
}

fun String.add62(): String {
    return "+62${substring(1)}"
}

fun String.add0(): String {
    return if (length < 2) "0$this" else this
}

fun String.base64ToBitmap(): Bitmap? {
    return try {
        val imageBytes = Base64.decode(this, 0)
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun <T>String?.toClass(cls: Class<T>): T {
    return Gson().fromJson(this, cls)
}

fun String.capitalized(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}