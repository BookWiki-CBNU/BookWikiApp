package com.devjeong.bookwiki_cbnu.View

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ExpandableListView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.devjeong.bookwiki_cbnu.Adapter.ExpandableListAdapter
import com.devjeong.bookwiki_cbnu.Model.BookDetailResponse
import com.devjeong.bookwiki_cbnu.Model.Bookmark
import com.devjeong.bookwiki_cbnu.R
import com.devjeong.bookwiki_cbnu.Retrofit.RetrofitClient
import com.devjeong.bookwiki_cbnu.databinding.ActivityBookDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookDetailActivity : AppCompatActivity() {
    private lateinit var expandableListView: ExpandableListView
    private var bookDetailResponse: BookDetailResponse? = null

    private lateinit var binding : ActivityBookDetailBinding

    private val BOOKMARKS_KEY = stringSetPreferencesKey("bookmarks")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = R.string.bookMark_data_store.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        expandableListView = binding.expandableListView

        val docId = intent.getStringExtra("docId")
        if (docId != null) {
            fetchBookDetail(docId)
        }

        binding.bookMarkBtn.setOnClickListener {
            val docName = binding.docNameTv.text.toString()
            val docId = binding.docIdTv.text.toString()
            val author = binding.authorTv.text.toString()
            val publisher = binding.publisherTv.text.toString()
            val kdcLabel = binding.kdcLabelTv.text.toString()

            val bookmark = Bookmark(docName, docId, author, publisher, kdcLabel)
            CoroutineScope(Dispatchers.IO).launch {
                saveBookmark(bookmark)
            }
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
    private suspend fun saveBookmark(bookmark: Bookmark) {
        val existingBookmarks = dataStore.data
            .map { preferences -> preferences[BOOKMARKS_KEY]?.toMutableSet() ?: mutableSetOf() }
            .first()
        existingBookmarks.add(bookmark.toString())
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[BOOKMARKS_KEY] = existingBookmarks
            }
        }
    }



}