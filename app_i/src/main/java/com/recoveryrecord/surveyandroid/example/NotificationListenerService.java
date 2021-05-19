package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.ALARM_SERVICE_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.ALARM_SERVICE_POST_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.CHINA_TIMES_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.CNA_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.CTS_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_DIARY_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_DIARY_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_DIARY_NOTIFICATION_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_NOTIFICATION_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_DAY_PUSH_PREFIX;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_INTERVAL;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_NOTIFICATION_CONTENT_TEXT;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_NOTIFICATION_CONTENT_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_NOT_IN_PUSH_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_OUT_OF_INTERVAL_LIMIT;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_PUSH;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_PUSH_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_STATUS;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_TIME_OUT;
import static com.recoveryrecord.surveyandroid.example.Constants.DOC_ID_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.EBC_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_DAY_PUSH_PREFIX;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_END_TIME_HOUR;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_END_TIME_MIN;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_INTERVAL;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_NOTIFICATION_CONTENT_TEXT;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_NOT_IN_PUSH_RANGE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_OUT_OF_INTERVAL_LIMIT;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_PUSH;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_PUSH_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_SET_ONCE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_START_TIME_HOUR;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_START_TIME_MIN;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_STATUS;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TIME_OUT;
import static com.recoveryrecord.surveyandroid.example.Constants.ETTODAY_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.LAST_DIARY_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.LAST_ESM_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_TYPE_DIARY;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_TYPE_ESM;
import static com.recoveryrecord.surveyandroid.example.Constants.LOADING_PAGE_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.LTN_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.MY_APP_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_DIARY_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_DIARY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_ESM;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_NEWS;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_ESM_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_MONITOR_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NOTI_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_SERVICE_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_SOURCE;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_TEXT;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_NOTIFICATION_CONTENT_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_DONE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_NOTI_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_RECEIEVE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_REMOVE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_TRIGGER_BY;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_TRIGGER_BY_ALARM;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_DIARY_TRIGGER_BY_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_NOTI_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_RECEIEVE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_REMOVE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_SAMPLE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TRIGGER_BY;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_TRIGGER_BY_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_RECEIEVE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_SERVICE_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_SERVICE_RECEIEVE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.REPEAT_ALARM_CHECKER;
import static com.recoveryrecord.surveyandroid.example.Constants.SETS_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.STORM_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.TEST_USER_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.UDN_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.VIBRATE_EFFECT;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListenerService extends android.service.notification.NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    String esm_status = "NA", diary_status = "NA";
    Boolean set_once = false;
