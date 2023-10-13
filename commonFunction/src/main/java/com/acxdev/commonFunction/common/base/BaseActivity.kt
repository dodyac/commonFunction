package com.acxdev.commonFunction.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Inflater.inflateBinding
import com.acxdev.commonFunction.utils.ext.toClass
import com.acxdev.sqlitez.DatabaseNameHolder
import com.acxdev.sqlitez.SqliteZ

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: ViewBinding? = null
    private var _sqliteZ: SqliteZ? = null

    @Suppress("UNCHECKED_CAST")
    private val binding: VB
        get() = _binding!! as VB

    protected val sqliteZ: SqliteZ
        get() = _sqliteZ!!

    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseNameHolder.setDatabaseName(databaseName)
        _sqliteZ = SqliteZ(this)

        _binding = inflateBinding(layoutInflater)
        setContentView(binding.root)

        doFetch()
        binding.setViews()
        binding.doAction()
    }

    protected fun scopeLayout(viewBinding: (VB.() -> Unit)) {
        try {
            viewBinding.invoke(binding)
        } catch (e: Exception) {
            println("${javaClass.simpleName} was destroyed")
            e.printStackTrace()
        }
    }

    protected open val databaseName: String = DatabaseNameHolder.dbName

    protected open fun doFetch() {}
    protected open fun VB.setViews() {}
    protected open fun VB.doAction() {}

    protected fun getStringExtra(path: String? = null): String? = intent.getStringExtra(path ?: ConstantLib.DATA)

    protected fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        intent.getStringExtra(data ?: ConstantLib.DATA).toClass(cls)
}