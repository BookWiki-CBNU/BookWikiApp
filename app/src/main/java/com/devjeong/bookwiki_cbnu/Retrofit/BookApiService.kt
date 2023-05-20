package com.devjeong.bookwiki_cbnu.Retrofit

import com.devjeong.bookwiki_cbnu.Model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BookApiService {
    @GET("book/read/{category}")
    suspend fun getBooksCategory(@Path("category") category: String): BookResponse
}