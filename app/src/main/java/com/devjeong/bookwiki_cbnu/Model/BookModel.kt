package com.devjeong.bookwiki_cbnu.Model

import android.nfc.tech.TagTechnology

data class Book(
    var doc_id : String,
    var doc_name : String,
    var kdc_label : String,
    var publisher : String
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

data class Bookmark(
    val docName: String,
    val docId: String,
    val author: String,
    val publisher: String,
    val kdcLabel: String
) {
    override fun toString(): String {
        return "$docName|$docId|$author|$publisher|$kdcLabel"
    }

    companion object {
        fun fromString(string: String): Bookmark {
            val parts = string.split("|")
            return Bookmark(parts[0], parts[1], parts[2], parts[3], parts[4])
        }
    }
}
