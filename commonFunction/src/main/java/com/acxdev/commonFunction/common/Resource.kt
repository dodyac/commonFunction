package com.acxdev.commonFunction.common

sealed class Resource<T>(val data: T? = null, val response: Response? = null, val s: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Unsuccessful<T>(code: String) : Resource<T>(null,null, code)
    class Failure<T>(message: String?): Resource<T>(null,null, message)
    class Loading<T> : Resource<T>()
}