package com.acxdev.commonFunction.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import com.acxdev.commonFunction.common.ConstantX
import org.joda.time.Days
import org.joda.time.LocalDate
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.String

@SuppressLint("SimpleDateFormat")
class DataTypeX {
    companion object{

        //Date

        fun getToday(pattern: String, locale: Locale? = null): String? {
            val date = System.currentTimeMillis()
            val sdf = if(locale!= null) SimpleDateFormat(pattern, locale) else SimpleDateFormat(pattern)
            return sdf.format(date)
        }

        fun daysBetween(from: String?, to: String?):Int{ return Days.daysBetween(LocalDate(from!!), LocalDate(to!!)).days }

        fun String.formatDate(before: String, after: String, locale: Locale? = null): String? {
            val dateFormat = if(locale!= null) SimpleDateFormat(before, locale) else SimpleDateFormat(before)
            val date = dateFormat.parse(this)
            val output = if(locale!= null) SimpleDateFormat(after, locale) else SimpleDateFormat(after)
            return output.format(date!!)
        }

        fun Long.toDate(format: String, locale: Locale? = null): String {
            val date = Date(this)
            val sdf = if(locale!= null) SimpleDateFormat(format, locale) else SimpleDateFormat(format)
            return sdf.format(date)
        }

        fun Long.toDateEpoch(format: String, locale: Locale? = null): String {
            val netDate = Date(this * 1000)
            val sdf = if(locale!= null) SimpleDateFormat(format, locale) else SimpleDateFormat(format)
            return sdf.format(netDate)
        }

        fun Double.toDateEpoch(format: String, locale: Locale? = null): String {
            val netDate = Date(this.toLong() * 1000)
            val sdf = if(locale!= null) SimpleDateFormat(format, locale) else SimpleDateFormat(format)
            return sdf.format(netDate)
        }

        //Adding Pattern

        fun Any.toIDR(): String{
            val format = DecimalFormat("###,###,###,###,##0.##", DecimalFormatSymbols.getInstance(Locale("id", "ID")))
            return "Rp${format.format(toString().toBigDecimal())}"
        }

        fun Any.toUSD(): String{
            val format = DecimalFormat("###,###,##0.##", DecimalFormatSymbols.getInstance(Locale.US))
            return "$${format.format(toString().toBigDecimal())}"
        }

        fun Any?.toPercent(): String {
            val format = DecimalFormat("0.##")
            return "${format.format(toString().removePrefix("-").toBigDecimal())}%"
        }
        fun String.withBearer(): String{ return "Bearer $this" }

        fun String.add62(): String{ return "+62${substring(1)}" }

        fun String.add0(): String{ return if(length < 2) "0$this" else this }

        //Boolean

        fun CharSequence.isEmailValid(): Boolean { return Patterns.EMAIL_ADDRESS.matcher(this).matches() }

        //Convert Data

        @RequiresApi(Build.VERSION_CODES.N)
        @WorkerThread
        fun String.urlFileLength(): Long {
            var conn: HttpURLConnection? = null
            return try {
                conn = URL(this).openConnection() as HttpURLConnection?
                conn!!.requestMethod = "HEAD"
                conn.contentLengthLong
            } catch (e: IOException) { return 0L } finally { conn?.disconnect() }
        }

        fun Long.formatSize(): String {
            if (this < 1024) return "$this B"
            val z = (63 - java.lang.Long.numberOfLeadingZeros(this)) / 10
            return String.format("%.1f %sB", toDouble() / (1L shl z * 10), " KMGTPE"[z])
        }

        fun Any?.isErrorToast(): Boolean{
            return when(toString().toInt()){
                404 -> true
                405 -> true
                406 -> true
                407 -> true
                408 -> true
                409 -> true
                410 -> true
                411 -> true
                412 -> true
                413 -> true
                414 -> true
                415 -> true
                416 -> true
                417 -> true
                418 -> true
                421 -> true
                422 -> true
                423 -> true
                424 -> true
                425 -> true
                426 -> true
                428 -> true
                429 -> true
                431 -> true
                451 -> true
                500 -> true
                501 -> true
                502 -> true
                503 -> true
                504 -> true
                505 -> true
                506 -> true
                507 -> true
                508 -> true
                510 -> true
                511 -> true
                else -> false
            }
        }

        fun Any?.getMessage(): String{
            return when(toString().toInt()){
                100 -> "Continue"
                101 -> "Switching Protocols"
                102 -> "Processing"
                200 -> "OK"
                201 -> "Created"
                202 -> "Accepted"
                203 -> "Non-Authoritative Information"
                204 -> "No Content"
                205 -> "Reset Content"
                206 -> "Partial Content"
                207 -> "Multi-Status"
                208 -> "Already Reported"
                226 -> "IM Used"
                300 -> "Multiple Choices"
                301 -> "Moved Permanently"
                302 -> "Found"
                303 -> "See Other"
                304 -> "Not Modified"
                305 -> "Use Proxy"
                307 -> "Temporary Redirect"
                308 -> "Permanent Redirect"
                400 -> "Bad Request"
                401 -> "Unauthorized"
                402 -> "Payment Required"
                403 -> "Forbidden"
                404 -> "Not Found"
                405 -> "Method Not Allowed"
                406 -> "Not Acceptable"
                407 -> "Proxy Authentication Required"
                408 -> "Request Timeout"
                409 -> "Conflict"
                410 -> "Gone"
                411 -> "Length Required"
                412 -> "Precondition Failed"
                413 -> "Payload Too Large"
                414 -> "URI Too Long"
                415 -> "Unsupported Media Type"
                416 -> "Range Not Satisfiable"
                417 -> "Expectation Failed"
                418 -> "I'm a teapot"
                421 -> "Misdirected Request"
                422 -> "Unprocessable Entity"
                423 -> "Locked"
                424 -> "Failed Dependency"
                425 -> "Reserved for WebDAV advanced collections expired proposal"
                426 -> "Upgrade Required"
                428 -> "Precondition Required"
                429 -> "Too Many Requests"
                431 -> "Request Header Fields Too Large"
                451 -> "Unavailable For Legal Reasons"
                500 -> "Internal Server Error"
                501 -> "Not Implemented"
                502 -> "Bad Gateway"
                503 -> "Service Unavailable"
                504 -> "Gateway Timeout"
                505 -> "HTTP Version Not Supported"
                506 -> "Variant Also Negotiates"
                507 -> "Insufficient Storage"
                508 -> "Loop Detected"
                510 -> "Not Extended"
                511 -> "Network Authentication Required"
                else -> ""
            }
        }

        fun Any?.toZero(): String{
            return if(toString().isEmpty() || toString() == ConstantX.NULL) ConstantX.ZERO else toString()
        }

        fun Bitmap.rotateImage(angle: Int): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle.toFloat())
            return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
        }
    }
}