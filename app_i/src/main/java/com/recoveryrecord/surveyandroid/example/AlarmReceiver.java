package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
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
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

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
import static com.recoveryrecord.surveyandroid.example.Constants.ALARM_SERVICE_COLLECTION;
//import static com.recoveryrecord.surveyandroid.example.Constants.DAIRY_ALARM_ACTION;
import static com.recoveryrecord.surveyandroid.example.Constants.CHECK_SERVICE_ACTION;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_DIARY_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_DIARY_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_DIARY_NOTIFICATION_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_NOTIFICATION_ID;
//import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_PARCELABLE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_ALARM_ACTION;
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
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_ALARM_ACTION;
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
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_VALUE_ALARM;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_VALUE_FAILED_RESTART;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_VALUE_MAIN_PAGE;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_VALUE_RESTART;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_VALUE_RUNNING;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_TIME;
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
import static com.recoveryrecord.surveyandroid.example.Constants.SCHEDULE_ALARM_ACTION;
import static com.recoveryrecord.surveyandroid.example.Constants.SCHEDULE_ALARM_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.SCHEDULE_ALARM_MAX_ESM;
import static com.recoveryrecord.surveyandroid.example.Constants.SCHEDULE_ALARM_SURVEY_END;
import static com.recoveryrecord.surveyandroid.example.Constants.SCHEDULE_ALARM_SURVEY_START;
import static com.recoveryrecord.surveyandroid.example.Constants.SCHEDULE_ALARM_TRIGGER_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.SERVICE_CHECKER_INTERVAL;
import static com.recoveryrecord.surveyandroid.example.Constants.TEST_USER_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.VIBRATE_EFFECT;

public class AlarmReceiver extends BroadcastReceiver {
    String device_id = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String esm_status = "NA", diary_status = "NA";
    Intent mServiceIntent;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean set_once = sharedPrefs.getBoolean(ESM_SET_ONCE, false);
        String action = intent.getAction();
//        Log.d("AlarmReceiver", action);
        if(action.equals(CHECK_SERVICE_ACTION)){
            isServiceAlive(context);
        }
        if(action.equals(ESM_ALARM_ACTION) && set_once) {
            scheduleNotification_esm(context, getNotification_esm(context, "Please fill out the questionnaire" ), 1000);
        }
        if(action.equals(DIARY_ALARM_ACTION) && set_once){
            scheduleNotification_diary(context, getNotification_diary(context, "Please fill out the questionnaire" ),  1000 );
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
//            Log.d("AlarmReceiver", action);
            schedule_alarm(context);
//            scheduleNotification_diary(context, getNotification_diary(context, "Please fill out the questionnaire" ),  1000 );
        }
//        if(set_once){
//            //send diary
//            if(check_diary_time_range(context)){
//                scheduleNotification_diary(context, getNotification_diary(context, "Please fill out the questionnaire" ), randomNumber * 3 * 60 *1000 + 1000 );
//                log_service.put("diary", true);
//            } else {
//                log_service.put("diary", false);
//            }
//            //send esm
//            if(check_esm_time_range(context)){
////            select_news();
//                Log.d("lognewsselect", "check_daily_time_range success");
//                scheduleNotification_esm(context, getNotification_esm(context, "Please fill out the questionnaire" ), randomNumber * 3 * 60 *1000 + 1000);
//                //            Handler handler = new Handler();
////            handler.postDelayed(new Runnable() {
////                public void run() {
////                    Log.d("lognewsselect", "check_daily_time_range success");
////                    // Actions to do after 10 seconds
////                    try {
////                        Log.d("lognewsselect", "check_daily_time_range success");
//////                        scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 1000);
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }, 3*1000);
//                log_service.put("esm", true);
//            } else {
//                log_service.put("esm", false);
//            }
//            log_service.put("start_hour", sharedPrefs.getInt(ESM_START_TIME_HOUR, 9));
//            log_service.put("end_hour", sharedPrefs.getInt(ESM_END_TIME_HOUR, 21));
//            log_service.put("start_min", sharedPrefs.getInt(ESM_START_TIME_MIN, 0));
//            log_service.put("end_min", sharedPrefs.getInt(ESM_END_TIME_MIN, 0));
//            log_service.put(DIARY_STATUS, diary_status);
//            log_service.put(ESM_STATUS, esm_status);
//            db.collection(TEST_USER_COLLECTION)
//                    .document(device_id)
//                    .collection(ALARM_SERVICE_COLLECTION)
//                    .document(String.valueOf(Timestamp.now()))
//                    .set(log_service);
//        }

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
//        Date date = new Date(System.currentTimeMillis());
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        db.collection(TEST_USER_COLLECTION)
            .document(device_id)
            .collection(NEWS_SERVICE_COLLECTION)
            .document(String.valueOf(Timestamp.now().toDate()))
