package com.recoveryrecord.surveyandroid.example.ui.history;

import static com.recoveryrecord.surveyandroid.example.config.Constants.READ_TOTAL;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.Timestamp;
import com.recoveryrecord.surveyandroid.example.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ReadHistoryWeeklyFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private BarChart barChart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ReadHistoryWeeklyFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ReadHistoryWeeklyFragment newInstance() {
        ReadHistoryWeeklyFragment fragment = new ReadHistoryWeeklyFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_read_history_weekly, container, false);
        barChart  = view.findViewById(R.id.barChart_view);
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(false);
        //remove the description label text located at the lower right corner
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        barChart.animateY(1000);
        //setting animation for x-axis, the bar will pop up separately within the time we set
//        barChart.animateX(1000);
        //start from 0
        barChart.getAxisLeft().setAxisMinimum(0);
        //set label

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Timestamp.now().toDate());

        calendar.add(Calendar.DAY_OF_YEAR, -6);
        ArrayList<String> xAxisLabel = new ArrayList<>();
        for(int i=0; i<7; i++){
            xAxisLabel.add((calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
//        barChart.getXAxis().setTextSize(5);
        XAxis xAxis = barChart.getXAxis();
        //change the position of x-axis to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(true);//label 上一條橫線
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false);//穿越bar的線

        YAxis leftAxis = barChart.getAxisLeft();
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);
        YAxis rightAxis = barChart.getAxisRight();
        //hiding the right y-axis line, default true if not set
        rightAxis.setEnabled(false);
        rightAxis.setDrawAxisLine(false);

        Legend l = barChart.getLegend();
        l.setTextSize(15f);
        l.setTextColor(Color.BLACK);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setWordWrapEnabled(true);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);


        showBarChart();


        //return inflater.inflate(R.layout.fragment_maths, container, false);
        return view;
    }

    private void showBarChart(){
        ArrayList<Integer> valueList = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "";

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(Timestamp.now().toDate());
        int day_index = calendar.get(Calendar.DAY_OF_YEAR);
//        int seven = sharedPrefs.getInt(READ_TOTAL + day_index, 0);
//        int six = sharedPrefs.getInt(READ_TOTAL + (day_index-1), 0);
//        int five = sharedPrefs.getInt(READ_TOTAL + (day_index-2), 0);
//        int four = sharedPrefs.getInt(READ_TOTAL + (day_index-3), 0);
//        int three = sharedPrefs.getInt(READ_TOTAL + (day_index-4), 0);
//        int two = sharedPrefs.getInt(READ_TOTAL + (day_index-5), 0);
//        int one = sharedPrefs.getInt(READ_TOTAL + (day_index-6), 0);
//        valueList.add(one);
//        valueList.add(two);
//        valueList.add(three);
//        valueList.add(four);
//        valueList.add(five);
//        valueList.add(six);
//        valueList.add(seven);

        //fit the data into a bar
        for (int i = 0; i < 7; i++) {
//            BarEntry barEntry = new BarEntry(i, valueList.get(i));
//            entries.add(barEntry);
//            float revenue_US = i*5;
//            float revenue_JP = i*2;
//            float revenue_KR = i*2;
//            float revenue_Other = i*4;
            Log.d("lognewsselect", "index" + (day_index - (6-i)));
            float chinatimes = sharedPrefs.getInt(READ_TOTAL + (day_index - (6-i)) + "chinatimes", 0);
            float cna = sharedPrefs.getInt(READ_TOTAL + (day_index - (6-i)) + "cna", 0);
            float cts = sharedPrefs.getInt(READ_TOTAL + (day_index - (6-i)) + "cts", 0);
            float ebc = sharedPrefs.getInt(READ_TOTAL + (day_index - (6-i)) + "ebc", 0);
            float ltn = sharedPrefs.getInt(READ_TOTAL + (day_index - (6-i)) + "ltn", 0);
            float storm = sharedPrefs.getInt(READ_TOTAL + (day_index - (6-i)) + "storm", 0);
            float udn = sharedPrefs.getInt(READ_TOTAL + (day_index - (6-i)) + "udn", 0);
            float ettoday = sharedPrefs.getInt(READ_TOTAL + (day_index - (6-i)) + "ettoday", 0);
            float setn = sharedPrefs.getInt(READ_TOTAL + (day_index - (6-i)) + "setn", 0);
            entries.add(new BarEntry(i, new float[]{chinatimes, cna, cts, ebc, ltn, storm, udn, ettoday, setn}));
        }

        BarDataSet barDataSet = new BarDataSet(entries, title);
        //隱藏資料上方數值： 資料數值顯示與否與資料有關，所以相關設定在BarData裡

        //設定顏色
        barDataSet.setColors(getChartColors());
        //設定顯示字串
        barDataSet.setStackLabels(getStackLabels());

        BarData data = new BarData(barDataSet);
        data.setValueTextColor(Color.BLACK);
        data.setBarWidth(0.7f);
        data.setHighlightEnabled(false);


        //origin
//        BarData data = new BarData(barDataSet);
//        data.setDrawValues(false);

        barChart.setData(data);
        barChart.invalidate();

    }

    private int[] getChartColors() {
        int[] colors = new int[]{getResourceColor(R.color.chart_color_chinatimes),
                getResourceColor(R.color.chart_color_cna),
                getResourceColor(R.color.chart_color_cts),
                getResourceColor(R.color.chart_color_ebc),
                getResourceColor(R.color.chart_color_ltn),
                getResourceColor(R.color.chart_color_storm),
                getResourceColor(R.color.chart_color_udn),
                getResourceColor(R.color.chart_color_ettoday),
                getResourceColor(R.color.chart_color_setn)};
        return colors;
    }

    private int getResourceColor(int resID){
        return getResources().getColor(resID);
    }

    private String[] getStackLabels(){
//        return new String[]{getString(R.string.chart_label_Others),
//                getString(R.string.chart_label_KR),
//                getString(R.string.chart_label_JP),
//                getString(R.string.chart_label_US)};
        return new String[]{getString(R.string.chart_label_chinatimes),
                getString(R.string.chart_label_cna),
                getString(R.string.chart_label_cts),
                getString(R.string.chart_label_ebc),
                getString(R.string.chart_label_ltn),
                getString(R.string.chart_label_storm),
                getString(R.string.chart_label_udn),
                getString(R.string.chart_label_ettoday),
                getString(R.string.chart_label_setn)};
    }

}