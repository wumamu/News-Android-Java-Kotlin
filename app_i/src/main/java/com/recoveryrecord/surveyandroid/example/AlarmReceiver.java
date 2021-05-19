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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.ALARM_SERVICE_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_DIARY_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_DIARY_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_DIARY_NOTIFICATION_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_NOTIFICATION_ID;
//import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_PARCELABLE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_INTERVAL;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_NOTIFICATION_CONTENT_TEXT;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_NOTIFICATION_CONTENT_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_NOT_IN_PUSH_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_OUT_OF_INTERVAL_LIMIT;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_PUSH;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_STATUS;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_TIME_OUT;
import static com.recoveryrecord.surveyandroid.example.Constants.DOC_ID_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_END_TIME_HOUR;
//import static com.recoveryrecord.surveyandroid.example.Constants.ESM_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_END_TIME_MIN;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_INTERVAL;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_NOTIFICATION_CONTENT_TEXT;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_NOTIFICATION_CONTENT_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_NOT_IN_PUSH_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_OUT_OF_INTERVAL_LIMIT;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_PUSH;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_SET_ONCE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_START_TIME_HOUR;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_START_TIME_MIN;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_STATUS;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TIME_OUT;
import static com.recoveryrecord.surveyandroid.example.Constants.LAST_DIARY_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.LAST_ESM_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_TYPE_DIARY;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_TYPE_ESM;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_DIARY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_ESM;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_DONE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_NOTI_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_TRIGGER_BY;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_TRIGGER_BY_ALARM;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_TRIGGER_BY_SELF;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOTI_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_SAMPLE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TRIGGER_BY;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TRIGGER_BY_ALARM;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TRIGGER_BY_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.TEST_USER_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.VIBRATE_EFFECT;

