package com.acxdev.usefulmethod

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface Endpoint {
    @GET("users")
    fun getUsersMain(): Call<List<User>>

    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}