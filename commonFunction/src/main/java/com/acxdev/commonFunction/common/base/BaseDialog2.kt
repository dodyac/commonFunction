package com.acxdev.commonFunction.common.base

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.GravityInt
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.model.BlurBackground
import com.acxdev.commonFunction.utils.ext.setBackgroundBlurRadius
import com.acxdev.commonFunction.utils.ext.toClass

abstract class BaseDialog2(@LayoutRes contentLayoutId: Int) : AppCompatDialogFragment(contentLayoutId) {

    val TAG = javaClass.simpleName

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            window?.apply {
                if (animationStyle != 0) {
                    attributes?.windowAnimations = animationStyle
                }
                setBackgroundDrawable(backgroundDrawable)
                setLayout(layout.width, layout.height)

                val windowAttributes = attributes
                windowAttributes?.gravity = layout.gravity

                attributes = windowAttributes
            }

            setCancelable(canDismiss)
            setCanceledOnTouchOutside(canDismiss)
            if (!canDismiss) {
                setOnKeyListener { _, keyCode, event ->
                    keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP
                }
            }

            setBackgroundBlurRadius(blurBackground)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doFetch()
        setViews()
        doAction()
    }

    override fun onDismiss(dialog: DialogInterface) {
        setBackgroundBlurRadius(blurBackground,true)
        super.onDismiss(dialog)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    protected fun safeContext(result: Context.() -> Unit) {
        context?.let {
            result.invoke(it)
        } ?: run {
            Log.e(TAG, "no attached Context")
        }
    }

    protected open val canDismiss: Boolean = true
    protected open val blurBackground: BlurBackground = BlurBackground.None
    protected open val backgroundDrawable = ColorDrawable(Color.TRANSPARENT)
    protected open val layout: Layout = Layout(
        width = ViewGroup.LayoutParams.WRAP_CONTENT,
        height = ViewGroup.LayoutParams.WRAP_CONTENT,
        gravity = Gravity.CENTER
    )
    @StyleRes
    protected open val animationStyle: Int = 0

    protected open fun doFetch() {}
    protected open fun setViews() {}
    protected open fun doAction() {}

    fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        arguments?.getString(data ?: ConstantLib.DATA).toClass(cls)

    data class Layout(
        val width: Int,
        val height: Int,
        @GravityInt
        val gravity: Int
    )
}