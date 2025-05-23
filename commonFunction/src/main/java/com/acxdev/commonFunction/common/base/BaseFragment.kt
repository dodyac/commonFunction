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
import com.acxdev.commonFunction.utils.ext.view.removeViewIfNeeded
import com.acxdev.sqlitez.SqliteZ

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB by lazy {
        _binding ?: throw IllegalStateException("$TAG already destroyed")
    }

    protected val sqliteZ by lazy {
        SqliteZ(context)
    }

    val TAG = javaClass.simpleName

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

    fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        arguments?.getString(data ?: ConstantLib.DATA).toClass(cls)
}