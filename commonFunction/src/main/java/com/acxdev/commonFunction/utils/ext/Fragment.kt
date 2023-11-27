package com.acxdev.commonFunction.utils.ext

import android.content.Intent
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.model.BlurBackground
import com.acxdev.commonFunction.model.Toast
import com.acxdev.commonFunction.utils.toast
import com.acxdev.commonFunction.utils.toasty

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

fun Fragment.getColor(@ColorRes colorRes: Int): Int {
    return context?.let { ContextCompat.getColor(it, colorRes) } ?: Color.TRANSPARENT
}

fun Fragment.resultLauncher(result: ActivityResult.() -> Unit): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result.invoke(it)
    }
}

fun Fragment.setBackgroundBlurRadius(
    blur: BlurBackground,
    isRemove: Boolean = false
) {
    if (blur !is BlurBackground.Blur) return
    val radius = if (isRemove) {
        0
    } else {
        blur.radius
    }
    if (Build.VERSION.SDK_INT >= 31) {
        activity?.window?.let {
            it.apply {
                setFlags(
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND
                )
                attributes?.blurBehindRadius = radius
                if (isRemove) {
                    decorView.setRenderEffect(null)
                } else {
                    decorView.setRenderEffect(RenderEffect.createBlurEffect(radius.toFloat(), radius.toFloat(), Shader.TileMode.MIRROR))
                }
                setBackgroundBlurRadius(radius)
            }
        }
    }
}