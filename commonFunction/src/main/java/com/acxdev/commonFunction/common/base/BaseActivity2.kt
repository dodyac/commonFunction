package com.acxdev.commonFunction.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.utils.ext.toClass

abstract class BaseActivity2(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {

    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        onTheme()
        super.onCreate(savedInstanceState)

        doFetch()
        setViews()
        doAction()
    }

    protected open fun onTheme() {}
    protected open fun doFetch() {}
    protected open fun setViews() {}
    protected open fun doAction() {}

    protected fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        intent.getStringExtra(data ?: ConstantLib.DATA).toClass(cls)
}