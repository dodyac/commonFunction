package com.acxdev.commonFunction.common.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Inflater.inflateBinding
import com.acxdev.commonFunction.model.BlurBackground
import com.acxdev.commonFunction.utils.ext.setBackgroundBlurRadius
import com.acxdev.commonFunction.utils.ext.toClass
import com.acxdev.sqlitez.SqliteZ
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseSheet<VB : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val binding: VB by lazy {
        _binding!!
    }
    protected val sqliteZ by lazy {
        SqliteZ(context)
    }

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

                        if (!isFullScreen) return@setOnShowListener

                        val params = layoutParams
                        params.height = WindowManager.LayoutParams.MATCH_PARENT

                        layoutParams = params

                        val behaviour = BottomSheetBehavior.from(this)
                        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                    }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doFetch()
        binding.setViews()
        binding.doAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        setBackgroundBlurRadius(blurBackground,true)
        super.onDismiss(dialog)
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
    protected open fun VB.setViews() {}
    protected open fun VB.doAction() {}

    fun getStringExtra(path: String? = null): String? = arguments?.getString(path ?: ConstantLib.DATA)

    fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        arguments?.getString(data ?: ConstantLib.DATA).toClass(cls)
}