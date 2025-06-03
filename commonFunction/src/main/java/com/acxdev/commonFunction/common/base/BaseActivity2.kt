package com.acxdev.commonFunction.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.acxdev.commonFunction.common.ConstantLib
import com.acxdev.commonFunction.utils.ext.toClass
import com.acxdev.sqlitez.SqliteZ
import com.acxdev.sqlitez.common.DatabaseNameHolder

abstract class BaseActivity2(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {

    protected val sqliteZ by lazy {
        SqliteZ(this)
    }

    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseNameHolder.setDatabaseName(databaseName)

        doFetch()
        setViews()
        doAction()
    }

    protected open val databaseName: String = DatabaseNameHolder.dbName

    protected open fun doFetch() {}
    protected open fun setViews() {}
    protected open fun doAction() {}

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