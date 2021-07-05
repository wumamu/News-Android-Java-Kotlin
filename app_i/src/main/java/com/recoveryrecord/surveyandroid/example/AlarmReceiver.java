package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.DbHelper.DiaryDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.ESMDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.PushNewsDbHelper;
import com.recoveryrecord.surveyandroid.example.sqlite.Diary;
import com.recoveryrecord.surveyandroid.example.sqlite.ESM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import static android.content.Context.ALARM_SERVICE;
import static com.recoveryrecord.surveyandroid.example.Constants.*;
//import static com.recoveryrecord.surveyandroid.example.Constants.DAIRY_ALARM_ACTION;
//import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_PARCELABLE;
//import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_NOT_IN_PUSH_RANGE;
//import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_OUT_OF_INTERVAL_LIMIT;
//import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_PUSH;
//import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_STATUS;
//import static com.recoveryrecord.surveyandroid.example.Constants.ESM_ID;
//import static com.recoveryrecord.surveyandroid.example.Constants.ESM_NOT_IN_PUSH_RANGE;
//import static com.recoveryrecord.surveyandroid.example.Constants.ESM_OUT_OF_INTERVAL_LIMIT;
//import static com.recoveryrecord.surveyandroid.example.Constants.ESM_PUSH;
//import static com.recoveryrecord.surveyandroid.example.Constants.ESM_SCHEDULE_SOURCE;
//import static com.recoveryrecord.surveyandroid.example.Constants.ESM_STATUS;


