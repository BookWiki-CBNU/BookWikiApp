package com.devjeong.bookwiki_cbnu.Model

import android.nfc.tech.TagTechnology
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Constructor

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
    val publisher: String? = "알수없음.",
    val summaryList: List<String>
)

data class BookYearCategory(
    @SerializedName("300")
    val code300: Int,
    @SerializedName("500")
    val code500: Int,
    @SerializedName("600")
    val code600: Int,
    val other: Int,
    val total: Int,
    @SerializedName("_id")
    val year: String
)

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey val docId : String,
    @ColumnInfo(name = "doc_name") val doc_name: String,
    @ColumnInfo(name = "kdc_label") val kdc_label: String,
    @ColumnInfo(name = "publisher") val publisher: String
)
