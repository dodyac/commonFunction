package com.acxdev.commonFunction.util

import com.acxdev.commonFunction.common.Response
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import java.lang.IllegalStateException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class QueOrigin {
    class CallbackBody<T>(private val body: (T?, Response, String?) -> Unit) : Callback<T> {
        override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
            if (!response.isSuccessful) {
                body.invoke(null, Response.UNSUCCESSFUL, response.code().toString())
                println(response.raw().toString())
            } else body.invoke(response.body()!!, Response.SUCCESS, null)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val string = when(t){
                is UnknownHostException -> "Could not connect to server!"
                is SocketTimeoutException -> "A connection timeout occurred"
                is IllegalStateException -> null
                is JsonSyntaxException -> null
                else -> null
            }
            body.invoke(null, Response.FAILURE, string)
            println("Retrofit Failure: ${t.message}")
        }
    }

    companion object {
        fun <T> Call<T>.observe(body: (T?, Response, String?) -> Unit) {
            val callBack = CallbackBody(body)
            enqueue(callBack)
        }
    }
}