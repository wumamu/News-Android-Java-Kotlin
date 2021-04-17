package com.recoveryrecord.surveyandroid.example;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

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

//    public static class SettingsFragment extends PreferenceFragmentCompat {
//        @Override
//        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            setPreferencesFromResource(R.xml.root_preferences, rootKey);
//
//            SwitchPreferenceCompat switchPref = findPreference("news_notification");
//            switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object newValue) {
////                    getActivity().finish();
//                    if (newValue.equals(true)){
//                        Log.d("mysetting", "service on");
////                        Intent intent = new Intent(getActivity(), NewService.class);
////                        startService(intent);
//                    } else {
//                        Log.d("mysetting", "service stop");
////                        Intent intent = new Intent(getActivity(), NewService.class);
////                        stopService(intent);
//                    }
//                    return true;
//                }
//            });
//        }
//
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                Intent intent_back = new Intent(SettingsActivity.this, NewsHybridActivity.class);
                startActivity(intent_back);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}