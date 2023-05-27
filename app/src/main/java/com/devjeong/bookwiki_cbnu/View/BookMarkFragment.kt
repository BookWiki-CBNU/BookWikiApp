package com.devjeong.bookwiki_cbnu.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.bookwiki_cbnu.Adapter.BookmarkAdapter
import com.devjeong.bookwiki_cbnu.BaseFragment
import com.devjeong.bookwiki_cbnu.Model.Bookmark
import com.devjeong.bookwiki_cbnu.Model.BookmarkViewModel
import com.devjeong.bookwiki_cbnu.databinding.FragmentBookMarkBinding

class BookMarkFragment : BaseFragment<FragmentBookMarkBinding>(FragmentBookMarkBinding::inflate), BookmarkAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookmarkAdapter

    private lateinit var bookmarkViewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookmarkViewModel = ViewModelProvider(this)[BookmarkViewModel::class.java]
        activity?.title = "북마크"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.BookmarkRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = BookmarkAdapter(this)
        recyclerView.adapter = adapter

        observeBookmarks()
    }

    private fun observeBookmarks() {
        bookmarkViewModel.getAllBookmarks().observe(viewLifecycleOwner) { bookmarks ->
            adapter.submitList(bookmarks)
        }
    }
    override fun onItemClick(bookmark: Bookmark) {
        val intent = Intent(recyclerView.context, BookDetailActivity::class.java)
        intent.putExtra("docId", bookmark.docId)
        recyclerView.context.startActivity(intent)
        Log.d("onItemClik", bookmark.docId)
    }

}