package com.acxdev.commonFunction.common.base

import android.util.Log
import com.acxdev.commonFunction.model.ApiResponse
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object BaseNetworking {

    private val TAG = javaClass.simpleName

    fun <T> Call<T>.whenLoaded(body: ApiResponse<T>.() -> Unit) {
        enqueue(CallbackBody(body))
    }

    fun <T> Call<T>.whenLoadedSuccess(responseBody: T.() -> Unit) {
        enqueue(CallbackBody<T> {
            if (this is ApiResponse.Success) {
                responseBody.invoke(body)
            }
        })
    }

    internal class CallbackBody<T> (private val body: ApiResponse<T>.() -> Unit) : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if(response.isSuccessful) {
                try {
                    val res = response.body()!!
                    body.invoke(ApiResponse.Success(res))

                    Log.i(TAG, "Response Success")
                } catch (e: Exception) {
                    val error = e.localizedMessage
                    body.invoke(ApiResponse.Error(error))

                    Log.e(TAG, "Response Body Error")
                    e.printStackTrace()
                }
            } else {
                body.invoke(ApiResponse.Unsuccessful(response.errorBody(), response.code()))

                Log.w(TAG, "Response Unsuccessful")
                Log.w(TAG, response.raw().toString())
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val error = when(t) {
                is UnknownHostException -> "Could not connect to server!"
                is SocketTimeoutException -> "A connection timeout occurred"
                is JsonSyntaxException -> "Parsing Error: ${t.localizedMessage}"
                else -> t.localizedMessage
            }
            body.invoke(ApiResponse.Error(error))

            Log.e(TAG, "Response Failure")
            t.printStackTrace()
        }
    }
}