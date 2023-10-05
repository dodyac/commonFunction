package com.acxdev.commonFunction.common.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.StyleRes
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Inflater.inflateBinding
import com.acxdev.commonFunction.util.ext.toClass
import com.acxdev.sqlitez.DatabaseNameHolder
import com.acxdev.sqlitez.SqliteX
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<VB : ViewBinding>(
    @StyleRes private val bottomSheetStyle: Int,
    private val canCancel: Boolean = true,
    private val isFullScreen: Boolean = false,
    private val databaseName: String = DatabaseNameHolder.dbName
) : BottomSheetDialogFragment() {

    private var _binding: ViewBinding? = null
    private var _sqliteX: SqliteX? = null

    @Suppress("UNCHECKED_CAST")
    private val binding: VB
        get() = _binding!! as VB

    protected val sqliteX: SqliteX
        get() = _sqliteX!!

    val TAG = javaClass.simpleName

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), bottomSheetStyle)
        if(isFullScreen) {
            dialog.setOnShowListener {
                val bottomSheetDialog = it as BottomSheetDialog
                val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                parentLayout?.let { c ->
                    val behaviour = BottomSheetBehavior.from(c)
                    setupFullHeight(c)
                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
        dialog.setCanceledOnTouchOutside(canCancel)

        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
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
        DatabaseNameHolder.setDatabaseName(databaseName)
        safeContext {
            _sqliteX = SqliteX(this)
        }
        doLoadData()
        binding.configureViews()
        binding.onClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun scopeLayout(viewBinding: (VB.() -> Unit)) {
        try {
            viewBinding.invoke(binding)
        } catch (e: Exception) {
            Log.e(TAG, "binding was destroyed")
            e.printStackTrace()
        }
    }

    protected fun safeContext(result: Context.() -> Unit) {
        context?.let {
            result.invoke(it)
        } ?: run {
            Log.e(TAG, "no attached Context")
        }
    }

    protected open fun doLoadData() {}
    protected open fun VB.configureViews() {}
    protected open fun VB.onClickListener() {}

    fun getStringExtra(path: String? = null): String? = arguments?.getString(path ?: ConstantLib.DATA)

    fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        arguments?.getString(data ?: ConstantLib.DATA).toClass(cls)
}