package com.recoveryrecord.surveyandroid.example.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.config.Constants.READ_TOTAL
import com.recoveryrecord.surveyandroid.example.model.MediaType

class ReadHistorySummaryFragment : Fragment() {
    private lateinit var pieChart: PieChart

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            mParam1 = getString(ARG_PARAM1)
            mParam2 = getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.activity_read_history_summary, container, false)
        pieChart = view.findViewById(R.id.activity_main_piechart)
        setupPieChart()
        loadPieChartData()
        return view
    }

    private fun setupPieChart() {
        pieChart.apply {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(12f)
            setCenterTextSize(24f)
            setEntryLabelColor(Color.BLACK)
            centerText = "各媒體閱讀總計"
            description.isEnabled = false
        }

        pieChart.legend.apply {
            textSize = 15f
            form = Legend.LegendForm.CIRCLE
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            orientation = Legend.LegendOrientation.VERTICAL
            setDrawInside(false)
            isEnabled = true
        }
    }

    private fun loadPieChartData() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val chinatimes = sharedPrefs.getInt(READ_TOTAL + "chinatimes", 0)
        val cna = sharedPrefs.getInt(READ_TOTAL + "cna", 0)
        val cts = sharedPrefs.getInt(READ_TOTAL + "cts", 0)
        val ebc = sharedPrefs.getInt(READ_TOTAL + "ebc", 0)
        val ltn = sharedPrefs.getInt(READ_TOTAL + "ltn", 0)
        val storm = sharedPrefs.getInt(READ_TOTAL + "storm", 0)
        val udn = sharedPrefs.getInt(READ_TOTAL + "udn", 0)
        val ettoday = sharedPrefs.getInt(READ_TOTAL + "ettoday", 0)
        val setn = sharedPrefs.getInt(READ_TOTAL + "setn", 0)
        val entries = ArrayList<PieEntry>()
        if (chinatimes != 0) {
            entries.add(PieEntry(chinatimes.toFloat(), MediaType.Chinatimes.chineseId))
        }
        if (cna != 0) {
            entries.add(PieEntry(cna.toFloat(), MediaType.Cna.chineseId))
        }
        if (cts != 0) {
            entries.add(PieEntry(cts.toFloat(), MediaType.Cts.chineseId))
        }
        if (ebc != 0) {
            entries.add(PieEntry(ebc.toFloat(), MediaType.Ebc.chineseId))
        }
        if (ltn != 0) {
            entries.add(PieEntry(ltn.toFloat(), MediaType.Ltn.chineseId))
        }
        if (storm != 0) {
            entries.add(PieEntry(storm.toFloat(), MediaType.Storm.chineseId))
        }
        if (udn != 0) {
            entries.add(PieEntry(udn.toFloat(), MediaType.Udn.chineseId))
        }
        if (ettoday != 0) {
            entries.add(PieEntry(ettoday.toFloat(), MediaType.Ettoday.chineseId))
        }
        if (setn != 0) {
            entries.add(PieEntry(setn.toFloat(), MediaType.Setn.chineseId))
        }
        val colors = ArrayList<Int>()
        for (color in ColorTemplate.MATERIAL_COLORS) colors.add(color)
        for (color in ColorTemplate.VORDIPLOM_COLORS) colors.add(color)

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        val newsData =
            PieData(dataSet).apply {
                setDrawValues(true)
                setValueFormatter(PercentFormatter())
                setValueTextSize(12f)
                setValueTextColor(Color.BLACK)
            }

        pieChart.apply {
            data = newsData
            invalidate()
            animateY(1400, Easing.EaseInOutQuad)
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        // TODO: Rename and change types and number of parameters
        fun newInstance(): ReadHistorySummaryFragment {
            return ReadHistorySummaryFragment()
        }
    }
}
