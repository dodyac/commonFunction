package com.acxdev.commonFunction.util.ext

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Toast
import com.acxdev.commonFunction.util.toast
import com.acxdev.commonFunction.util.toasty
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*

@ColorInt
fun Fragment.getResourceColor(@AttrRes resource: Int, alphaFactor: Float = 1f): Int {
    return requireContext().getResourceColor(resource, alphaFactor)
}

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

fun Fragment.toasty(toast: Toast, string: String) {
    requireContext().toasty(toast, string)
}

fun Fragment.toasty(toast: Toast, @StringRes string: Int) {
    requireContext().toasty(toast, string)
}

fun Fragment.toast(@StringRes string: Int) {
    requireContext().toast(string)
}

fun Fragment.toast(string: String) {
    requireContext().toast(string)
}

fun Fragment.getAccessPermission(permission: String, permissionGranted: (() -> Unit)) {
    requireContext().getAccessPermission(permission) {
        permissionGranted.invoke()
    }
}

fun Fragment.getAccessPermissions(vararg permissions: String, permissionGranted: (() -> Unit)) {
    requireContext().getAccessPermissions(*permissions) {
        permissionGranted.invoke()
    }
}