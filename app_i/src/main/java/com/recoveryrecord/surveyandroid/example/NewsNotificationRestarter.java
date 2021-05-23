package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_VALUE_BOOT_UP;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_VALUE_FAILED_RESTART;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_VALUE_RESTART;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.TEST_USER_COLLECTION;

public class NewsNotificationRestarter extends BroadcastReceiver {
    @SuppressLint("HardwareIds")
    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d("Broadcast Listened", "Service tried to stop");
//        Log.d("lognewsselect", "onReceive");
//        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, NewsNotificationService.class));
        } else {
            context.startService(new Intent(context, NewsNotificationService.class));
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> log_service = new HashMap<>();
        log_service.put(NEWS_SERVICE_TIME, Timestamp.now());
        log_service.put(NEWS_SERVICE_STATUS_KEY, NEWS_SERVICE_STATUS_VALUE_RESTART);
        log_service.put(NEWS_SERVICE_CYCLE_KEY, NEWS_SERVICE_CYCLE_VALUE_FAILED_RESTART);
        db.collection(TEST_USER_COLLECTION)
                .document(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))
                .collection(NEWS_SERVICE_COLLECTION)
                .document(String.valueOf(Timestamp.now().toDate()))
                .set(log_service);
    }
}
