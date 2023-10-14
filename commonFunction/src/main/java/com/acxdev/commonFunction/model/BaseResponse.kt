package com.acxdev.commonFunction.model

import okhttp3.ResponseBody

sealed class BaseResponse<out T> {
    data class Success<out T>(val body: T) : BaseResponse<T>()
    data class Unsuccessful(val errorBody: ResponseBody?, val code: Int) : BaseResponse<Nothing>()
    data class Error(val msg: String) : BaseResponse<Nothing>()
    object Load : BaseResponse<Nothing>()
}