//    private NotificationListenerServiceReceiver nlservicereciver;
    @Override
    public void onCreate() {
        super.onCreate();
//        nlservicereciver = new NotificationListenerServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
//        registerReceiver(nlservicereciver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(nlservicereciver);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
//        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//        assert alarmManager != null;
//        alarmManager.set(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(), pendingIntent);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        set_once = sharedPrefs.getBoolean(ESM_SET_ONCE, false);
        if(set_once){
            check_my_diary_and_esm();
        }

//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),REPEAT_ALARM_CHECKER, pendingIntent);//every ten min check
        Log.i(TAG,"**********  onNotificationPosted ***********");
        boolean is_target = false;
        switch (sbn.getPackageName()) {
            case CHINA_TIMES_PACKAGE_NAME:
            case CNA_PACKAGE_NAME:
            case UDN_PACKAGE_NAME:
            case LTN_PACKAGE_NAME:
            case ETTODAY_PACKAGE_NAME:
            case CTS_PACKAGE_NAME:
            case EBC_PACKAGE_NAME:
            case STORM_PACKAGE_NAME:
            case SETS_PACKAGE_NAME:
                is_target = true;
                break;
            default:
                is_target = false;
                break;
        }
        //cancel notification
        //if (sbn.getPackageName().equals("com.recoveryrecord.surveyandroid")){
        if (is_target){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cancelNotification(sbn.getKey());
                    Log.i(TAG, sbn.getPackageName() + "being canceled");
                } else {
                    Log.i(TAG, sbn.getPackageName() + "Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP");
                }
            } else {
                Log.i(TAG, sbn.getPackageName() + "Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH");
            }
        } else {
            Log.i(TAG, sbn.getPackageName() + " not target");
        }
        //MY APP
        if(sbn.getPackageName().equals(MY_APP_PACKAGE_NAME)){
            final Timestamp current_now = Timestamp.now();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            @SuppressLint("HardwareIds")
            String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            Bundle extras = sbn.getNotification().extras;
//            String type = "", doc_id = "";
            if(extras.getString(NOTIFICATION_TYPE_KEY)!=null && extras.getString(DOC_ID_KEY)!=null){
                String type = Objects.requireNonNull(extras.getString(NOTIFICATION_TYPE_KEY));
                String doc_id = Objects.requireNonNull(extras.getString(DOC_ID_KEY));
                String collection_id = "";
                String receieve_field = "";
                Boolean mark = false;
                if(type.equals(NOTIFICATION_TYPE_VALUE_ESM)){
                    collection_id = PUSH_ESM_COLLECTION;
                    receieve_field = PUSH_ESM_RECEIEVE_TIME;
                    mark = true;

                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Calendar calendar = Calendar.getInstance();
                    int day_index = calendar.get(Calendar.DAY_OF_YEAR);
                    int esm_sum = sharedPrefs.getInt(ESM_PUSH_TOTAL, 0);
                    int esm_day_sum = sharedPrefs.getInt(ESM_DAY_PUSH_PREFIX+ day_index, 0);
                    editor.putInt(ESM_PUSH_TOTAL, esm_sum+1);
                    editor.putInt(ESM_DAY_PUSH_PREFIX + day_index, esm_day_sum+1);
                    editor.apply();
                } else if(type.equals(NOTIFICATION_TYPE_VALUE_NEWS)){
                    collection_id = PUSH_NEWS_COLLECTION;
                    receieve_field = PUSH_NEWS_RECEIEVE_TIME;
                } else if(type.equals(NOTIFICATION_TYPE_VALUE_DIARY)){
                    collection_id = PUSH_DIARY_COLLECTION;
                    receieve_field = PUSH_DIARY_RECEIEVE_TIME;
                    mark = true;

                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Calendar calendar = Calendar.getInstance();
                    int day_index = calendar.get(Calendar.DAY_OF_YEAR);
                    int diary_sum = sharedPrefs.getInt(DIARY_PUSH_TOTAL, 0);
                    int diary_day_sum = sharedPrefs.getInt(DIARY_DAY_PUSH_PREFIX+ day_index, 0);
                    editor.putInt(DIARY_PUSH_TOTAL, diary_sum+1);
                    editor.putInt(DIARY_DAY_PUSH_PREFIX + day_index, diary_day_sum+1);
                    editor.apply();
                } else {
                    collection_id = PUSH_SERVICE_COLLECTION;
                    receieve_field = PUSH_SERVICE_RECEIEVE_TIME;
                }
                //news
                //service
                //esm
                //diary
                if(mark){
                    final DocumentReference rbRef = db.collection(TEST_USER_COLLECTION).document(device_id).collection(collection_id).document(doc_id);
                    final String finalReceieve_field = receieve_field;
                    rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                assert document != null;
                                if (document.exists()) {
                                    rbRef.update(finalReceieve_field, current_now)//another field
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("lognewsselect", "DocumentSnapshot successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("lognewsselect", "Error updating document", e);
                                                }
                                            });
                                } else {
                                    Log.d("lognewsselect", "No such document");
                                }
                            } else {
                                Log.d("lognewsselect", "get failed with ", task.getException());
                            }
                        }
                    });
                }
            }
        }
        //add to fire base
        boolean receieve_to_firestore = false;
        String document_name ="";
        switch (sbn.getPackageName()) {
//            case MY_APP_PACKAGE_NAME:
//                receieve_to_firestore = true;
//                document_name = NOTIFICATION_BAR_SERVICE_COLLECTION;
//                break;
            case CHINA_TIMES_PACKAGE_NAME:
            case CNA_PACKAGE_NAME:
            case UDN_PACKAGE_NAME:
            case LTN_PACKAGE_NAME:
            case ETTODAY_PACKAGE_NAME:
            case CTS_PACKAGE_NAME:
            case EBC_PACKAGE_NAME:
            case STORM_PACKAGE_NAME:
            case SETS_PACKAGE_NAME:
                receieve_to_firestore = true;
                document_name = NOTIFICATION_BAR_NEWS_MONITOR_COLLECTION;
                break;
            default:
                receieve_to_firestore = false;
        }
        boolean check_title = false, check_text = false;
        if(receieve_to_firestore){
            Log.d("checking", "NLService");
            Bundle extras = sbn.getNotification().extras;
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(sbn.getPostTime());
            List<String> in_time_split = new ArrayList<String>(Arrays.asList(formatter.format(date).split(" ")));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Timestamp mytimestamp = Timestamp.now();//new Timestamp(System.currentTimeMillis());
            Map<String, Object> receieve_notification = new HashMap<>();
            receieve_notification.put(NOTIFICATION_BAR_SOURCE, sbn.getPackageName());
            receieve_notification.put(NOTIFICATION_BAR_NOTI_TIME, mytimestamp);
            if (extras.containsKey("android.title")) {
                if(extras.getString("android.title")!=null){
                    receieve_notification.put(NOTIFICATION_BAR_TITLE, Objects.requireNonNull(extras.getString("android.title")));
                    //check if is news or esm
//                    if(extras.getString("android.title").equals(ESM_NOTIFICATION_CONTENT_TITLE)){
//                        document_name = NOTIFICATION_BAR_ESM_COLLECTION;
////                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//                        SharedPreferences.Editor editor = sharedPrefs.edit();
//                        Calendar calendar = Calendar.getInstance();
//                        int day_index = calendar.get(Calendar.DAY_OF_YEAR);
//                        int esm_sum = sharedPrefs.getInt(ESM_PUSH_TOTAL, 0);
//                        int esm_day_sum = sharedPrefs.getInt(ESM_DAY_PUSH_PREFIX+ day_index, 0);
//                        editor.putInt(ESM_PUSH_TOTAL, esm_sum+1);
//                        editor.putInt(ESM_DAY_PUSH_PREFIX + day_index, esm_day_sum+1);
//                        editor.apply();
//                    } else if(extras.getString("android.title").equals(DIARY_NOTIFICATION_CONTENT_TITLE)){
//                        document_name = NOTIFICATION_BAR_DIARY_COLLECTION;
////                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//                        SharedPreferences.Editor editor = sharedPrefs.edit();
//                        Calendar calendar = Calendar.getInstance();
//                        int day_index = calendar.get(Calendar.DAY_OF_YEAR);
//                        int diary_sum = sharedPrefs.getInt(DIARY_PUSH_TOTAL, 0);
//                        int diary_day_sum = sharedPrefs.getInt(DIARY_DAY_PUSH_PREFIX+ day_index, 0);
//                        editor.putInt(DIARY_PUSH_TOTAL, diary_sum+1);
//                        editor.putInt(DIARY_DAY_PUSH_PREFIX + day_index, diary_day_sum+1);
//                        editor.apply();
//                    }
                    check_title = true;
                } else {
                    receieve_notification.put(NOTIFICATION_BAR_TITLE,  "null");
                }
            } else {
                receieve_notification.put(NOTIFICATION_BAR_TITLE,  "null");
            }
            if (extras.containsKey("android.text")) {
                if(extras.getCharSequence("android.text")!=null){
                    receieve_notification.put(NOTIFICATION_BAR_TEXT,  extras.getCharSequence("android.text").toString());
                    check_text = true;
                } else {
                    receieve_notification.put(NOTIFICATION_BAR_TEXT, "null");
                }
            } else {
                receieve_notification.put(NOTIFICATION_BAR_TEXT, "null");
            }
            // if both is null then we don't need it
            if (check_title && check_text){
                @SuppressLint("HardwareIds")
                String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                boolean is_me = false;
                if (device_id.equals("564da153307f5547")){
                    is_me = true;
                }
                if (is_me){
                    db.collection("compare")
                            .document(formatter.format(date))
                            .set(receieve_notification);
                }
                db.collection(TEST_USER_COLLECTION)
                        .document(device_id)
                        .collection(document_name)
                        .document(formatter.format(date))
                        .set(receieve_notification);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void check_my_diary_and_esm() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        esm_status = "NA";
        diary_status = "NA";
//        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Boolean set_once = sharedPrefs.getBoolean(ESM_SET_ONCE, false);
        Map<String, Object> log_service = new HashMap<>();
        log_service.put("service_timestamp", Timestamp.now());
        Random r=new Random();
        int randomNumber = r.nextInt(5);
        log_service.put("delay_min", randomNumber*3);
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        if(set_once){
            //send diary
        if(check_diary_time_range(getApplicationContext())){
            scheduleNotification_diary(getApplicationContext(), getNotification_diary(getApplicationContext(), "Please fill out the questionnaire" ), randomNumber * 3 * 60 *1000 + 1000 );
            log_service.put("diary", true);
        } else {
            log_service.put("diary", false);
        }
        if(check_esm_time_range(getApplicationContext())){//send esm
//            select_news();
            Log.d("lognewsselect", "check_daily_time_range success");
            scheduleNotification_esm(getApplicationContext(), getNotification_esm(getApplicationContext(), "Please fill out the questionnaire" ), randomNumber * 3 * 60 *1000 + 1000);
            log_service.put("esm", true);
        } else {
            log_service.put("esm", false);
        }
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        log_service.put("start_hour", sharedPrefs.getInt(ESM_START_TIME_HOUR, 9));
        log_service.put("end_hour", sharedPrefs.getInt(ESM_END_TIME_HOUR, 21));
        log_service.put("start_min", sharedPrefs.getInt(ESM_START_TIME_MIN, 0));
        log_service.put("end_min", sharedPrefs.getInt(ESM_END_TIME_MIN, 0));
        log_service.put(DIARY_STATUS, diary_status);
        log_service.put(ESM_STATUS, esm_status);
        db.collection(TEST_USER_COLLECTION)
                .document(device_id)
                .collection(ALARM_SERVICE_POST_COLLECTION)
                .document(String.valueOf(Timestamp.now()))
                .set(log_service);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG, java.text.DateFormat.getDateTimeInstance().format(new Date()) + "\t" + "\t" + sbn.getPackageName());
        final Timestamp current_now = Timestamp.now();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        @SuppressLint("HardwareIds")
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if(sbn.getPackageName().equals("com.recoveryrecord.surveyandroid")){
            Bundle extras = sbn.getNotification().extras;
//            String type = "", doc_id = "";
            if(extras.getString(NOTIFICATION_TYPE_KEY)!=null && extras.getString(DOC_ID_KEY)!=null){
                String type = Objects.requireNonNull(extras.getString(NOTIFICATION_TYPE_KEY));
                String doc_id = Objects.requireNonNull(extras.getString(DOC_ID_KEY));
                String collection_id = "";
                String remove_field = "";
                Boolean mark = false;
                if(type.equals(NOTIFICATION_TYPE_VALUE_ESM)){
                    collection_id = PUSH_ESM_COLLECTION;
                    remove_field = PUSH_ESM_REMOVE_TIME;
                    mark = true;
                } else if(type.equals(NOTIFICATION_TYPE_VALUE_NEWS)){
                    collection_id = PUSH_NEWS_COLLECTION;
                } else if(type.equals(NOTIFICATION_TYPE_VALUE_DIARY)){
                    collection_id = PUSH_DIARY_COLLECTION;
                    remove_field = PUSH_DIARY_REMOVE_TIME;
                    mark = true;
                }
                Log.i(TAG,"********** onNOtificationRemoved type" + collection_id);
                Log.i(TAG,"********** onNOtificationRemoved id" + doc_id);
                if(mark){
                    final DocumentReference rbRef = db.collection(TEST_USER_COLLECTION).document(device_id).collection(collection_id).document(doc_id);
                    final String finalRemove_field = remove_field;
                    rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                assert document != null;
                                if (document.exists()) {
                                    rbRef.update(finalRemove_field, current_now)//another field
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("lognewsselect", "DocumentSnapshot successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("lognewsselect", "Error updating document", e);
                                                }
                                            });
                                } else {
                                    Log.d("lognewsselect", "No such document");
                                }
                            } else {
                                Log.d("lognewsselect", "get failed with ", task.getException());
                            }
                        }
                    });
                }
            }
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
        esm.put(PUSH_ESM_TRIGGER_BY, PUSH_ESM_TRIGGER_BY_NOTIFICATION);
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
        diary.put(PUSH_DIARY_TRIGGER_BY, PUSH_DIARY_TRIGGER_BY_NOTIFICATION);
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
//    class NotificationListenerServiceReceiver extends BroadcastReceiver{
//        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.i(TAG,"**********  onNotificationonReceive");
//            if(intent.getStringExtra("command").equals("clearall")){
//                NotificationListenerService.this.cancelAllNotifications();
//            } else if(intent.getStringExtra("command").equals("list")){
//                Log.i("log: NLService", "NLService");
//                List<String> noti_list = new ArrayList<String>();
////                String tmp = "=====================\n";
//                String tmp = "";
////                noti_list.add(tmp);
//                int count=1;
//                for (StatusBarNotification sbn : NotificationListenerService.this.getActiveNotifications()) {
//                    tmp = "";
//                    tmp = count + "\n";
//                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
////                    Date date = new Date(System.currentTimeMillis());
//                    Date date = new Date(sbn.getPostTime());
////                    java.text.DateFormat.getDateTimeInstance().format(new Date())
//                    tmp = tmp + formatter.format(date) + "\n";
//                    tmp = tmp + sbn.getPackageName() + "\n";
//                    tmp = tmp + "tickertext: " + sbn.getNotification().tickerText + "\n";
//                    Bundle extras = sbn.getNotification().extras;
//                    if (extras.containsKey("android.title")) {
//                        tmp = tmp + "title: " + extras.getString("android.title") + "\n";
////                        Log.i(TAG, "------------------------- in onNotificationPosted(), Bundle android.title = " + extras.getString("android.title"));
////                        StringBuilder builder = new StringBuilder("Extras:\n");
////                        for (String key : extras.keySet()) { //extras is the Bundle containing info
////                            Object value = extras.get(key); //get the current object
////                            builder.append(key).append(": ").append(value).append("\n"); //add the key-value pair to the
////                        }
////                        Log.i("Extras",builder.toString()); //log the data or use it as needed.
//                    } else {
//                        tmp = tmp + "title: null\n";
//                    }
//                    if (extras.containsKey("android.text")) {
//                        if (extras.getCharSequence("android.text") != null) {
//                            String text = extras.getCharSequence("android.text").toString();
////                            Log.i(TAG, "------------------------- in onNotificationPosted(), Bundle.text != NULL, so here it is = " + text);
//                            tmp = tmp + "text: " + text + "\n";
//                        } else {
//                            tmp = tmp + "text: null\n";
//                        }
//                    } else {
//                        tmp = tmp + "text: null\n";
//                    }
//                    count++;
//                    noti_list.add(tmp);
//                }
//                for (int index = noti_list.size()-1; index >=0; index--) {
//                    Intent i1 = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
//                    i1.putExtra("notification_list",noti_list.get(index));
//                    System.out.println(noti_list.get(index));
//                    sendBroadcast(i1);
//                }
//                Toast.makeText(getApplicationContext(), "Catch Successfully", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }

}
