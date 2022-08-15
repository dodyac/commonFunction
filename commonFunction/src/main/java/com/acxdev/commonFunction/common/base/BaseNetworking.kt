package com.acxdev.commonFunction.common.base

import com.acxdev.commonFunction.common.Response
import com.acxdev.commonFunction.model.ResponseLib
import com.acxdev.commonFunction.util.ext.emptyString
import retrofit2.Call
import retrofit2.Callback
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class BaseNetworking {

    class CallbackBody<T>(private val body: ResponseLib<T>.() -> Unit) : Callback<T> {
        override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
            if(response.isSuccessful) {
                body.invoke(ResponseLib(response.body(), Response.SUCCESS, emptyString()))
            } else {
                body.invoke(ResponseLib(null, Response.UNSUCCESSFUL, response.code().toString()))
                println(response.raw().toString())
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val string = when(t) {
                is UnknownHostException -> "Could not connect to server!"
                is SocketTimeoutException -> "A connection timeout occurred"
                else -> t.localizedMessage
            }
            body.invoke(ResponseLib(null, Response.FAILURE, string))
            println("Retrofit Failure: ${t.message}")
        }
    }

    companion object {
        fun <T> Call<T>.whenLoaded(body: ResponseLib<T>.() -> Unit) {
            enqueue(CallbackBody(body))
        }
    }
}