package com.devjeong.bookwiki_cbnu.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
    private lateinit var searchEt : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        supportActionBar?.title = "리스트"

        val category = intent.getStringExtra("category")
        Log.d("category", category.toString())

        recyclerView = findViewById(R.id.bookRecyclerView)
        searchEt = findViewById(R.id.searchEt)
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

        ///검색 기능 구현중///
        searchEt.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val keyword = searchEt.text.toString().trim()
                if (keyword.isNotEmpty()) {
                    val bookCategory = when (category) {
                        "Technology" -> "technology"
                        "Art" -> "art"
                        "Other" -> "other"
                        "Social" -> "social"
                        else -> null
                    }
                    Log.d("keyword", "$keyword $category")
                    searchBooks(keyword, bookCategory!!)
                } else {

                }
                return@setOnKeyListener true
            }
            false
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    private fun searchBooks(keyword: String, category: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.bookApiService.searchBooks(keyword, category)
                if (response.list.isNullOrEmpty()) {
                    // 검색 결과가 없는 경우 처리
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext,"검색한 요청의 책이 없습니다.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val books = response.list
                    withContext(Dispatchers.Main) {
                        bookAdapter.updateData(books)
                    }
                }
            } catch (e: Exception) {
                Log.e("searchBooks", "Error: ${e.message}")
                withContext(Dispatchers.Main) {
                    // TODO: 예외 처리 로직 구현
                }
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