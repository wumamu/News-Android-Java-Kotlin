package com.recoveryrecord.surveyandroid.example;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class UploadPagesActivity extends AppCompatActivity {
    Boolean set_once = false;
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("資料上傳頁面");
        setContentView(R.layout.activity_upload);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        set_once = sharedPrefs.getBoolean(ESM_SET_ONCE, false);


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
