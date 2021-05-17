package com.recoveryrecord.surveyandroid.example;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_DAY_DONE_PREFIX;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_DAY_PUSH_PREFIX;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_DONE_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_PUSH_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_DAY_DONE_PREFIX;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_DAY_PUSH_PREFIX;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_DONE_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_PUSH_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_SET_ONCE;

public class SurveyProgressActivity extends AppCompatActivity {
    Boolean set_once = false;
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("問卷進度");
        setContentView(R.layout.activity_survey_progress);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        set_once = sharedPrefs.getBoolean(ESM_SET_ONCE, false);
        if(set_once){
            Calendar calendar = Calendar.getInstance();
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            String time_now = formatter.format(date);
            List<String> tmp = new ArrayList<String>(Arrays.asList(time_now.split(" ")));
            int day_index = calendar.get(Calendar.DAY_OF_YEAR);


            int esm_push_total = sharedPrefs.getInt(ESM_PUSH_TOTAL, 0);
            int esm_done_total = sharedPrefs.getInt(ESM_DONE_TOTAL, 0);
            int esm_day_push = sharedPrefs.getInt(ESM_DAY_PUSH_PREFIX + day_index, 0);
            int esm_day_done = sharedPrefs.getInt(ESM_DAY_DONE_PREFIX + day_index, 0);
            int diary_push_total = sharedPrefs.getInt(DIARY_PUSH_TOTAL, 0);
            int diary_done_total = sharedPrefs.getInt(DIARY_DONE_TOTAL, 0);
            int diary_day_push = sharedPrefs.getInt(DIARY_DAY_PUSH_PREFIX + day_index, 0);
            int diary_day_done = sharedPrefs.getInt(DIARY_DAY_DONE_PREFIX + day_index, 0);
            TextView esmSumTextView = new TextView(this);
            TextView diarySumTextView = new TextView(this);
            TextView esmDailySumTextView = new TextView(this);
            TextView diaryDailySumTextView = new TextView(this);
            TextView dayTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            esmSumTextView.setText("總體esm問卷進度(已回答/總共)" + esm_done_total + "/" + esm_push_total);
            diarySumTextView.setText("總體回顧問卷進度(已回答/總共)" + diary_done_total + "/" + diary_push_total);
            esmDailySumTextView.setText("本日回顧問卷進度(已回答/總共)" + esm_day_done + "/" + esm_day_push);
            diaryDailySumTextView.setText("本日回顧問卷進度(已回答/總共)" + diary_day_done + "/" + diary_day_push);
            dayTextView.setText(tmp.get(0));
            esmSumTextView.setGravity(Gravity.CENTER);
            diarySumTextView.setGravity(Gravity.CENTER);
            esmDailySumTextView.setGravity(Gravity.CENTER);
            diaryDailySumTextView.setGravity(Gravity.CENTER);
            dayTextView.setGravity(Gravity.CENTER);
            esmSumTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            diarySumTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            esmDailySumTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            diaryDailySumTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

            ((LinearLayout) findViewById(R.id.layout_inside)).addView(dayTextView);
            ((LinearLayout) findViewById(R.id.layout_inside)).addView(esmDailySumTextView);
            ((LinearLayout) findViewById(R.id.layout_inside)).addView(diaryDailySumTextView);
            ((LinearLayout) findViewById(R.id.layout_inside)).addView(esmSumTextView);
            ((LinearLayout) findViewById(R.id.layout_inside)).addView(diarySumTextView);
        } else {
            TextView dynamicTextView = new TextView(this);
            Button dynamicButton = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(30, 10, 10, 10);
            dynamicTextView.setLayoutParams(params);
            dynamicTextView.setText("您尚未設定問券發送區間");
            dynamicTextView.setGravity(Gravity.CENTER);
            dynamicTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
//            rowTextView.setTextColor(Color.parseColor("black"));
//            rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, text_size);
            dynamicButton.setLayoutParams(params);
            dynamicButton.setText("點我去設定");
            dynamicButton.setGravity(Gravity.CENTER);
            dynamicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent();
                    intent.setClass(getApplicationContext(), NotificationRangeActivity.class);
                    startActivity(intent);
                }
            });
            ((LinearLayout) findViewById(R.id.layout_inside)).addView(dynamicTextView);
            ((LinearLayout) findViewById(R.id.layout_inside)).addView(dynamicButton);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
