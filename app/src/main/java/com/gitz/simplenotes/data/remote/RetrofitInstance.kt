package com.gitz.simplenotes.data.remote

import okhttp3.OkHttpClient
import okhttp3.internal.addHeaderLenient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.gitz.simplenotes.BuildConfig

object RetrofitInstance {
    private const val BASE_URL = "https://api.api-ninjas.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private const val API_KEY = BuildConfig.API_KEY

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequestBuilder = originalRequest.newBuilder()
                .header("X-Api-Key", API_KEY)
            val newRequest = newRequestBuilder.build()
            chain.proceed(newRequest)
        }
        .addInterceptor(loggingInterceptor)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}