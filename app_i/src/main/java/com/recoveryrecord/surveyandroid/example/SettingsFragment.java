package com.recoveryrecord.surveyandroid.example;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    Intent mServiceIntent;
    private NewsNotificationService mYourService;
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
                    getActivity().getApplicationContext().startService(intent);
                    Toast.makeText(getActivity().getApplicationContext(), "可能會突然迸出很多通知xd", Toast.LENGTH_SHORT).show();
//                    if (!isMyServiceRunning(mYourService.getClass())) {
//                        //is running
//                    } else {
//                        //dead
////                        Toast.makeText(this, "service running", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(getActivity().getApplicationContext(), NewsNotificationService.class);
//                        getActivity().getApplicationContext().startService(intent);
//                        Toast.makeText(getActivity().getApplicationContext(), "可能會突然迸出很多通知xd", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    Log.d("mysetting", "service stop");
                    Intent intent = new Intent(getActivity().getApplicationContext(), NewsNotificationService.class);
                    getActivity().getApplicationContext().stopService(intent);
                }
                return true;
            }
        });
    }
//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) mYourService.getSystemService();
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                Log.d ("Servicestatus", "Running");
//                return true;
//            }
//        }
//        Log.d ("Servicestatus", "Not running");
//        return false;
//    }

}