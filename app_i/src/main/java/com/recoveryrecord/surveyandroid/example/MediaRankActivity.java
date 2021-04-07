package com.recoveryrecord.surveyandroid.example;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MediaRankActivity extends AppCompatActivity {

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("首頁媒體排序");
        setContentView(R.layout.activity_media_rank);

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
