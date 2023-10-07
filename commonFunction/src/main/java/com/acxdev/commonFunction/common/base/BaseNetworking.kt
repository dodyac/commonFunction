package com.acxdev.commonFunction.common.base

import com.acxdev.commonFunction.common.Response
import com.acxdev.commonFunction.utils.ext.emptyString
import com.acxdev.commonFunction.utils.ext.isSuccess
import retrofit2.Call
import retrofit2.Callback
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class BaseNetworking {

    class CallbackBody<T>(private val body: com.acxdev.commonFunction.model.ResponseData<T>.() -> Unit) : Callback<T> {
        override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
            if(response.isSuccessful) {
                body.invoke(com.acxdev.commonFunction.model.ResponseData(response.body(), Response.SUCCESS, emptyString()))
            } else {
                val errorCode = response.code().toString()
                val errorBody = response.errorBody()?.string()
                val joinError = "$errorCode###$errorBody"
                body.invoke(com.acxdev.commonFunction.model.ResponseData(null, Response.UNSUCCESSFUL, joinError))
                println(response.raw().toString())
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val string = when(t) {
                is UnknownHostException -> "Could not connect to server!"
                is SocketTimeoutException -> "A connection timeout occurred"
                else -> t.localizedMessage
            }
            body.invoke(com.acxdev.commonFunction.model.ResponseData(null, Response.FAILURE, string))
            println("Retrofit Failure: ${t.message}")
        }
    }

    companion object {
        fun <T> Call<T>.whenLoaded(body: com.acxdev.commonFunction.model.ResponseData<T>.() -> Unit) {
            enqueue(CallbackBody(body))
        }

        fun <T> Call<T>.whenLoadedSuccess(body: T.() -> Unit) {
            enqueue(CallbackBody<T> {
                if (this.response.isSuccess()) {
                    data?.let {
                        body.invoke(it)
                    }
                }
            })
        }

        fun String.splitResponseUnsuccessful(response : (Int, String) -> Unit) {
            val split = split("###")
            val errorCode = split[0]
            val errorBody = split[1]
            response.invoke(errorCode.toInt(), errorBody)
        }
    }
}