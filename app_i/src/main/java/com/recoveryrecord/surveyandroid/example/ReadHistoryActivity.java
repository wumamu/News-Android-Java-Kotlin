package com.recoveryrecord.surveyandroid.example;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ReadHistoryActivity extends AppCompatActivity {

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("閱讀紀錄");
        setContentView(R.layout.activity_reading_history);

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