public class AlarmReceiver extends BroadcastReceiver {
    String device_id = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String esm_status = "NA", diary_status = "NA";
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean set_once = sharedPrefs.getBoolean(ESM_SET_ONCE, false);
        Map<String, Object> log_service = new HashMap<>();
        log_service.put("service_timestamp", Timestamp.now());
        Random r=new Random();
        int randomNumber = r.nextInt(5);
        log_service.put("delay_min", randomNumber*3);
        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if(set_once){
            //send diary
            if(check_diary_time_range(context)){
                scheduleNotification_diary(context, getNotification_diary(context, "Please fill out the questionnaire" ), randomNumber * 3 * 60 *1000 + 1000 );
                log_service.put("diary", true);
            } else {
                log_service.put("diary", false);
            }
            if(check_esm_time_range(context)){//send esm
//            select_news();
                Log.d("lognewsselect", "check_daily_time_range success");
                scheduleNotification_esm(context, getNotification_esm(context, "Please fill out the questionnaire" ), randomNumber * 3 * 60 *1000 + 1000);
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
                log_service.put("esm", true);
            } else {
                log_service.put("esm", false);
            }
            log_service.put("start_hour", sharedPrefs.getInt(ESM_START_TIME_HOUR, 9));
            log_service.put("end_hour", sharedPrefs.getInt(ESM_END_TIME_HOUR, 21));
            log_service.put("start_min", sharedPrefs.getInt(ESM_START_TIME_MIN, 0));
            log_service.put("end_min", sharedPrefs.getInt(ESM_END_TIME_MIN, 0));
            log_service.put(DIARY_STATUS, diary_status);
            log_service.put(ESM_STATUS, esm_status);
            db.collection(TEST_USER_COLLECTION)
                    .document(device_id)
                    .collection(ALARM_SERVICE_COLLECTION)
                    .document(String.valueOf(Timestamp.now()))
                    .set(log_service);
        }

    }

    private boolean check_diary_time_range(Context context) {
        Long now = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        long LastDiaryTime = sharedPrefs.getLong(LAST_DIARY_TIME, 0L);
        boolean in_range = false;
        int EndHour = sharedPrefs.getInt(ESM_END_TIME_HOUR, 21);
        int low = 0;
        Log.d("lognewsselect", "current" + c.get(Calendar.HOUR_OF_DAY));
        if(EndHour==0){
            //下午11
            low = 23;
            if(c.get(Calendar.HOUR_OF_DAY)>=low){
                Log.d("lognewsselect", "diary in push interval");
                if(now - LastDiaryTime > DIARY_INTERVAL){
                    Log.d("lognewsselect", "diary in 23 hour interval");
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putLong(LAST_DIARY_TIME, now);
                    editor.apply();
                    in_range = true;
                    diary_status = DIARY_PUSH;
                } else {
                    diary_status = DIARY_OUT_OF_INTERVAL_LIMIT;
                    Log.d("lognewsselect", "diary not in 23 hour interval");
                }
            } else {
                diary_status = DIARY_NOT_IN_PUSH_RANGE;
                Log.d("lognewsselect", "diary not in push interval");
            }
        } else {
            low = EndHour-1;
            if(c.get(Calendar.HOUR_OF_DAY) >=low && c.get(Calendar.HOUR_OF_DAY) <EndHour){
                Log.d("lognewsselect", "diary in push interval");
                if(now - LastDiaryTime > DIARY_INTERVAL){
                    Log.d("lognewsselect", "diary in 23 hour interval");
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putLong(LAST_DIARY_TIME, now);
                    editor.apply();
                    in_range = true;
                    diary_status = DIARY_PUSH;
                } else {
                    diary_status = DIARY_OUT_OF_INTERVAL_LIMIT;
                    Log.d("lognewsselect", "diary not in 23 hour interval");
                }
            } else {
                diary_status = DIARY_NOT_IN_PUSH_RANGE;
                Log.d("lognewsselect", "diary not in push interval");
            }
        }


        return in_range;
    }

    private boolean check_esm_time_range(Context context){
        Long now = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //下午12點 12 凌晨12 - 0
        int StartHour = sharedPrefs.getInt(ESM_START_TIME_HOUR, 9);
        int EndHour = sharedPrefs.getInt(ESM_END_TIME_HOUR, 21);
        Log.d("lognewsselect", "MinHour" + StartHour);
        Log.d("lognewsselect", "MaxHour" + EndHour);
        long LastEsmTime = sharedPrefs.getLong(LAST_ESM_TIME, 0L);
        boolean in_range = false;
        if(EndHour==0){
            //StartHour 9 EndHour 0
            if((c.get(Calendar.HOUR_OF_DAY) >= StartHour)){
                in_range = true;
            }
        } else if(EndHour>StartHour){
            //StartHour 9 EndHour 21
            //8 23
            if((c.get(Calendar.HOUR_OF_DAY) >= StartHour && c.get(Calendar.HOUR_OF_DAY) < EndHour)){
                in_range = true;
            }
        } else {
            //StartHour 11 EndHour 2
            //in midnight
            if(((c.get(Calendar.HOUR_OF_DAY) >= StartHour || c.get(Calendar.HOUR_OF_DAY) < EndHour))){
                in_range = true;
            }
        }
        if(in_range) {
            Log.d("lognewsselect", "esm in daily interval");
            if(now - LastEsmTime > ESM_INTERVAL){
                Log.d("lognewsselect", "esm in hour interval");
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putLong(LAST_ESM_TIME, now);
                editor.apply();
                esm_status = ESM_PUSH;
                return true;
            } else {
                Log.d("lognewsselect", "esm not in hour interval");
                esm_status = ESM_OUT_OF_INTERVAL_LIMIT;
                return false;
            }
        } else {
            Log.d("lognewsselect", "esm not in interval");
            esm_status = ESM_NOT_IN_PUSH_RANGE;
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("HardwareIds")
    private Notification getNotification_esm(Context context, String content){

        Date date = new Date(System.currentTimeMillis());
        String esm_id = "";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String time_now = formatter.format(date);
        esm_id = time_now;

        Intent intent_esm = new Intent();
        intent_esm.setClass(context, ESMLoadingPageActivity.class);
        intent_esm.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent_esm.putExtra(LOADING_PAGE_ID, esm_id);
        intent_esm.putExtra(LOADING_PAGE_TYPE_KEY, LOADING_PAGE_TYPE_ESM);
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
        esm.put(PUSH_ESM_TRIGGER_BY, PUSH_ESM_TRIGGER_BY_ALARM);
        db.collection(TEST_USER_COLLECTION)
                .document(device_id)
                .collection(PUSH_ESM_COLLECTION)
                .document(esm_id)
                .set(esm);
        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification_esm (Context context, Notification notification, int delay) {
        Intent notificationIntent = new Intent(context, NotificationListenerESM.class);
        notificationIntent.putExtra(DEFAULT_ESM_NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(DEFAULT_ESM_NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private Notification getNotification_diary (Context context, String content) {
        Date date = new Date(System.currentTimeMillis());
        String diary_id = "";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String time_now = formatter.format(date);
        diary_id = time_now;

        Intent intent_diary = new Intent();
        intent_diary.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent_diary.setClass(context, DiaryLoadingPageActivity.class);
        intent_diary.putExtra(LOADING_PAGE_ID, diary_id);
        intent_diary.putExtra(LOADING_PAGE_TYPE_KEY, LOADING_PAGE_TYPE_DIARY);
        int nid = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, nid, intent_diary, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, DEFAULT_DIARY_CHANNEL_ID);
        builder.setContentTitle(DIARY_NOTIFICATION_CONTENT_TITLE);
        builder.setContentText(DIARY_NOTIFICATION_CONTENT_TEXT);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setChannelId(DIARY_CHANNEL_ID);
        builder.setVibrate(VIBRATE_EFFECT);              //震動模式
        builder.setTimeoutAfter(DIARY_TIME_OUT);           //自動消失 15*60*1000
        builder.setPriority(NotificationManager.IMPORTANCE_MAX);
        builder.setCategory(Notification.CATEGORY_REMINDER);
        Bundle extras = new Bundle();
        extras.putString(DOC_ID_KEY, diary_id);
        extras.putString(NOTIFICATION_TYPE_KEY, NOTIFICATION_TYPE_VALUE_DIARY);
        builder.setExtras(extras);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Map<String, Object> diary = new HashMap<>();
        diary.put(PUSH_DIARY_NOTI_TIME, Timestamp.now());
        diary.put(PUSH_DIARY_DONE, 0);
        diary.put(PUSH_DIARY_TRIGGER_BY, PUSH_DIARY_TRIGGER_BY_ALARM);
        db.collection(TEST_USER_COLLECTION)
                .document(device_id)
                .collection(PUSH_DIARY_COLLECTION)
                .document(diary_id)
                .set(diary);
        return builder.build();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification_diary (Context context, Notification notification, int delay) {
        Intent notificationIntent = new Intent(context, NotificationListenerDiary.class);
        notificationIntent.putExtra(DEFAULT_DIARY_NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(DEFAULT_DIARY_NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( context, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//        Log.d ("shit", "scheduleNotification_esm");
    }
}
