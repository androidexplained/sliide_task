package com.sample.sliide.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object UsersAPI {
    private const val BASE_URL = "https://gorest.co.in/"

    private const val token = "34003a1ab8b5c0fdb8748f1c091e2263946d782b31e0d2996a18386d3c70bb3c"

    val usersService: UsersService by lazy { retrofit.create(UsersService::class.java) }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val newRequest = it.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            it.proceed(newRequest)
        }.build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()

    }
}