package com.acxdev.commonFunction.model

data class ResponseData<T>(
    val data: T?,
    val response: Response,
    val string: String
)