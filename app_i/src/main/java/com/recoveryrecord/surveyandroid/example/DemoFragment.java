package com.recoveryrecord.surveyandroid.example;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class DemoFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource

        setPreferencesFromResource(R.xml.demo_preference, rootKey);
    }
}
