package com.acdev.commonFunction.common

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibQue {
    class CallBack<T> : Callback<T> {
        var response: ((Response<T>) -> Unit)? = null
        var failure: ((t: Throwable?) -> Unit)? = null

        override fun onResponse(call: Call<T>, response: Response<T>) { this.response?.invoke(response) }

        override fun onFailure(call: Call<T>, t: Throwable) { failure?.invoke(t) }
    }

    companion object {
        fun <T> Call<T>?.libque(callback: CallBack<T>.() -> Unit) {
            val callBack = CallBack<T>()
            callback.invoke(callBack)
            this?.enqueue(callBack)
        }
    }
}