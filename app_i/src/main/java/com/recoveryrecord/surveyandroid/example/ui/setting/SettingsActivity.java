package com.recoveryrecord.surveyandroid.example.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.recoveryrecord.surveyandroid.example.NewsHybridActivity;
import com.recoveryrecord.surveyandroid.example.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                Intent intent_back = new Intent(SettingsActivity.this, NewsHybridActivity.class);
                startActivity(intent_back);
                return true;
            default:
//                Intent intent_backb = new Intent(SettingsActivity.this, NewsHybridActivity.class);
//                startActivity(intent_backb);
//                return true;
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent_back = new Intent(SettingsActivity.this, NewsHybridActivity.class);
        startActivity(intent_back);
    }
}