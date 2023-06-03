package com.devjeong.bookwiki_cbnu.Retrofit

import com.devjeong.bookwiki_cbnu.BuildConfig
import com.devjeong.bookwiki_cbnu.Model.BookYearCategory
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object RetrofitClient {
    private const val BASE_URL = BuildConfig.API_KEY

    val gson = GsonBuilder()
        .registerTypeAdapter(
            object : TypeToken<List<BookYearCategory>>() {}.type,
            object : InstanceCreator<List<BookYearCategory>> {
                override fun createInstance(type: Type): List<BookYearCategory> {
                    return ArrayList()
                }
            }
        )
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val bookApiService: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }
}