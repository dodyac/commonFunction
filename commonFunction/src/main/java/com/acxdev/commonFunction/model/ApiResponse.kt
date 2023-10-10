package com.acxdev.commonFunction.model

import okhttp3.ResponseBody

sealed class ApiResponse<out T> {
    data class Success<out T>(val body: T) : ApiResponse<T>()
    data class Unsuccessful(val errorBody: ResponseBody?, val code: Int) : ApiResponse<Nothing>()
    data class Error(val msg: String) : ApiResponse<Nothing>()
}