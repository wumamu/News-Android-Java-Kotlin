package com.recoveryrecord.surveyandroid.example.ui.loadingpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.recoveryrecord.surveyandroid.example.ui.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d ("Servicestatus", getPackageName());
        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        finish();
    }
}
