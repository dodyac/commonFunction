package com.acxdev.commonFunction.model

import com.acxdev.commonFunction.common.Response

data class Response<T>(
    val data: T?,
    val response: Response,
    val string: String?
)