package com.devjeong.bookwiki_cbnu.Model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.devjeong.bookwiki_cbnu.DAO.BookmarkDao
import com.devjeong.bookwiki_cbnu.DataBase.BookmarkDatabase
import androidx.lifecycle.asLiveData

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    private val bookmarkDao: BookmarkDao

    init {
        val bookmarkDatabase = BookmarkDatabase.getDataBase(application)
        bookmarkDao = bookmarkDatabase.bookmarkDao()
    }

    fun getAllBookmarks(): LiveData<List<Bookmark>> {
        return bookmarkDao.getAllBookmarks().asLiveData()
    }
}
