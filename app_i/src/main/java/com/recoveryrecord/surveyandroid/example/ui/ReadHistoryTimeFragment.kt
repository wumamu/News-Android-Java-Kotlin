package com.recoveryrecord.surveyandroid.example.ui

import android.app.usage.UsageStatsManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.Timestamp
import com.recoveryrecord.surveyandroid.example.R
import java.util.Calendar

class ReadHistoryTimeFragment : Fragment() {
    private lateinit var usageStatsManager: UsageStatsManager
    private lateinit var barChart: BarChart
    private lateinit var packageName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        packageName = requireContext().packageName
        usageStatsManager =
            requireContext().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.activity_read_history_weekly, container, false)
        barChart = view.findViewById(R.id.barChart_view)
        barChartIntiSetting()
        showBarChart()
        return view
    }

    private fun barChartIntiSetting() {
        val modifyDescription =
            Description().apply {
                isEnabled = false
                text = "NewsMoment螢幕使用分鐘數"
            }
        barChart.apply {
            setDrawBorders(false)
            description = modifyDescription
            animateY(1000)
            axisLeft.axisMinimum = 0f
        }
        // set label
        val calendar = Calendar.getInstance()
        calendar.time = Timestamp.now().toDate()
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        val xAxisLabel = ArrayList<String>()
        for (i in 0..6) {
            xAxisLabel.add((calendar[Calendar.MONTH] + 1).toString() + "/" + calendar[Calendar.DAY_OF_MONTH])
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabel)
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawAxisLine(true) // label 上一條橫線
            setDrawGridLines(false) // 穿越bar的線
        }
        barChart.axisLeft.apply {
            setDrawAxisLine(true)
            setDrawGridLines(false)
        }
        barChart.axisRight.apply {
            isEnabled = false
            setDrawAxisLine(false)
        }
        barChart.legend.apply {
            textSize = 15f
            textColor = Color.BLACK
            form = Legend.LegendForm.CIRCLE
            isWordWrapEnabled = true
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        }
    }

    private fun showBarChart() {
        val entries = ArrayList<BarEntry>()
        val title = "NewsMoment螢幕使用分鐘數"
        val startTime = Calendar.getInstance()
        startTime.add(Calendar.DAY_OF_YEAR, -7)
        val endTime = Calendar.getInstance()
        endTime.add(Calendar.DAY_OF_YEAR, -7)

        // fit the data into a bar
        for (i in 0..6) {
            startTime.add(Calendar.DAY_OF_YEAR, 1)
            endTime.add(Calendar.DAY_OF_YEAR, 1)
            startTime.set(Calendar.HOUR_OF_DAY, 0)
            startTime.set(Calendar.MINUTE, 0)
            startTime.set(Calendar.SECOND, 0)
            endTime.set(Calendar.HOUR_OF_DAY, 23)
            endTime.set(Calendar.MINUTE, 59)
            endTime.set(Calendar.SECOND, 59)

            val usageStats =
                usageStatsManager.queryAndAggregateUsageStats(
                    startTime.timeInMillis,
                    endTime.timeInMillis,
                )
            var displayMinute = 0f
            if (usageStats.isNotEmpty() && usageStats.containsKey(packageName)) {
                val totalTimeUsageInMillis =
                    usageStats[packageName]?.totalTimeInForeground?.toFloat() ?: 0f
                displayMinute = totalTimeUsageInMillis / 60000
            }
            entries.add(BarEntry(i.toFloat(), floatArrayOf(displayMinute)))
        }
        val barDataSet = BarDataSet(entries, title)
        // 隱藏資料上方數值： 資料數值顯示與否與資料有關，所以相關設定在BarData裡

        // 設定顏色
        barDataSet.setColors(*chartColors)
        // 設定顯示字串
        barDataSet.stackLabels = stackLabels
        val data = BarData(barDataSet)
        data.setValueTextColor(Color.BLACK)
        data.barWidth = 0.7f
        data.isHighlightEnabled = false

        barChart.data = data
        barChart.invalidate()
    }

    private val chartColors: IntArray
        get() =
            intArrayOf(
                resources.getColor(R.color.chart_color_chinatimes),
            )

//    private fun getResourceColor(resID: Int): Int {
//        return resources.getColor(resID)
//    }

    private val stackLabels: Array<String>
        get() =
            arrayOf(
                getString(R.string.chart_label_chinatimes),
                getString(R.string.chart_label_cna),
                getString(R.string.chart_label_cts),
                getString(R.string.chart_label_ebc),
                getString(R.string.chart_label_ltn),
                getString(R.string.chart_label_storm),
                getString(R.string.chart_label_udn),
                getString(R.string.chart_label_ettoday),
                getString(R.string.chart_label_setn),
            )

    companion object {
        fun newInstance(): ReadHistoryTimeFragment {
            return ReadHistoryTimeFragment()
        }
    }
}
