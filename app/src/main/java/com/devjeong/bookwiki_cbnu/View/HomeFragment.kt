package com.devjeong.bookwiki_cbnu.View

import android.content.Intent
import android.os.Bundle
import com.devjeong.bookwiki_cbnu.BaseFragment
import com.devjeong.bookwiki_cbnu.R
import com.devjeong.bookwiki_cbnu.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

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

}