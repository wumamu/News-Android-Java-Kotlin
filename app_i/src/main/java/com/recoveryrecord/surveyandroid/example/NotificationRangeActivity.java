package com.recoveryrecord.surveyandroid.example;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationRangeActivity extends AppCompatActivity {

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("通知時間調整");
        setContentView(R.layout.activity_notification_range);

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
