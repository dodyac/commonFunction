package com.acxdev.commonFunction.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.InflateViewGroup
import com.acxdev.commonFunction.util.ext.toClass

abstract class BaseDialogLib<VB : ViewBinding>(private val inflateViewGroup: InflateViewGroup<VB>) : AppCompatDialogFragment() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding!!as VB

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
        binding.configureViews()
        binding.onClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun scopeLayout(viewBinding: (VB.() -> Unit)) {
        viewBinding.invoke(binding)
    }

    protected abstract fun VB.configureViews()
    protected abstract fun VB.onClickListener()

    fun getStringExtra(path: String? = null): String? = arguments?.getString(path ?: ConstantLib.DATA)

    fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        arguments?.getString(data ?: ConstantLib.DATA).toClass(cls)
}