public class AlarmReceiver extends BroadcastReceiver {
    String device_id = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String esm_status = "NA", diary_status = "NA";
    Intent mServiceIntent;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean set_once = sharedPrefs.getBoolean(ESM_SET_ONCE, false);
        String action = intent.getAction();
//        Log.d("AlarmReceiver", action);
        if(action.equals(CHECK_SERVICE_ACTION)){
            isServiceAlive(context);
        }
        if(action.equals(ESM_ALARM_ACTION) && set_once) {
            String esm_name = "NA", esm_source = "NA";
            if (intent.getExtras().getString(ESM_SCHEDULE_ID) != null && intent.getExtras().getString(SCHEDULE_SOURCE) != null ) {
                esm_name = intent.getExtras().getString(ESM_SCHEDULE_ID);
                esm_source = intent.getExtras().getString(SCHEDULE_SOURCE);
            }
            PushNewsDbHelper dbHandler = new PushNewsDbHelper(context.getApplicationContext());
            Boolean exist_noti = false;
            Cursor cursor = dbHandler.checkNotiDataForESM(Timestamp.now().getSeconds());
            if (cursor.moveToFirst()) {
                while(!cursor.isAfterLast()) {
                    //returns true when cursor is at last row position
                    exist_noti = true;
                    break;
                }
                cursor.moveToNext();
            }
            if (!cursor.isClosed())  {
                cursor.close();
            }
            if(exist_noti){
                scheduleNotification_esm(context, getNotification_esm(context, esm_name, esm_source), 1000);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt(ESM_DELAY_COUNT, 0);
                editor.apply();
            } else {
                Toast.makeText(context, "暫無通知紀錄，10分鐘後重送", Toast.LENGTH_SHORT).show();
                int my_delay_count = sharedPrefs.getInt(ESM_DELAY_COUNT, 0);
                if(my_delay_count<3){
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putInt(ESM_DELAY_COUNT, my_delay_count+1);
                    editor.apply();
                    //delay 10 up 2 times
                    Intent intent_esm = new Intent(context, AlarmReceiver.class);
                    intent_esm.setAction(ESM_ALARM_ACTION);
                    PendingIntent pendingIntent_esm = PendingIntent.getBroadcast(context, 77, intent_esm, 0);
                    AlarmManager alarmManager_esm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    Calendar cal_esm = Calendar.getInstance();
                    cal_esm.add(Calendar.MINUTE, 10);
                    assert alarmManager_esm != null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager_esm.setExact(AlarmManager.RTC_WAKEUP, cal_esm.getTimeInMillis() , pendingIntent_esm);
                    }else {
                        alarmManager_esm.set(AlarmManager.RTC_WAKEUP, cal_esm.getTimeInMillis() , pendingIntent_esm);
                    }
                }

            }

        }
        if(action.equals(DIARY_ALARM_ACTION) && set_once){
            String diary_source = "NA";
            if (intent.getExtras().getString(SCHEDULE_SOURCE) != null ) {
                diary_source = intent.getExtras().getString(SCHEDULE_SOURCE);
            }
            scheduleNotification_diary(context, getNotification_diary(context, diary_source ),  1000 );
            //call next schedule
            Intent intent_schedule = new Intent(context, AlarmReceiver.class);
            intent_schedule.setAction(SCHEDULE_ALARM_ACTION);
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1000, intent_schedule, 0);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR, 2);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() , pendingIntent);
        }
        if(action.equals(SCHEDULE_ALARM_ACTION) && set_once){
            Map<String, Object> my_alarm = new HashMap<>();
            Date date = new Date(System.currentTimeMillis());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            my_alarm.put(SCHEDULE_ALARM_ACTION_TYPE, "schedule");
            my_alarm.put(SCHEDULE_ALARM_DEVICE_ID, device_id);
            db.collection(SCHEDULE_ALARM_COLLECTION)
                    .document(device_id + " schedule " + formatter.format(date))
                    .set(my_alarm);
            schedule_alarm(context);

        }
        if(action.equals(RESTART_ALARM_ACTION) && set_once){
            //cancel diary request code 100
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent updateDiaryServiceIntent = new Intent(context, AlarmReceiver.class);
            updateDiaryServiceIntent.setAction(DIARY_ALARM_ACTION);
            PendingIntent pendingUpdateIntent = PendingIntent.getService(context, 100, updateDiaryServiceIntent, 0);
            try {
                alarmManager.cancel(pendingUpdateIntent);
            } catch (Exception e) {
//                Log.e(TAG, "AlarmManager update was not canceled. " + e.toString());
            }
            //cancel esm request code 0-15
            for(int i=0; i<16;i++){
                Intent updateEsmServiceIntent = new Intent(context, AlarmReceiver.class);
                updateEsmServiceIntent.setAction(ESM_ALARM_ACTION);
                PendingIntent pendingEsmUpdateIntent = PendingIntent.getService(context, i, updateEsmServiceIntent, 0);
                try {
                    alarmManager.cancel(pendingEsmUpdateIntent);
                } catch (Exception e) {
//                Log.e(TAG, "AlarmManager update was not canceled. " + e.toString());
                }
            }
            Map<String, Object> my_alarm = new HashMap<>();
            Date date = new Date(System.currentTimeMillis());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            my_alarm.put(SCHEDULE_ALARM_ACTION_TYPE, "restart");
            my_alarm.put(SCHEDULE_ALARM_DEVICE_ID, device_id);
            db.collection(SCHEDULE_ALARM_COLLECTION)
                    .document(device_id + " restart " + formatter.format(date))
                    .set(my_alarm);
            //regenerate new alarm
            schedule_alarm(context);
        }
        if(action.equals(CANCEL_ALARM_ACTION)){
            //cancel diary request code 100
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent updateDiaryServiceIntent = new Intent(context, AlarmReceiver.class);
            updateDiaryServiceIntent.setAction(DIARY_ALARM_ACTION);
            PendingIntent pendingUpdateIntent = PendingIntent.getService(context, 100, updateDiaryServiceIntent, 0);
            try {
                alarmManager.cancel(pendingUpdateIntent);
            } catch (Exception e) {
//                Log.e(TAG, "AlarmManager update was not canceled. " + e.toString());
            }
            //cancel esm request code 0-15
            for(int i=0; i<16;i++){
                Intent updateEsmServiceIntent = new Intent(context, AlarmReceiver.class);
                updateEsmServiceIntent.setAction(ESM_ALARM_ACTION);
                PendingIntent pendingEsmUpdateIntent = PendingIntent.getService(context, i, updateEsmServiceIntent, 0);
                try {
                    alarmManager.cancel(pendingEsmUpdateIntent);
                } catch (Exception e) {
//                Log.e(TAG, "AlarmManager update was not canceled. " + e.toString());
                }
            }
            Map<String, Object> my_alarm = new HashMap<>();
            Date date = new Date(System.currentTimeMillis());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            my_alarm.put(SCHEDULE_ALARM_ACTION_TYPE, "cancel");
            my_alarm.put(SCHEDULE_ALARM_DEVICE_ID, device_id);
            db.collection(SCHEDULE_ALARM_COLLECTION)
                    .document(device_id + " cancel " + formatter.format(date))
                    .set(my_alarm);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void isServiceAlive(Context context) {
        Map<String, Object> service_check = new HashMap<>();
        NewsNotificationService mYourService = new NewsNotificationService();
        mServiceIntent = new Intent(context, NewsNotificationService.class);
        if (!isMyServiceRunning(mYourService.getClass(), context)) {
            context.startService(mServiceIntent);
            //restart service
            service_check.put(NEWS_SERVICE_STATUS_KEY, NEWS_SERVICE_STATUS_VALUE_RESTART);
        } else {
            service_check.put(NEWS_SERVICE_STATUS_KEY, NEWS_SERVICE_STATUS_VALUE_RUNNING);
        }
        service_check.put(NEWS_SERVICE_TIME, Timestamp.now());
        service_check.put(NEWS_SERVICE_CYCLE_KEY, NEWS_SERVICE_CYCLE_VALUE_ALARM);
        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        service_check.put(NEWS_SERVICE_DEVICE_ID, device_id);

        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

        db.collection(NEWS_SERVICE_COLLECTION)
            .document(device_id + " " + formatter.format(date))
            .set(service_check);
        add_ServiceChecker(context);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;//Running
            }
        }
        return false;//Not running
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void add_ServiceChecker(Context context) {
        try {
            Thread.sleep(200);
        } catch (Exception e) {

        }
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(CHECK_SERVICE_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(context, 60, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        long time_fired = System.currentTimeMillis() + SERVICE_CHECKER_INTERVAL;
        assert am != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, time_fired, pi);       //註冊鬧鐘
        } else {
            am.set(AlarmManager.RTC_WAKEUP, time_fired, pi);       //註冊鬧鐘
        }
//        am.setExact(AlarmManager.RTC_WAKEUP, time_fired, pi);       //註冊鬧鐘
    }
    private void schedule_alarm(Context context) {
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        //initial
        //schedule after diary receieve
        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Map<String, Object> my_alarm = new HashMap<>();
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        ArrayList<PendingIntent> intentEsmArray = new ArrayList<PendingIntent>();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int StartHour = sharedPrefs.getInt(ESM_START_TIME_HOUR, 9);
        int EndHour = sharedPrefs.getInt(ESM_END_TIME_HOUR, 21);
        int hour_interval = 12;
        if(EndHour==0){
            //StartHour 9 EndHour 0 -> 15
            hour_interval = 24-9;
        } else if(EndHour>StartHour){
            //StartHour 9 EndHour 21
            //8 23
            hour_interval = EndHour-StartHour;
        } else {
            //StartHour 11 EndHour 2 -> 15
            //in midnight
            hour_interval = 24 - StartHour + EndHour;
        }
        Calendar now_cal = Calendar.getInstance();

        //diary alarm
        Random r = new Random();
        int randomNumberDiary = r.nextInt(20) + 30;
        Calendar cal_diary = Calendar.getInstance();
        if(EndHour==0){
            //10 0
            cal_diary.set(Calendar.HOUR_OF_DAY, 23);

        } else if (EndHour<StartHour) {
            //11 2
            cal_diary.set(Calendar.HOUR_OF_DAY, EndHour-1);
        } else {
            //9 23
            cal_diary.set(Calendar.HOUR_OF_DAY, EndHour-1);
        }
        cal_diary.set(Calendar.MINUTE, randomNumberDiary);
        cal_diary.set(Calendar.SECOND, 0);
        if(!now_cal.before(cal_diary)){
            cal_diary.add(Calendar.HOUR, 24);
        }
        my_alarm.put("diary", cal_diary.getTime());
        Intent intent_diary = new Intent(context, AlarmReceiver.class);
        intent_diary.setAction(DIARY_ALARM_ACTION);
        intent_diary.putExtra(SCHEDULE_SOURCE, formatter.format(date));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent_diary, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_diary.getTimeInMillis() , pendingIntent);

        //esm alarm
        int max_esm = hour_interval*2/3;
        int sum = 0;
        for (int i=0;; i++){
            Calendar cal_esm = Calendar.getInstance();
            cal_esm.set(Calendar.HOUR_OF_DAY, 0);
            cal_esm.set(Calendar.MINUTE, 0);
            cal_esm.set(Calendar.MINUTE, 0);
            Random r_esm = new Random();
            int randomNumber_esm;//"配置鬧終於+sum 分鐘後
            if(i==0){
                randomNumber_esm = r_esm.nextInt(30);
            } else {
                randomNumber_esm = r_esm.nextInt(20);
            }
            sum+= randomNumber_esm;
            cal_esm.add(Calendar.MINUTE, sum);//"配置鬧終於+sum 分鐘後
            //generate same day alarm
//            Log.d("AlarmReceiver", "before " + cal_esm.getTime().toString());
            sum+=80;
            if(sum>=1440){
                break;//24*60
            }
            if(now_cal.before(cal_esm)){
                if(check_esm_time_cal(cal_esm, context)){
//                    Log.d("AlarmReceiver", "456" +  cal_esm.getTime().toString());
                    if(cal_esm.before(cal_diary)){
                        my_alarm.put(i + "_esm", cal_esm.getTime());
                        Intent intent_e = new Intent(context, AlarmReceiver.class);
                        intent_e.setAction(ESM_ALARM_ACTION);
                        intent_e.putExtra(ESM_SCHEDULE_ID, i + "_esm");
                        intent_e.putExtra(SCHEDULE_SOURCE, formatter.format(date));
                        PendingIntent pendingIntent_e = PendingIntent.getBroadcast(context, i, intent_e, 0);
                        intentEsmArray.add(pendingIntent_e);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_esm.getTimeInMillis() , pendingIntent_e);
                    }
                }
            } else {
                if(check_esm_time_cal(cal_esm, context)){
                    cal_esm.add(Calendar.HOUR, 24);//+24 hour
//                    Log.d("AlarmReceiver", "123" +  cal_esm.getTime().toString());
                    if(cal_esm.before(cal_diary)){
                        //otherwise the other turn
                        my_alarm.put(i + "_esm", cal_esm.getTime());
                        Intent intent_e = new Intent(context, AlarmReceiver.class);
                        intent_e.setAction(ESM_ALARM_ACTION);
                        intent_e.putExtra(ESM_SCHEDULE_ID, i + "_esm");
                        intent_e.putExtra(SCHEDULE_SOURCE, formatter.format(date));
                        PendingIntent pendingIntent_e = PendingIntent.getBroadcast(context, i, intent_e, 0);
                        intentEsmArray.add(pendingIntent_e);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_esm.getTimeInMillis() , pendingIntent_e);
                    }
                }
            }
        }
        my_alarm.put(SCHEDULE_ALARM_SURVEY_START, StartHour);
        my_alarm.put(SCHEDULE_ALARM_SURVEY_END, EndHour);
        my_alarm.put(SCHEDULE_ALARM_TRIGGER_TIME, Timestamp.now().toDate());
        my_alarm.put(SCHEDULE_ALARM_MAX_ESM, max_esm);
        my_alarm.put(SCHEDULE_ALARM_DEVICE_ID, device_id);
        db.collection(SCHEDULE_ALARM_COLLECTION)
                .document(device_id + " " + formatter.format(date))
                .set(my_alarm);
    }

    private boolean check_esm_time_cal(Calendar cal_esm, Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //下午12點 12 凌晨12 - 0
        int StartHour = sharedPrefs.getInt(ESM_START_TIME_HOUR, 9);
        int EndHour = sharedPrefs.getInt(ESM_END_TIME_HOUR, 21);
        if(EndHour==0){
            //StartHour 9 EndHour 0
            return cal_esm.get(Calendar.HOUR_OF_DAY) >= StartHour;
        } else if(EndHour>StartHour){
            //StartHour 9 EndHour 21
            //8 23
            return cal_esm.get(Calendar.HOUR_OF_DAY) >= StartHour && cal_esm.get(Calendar.HOUR_OF_DAY) < EndHour;
        } else {
            //StartHour 11 EndHour 2
            //in midnight
            return cal_esm.get(Calendar.HOUR_OF_DAY) >= StartHour || cal_esm.get(Calendar.HOUR_OF_DAY) < EndHour;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("HardwareIds")
    private Notification getNotification_esm(Context context, String esm_schedule_name, String esm_schedule_source){

        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String time_now = formatter.format(date);
        String esm_id = "";
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
        builder.setOngoing(true);
        Bundle extras = new Bundle();
        extras.putString(DOC_ID_KEY, esm_id);
        extras.putString(NOTIFICATION_TYPE_KEY, NOTIFICATION_TYPE_VALUE_ESM);
        builder.setExtras(extras);


        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //sql
        ESM myesm = new ESM();
        myesm.setKEY_DOC_ID(device_id + " " + esm_id);
        myesm.setKEY_DEVICE_ID(device_id);
        myesm.setKEY_USER_ID(sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
        myesm.setKEY_ESM_SCHEDULE_ID(esm_schedule_name);
        myesm.setKEY_ESM_SCHEDULE_SOURCE(esm_schedule_source);
        myesm.setKEY_NOTI_TIMESTAMP(Timestamp.now().getSeconds());
        ESMDbHelper dbHandler = new ESMDbHelper(context.getApplicationContext());
        dbHandler.insertPushESMDetailsCreate(myesm);
        //firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> esm = new HashMap<>();
        esm.put(PUSH_ESM_DOC_ID, device_id + " " + esm_id);
        esm.put(PUSH_ESM_DEVICE_ID, device_id);
        esm.put(PUSH_ESM_USER_ID, sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));

        esm.put(PUSH_ESM_TYPE, 0);//??
        esm.put(PUSH_ESM_NOTI_ARRAY, "NA");
        esm.put(PUSH_ESM_READ_ARRAY, "NA");

        esm.put(PUSH_ESM_SCHEDULE_ID, esm_schedule_name);//??
        esm.put(PUSH_ESM_SCHEDULE_SOURCE, esm_schedule_source);//??
        esm.put(PUSH_ESM_SAMPLE_TIME, 0);
        esm.put(PUSH_ESM_SAMPLE, 0);//??
        esm.put(PUSH_ESM_SAMPLE_ID, "NA");//??

        esm.put(PUSH_ESM_NOTI_TIME, Timestamp.now());
        esm.put(PUSH_ESM_RECEIEVE_TIME, 0);
        esm.put(PUSH_ESM_OPEN_TIME, 0);
        esm.put(PUSH_ESM_CLOSE_TIME, 0);
        esm.put(PUSH_ESM_SUBMIT_TIME, 0);
        esm.put(PUSH_ESM_REMOVE_TIME, 0);
        esm.put(PUSH_ESM_REMOVE_TYPE, "NA");

        esm.put(PUSH_ESM_RESULT, "NA");
        esm.put(PUSH_ESM_TARGET_NEWS_ID, "NA");
        esm.put(PUSH_ESM_TARGET_TITLE, "NA");
        esm.put(PUSH_ESM_TARGET_IN_TIME, "NA");
        esm.put(PUSH_ESM_TARGET_SITUATION, "NA");
        esm.put(PUSH_ESM_TARGET_PLACE, "NA");


        db.collection(PUSH_ESM_COLLECTION)
                .document(device_id + " " + esm_id)
                .set(esm);
        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification_esm(Context context, Notification notification, int delay) {
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
    private Notification getNotification_diary(Context context, String diary_schedule_source) {
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
        builder.setTimeoutAfter(DIARY_TIME_OUT);           //自動消失 120 min
        builder.setPriority(NotificationManager.IMPORTANCE_MAX);
        builder.setCategory(Notification.CATEGORY_REMINDER);
        builder.setOngoing(true);

        Bundle extras = new Bundle();
        extras.putString(DOC_ID_KEY, diary_id);
        extras.putString(NOTIFICATION_TYPE_KEY, NOTIFICATION_TYPE_VALUE_DIARY);
        builder.setExtras(extras);

        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //sql
        Diary mydiary = new Diary();
        mydiary.setKEY_DOC_ID(device_id + " " + diary_id);
        mydiary.setKEY_DEVICE_ID(device_id);
        mydiary.setKEY_USER_ID(sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
        mydiary.setKEY_DIARY_SCHEDULE_SOURCE(diary_schedule_source);
        mydiary.setKEY_NOTI_TIMESTAMP(Timestamp.now().getSeconds());
        DiaryDbHelper dbHandler = new DiaryDbHelper(context.getApplicationContext());
        dbHandler.insertPushDiaryDetailsCreate(mydiary);
        //firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> diary = new HashMap<>();
        diary.put(PUSH_DIARY_DOC_ID, device_id + " " + diary_id);
        diary.put(PUSH_DIARY_DEVICE_ID, device_id);
        diary.put(PUSH_DIARY_USER_ID, sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));

        diary.put(PUSH_DIARY_SCHEDULE_SOURCE, diary_schedule_source);
        diary.put(PUSH_DIARY_SAMPLE_TIME, 0);
        diary.put(PUSH_DIARY_OPTION, "NA");

        diary.put(PUSH_DIARY_NOTI_TIME, Timestamp.now());
        diary.put(PUSH_DIARY_RECEIEVE_TIME, 0);
        diary.put(PUSH_DIARY_OPEN_TIME, 0);
        diary.put(PUSH_DIARY_CLOSE_TIME, 0);
        diary.put(PUSH_DIARY_SUBMIT_TIME, 0);
        diary.put(PUSH_DIARY_REMOVE_TIME, 0);

        diary.put(PUSH_DIARY_REMOVE_TYPE, "NA");
        diary.put(PUSH_DIARY_RESULT, "NA");
        diary.put(PUSH_DIARY_INOPPORTUNE_TARGET, "NA");
        diary.put(PUSH_DIARY_OPPORTUNE_TARGET, "NA");


        db.collection(PUSH_DIARY_COLLECTION)
                .document(device_id + " " + diary_id)
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
