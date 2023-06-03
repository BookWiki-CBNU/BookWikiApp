package com.devjeong.bookwiki_cbnu.View

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.bookwiki_cbnu.Adapter.BookListRecyclerAdapter
import com.devjeong.bookwiki_cbnu.Adapter.GradeBookListRecyclerAdapter
import com.devjeong.bookwiki_cbnu.BaseFragment
import com.devjeong.bookwiki_cbnu.Retrofit.RetrofitClient
import com.devjeong.bookwiki_cbnu.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.*

class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(FragmentStatisticsBinding::inflate) {
    private lateinit var pieChart : PieChart
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: GradeBookListRecyclerAdapter

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
        fetchYearData()

        recyclerView = binding.bestBookRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        bookAdapter = GradeBookListRecyclerAdapter() // 빈 리스트로 초기화
        recyclerView.adapter = bookAdapter
        fetchBestBookData()

        return view
    }

    private fun fetchYearData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val groupedBarEntries = ArrayList<BarEntry>()
                val years = ArrayList<String>()
                val bookCounts = RetrofitClient.bookApiService.getBookCountByYear()

                withContext(Dispatchers.Main) {
                    for ((index, bookCount) in bookCounts.withIndex()) {
                        val barEntry = BarEntry(
                            index.toFloat(),
                            floatArrayOf(
                                bookCount.code300.toFloat(),
                                bookCount.code500.toFloat(),
                                bookCount.code600.toFloat(),
                                bookCount.other.toFloat()
                            )
                        )
                        groupedBarEntries.add(barEntry)
                        years.add(bookCount.year)
                    }

                    val barColors = intArrayOf(*ColorTemplate.JOYFUL_COLORS, *ColorTemplate.JOYFUL_COLORS, *ColorTemplate.JOYFUL_COLORS, *ColorTemplate.JOYFUL_COLORS)
                    val labels = arrayOf("사회과학", "기술과학", "예술과학", "그 외")

                    val dataSets = ArrayList<IBarDataSet>()

                    for (i in labels.indices) {
                        val values = ArrayList<BarEntry>()

                        for (j in groupedBarEntries.indices) {
                            val barEntry = groupedBarEntries[j]
                            val stackedValue = barEntry.yVals[i]
                            values.add(BarEntry(barEntry.x, floatArrayOf(stackedValue)))
                        }

                        val dataSet = BarDataSet(values, labels[i])
                        dataSet.color = barColors[i]
                        dataSets.add(dataSet)
                    }

                    val data = BarData(dataSets as List<IBarDataSet>)

                    data.barWidth = 0.2f
                    data.groupBars(-0.5f, 0.3f, 0.04f)

                    val chart = binding.countBarChart

                    /*val xAxis = chart.xAxis
                    xAxis.spaceMin = 0.5f // 그룹 간의 최소 공간 설정
                    xAxis.spaceMax = 3f // 그룹 간의 최대 공간 설정
                    xAxis.valueFormatter = IndexAxisValueFormatter(years)
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.labelCount = years.size
                    xAxis.setDrawGridLines(false)*/

                    chart.data = data
                    chart.setDrawGridBackground(false)
                    chart.setFitBars(true)
                    chart.description.isEnabled = false
                    chart.legend.isEnabled = true
                    chart.xAxis.valueFormatter = IndexAxisValueFormatter(years)
                    chart.xAxis.setCenterAxisLabels(false)
                    chart.xAxis.setDrawGridLines(false)
                    chart.axisLeft.axisMinimum = 0f
                    chart.axisRight.isEnabled = false
                    chart.setTouchEnabled(false)
                    chart.animateY(1000)
                    chart.invalidate()
                }
            } catch (e: Exception) {
                Log.e("StatisticsFragment", "Error: ${e.message}")
            }
        }

    }

    private fun fetchBestBookData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.bookApiService.getTopGrade()

                val books = response

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