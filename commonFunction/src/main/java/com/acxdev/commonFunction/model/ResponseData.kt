package com.acxdev.commonFunction.model

import com.acxdev.commonFunction.common.Response

data class ResponseData<T>(
    val data: T?,
    val response: Response,
    val string: String
)