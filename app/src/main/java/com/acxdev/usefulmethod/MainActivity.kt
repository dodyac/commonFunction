package com.acxdev.usefulmethod

import com.acxdev.commonFunction.common.base.BaseActivity
import com.acxdev.commonFunction.common.base.BaseNetworking.whenLoaded
import com.acxdev.commonFunction.common.base.BaseNetworking.whenLoadedSuccess
import com.acxdev.commonFunction.model.ApiResponse
import com.acxdev.commonFunction.utils.toast
import com.acxdev.usefulmethod.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit
import com.google.gson.annotations.SerializedName


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun doLoadData() {
        fetch<Endpoint>().getUsers().whenLoaded {
            when(this) {
                is ApiResponse.Success -> {
                    toast(this.body.first().name)
                }
                is ApiResponse.Unsuccessful -> {
                    toast(this.code.toString())
                }
                is ApiResponse.Error -> {
                    toast(this.msg)
                }
            }
        }

        fetch<Endpoint>().getUsers().whenLoadedSuccess {
            toast(first().name)
        }
    }

    private inline fun <reified T: Any> fetch(
        baseUrl: String = "https://jsonplaceholder.typicode.com/",
        level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC,
        timeout: Long = 30
    ): T {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(level))
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(T::class.java)
    }

    interface Endpoint {
        @GET("users")
        fun getUsers(): Call<List<User>>
    }

    data class User(
        @SerializedName("address")
        val address: Address,
        @SerializedName("company")
        val company: Company,
        @SerializedName("email")
        val email: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("username")
        val username: String,
        @SerializedName("website")
        val website: String
    ) {
        data class Address(
            @SerializedName("city")
            val city: String,
            @SerializedName("geo")
            val geo: Geo,
            @SerializedName("street")
            val street: String,
            @SerializedName("suite")
            val suite: String,
            @SerializedName("zipcode")
            val zipcode: String
        ) {
            data class Geo(
                @SerializedName("lat")
                val lat: String,
                @SerializedName("lng")
                val lng: String
            )
        }

        data class Company(
            @SerializedName("bs")
            val bs: String,
            @SerializedName("catchPhrase")
            val catchPhrase: String,
            @SerializedName("name")
            val name: String
        )
    }
}
