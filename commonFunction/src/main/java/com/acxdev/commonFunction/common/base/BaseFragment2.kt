package com.acxdev.commonFunction.common.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.utils.ext.toClass
import com.acxdev.sqlitez.SqliteZ

abstract class BaseFragment2(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected val sqliteZ by lazy {
        SqliteZ(context)
    }

    val TAG = javaClass.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doFetch()
        setViews()
        doAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
    protected open fun setViews() {}
    protected open fun doAction() {}

    fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        arguments?.getString(data ?: ConstantLib.DATA).toClass(cls)
}