package com.devjeong.bookwiki_cbnu.Retrofit

import com.devjeong.bookwiki_cbnu.Model.BookCountResponse
import com.devjeong.bookwiki_cbnu.Model.BookDetailResponse
import com.devjeong.bookwiki_cbnu.Model.BookResponse
import com.devjeong.bookwiki_cbnu.Model.BookYearCategory
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {
    @GET("book/read/{category}")
    suspend fun getBooksCategory(@Path("category") category: String): BookResponse

    @GET("book/read/count")
    suspend fun getBookCount() : BookCountResponse

    @GET("book/read/random")
    suspend fun getRandom() : BookResponse

    @GET("book/read/detail/{docId}")
    suspend fun getBookDetail(@Path("docId") docId: String): Response<BookDetailResponse>

    @GET("book/read/bookNameSearch")
    suspend fun searchBooks(
        @Query("bookName") bookName: String,
        @Query("category") category: String
    ): BookResponse

    @GET("book/read/count/year")
    suspend fun getBookCountByYear(): List<BookYearCategory>
}