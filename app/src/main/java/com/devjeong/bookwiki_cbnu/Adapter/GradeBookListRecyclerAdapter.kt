package com.devjeong.bookwiki_cbnu.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.bookwiki_cbnu.Model.TopGrade
import com.devjeong.bookwiki_cbnu.R
import com.devjeong.bookwiki_cbnu.View.BookDetailActivity

class GradeBookListRecyclerAdapter : RecyclerView.Adapter<GradeBookListRecyclerAdapter.BookViewHolder>() {

    private var books: List<TopGrade> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grade_list_item, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bindData(book)
    }

    fun updateData(newBooks: List<TopGrade>) {
        books = newBooks
        notifyDataSetChanged()
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val book = books[position]
                    val intent = Intent(itemView.context, BookDetailActivity::class.java)
                    intent.putExtra("docId", book._id)
                    itemView.context.startActivity(intent)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindData(book: TopGrade) {
            itemView.apply {
                val bookTitle: TextView = findViewById(R.id.tv_book_title)
                val bookLabel: TextView = findViewById(R.id.tv_book_label)
                val bookPublisher: TextView = findViewById(R.id.tv_book_publisher)
                val bookDocId: TextView = findViewById(R.id.tv_book_docId)
                val bookGrade: TextView = findViewById(R.id.tv_book_grade)

                bookTitle.text = book.doc_name
                bookLabel.text = book.kdc_label
                bookPublisher.text = book.publisher
                bookDocId.text = book._id
                bookGrade.text = "책 점수 : " + book.grade.toString() + "점"
            }
        }
    }
}

