package com.devjeong.bookwiki_cbnu.View

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ExpandableListView
import com.devjeong.bookwiki_cbnu.Adapter.ExpandableListAdapter
import com.devjeong.bookwiki_cbnu.DAO.BookmarkDao
import com.devjeong.bookwiki_cbnu.DataBase.BookmarkDatabase
import com.devjeong.bookwiki_cbnu.Model.BookDetailResponse
import com.devjeong.bookwiki_cbnu.Model.Bookmark
import com.devjeong.bookwiki_cbnu.Retrofit.RetrofitClient
import com.devjeong.bookwiki_cbnu.databinding.ActivityBookDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class BookDetailActivity : AppCompatActivity() {
    private lateinit var expandableListView: ExpandableListView
    private var bookDetailResponse: BookDetailResponse? = null

    private lateinit var binding : ActivityBookDetailBinding
    private lateinit var bookmarkDao: BookmarkDao

    private var isBookmark : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookmarkDao = BookmarkDatabase.getDataBase(this).bookmarkDao()

        expandableListView = binding.expandableListView

        val docId = intent.getStringExtra("docId")
        if (docId != null) {
            fetchBookDetail(docId)
        }

        // 북마크 버튼 클릭 리스너 등록
        binding.bookMarkBtn.setOnClickListener {
            val docId = binding.docIdTv.text.toString()
            val docName = binding.docNameTv.text.toString()
            val kdcLabel = binding.kdcLabelTv.text.toString()
            val publisher = binding.publisherTv.text.toString()

            saveBookmark(docId, docName, kdcLabel, publisher)
        }
    }
    private fun fetchBookDetail(docId: String) {
        val apiService = RetrofitClient.bookApiService
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getBookDetail(docId)
                if (response.isSuccessful) {
                    val bookDetail = response.body()
                    withContext(Dispatchers.Main) {
                        bookDetail?.let {
                            bookDetailResponse = it

                            binding.docNameTv.text = it.docName
                            Log.d("docName", it.docId)
                            binding.docIdTv.text = it.docId
                            binding.authorTv.text = it.author
                            binding.publisherTv.text = it.publisher
                            binding.kdcLabelTv.text = it.kdcLabel

                            setupExpandableListView()
                        }
                    }
                } else {
                    // Handle API error
                }
            } catch (e: Exception) {
                // Handle network error
            }
        }
    }
    private fun setupExpandableListView() {
        bookDetailResponse?.let { bookDetail ->
            val summaryList = bookDetail.summaryList

            val groupList = mutableListOf<String>()
            val childMap = mutableMapOf<String, List<String>>()

            for (i in summaryList.indices) {
                val groupName = "챕터 ${i + 1}"
                groupList.add(groupName)
                childMap[groupName] = listOf(summaryList[i])
            }

            val adapter = ExpandableListAdapter(this, groupList, childMap)
            expandableListView.setAdapter(adapter)
        }
    }
    private fun saveBookmark(docId: String, docName:String, kdcLabel:String, publisher:String) {
        val bookmark = Bookmark(docId, docName, kdcLabel, publisher)
        CoroutineScope(Dispatchers.IO).launch {
            bookmarkDao.insertBookmark(bookmark)
            Log.d("Bookmark", "Bookmark added : $docId")
            finish()
        }
    }
}