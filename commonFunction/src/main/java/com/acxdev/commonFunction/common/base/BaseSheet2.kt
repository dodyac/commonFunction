package com.acxdev.commonFunction.common.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.model.BlurBackground
import com.acxdev.commonFunction.utils.ext.setBackgroundBlurRadius
import com.acxdev.commonFunction.utils.ext.toClass
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseSheet2(@LayoutRes contentLayoutId: Int) : BottomSheetDialogFragment(contentLayoutId) {

    protected var sheetBehavior: BottomSheetBehavior<View>? = null

    val TAG = javaClass.simpleName

    override fun onStart() {
        super.onStart()
        dialog?.apply {
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                (it as BottomSheetDialog)
                    .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                    ?.apply {
                        background = ColorDrawable(Color.TRANSPARENT)

                        sheetBehavior = BottomSheetBehavior.from(this)

                        if (!isFullScreen) return@setOnShowListener

                        val params = layoutParams
                        params.height = WindowManager.LayoutParams.MATCH_PARENT

                        layoutParams = params

                        sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                    }
            }
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
    protected open val isFullScreen: Boolean = false

    protected open fun doFetch() {}
    protected open fun setViews() {}
    protected open fun doAction() {}

    fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        arguments?.getString(data ?: ConstantLib.DATA).toClass(cls)
}