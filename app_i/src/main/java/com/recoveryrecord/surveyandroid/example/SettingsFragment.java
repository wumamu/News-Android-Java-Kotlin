package com.recoveryrecord.surveyandroid.example;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
//        String model = "MODEL : " + Build.MODEL;
        SwitchPreferenceCompat switchPref = findPreference("news_notification");
        switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                    getActivity().finish();
                if (newValue.equals(true)){
                    Log.d("mysetting", "service on");
                        Intent intent = new Intent(getActivity().getApplicationContext(), NewsNotificationService.class);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        getActivity().getApplicationContext().startForegroundService(intent);
//                    } else {
//                        getActivity().getApplicationContext().startService(intent);
//                    }
                        getActivity().getApplicationContext().startService(intent);
                        Toast.makeText(getActivity().getApplicationContext(), "可能會突然迸出很多通知xd", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("mysetting", "service stop");
                    Intent intent = new Intent(getActivity().getApplicationContext(), NewsNotificationService.class);
                    getActivity().getApplicationContext().stopService(intent);
                }
                return true;
            }
        });
    }

}