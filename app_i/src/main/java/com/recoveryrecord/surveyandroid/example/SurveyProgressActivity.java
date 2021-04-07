package com.recoveryrecord.surveyandroid.example;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SurveyProgressActivity extends AppCompatActivity {

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("問卷進度");
        setContentView(R.layout.activity_survey_progress);

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
