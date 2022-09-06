package com.acxdev.commonFunction.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.Inflate

abstract class BaseActivityLib<VB : ViewBinding>(private val inflate: Inflate<VB>)  : AppCompatActivity() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)

        binding.configureViews()
        binding.onClickListener()
    }

    protected fun scopeLayout(viewBinding: (VB.() -> Unit)) {
        try {
            viewBinding.invoke(binding)
        } catch (e: Exception) {
            println("${javaClass.simpleName} was destroyed")
            e.printStackTrace()
        }
    }

    protected abstract fun VB.configureViews()
    protected abstract fun VB.onClickListener()
}