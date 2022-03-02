package com.recoveryrecord.surveyandroid.example;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class ReadHistoryTimeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private BarChart barChart;

    Calendar start_time, end_time;
    Long start_long, end_long;

    public ReadHistoryTimeFragment() {
        // Required empty public constructor
    }


    public static ReadHistoryTimeFragment newInstance() {

        return new ReadHistoryTimeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
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
        description.setText("NewsMoment螢幕使用分鐘數");

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void showBarChart(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "NewsMoment螢幕使用分鐘數";
        start_time = Calendar.getInstance();
        start_time.add(Calendar.DAY_OF_YEAR, -7);
        end_time = Calendar.getInstance();
        end_time.add(Calendar.DAY_OF_YEAR, -7);
        String packageName = getContext().getPackageName();

        //fit the data into a bar
        for (int i = 0; i < 7; i++) {
            start_time.add(Calendar.DAY_OF_YEAR, 1);
            end_time.add(Calendar.DAY_OF_YEAR, 1);
            start_time.set(Calendar.HOUR_OF_DAY, 0);
            start_time.set(Calendar.MINUTE, 0);
            start_time.set(Calendar.SECOND, 0);
            end_time.set(Calendar.HOUR_OF_DAY, 23);
            end_time.set(Calendar.MINUTE, 59);
            end_time.set(Calendar.SECOND, 59);
            start_long = start_time.getTimeInMillis();
            end_long = end_time.getTimeInMillis();
//            UsageStatsManager mUsageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
//            Map<String, UsageStats> lUsageStatsMap = mUsageStatsManager.queryAndAggregateUsageStats(start_long, end_long);
//
//            long totalTimeUsageInMillis = lUsageStatsMap.get("com.recoveryrecord.surveyandroid.example").getTotalTimeInForeground();
//            Log.d("123456789", getContext().getPackageName());
//            Log.d("123456789", String.valueOf(start_long));
//            Log.d("123456789", String.valueOf(end_long));
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
            Map<String, UsageStats> usageStats = mUsageStatsManager.queryAndAggregateUsageStats(start_long, end_long);
//            Map<String, UsageStats> usageStats = mUsageStatsManager.queryAndAggregateUsageStats(System.currentTimeMillis()-1000*300, System.currentTimeMillis());
//            Log.d("123456789", String.valueOf(usageStats));
            float totalTimeUsageInMillis;
            float displayMinute = 0;

            if(!usageStats.isEmpty() && usageStats.containsKey(packageName)){
                totalTimeUsageInMillis = usageStats.get(packageName).getTotalTimeInForeground();
//                float seconds = (int) (totalTimeUsageInMillis/1000);
                displayMinute = totalTimeUsageInMillis/60000;
            }
            entries.add(new BarEntry(i, new float[]{displayMinute}));
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
        return new int[]{getResourceColor(R.color.chart_color_chinatimes)//,
//                getResourceColor(R.color.chart_color_cna),
//                getResourceColor(R.color.chart_color_cts),
//                getResourceColor(R.color.chart_color_ebc),
//                getResourceColor(R.color.chart_color_ltn),
//                getResourceColor(R.color.chart_color_storm),
//                getResourceColor(R.color.chart_color_udn),
//                getResourceColor(R.color.chart_color_ettoday),
//                getResourceColor(R.color.chart_color_setn)
        };
    }

    private int getResourceColor(int resID){
        return getResources().getColor(resID);
    }

//    private String[] getStackLabels(){
//        return new String[]{getString(R.string.chart_label_newsmoment)};
//    }
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