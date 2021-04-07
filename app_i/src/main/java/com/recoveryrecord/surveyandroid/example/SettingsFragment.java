package com.recoveryrecord.surveyandroid.example;

import android.app.Activity;
import android.content.Intent;
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

        SwitchPreferenceCompat switchPref = findPreference("news_notification");
        switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                    getActivity().finish();
                if (newValue.equals(true)){
                    Log.d("mysetting", "service on");
                        Intent intent = new Intent(getActivity().getApplicationContext(), NewService.class);
                        getActivity().getApplicationContext().startService(intent);
                        Toast.makeText(getActivity().getApplicationContext(), "可能會突然迸出很多通知xd", Toast.LENGTH_SHORT).show();
//                        startService(new Intent( this, NewService.class ) );
                } else {
                    Log.d("mysetting", "service stop");
                    Intent intent = new Intent(getActivity().getApplicationContext(), NewService.class);
                    getActivity().getApplicationContext().stopService(intent);
                }
                return true;
            }
        });
    }

}