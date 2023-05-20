package com.devjeong.bookwiki_cbnu.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.bookwiki_cbnu.Model.Book
import com.devjeong.bookwiki_cbnu.R

class BookListRecyclerAdapter(var books: List<Book>)
    : RecyclerView.Adapter<BookListRecyclerAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount", books.size.toString())
        return books.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bindData(book)
    }

    fun updateData(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()

    }


    class BookViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){
            fun bindData(book: Book){
                itemView.apply {
                    val bookTitle : TextView = itemView.findViewById(R.id.tv_book_title)
                    val bookLabel : TextView = itemView.findViewById(R.id.tv_book_label)
                    val bookPublisher : TextView = itemView.findViewById(R.id.tv_book_publisher)

                    bookTitle.text = book.doc_name
                    bookLabel.text = book.kdc_label
                    bookPublisher.text = book.publisher
                }
            }
        }
    }
