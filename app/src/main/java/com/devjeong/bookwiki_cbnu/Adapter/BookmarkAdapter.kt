package com.devjeong.bookwiki_cbnu.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.bookwiki_cbnu.Model.Bookmark
import com.devjeong.bookwiki_cbnu.R
import com.devjeong.bookwiki_cbnu.databinding.ListItemBinding

class BookmarkAdapter(private val itemClickListener: OnItemClickListener)  : ListAdapter<Bookmark, BookmarkAdapter.BookmarkViewHolder>(BookmarkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return BookmarkViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClick(bookmark: Bookmark)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val bookmark = getItem(position)
        holder.bind(bookmark)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(bookmark)
        }
    }

    inner class BookmarkViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bookmark: Bookmark) {
            binding.tvBookTitle.text = bookmark.doc_name
            binding.tvBookLabel.text = bookmark.kdc_label
            binding.tvBookPublisher.text = bookmark.publisher
        }
    }

    private class BookmarkDiffCallback : DiffUtil.ItemCallback<Bookmark>() {
        override fun areItemsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
            return oldItem.docId == newItem.docId
        }

        override fun areContentsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
            return oldItem == newItem
        }
    }
}
