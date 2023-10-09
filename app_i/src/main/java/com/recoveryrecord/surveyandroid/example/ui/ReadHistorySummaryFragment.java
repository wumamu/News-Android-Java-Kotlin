package com.recoveryrecord.surveyandroid.example.ui;

import static com.recoveryrecord.surveyandroid.example.Constants.READ_TOTAL;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.recoveryrecord.surveyandroid.example.R;

import java.util.ArrayList;

public class ReadHistorySummaryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private PieChart pieChart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ReadHistorySummaryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ReadHistorySummaryFragment newInstance() {
        ReadHistorySummaryFragment fragment = new ReadHistorySummaryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




//        Log.d("lognewsselect", "chinatimes" + chinatimes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_read_history_summary, container, false);
        pieChart = view.findViewById(R.id.activity_main_piechart);
        setupPieChart();
        loadPieChartData();

//        return inflater.inflate(R.layout.activity_read_history_summary, container, false);
//        l.setCustom(lentries);
        //return inflater.inflate(R.layout.fragment_maths, container, false);
        return view;
    }
    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("各媒體閱讀總計");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setTextSize(15f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        int chinatimes = sharedPrefs.getInt(READ_TOTAL + "chinatimes", 0);
        int cna = sharedPrefs.getInt(READ_TOTAL + "cna", 0);
        int cts = sharedPrefs.getInt(READ_TOTAL + "cts", 0);
        int ebc = sharedPrefs.getInt(READ_TOTAL + "ebc", 0);
        int ltn = sharedPrefs.getInt(READ_TOTAL + "ltn", 0);
        int storm = sharedPrefs.getInt(READ_TOTAL + "storm", 0);
        int udn = sharedPrefs.getInt(READ_TOTAL + "udn", 0);
        int ettoday = sharedPrefs.getInt(READ_TOTAL + "ettoday", 0);
        int setn = sharedPrefs.getInt(READ_TOTAL + "setn", 0);

        ArrayList<PieEntry> entries = new ArrayList<>();
        if(chinatimes!=0){
            entries.add(new PieEntry(chinatimes, "中時"));
        }
        if(cna!=0){
            entries.add(new PieEntry(cna, "中央社"));
        }
        if(cts!=0){
            entries.add(new PieEntry(cts, "華視"));
        }
        if(ebc!=0){
            entries.add(new PieEntry(ebc, "東森"));
        }
        if(ltn!=0){
            entries.add(new PieEntry(ltn, "自由時報"));
        }
        if(storm!=0){
            entries.add(new PieEntry(storm, "風傳媒"));
        }
        if(udn!=0){
            entries.add(new PieEntry(udn, "聯合"));
        }
        if(ettoday!=0){
            entries.add(new PieEntry(ettoday, "ETTODAY"));
        }
        if(setn!=0){
            entries.add(new PieEntry(setn, "三立"));
        }











        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
}