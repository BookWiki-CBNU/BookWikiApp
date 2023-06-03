package com.devjeong.bookwiki_cbnu.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.bookwiki_cbnu.Adapter.BookListRecyclerAdapter
import com.devjeong.bookwiki_cbnu.BaseFragment
import com.devjeong.bookwiki_cbnu.R
import com.devjeong.bookwiki_cbnu.Retrofit.RetrofitClient
import com.devjeong.bookwiki_cbnu.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookListRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "책무위키"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        recyclerView = binding.randomRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        bookAdapter = BookListRecyclerAdapter(emptyList()) // 빈 리스트로 초기화
        recyclerView.adapter = bookAdapter
        fetchRandomData()

        return view
    }

    override fun onStart() {
        super.onStart()

        val categoryButtons = listOf(binding.techBtn, binding.artBtn, binding.otherBtn, binding.socialBtn)

        categoryButtons.forEach { button ->
            button.setOnClickListener {
                val category = when (button.id) {
                    R.id.techBtn -> "Technology"
                    R.id.artBtn -> "Art"
                    R.id.otherBtn -> "Other"
                    R.id.socialBtn -> "Social"
                    else -> null
                }
                if (category != null) {
                    val intent = Intent(requireActivity(), BookListActivity::class.java)
                    intent.putExtra("category", category)
                    requireActivity().startActivity(intent)
                }
            }
        }
    }

    private fun fetchRandomData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.bookApiService.getRandom()
                val books = response.list

                withContext(Dispatchers.Main) {
                    bookAdapter.updateData(books)
                }
            } catch (e: Exception) {
                Log.e("StatisticsFragment", "Error: ${e.message}")
            }
        }
    }

}