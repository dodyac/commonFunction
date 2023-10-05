package com.acxdev.commonFunction.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.common.Inflater.inflateBinding
import com.acxdev.commonFunction.util.ext.toClass
import com.acxdev.sqlitez.DatabaseNameHolder
import com.acxdev.sqlitez.SqliteX

abstract class BaseActivity<VB : ViewBinding>(
    private val databaseName: String = DatabaseNameHolder.dbName
) : AppCompatActivity() {

    private var _binding: ViewBinding? = null
    private var _sqliteX: SqliteX? = null

    @Suppress("UNCHECKED_CAST")
    private val binding: VB
        get() = _binding!! as VB

    protected val sqliteX: SqliteX
        get() = _sqliteX!!

    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseNameHolder.setDatabaseName(databaseName)
        _sqliteX = SqliteX(this)

        _binding = inflateBinding(layoutInflater)
        setContentView(binding.root)

        doLoadData()
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

    protected open fun doLoadData() {}
    protected open fun VB.configureViews() {}
    protected open fun VB.onClickListener() {}

    protected fun getStringExtra(path: String? = null): String? = intent.getStringExtra(path ?: ConstantLib.DATA)

    protected fun <T> getExtraAs(cls: Class<T>, data: String? = null): T =
        intent.getStringExtra(data ?: ConstantLib.DATA).toClass(cls)
}