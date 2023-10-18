package com.recoveryrecord.surveyandroid.example.receiver;//package com.example.test;

import static com.recoveryrecord.surveyandroid.example.config.Config.UsingApp;
import static com.recoveryrecord.surveyandroid.example.config.Constants.CURRENT_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SCREEN_STATUS;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SCREEN_STATUS_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USER_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USER_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USING_APP_OR_NOT;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ScreenStateReceiver implements StreamGenerator {
    private static final String TAG = "Main";
    private ScreenStateReceiver.ScreenStateBroadcastReceiver mReceiver;
    private final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String device_id;
    private static String ScreenState = "Screen On";

    @SuppressLint("HardwareIds")
    public void registerScreenStateReceiver(Context context) {
        if (mReceiver == null) {
            mReceiver = new ScreenStateBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        context.registerReceiver(mReceiver, filter);
    }

    public static class ScreenStateBroadcastReceiver extends BroadcastReceiver {
        @SuppressLint("HardwareIds")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                ScreenState = "Screen On App";
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                ScreenState = "Screen leave App";
            }
            Map<String, Object> sensordb = new HashMap<>();
            sensordb.put(CURRENT_TIME, Timestamp.now());
            sensordb.put(SCREEN_STATUS, ScreenState);
            sensordb.put(USING_APP_OR_NOT, UsingApp);
            sensordb.put(USER_DEVICE_ID, device_id);
            sensordb.put(USER_ID, "");
            db.collection(SCREEN_STATUS_COLLECTION).add(sensordb);
        }
    }
    public void updateStream(Context context) {
        Map<String, Object> sensordb = new HashMap<>();
        sensordb.put(CURRENT_TIME, Timestamp.now());
        sensordb.put(SCREEN_STATUS, ScreenState);
        sensordb.put(USING_APP_OR_NOT, UsingApp);
        sensordb.put(USER_DEVICE_ID, device_id);
        sensordb.put(USER_ID, "");
        db.collection(SCREEN_STATUS_COLLECTION).add(sensordb);
    }
    public void unregisterScreenStateReceiver(Context context){
        if(mReceiver != null){
            context.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
}