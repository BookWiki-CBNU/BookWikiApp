package com.devjeong.bookwiki_cbnu.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.bookwiki_cbnu.Adapter.BookListRecyclerAdapter
import com.devjeong.bookwiki_cbnu.Model.Book
import com.devjeong.bookwiki_cbnu.R
import com.devjeong.bookwiki_cbnu.Retrofit.BookCategory
import com.devjeong.bookwiki_cbnu.Retrofit.RetrofitClient
import kotlinx.coroutines.*

class BookListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        val category = intent.getStringExtra("category")
        Log.d("category", category.toString())

        recyclerView = findViewById(R.id.bookRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        bookAdapter = BookListRecyclerAdapter(emptyList()) // 빈 리스트로 초기화
        recyclerView.adapter = bookAdapter

        if (category != null) {
            val bookCategory = when (category) {
                "Technology" -> BookCategory.Technology
                "Art" -> BookCategory.Art
                "Other" -> BookCategory.Other
                "Social" -> BookCategory.Social
                else -> null
            }
            if (bookCategory != null) {
                fetchDataFromApi(bookCategory)
            }
        }
    }

    private fun fetchDataFromApi(category: BookCategory) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = when (category) {
                    is BookCategory.Technology -> RetrofitClient.bookApiService.getBooksCategory("technology")
                    is BookCategory.Art -> RetrofitClient.bookApiService.getBooksCategory("art")
                    is BookCategory.Other -> RetrofitClient.bookApiService.getBooksCategory("other")
                    is BookCategory.Social -> RetrofitClient.bookApiService.getBooksCategory("social")
                }
                val books = response.list

                withContext(Dispatchers.Main) {
                    bookAdapter.updateData(books)
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error: ${e.message}")
                withContext(Dispatchers.Main) {
                }
            }
        }
    }
}