package com.acxdev.commonFunction.utils.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.annotation.WorkerThread
import com.google.gson.Gson
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

fun String.formatDate(
    before: String,
    after: String,
    locale: Locale = Locale.getDefault()
): String {
    val dateFormat =  SimpleDateFormat(before, locale)
    val date = dateFormat.parse(this)
    val output = SimpleDateFormat(after, locale)
    return date?.let { output.format(it) }.toString()
}

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

val String.isNumber: Boolean
    get() = try {
        Integer.parseInt(this)
        true
    } catch (e: Exception) {
        false
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

val String.base64ToBitmap: Bitmap?
    get() = try {
        val imageBytes = Base64.decode(this, 0)
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

fun <T> String?.toClass(cls: Class<T>): T {
    return Gson().fromJson(this, cls)
}

val String.capitalized: String
    get() = replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(Locale.getDefault())
        } else {
            it.toString()
        }
    }

fun String?.or(default: String): String {
    return this?.let {
        it.ifEmpty { default }
    } ?: default
}

val loremIpsum: String
    get() {
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    }