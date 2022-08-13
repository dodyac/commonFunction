package com.acxdev.commonFunction.common.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.StyleRes
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.InflateViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson

abstract class BaseBottomSheetLib<out VB : ViewBinding>(
    @StyleRes private val bottomSheetStyle: Int,
    private val inflateViewGroup: InflateViewGroup<VB>
    ) : BottomSheetDialogFragment() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), bottomSheetStyle)
        if(getStringExtra(ConstantLib.IS_SHEET_FULL_SCREEN).toBoolean()) {
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
        dialog.setCanceledOnTouchOutside(true)

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
        _binding = inflateViewGroup.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
        setOnClickListener()
    }

    open fun scopeLayout(viewBinding: (VB.() -> Unit)) {
        viewBinding.invoke(binding)
    }
    open fun configureViews() {}
    open fun setOnClickListener() {}

    fun getStringExtra(path: String? = null): String? = arguments?.getString(path ?: ConstantLib.DATA)

    fun <T> getExtraAs(cls: Class<T>, path: String? = null): T =
        Gson().fromJson(requireArguments().getString(path ?: ConstantLib.DATA), cls)

    //not ready yet
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}