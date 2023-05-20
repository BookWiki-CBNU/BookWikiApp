package com.devjeong.bookwiki_cbnu.Retrofit

import com.devjeong.bookwiki_cbnu.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = BuildConfig.API_KEY

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val bookApiService: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }
}