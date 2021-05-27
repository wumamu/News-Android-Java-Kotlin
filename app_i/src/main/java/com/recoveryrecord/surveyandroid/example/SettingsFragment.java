package com.recoveryrecord.surveyandroid.example;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceDialogFragmentCompat;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_CLEAR_CACHE;

public class SettingsFragment extends PreferenceFragmentCompat {
    Intent mServiceIntent;
    private NewsNotificationService mYourService;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
//        String model = "MODEL : " + Build.MODEL;
        SwitchPreferenceCompat switchPref = findPreference("news_notification");
        Preference clearPref = findPreference(SHARE_PREFERENCE_CLEAR_CACHE);
//        DialogPreference clearPref = findPreference("clear_cache");
//        clearPref.setDialogTitle("清除站存");
//        clearPref.setDialogMessage("請勿在非指示下刪除資料!");
//        clearPref.setPositiveButtonText("確認刪除");
//        clearPref.setNegativeButtonText("取消");

        clearPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // handle click here
                new AlertDialog.Builder(getContext())
                        .setTitle("清除本地端資料")
                        .setMessage("請勿在非指示下刪除資料!\n可能會影響app運行")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                                sharedPrefs.edit().clear().apply();
                                Toast.makeText(getContext(), "清除成功請重啟app", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton("取消", null).show();
                return true;
            }
        });
//        switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
////                    getActivity().finish();
//                if (newValue.equals(true)){
//                    Log.d("mysetting", "service on");
//                    Intent intent = new Intent(getActivity().getApplicationContext(), NewsNotificationService.class);
//                    getActivity().getApplicationContext().startService(intent);
//                    Toast.makeText(getActivity().getApplicationContext(), "可能會突然迸出很多通知xd", Toast.LENGTH_SHORT).show();
////                    if (!isMyServiceRunning(mYourService.getClass())) {
////                        //is running
////                    } else {
////                        //dead
//////                        Toast.makeText(this, "service running", Toast.LENGTH_LONG).show();
////                        Intent intent = new Intent(getActivity().getApplicationContext(), NewsNotificationService.class);
////                        getActivity().getApplicationContext().startService(intent);
////                        Toast.makeText(getActivity().getApplicationContext(), "可能會突然迸出很多通知xd", Toast.LENGTH_SHORT).show();
////                    }
//                } else {
//                    Log.d("mysetting", "service stop");
//                    Intent intent = new Intent(getActivity().getApplicationContext(), NewsNotificationService.class);
//                    getActivity().getApplicationContext().stopService(intent);
//                }
//                return true;
//            }
//        });
    }


}