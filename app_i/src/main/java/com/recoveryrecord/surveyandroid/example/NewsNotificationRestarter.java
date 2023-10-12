//package com.recoveryrecord.surveyandroid.example;
//
//import static com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_SERVICE_COLLECTION;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_SERVICE_CYCLE_KEY;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_SERVICE_CYCLE_VALUE_FAILED_RESTART;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_SERVICE_DEVICE_ID;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_SERVICE_STATUS_KEY;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_SERVICE_STATUS_VALUE_RESTART;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_SERVICE_TIME;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.USER_COLLECTION;
//
//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.provider.Settings;
//import android.util.Log;
//
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//public class NewsNotificationRestarter extends BroadcastReceiver {
//    @SuppressLint("HardwareIds")
//    @Override
//    public void onReceive(Context context, Intent intent) {
////        Log.d("Broadcast Listened", "Service tried to stop");
////        Log.d("lognewsselect", "onReceive");
////        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(new Intent(context, NewsNotificationService.class));
//        } else {
//            context.startService(new Intent(context, NewsNotificationService.class));
//        }
//
//        Date date = new Date(System.currentTimeMillis());
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String, Object> log_service = new HashMap<>();
//        log_service.put(NEWS_SERVICE_TIME, Timestamp.now());
//        log_service.put(NEWS_SERVICE_STATUS_KEY, NEWS_SERVICE_STATUS_VALUE_RESTART);
//        log_service.put(NEWS_SERVICE_CYCLE_KEY, NEWS_SERVICE_CYCLE_VALUE_FAILED_RESTART);
//        @SuppressLint("HardwareIds")
//        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//        log_service.put(NEWS_SERVICE_DEVICE_ID, device_id);
//        db.collection(NEWS_SERVICE_COLLECTION)
//                .document(device_id + " " + formatter.format(date))
//                .set(log_service);
//
//        DocumentReference rbRef_check = db.collection(USER_COLLECTION).document(device_id);
//        rbRef_check.update("check_last_service", Timestamp.now())
//                .addOnSuccessListener(aVoid -> Log.d("log: firebase share", "DocumentSnapshot successfully updated!"))
//                .addOnFailureListener(e -> Log.w("log: firebase share", "Error updating document", e));
//    }
//}
