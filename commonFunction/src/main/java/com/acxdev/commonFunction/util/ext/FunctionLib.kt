package com.acxdev.commonFunction.util.ext

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.StrictMode
import android.util.Base64
import android.util.Patterns
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Response
import com.acxdev.commonFunction.util.toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun getToday(pattern: String, locale: Locale? = null): String {
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

fun getDirSize(dir: File): Long {
    var size: Long = 0
    dir.listFiles()?.forEach {
        if (it.isDirectory) {
            size += getDirSize(it)
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
    return Bitmap.createScaledBitmap(this, bitmapWidth, bitmapHeight, false)
}

fun Bitmap.rotateImage(angle: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle.toFloat())
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Response.isSuccess(): Boolean = this == Response.SUCCESS

fun Context.getAccessPermission(permissions: String, permissionGranted: (() -> Unit)) {
    Dexter.withContext(this)
        .withPermission(permissions)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                permissionGranted.invoke()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                toast("Permission Denied")
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }

        }).check()
}

fun Context.getAccessPermissions(vararg permissions: String, permissionGranted: (() -> Unit)) {
    Dexter.withContext(this)
        .withPermissions(permissions.toMutableList())
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                permissionGranted.invoke()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }
        }).check()
}