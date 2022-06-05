package com.recoveryrecord.surveyandroid.example;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceDialogFragmentCompat;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_CLEAR_CACHE;

public class SettingsFragment extends PreferenceFragmentCompat {
    Intent mServiceIntent;
    private NewsNotificationService mYourService;
    private final Handler mHandler = new Handler();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
//        String model = "MODEL : " + Build.MODEL;
        SwitchPreferenceCompat switchPref = findPreference("block_esm_diary");
        Preference clearPref = findPreference(SHARE_PREFERENCE_CLEAR_CACHE);
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
                                Toast.makeText(getContext(), "清除成功正在重啟app", Toast.LENGTH_SHORT).show();

                                mHandler.postDelayed(new Runnable() {
                                    public void run() {
                                        System.exit(0);
                                    }
                                }, 3000);
//                                System.exit(0);
//                                Runtime.getRuntime().exit(0);
                            }})
                        .setNegativeButton("取消", null).show();
                return true;
            }
        });

        Preference clearPref2 = findPreference("notification_policy_access");
        clearPref2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // handle click here
                NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                        Intent intent = null;
                        intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "權限已經開啟", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Android 8 以下版本不需要開此權限", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        Preference clearPref0 = findPreference("PhysicalActivity");
        clearPref0.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // handle click here
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
                    } else {
                        Toast.makeText(getContext(), "權限已經開啟", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Android 10 以下版本不需要開此權限", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        Preference clearPref1 = findPreference("CSV");

        clearPref1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // handle click here
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        Toast.makeText(getContext(), "權限已經開啟", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        Preference clearPref3 = findPreference("battery_optimization");

        clearPref3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @SuppressLint("BatteryLife")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // handle click here
                Intent intent = new Intent();
                String packageName = getContext().getPackageName();
                PowerManager pm = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                } else {
//                    intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    Toast.makeText(getContext(), "權限已經開啟", Toast.LENGTH_SHORT).show();
                }

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        // Permission is not granted
//                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                    } else {
//                        Toast.makeText(getContext(), "權限已經開啟", Toast.LENGTH_SHORT).show();
//                    }
//                }
                return true;
            }
        });
//        switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
////                    getActivity().finish();
//                if (newValue.equals(true)){
////                    Log.d("mysetting", "service on");
////                    Intent intent = new Intent(getActivity().getApplicationContext(), NewsNotificationService.class);
////                    getActivity().getApplicationContext().startService(intent);
//                    Toast.makeText(getActivity().getApplicationContext(), "您之後將不會再收到任何問卷通知", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(getActivity().getApplicationContext(), "欲觸發問卷請到問卷推播頁面將開關重新開啟", Toast.LENGTH_SHORT).show();
////                    Log.d("mysetting", "service stop");
////                    Intent intent = new Intent(getActivity().getApplicationContext(), NewsNotificationService.class);
////                    getActivity().getApplicationContext().stopService(intent);
//                }
//                return true;
//            }
//        });
    }


}