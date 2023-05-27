package com.devjeong.bookwiki_cbnu.View

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
import com.devjeong.bookwiki_cbnu.Retrofit.BookCategory
import com.devjeong.bookwiki_cbnu.Retrofit.RetrofitClient
import com.devjeong.bookwiki_cbnu.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.*

class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(FragmentStatisticsBinding::inflate) {
    private lateinit var pieChart : PieChart
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "통계페이지"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        pieChart = binding.countPieChart

        fetchChartData()

        recyclerView = binding.randomRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        bookAdapter = BookListRecyclerAdapter(emptyList()) // 빈 리스트로 초기화
        recyclerView.adapter = bookAdapter
        fetchRandomData()

        return view
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

    private fun fetchChartData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.bookApiService.getBookCount()

                withContext(Dispatchers.Main){
                    val entries = mutableListOf<PieEntry>()
                    entries.add(PieEntry(response.technology.toFloat(), "기술과학"))
                    entries.add(PieEntry(response.social.toFloat(), "사회과학"))
                    entries.add(PieEntry(response.art.toFloat(), "예술"))
                    entries.add(PieEntry(response.other.toFloat(), "기타"))

                    val total = entries.sumByDouble { it.value.toDouble() }
                    val percentEntries = entries.map { PieEntry((it.value.toDouble() / total * 100).toFloat(), it.label) }

                    val dataSet = PieDataSet(percentEntries, "")
                    with(dataSet) {
                        sliceSpace = 3f
                        selectionShift = 5f
                        setColors(*ColorTemplate.JOYFUL_COLORS)
                    }

                    dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
                    dataSet.valueFormatter = PercentFormatter(pieChart)
                    dataSet.valueTextSize = 12f

                    val data = PieData(dataSet)

                    pieChart.data = data
                    pieChart.description.isEnabled = false
                    pieChart.animateY(1000)
                    pieChart.invalidate()
                }
            } catch (e : Exception){
                Log.e("BookChartFragment", "Error: ${e.message}")
            }
        }
    }

}