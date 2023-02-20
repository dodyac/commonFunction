package com.acxdev.commonFunction.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Inflate
import com.acxdev.commonFunction.util.ext.toClass

abstract class BaseActivityLib<VB : ViewBinding>(private val inflate: Inflate<VB>)  : AppCompatActivity() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    private val binding: VB
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

    protected fun getStringExtra(path: String? = null): String? = intent.getStringExtra(path ?: ConstantLib.DATA)

    protected fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        intent.getStringExtra(data ?: ConstantLib.DATA).toClass(cls)
}