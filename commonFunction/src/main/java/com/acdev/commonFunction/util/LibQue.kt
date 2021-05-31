package com.acdev.commonFunction.util

import android.content.Context
import com.acdev.commonFunction.R
import com.acdev.commonFunction.common.Toast
import com.acdev.commonFunction.util.Toast.Companion.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibQue {

    class CallBackx<T>(val context: Context, private val response: (Response<T>.() -> Unit)) : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (!response.isSuccessful) {
                val msg = when(response.code()){
                    100 -> "Continue"
                    101 -> "Switching Protocols"
                    102 -> "Processing"
                    200 -> "OK"
                    201 -> "Created"
                    202 -> "Accepted"
                    203 -> "Non-Authoritative Information"
                    204 -> "No Content"
                    205 -> "Reset Content"
                    206 -> "Partial Content"
                    207 -> "Multi-Status"
                    208 -> "Already Reported"
                    226 -> "IM Used"
                    300 -> "Multiple Choices"
                    301 -> "Moved Permanently"
                    302 -> "Found"
                    303 -> "See Other"
                    304 -> "Not Modified"
                    305 -> "Use Proxy"
                    307 -> "Temporary Redirect"
                    308 -> "Permanent Redirect"
                    400 -> "Bad Request"
                    401 -> "Unauthorized"
                    402 -> "Payment Required"
                    403 -> "Forbidden"
                    404 -> "Not Found"
                    405 -> "Method Not Allowed"
                    406 -> "Not Acceptable"
                    407 -> "Proxy Authentication Required"
                    408 -> "Request Timeout"
                    409 -> "Conflict"
                    410 -> "Gone"
                    411 -> "Length Required"
                    412 -> "Precondition Failed"
                    413 -> "Payload Too Large"
                    414 -> "URI Too Long"
                    415 -> "Unsupported Media Type"
                    416 -> "Range Not Satisfiable"
                    417 -> "Expectation Failed"
                    418 -> "I'm a teapot"
                    421 -> "Misdirected Request"
                    422 -> "Unprocessable Entity"
                    423 -> "Locked"
                    424 -> "Failed Dependency"
                    425 -> "Reserved for WebDAV advanced collections expired proposal"
                    426 -> "Upgrade Required"
                    428 -> "Precondition Required"
                    429 -> "Too Many Requests"
                    431 -> "Request Header Fields Too Large"
                    451 -> "Unavailable For Legal Reasons"
                    500 -> "Internal Server Error"
                    501 -> "Not Implemented"
                    502 -> "Bad Gateway"
                    503 -> "Service Unavailable"
                    504 -> "Gateway Timeout"
                    505 -> "HTTP Version Not Supported"
                    506 -> "Variant Also Negotiates"
                    507 -> "Insufficient Storage"
                    508 -> "Loop Detected"
                    510 -> "Not Extended"
                    511 -> "Network Authentication Required"
                    else -> ""
                }
                context.toast(Toast.ERROR, context.getString(R.string.error, response.code(), msg))
                println(response.raw().toString())
            } else this.response.invoke(response)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            context.toast(Toast.ERROR, context.getString(R.string.cannotConnect))
            println("Failure: ${t.message}")
        }
    }

    companion object {
        fun <T> Call<T>?.libque(context: Context, response: Response<T>.() -> Unit) {
            val callBack = CallBackx(context, response)
            this?.enqueue(callBack)
        }
    }
}