package com.devjeong.bookwiki_cbnu.Model

data class Book(
    var doc_id : String,
    var doc_name : String,
    var kdc_label : String,
    var publisher : String
)

data class BookResponse(
    val list: List<Book>
)