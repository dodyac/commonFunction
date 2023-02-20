package com.acxdev.commonFunction.util.ext

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.toast
import com.acxdev.commonFunction.util.toasty
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*

fun Fragment.startCrop(uri: Uri?) {
    val destination = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()
    UCrop.of(uri!!, Uri.fromFile(File(context?.cacheDir, destination)))
        .withAspectRatio(1f, 1f)
        .withMaxResultSize(512, 512).start(context!!, this)
}

fun Fragment.putExtra(
    data: String,
    secondPath: String? = null,
    secondData: String? = null,
    thirdPath: String? = null,
    thirdData: String? = null
): Fragment {
    val args = Bundle()
    args.putString(ConstantLib.DATA, data)
    args.putString(secondPath, secondData)
    args.putString(thirdPath, thirdData)
    arguments = args
    return this
}

fun Fragment.toasty(toast: Toast, string: String, isLengthLong: Boolean = true) {
    context?.toasty(toast, string, isLengthLong)
}

fun Fragment.toasty(toast: Toast, @StringRes string: Int, isLengthLong: Boolean = true) {
    context?.toasty(toast, string, isLengthLong)
}

fun Fragment.toast(@StringRes string: Int, isLengthLong: Boolean = true) {
    context?.toast(string, isLengthLong)
}

fun Fragment.toast(string: String, isLengthLong: Boolean = true) {
    context?.toast(string, isLengthLong)
}

fun Fragment.whenPermissionGranted(permission: String, permissionGranted: (() -> Unit)) {
    context?.whenPermissionGranted(permission) {
        permissionGranted.invoke()
    }
}

fun Fragment.whenPermissionsGranted(vararg permissions: String, permissionGranted: (() -> Unit)) {
    context?.whenPermissionsGranted(*permissions) {
        permissionGranted.invoke()
    }
}

fun Fragment.getColor(@ColorRes colorRes: Int): Int {
    return context?.let { ContextCompat.getColor(it, colorRes) } ?: Color.TRANSPARENT
}