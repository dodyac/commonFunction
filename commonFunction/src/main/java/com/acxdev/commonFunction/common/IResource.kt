package com.acxdev.commonFunction.common

sealed class IResource<T>(val data: T? = null, val response: Response? = null, val s: String? = null) {
    class Success<T>(data: T) : IResource<T>(data)
    class Unsuccessful<T>(code: String) : IResource<T>(null,null, code)
    class Failure<T>(message: String?): IResource<T>(null,null, message)
    class Loading<T> : IResource<T>()
}