//            .document(formatter.format(date))
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
        am.setExact(AlarmManager.RTC_WAKEUP, time_fired, pi);       //註冊鬧鐘
    }
    private void schedule_alarm(Context context) {
        //initial
        //schedule after diary receieve
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
//            Log.d("AlarmReceiver", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//                my_alarm.put("diary", tmp_cal.getTime());
        }
        my_alarm.put("diary", cal_diary.getTime());
        Intent intent_diary = new Intent(context, AlarmReceiver.class);
        intent_diary.setAction(DIARY_ALARM_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent_diary, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_diary.getTimeInMillis() , pendingIntent);

        //esm alarm
        int max_esm = hour_interval*2/3;
        int sum = 0;
//        Log.d("AlarmReceiver", now_cal.getTime().toString());
        for (int i=0; i< 16; i++){

            Calendar cal_esm = Calendar.getInstance();
            cal_esm.set(Calendar.HOUR_OF_DAY, 0);
            cal_esm.set(Calendar.MINUTE, 0);
            cal_esm.set(Calendar.SECOND, 0);
            Random r_esm = new Random();
            int randomNumber_esm;//"配置鬧終於+sum 分鐘後
            if(i==0){
                randomNumber_esm = r_esm.nextInt(30);
            } else {
                randomNumber_esm = r_esm.nextInt(20) + 20;
            }
            sum+= randomNumber_esm;
            cal_esm.add(Calendar.MINUTE, sum);//"配置鬧終於+sum 分鐘後

            //generate same day alarm
//            Log.d("AlarmReceiver", "before " + cal_esm.getTime().toString());
            sum+=60;
            if(now_cal.before(cal_esm)){
                if(check_esm_time_cal(cal_esm, context)){
//                    Log.d("AlarmReceiver", "456" +  cal_esm.getTime().toString());
                    if(cal_esm.before(cal_diary)){
                        my_alarm.put(i + "_esm", cal_esm.getTime());
                        Intent intent_e = new Intent(context, AlarmReceiver.class);
                        intent_e.setAction(ESM_ALARM_ACTION);
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
                        PendingIntent pendingIntent_e = PendingIntent.getBroadcast(context, i, intent_e, 0);
                        intentEsmArray.add(pendingIntent_e);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_esm.getTimeInMillis() , pendingIntent_e);
                    }

                }
            }
//            Log.d("AlarmReceiver", "after " + cal_esm.getTime().toString());

        }





        my_alarm.put(SCHEDULE_ALARM_SURVEY_START, StartHour);
        my_alarm.put(SCHEDULE_ALARM_SURVEY_END, EndHour);
        my_alarm.put(SCHEDULE_ALARM_TRIGGER_TIME, Timestamp.now().toDate());
        my_alarm.put(SCHEDULE_ALARM_MAX_ESM, max_esm);

        db.collection(TEST_USER_COLLECTION)
                .document(device_id)
                .collection(SCHEDULE_ALARM_COLLECTION)
                .document(String.valueOf(Timestamp.now().toDate()))
                .set(my_alarm);
    }

    private boolean check_esm_time_cal(Calendar cal_esm, Context context) {
//        Long now = System.currentTimeMillis();
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(now);
//        Log.d("AlarmReceiver", cal_esm.getTime().toString());
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //下午12點 12 凌晨12 - 0
        int StartHour = sharedPrefs.getInt(ESM_START_TIME_HOUR, 9);
        int EndHour = sharedPrefs.getInt(ESM_END_TIME_HOUR, 21);
        if(EndHour==0){
            //StartHour 9 EndHour 0
            if((cal_esm.get(Calendar.HOUR_OF_DAY) >= StartHour)){
                return true;
            }
        } else if(EndHour>StartHour){
            //StartHour 9 EndHour 21
            //8 23
            if((cal_esm.get(Calendar.HOUR_OF_DAY) >= StartHour && cal_esm.get(Calendar.HOUR_OF_DAY) < EndHour)){
                return true;
            }
        } else {
            //StartHour 11 EndHour 2
            //in midnight
            if(((cal_esm.get(Calendar.HOUR_OF_DAY) >= StartHour || cal_esm.get(Calendar.HOUR_OF_DAY) < EndHour))){
                return true;
            }
        }
        return false;
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
