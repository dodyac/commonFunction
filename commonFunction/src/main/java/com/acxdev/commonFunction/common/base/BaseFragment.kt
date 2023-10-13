package com.acxdev.commonFunction.common.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Inflater.inflateBinding
import com.acxdev.commonFunction.utils.ext.toClass
import com.acxdev.sqlitez.SqliteZ

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: ViewBinding? = null
    private var _sqliteZ: SqliteZ? = null

    @Suppress("UNCHECKED_CAST")
    private val binding: VB
        get() = _binding!! as VB

    protected val sqliteZ: SqliteZ
        get() = _sqliteZ!!

    val TAG = javaClass.simpleName

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
        safeContext {
            _sqliteZ = SqliteZ(this)
        }
        doFetch()
        binding.setViews()
        binding.doAction()
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

    protected open fun doFetch() {}
    protected open fun VB.setViews() {}
    protected open fun VB.doAction() {}

    fun getStringExtra(path: String? = null): String? = arguments?.getString(path ?: ConstantLib.DATA)

    fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        arguments?.getString(data ?: ConstantLib.DATA).toClass(cls)
}