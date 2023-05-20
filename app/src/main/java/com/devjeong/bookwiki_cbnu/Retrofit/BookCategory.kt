package com.devjeong.bookwiki_cbnu.Retrofit

sealed class BookCategory{
    object Technology : BookCategory()
    object Art : BookCategory()
    object Other : BookCategory()
    object Social : BookCategory()
}
