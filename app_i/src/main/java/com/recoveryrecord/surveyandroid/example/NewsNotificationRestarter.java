package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
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

public class NewsNotificationRestarter extends BroadcastReceiver {
    @SuppressLint("HardwareIds")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Broadcast Listened", "Service tried to stop");
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, NewsNotificationService.class));
        } else {
            context.startService(new Intent(context, NewsNotificationService.class));
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> log_service = new HashMap<>();
        log_service.put("noti_timestamp", Timestamp.now());
        log_service.put("cycle", "destroy");
        log_service.put("status", "restart");
        db.collection("test_users")
                .document(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))
                .collection("notification_service")
                .document(String.valueOf(Timestamp.now()))
                .set(log_service);
    }
}
