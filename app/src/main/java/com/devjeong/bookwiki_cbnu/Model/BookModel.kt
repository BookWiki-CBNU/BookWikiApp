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