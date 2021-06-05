package com.acdev.commonFunction.util

import android.content.Context
import com.acdev.commonFunction.R
import com.acdev.commonFunction.common.Toast
import com.acdev.commonFunction.util.Toast.Companion.toast
import com.acdev.commonFunction.util.DataType.Companion.getMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibQue {

    class CallBackX<T>(val context: Context, private val response: (Response<T>.() -> Unit)) : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (!response.isSuccessful) {
                context.toast(Toast.ERROR, context.getString(R.string.error,
                    response.code(), response.code().getMessage()))
                println(response.raw().toString())
            } else this.response.invoke(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            context.toast(Toast.ERROR, context.getString(R.string.cannotConnect))
            println("Failure: ${t.message}")
        }
    }

    companion object {
        fun <T> Call<T>?.libQue(context: Context, response: Response<T>.() -> Unit) {
            val callBack = CallBackX(context, response)
            this?.enqueue(callBack)
        }
    }
}