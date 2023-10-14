package com.acxdev.commonFunction.common.base

import android.util.Log
import androidx.lifecycle.ViewModel
import com.acxdev.commonFunction.model.BaseResponse
import com.google.gson.JsonSyntaxException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseViewModel : ViewModel() {

    companion object {
        const val TAG = "Networking"
    }

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>, result: BaseResponse<T>.() -> Unit) {
        try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                try {
                    val res = response.body()!!
                    result.invoke(BaseResponse.Success(res))
                    Log.i(TAG, "Response Success")
                } catch (e: Exception) {
                    val error = e.localizedMessage ?: "Response Body Error"
                    result.invoke(BaseResponse.Error(error))

                    Log.e(TAG, "Response Body Error")
                    e.printStackTrace()
                }
            } else {
                result.invoke(BaseResponse.Unsuccessful(response.errorBody(), response.code()))
                Log.w(TAG, "Response Unsuccessful")
                Log.w(TAG, response.raw().toString())
            }
        } catch (e: Exception) {
            val error = when(e) {
                is UnknownHostException -> "Could not connect to server!"
                is SocketTimeoutException -> "A connection timeout occurred"
                is JsonSyntaxException -> "Parsing Error: ${e.localizedMessage}"
                else -> e.localizedMessage
            }

            result.invoke(BaseResponse.Error(error))
            Log.e(TAG, "Response Failure")
            e.printStackTrace()
        }
    }
}