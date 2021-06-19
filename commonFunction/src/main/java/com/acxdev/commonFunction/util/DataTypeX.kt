package com.acxdev.commonFunction.util

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import com.acxdev.commonFunction.common.Constantx
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

class DataTypeX {
    companion object{

        //Date

        fun getToday(pattern: String): String? {
            val date = System.currentTimeMillis()
            val sdf = SimpleDateFormat(pattern, Locale("id", "ID"))
            return sdf.format(date)
        }

        fun daysBetween(from: String?, to: String?):Int{
            return Days.daysBetween(LocalDate(from!!), LocalDate(to!!)).days
        }

        fun String.newFormatDate(pattern: String): String? {
            val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale("id", "ID"))
            val date = dateFormat.parse(this)!!
            val output = SimpleDateFormat(pattern, Locale("id", "ID"))
            return output.format(date)
        }

        fun String.formatDate(before: String, after: String): String? {
            val dateFormat = SimpleDateFormat(before, Locale("id", "ID"))
            val date = dateFormat.parse(this)
            val output = SimpleDateFormat(after, Locale("id", "ID"))
            return output.format(date!!)
        }

        fun Long.toDate(format: String): String {
            val date = Date(this)
            val sdf = SimpleDateFormat(format)
            return sdf.format(date)
        }

        fun Long.toDateEpoc(format: String): String {
            val netDate = Date(this * 1000)
            val sdf = SimpleDateFormat(format)
            return sdf.format(netDate)
        }

        fun Double.toDateEpoc(format: String): String {
            val netDate = Date(this.toLong() * 1000)
            val sdf = SimpleDateFormat(format)
            return sdf.format(netDate)
        }


        //Adding Pattern

        fun Any.toIDR(): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append(this)
            var three = 0
            for (i in toString().length downTo 1) {
                three++
                while (three > 3) {
                    stringBuilder.insert(i - 0, ".")
                    three = +1
                }
            }
            return Constantx.PATTERN_CURRENCY + stringBuilder.toString() + Constantx.PATTERN_CURRENCY_END
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

        fun String.removeIDR(): String {
            return replace(Constantx.PATTERN_CURRENCY, "").replace(Constantx.PATTERN_CURRENCY_END, "")
                .replace(".", "")
        }

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


        fun Int.getMessage(): String{
            return when(this){
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

        fun Bitmap.rotateImage(angle: Int): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle.toFloat())
            return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
        }
    }
}