package com.acxdev.commonFunction.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.InflateViewGroup
import com.acxdev.commonFunction.util.ext.toClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson

abstract class BaseFragmentLib<out VB : ViewBinding>(private val inflateViewGroup: InflateViewGroup<VB>) : BottomSheetDialogFragment() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    fun setupFullHeight(bottomSheet: View) {
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

    fun <T> getExtraAs(cls: Class<T>, path: String? = null): T {
        val str = path ?: ConstantLib.DATA
        return str.toClass(cls)
    }

    //not ready yet
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}