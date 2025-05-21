package com.acxdev.commonFunction.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Inflater.inflateBinding
import com.acxdev.commonFunction.utils.ext.toClass
import com.acxdev.sqlitez.SqliteZ
import com.acxdev.sqlitez.common.DatabaseNameHolder

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding: VB by lazy {
        _binding ?: throw IllegalStateException("$TAG already destroyed")
    }

    protected val sqliteZ by lazy {
        SqliteZ(this)
    }

    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseNameHolder.setDatabaseName(databaseName)

        _binding = inflateBinding(layoutInflater)
        setContentView(binding.root)

        doFetch()
        binding.setViews()
        binding.doAction()
    }

    protected open val databaseName: String = DatabaseNameHolder.dbName

    protected open fun doFetch() {}
    protected open fun VB.setViews() {}
    protected open fun VB.doAction() {}

    protected fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        intent.getStringExtra(data ?: ConstantLib.DATA).toClass(cls)

    override fun onDestroy() {
        super.onDestroy()
        val db = sqliteZ.readableDatabase
        if (db.isOpen) {
            db.close()
        }
    }
}