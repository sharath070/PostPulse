package com.sharath070.imguram.api

import com.sharath070.imguram.utils.Constants.Companion.BASE_URL
import com.sharath070.imguram.utils.Constants.Companion.CLIENT_ID
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {


    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectionPool(ConnectionPool(5, 30, TimeUnit.SECONDS))
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader("Authorization", "Client-ID $CLIENT_ID")
                    .build()
                it.proceed(request)
            }.build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }

}