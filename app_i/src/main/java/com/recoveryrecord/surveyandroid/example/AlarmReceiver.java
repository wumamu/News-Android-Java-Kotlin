package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_NOTIFICATION_ID;
//import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_PARCELABLE;
import static com.recoveryrecord.surveyandroid.example.Constants.DOC_ID_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_END_TIME_HOUR;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_INTERVAL;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_NOTIFICATION_CONTENT_TEXT;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_NOTIFICATION_CONTENT_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_START_TIME_HOUR;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TIME_OUT;
import static com.recoveryrecord.surveyandroid.example.Constants.LAST_ESM_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_ESM;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOTI_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_SAMPLE;
import static com.recoveryrecord.surveyandroid.example.Constants.TEST_USER_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.VIBRATE_EFFECT;

public class AlarmReceiver extends BroadcastReceiver {
    String device_id = "";
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        //send esm
        if(check_esm_time_range(context)){
//            select_news();
            Log.d("lognewsselect", "check_daily_time_range success");
            try {
                scheduleNotification_esm(context, getNotification_esm(context, "Please fill out the questionnaire" ), 1000);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    Log.d("lognewsselect", "check_daily_time_range success");
//                    // Actions to do after 10 seconds
//                    try {
//                        Log.d("lognewsselect", "check_daily_time_range success");
////                        scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 1000);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 3*1000);

        } else {
            Log.d("lognewsselect", "check_daily_time_range failed");

        }
    }

    private boolean check_esm_time_range(Context context){
        Long now = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
//        int MinHour = 9;//09:00:00
//        int MaxHour = 22;//23:00:00
        int MinHour = sharedPrefs.getInt(ESM_START_TIME_HOUR, 9);
        int MaxHour = sharedPrefs.getInt(ESM_END_TIME_HOUR, 21);
        long LastEsmTime = sharedPrefs.getLong(LAST_ESM_TIME, 0L);
        boolean in_range = false;
        if(MaxHour>MinHour){
            //min 9 max 21
            if((c.get(Calendar.HOUR_OF_DAY) >= MinHour && c.get(Calendar.HOUR_OF_DAY) < MaxHour)){
                in_range = true;
            }
        } else {
            //min 11 max 2
            //in midnight
            if(((c.get(Calendar.HOUR_OF_DAY) >= MinHour || c.get(Calendar.HOUR_OF_DAY) < MaxHour))){
                in_range = true;
            }
        }
        if(in_range) {
            Log.d("lognewsselect", "in daily interval");
            if(now - LastEsmTime > ESM_INTERVAL){
                Log.d("lognewsselect", "in hour interval");
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putLong(LAST_ESM_TIME, now);
                editor.apply();
                return true;
            } else {
                Log.d("lognewsselect", "not in hour interval");
                return false;
            }
        } else {
            Log.d("lognewsselect", "not in interval");
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("HardwareIds")
    private Notification getNotification_esm(Context context, String content) throws JSONException {

        Date date = new Date(System.currentTimeMillis());
        String esm_id = "";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String time_now = formatter.format(date);
        esm_id = time_now;

        Intent intent_esm = new Intent();
//        intent_esm.setClass(NewsHybridActivity.this, SurveyActivity.class);
        intent_esm.setClass(context, ESMLoadingPageActivity.class);
        intent_esm.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent_esm.putExtra(ESM_ID, esm_id);
        int nid = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, nid, intent_esm, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, DEFAULT_ESM_CHANNEL_ID);
        builder.setContentTitle(ESM_NOTIFICATION_CONTENT_TITLE);
        builder.setContentText(ESM_NOTIFICATION_CONTENT_TEXT);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setChannelId(ESM_CHANNEL_ID);
        builder.setVibrate(VIBRATE_EFFECT);              //震動模式
        builder.setTimeoutAfter(ESM_TIME_OUT);           //自動消失 15*60*1000
        builder.setPriority(NotificationManager.IMPORTANCE_MAX);
        builder.setCategory(Notification.CATEGORY_REMINDER);
        Bundle extras = new Bundle();
        extras.putString(DOC_ID_KEY, esm_id);
        extras.putString(NOTIFICATION_TYPE_KEY, NOTIFICATION_TYPE_VALUE_ESM);
        builder.setExtras(extras);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Map<String, Object> esm = new HashMap<>();
        esm.put(PUSH_ESM_NOTI_TIME, Timestamp.now());
        esm.put(PUSH_ESM_SAMPLE, 0);
        db.collection(TEST_USER_COLLECTION)
                .document(device_id)
                .collection(PUSH_ESM_COLLECTION)
                .document(esm_id)
                .set(esm);
        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification_esm (Context context, Notification notification, int delay) {
        Intent notificationIntent = new Intent(context, NotificationListenerNews.class);
        notificationIntent.putExtra(DEFAULT_ESM_NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(DEFAULT_ESM_NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
