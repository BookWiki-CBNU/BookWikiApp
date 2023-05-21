package com.devjeong.bookwiki_cbnu.Retrofit

import com.devjeong.bookwiki_cbnu.Model.BookCountResponse
import com.devjeong.bookwiki_cbnu.Model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BookApiService {
    @GET("book/read/{category}")
    suspend fun getBooksCategory(@Path("category") category: String): BookResponse

    @GET("book/read/count")
    suspend fun getBookCount() : BookCountResponse

    @GET("book/read/random")
    suspend fun getRandom() : BookResponse

    @GET("book/read/{category}")
    suspend fun getBooksPublisher(@Path("category") category: String): BookResponse
}