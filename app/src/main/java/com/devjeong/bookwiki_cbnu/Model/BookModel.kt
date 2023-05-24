package com.devjeong.bookwiki_cbnu.Model

import android.nfc.tech.TagTechnology
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Book(
    val doc_id: String,
    val doc_name: String,
    val kdc_label: String,
    val publisher: String
)

data class BookResponse(
    val list: List<Book>
)

data class BookCountResponse(
    val technology: Int,
    val social: Int,
    val art: Int,
    val other: Int
)
data class BookDetailResponse(
    val author: String,
    val docId: String,
    val docName: String,
    val image: String,
    val kdcLabel: String,
    val publisher: String,
    val summaryList: List<String>
)

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey val docId : String,
    @ColumnInfo(name = "doc_name") val doc_name: String,
    @ColumnInfo(name = "kdc_label") val kdc_label: String,
    @ColumnInfo(name = "publisher") val publisher: String
)
