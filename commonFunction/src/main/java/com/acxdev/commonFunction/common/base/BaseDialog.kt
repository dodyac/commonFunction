package com.acxdev.commonFunction.common.base

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.GravityInt
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Inflater.inflateBinding
import com.acxdev.commonFunction.model.BlurBackground
import com.acxdev.commonFunction.utils.ext.setBackgroundBlurRadius
import com.acxdev.commonFunction.utils.ext.toClass
import com.acxdev.commonFunction.utils.ext.view.removeViewIfNeeded
import com.acxdev.sqlitez.SqliteZ

abstract class BaseDialog<VB : ViewBinding> : AppCompatDialogFragment() {

    private var _binding: VB? = null
    protected val binding: VB by lazy {
        _binding ?: throw IllegalStateException("$TAG already destroyed")
    }

    protected val sqliteZ by lazy {
        SqliteZ(context)
    }

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root.removeViewIfNeeded
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
        val db = sqliteZ.readableDatabase
        if (db.isOpen) {
            db.close()
        }
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
    protected open fun VB.setViews() {}
    protected open fun VB.doAction() {}

    fun getStringExtra(path: String? = null): String? =
        arguments?.getString(path ?: ConstantLib.DATA)

    fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        arguments?.getString(data ?: ConstantLib.DATA).toClass(cls)

    data class Layout(
        val width: Int,
        val height: Int,
        @GravityInt
        val gravity: Int
    )
}