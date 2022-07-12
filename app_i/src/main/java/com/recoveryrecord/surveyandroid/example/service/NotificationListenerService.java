package com.recoveryrecord.surveyandroid.example.service;

import static com.recoveryrecord.surveyandroid.example.config.Constants.CHINA_TIMES_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.CNA_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.CTS_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.DIARY_DAY_PUSH_PREFIX;
import static com.recoveryrecord.surveyandroid.example.config.Constants.DIARY_PUSH_TOTAL;
import static com.recoveryrecord.surveyandroid.example.config.Constants.DOC_ID_KEY;
import static com.recoveryrecord.surveyandroid.example.config.Constants.EBC_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.ESM_DAY_PUSH_PREFIX;
import static com.recoveryrecord.surveyandroid.example.config.Constants.ESM_PUSH_TOTAL;
import static com.recoveryrecord.surveyandroid.example.config.Constants.ETTODAY_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.LTN_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.MY_APP_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_NEWS_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_NEWS_MONITOR_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_NEWS_NOTI_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_NEWS_PACKAGE_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_NEWS_SOURCE;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_NEWS_TEXT;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_NEWS_TITLE;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_OTHER_APP_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_PACKAGE_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_RECEIEVE_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_REMOVE_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_BAR_REMOVE_TYPE;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_TYPE_VALUE_DIARY;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_TYPE_VALUE_ESM;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_TYPE_VALUE_NEWS;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_DIARY_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_DIARY_RECEIEVE_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_DIARY_REMOVE_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_DIARY_REMOVE_TYPE;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_ESM_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_ESM_RECEIEVE_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_ESM_REMOVE_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_ESM_REMOVE_TYPE;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_RECEIEVE_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_REMOVE_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_REMOVE_TYPE;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_SERVICE_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_SERVICE_RECEIEVE_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SETS_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.STORM_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.UDN_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USER_COLLECTION;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.DbHelper.DiaryDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.ESMDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.PushNewsDbHelper;
import com.recoveryrecord.surveyandroid.example.sqlite.Diary;
import com.recoveryrecord.surveyandroid.example.sqlite.ESM;
import com.recoveryrecord.surveyandroid.example.sqlite.PushNews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressLint("OverrideAbstract")
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
    public void onNotificationPosted (StatusBarNotification sbn, NotificationListenerService.RankingMap rankingMap) {

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
                break;
        }
        //cancel notification
        if (is_target){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cancelNotification(sbn.getKey());
                Log.i(TAG, sbn.getPackageName() + "being canceled");
            } else {
                Log.i(TAG, sbn.getPackageName() + "Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP");
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
                boolean mark = false;
                switch (type) {
                    case NOTIFICATION_TYPE_VALUE_ESM: {//esm
                        collection_id = PUSH_ESM_COLLECTION;
                        receieve_field = PUSH_ESM_RECEIEVE_TIME;
                        mark = true;
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Calendar calendar = Calendar.getInstance();
                        int day_index = calendar.get(Calendar.DAY_OF_YEAR);
                        int esm_sum = sharedPrefs.getInt(ESM_PUSH_TOTAL, 0);
                        int esm_day_sum = sharedPrefs.getInt(ESM_DAY_PUSH_PREFIX + day_index, 0);
//                        editor.putBoolean(ESM_LAST_TIME, true);
                        editor.putInt(ESM_PUSH_TOTAL, esm_sum + 1);
                        editor.putInt(ESM_DAY_PUSH_PREFIX + day_index, esm_day_sum + 1);
                        editor.apply();
                        doc_id = device_id + " " + doc_id;
                        ESM myesm = new ESM();
                        myesm.setKEY_DOC_ID(doc_id);
                        myesm.setKEY_RECEIEVE_TIMESTAMP(Timestamp.now().getSeconds());
                        ESMDbHelper dbHandler = new ESMDbHelper(getApplicationContext());
                        dbHandler.UpdatePushESMDetailsReceieve(myesm);
                        break;
                    }
                    case NOTIFICATION_TYPE_VALUE_NEWS: {//news
                        collection_id = PUSH_NEWS_COLLECTION;
                        receieve_field = PUSH_NEWS_RECEIEVE_TIME;
                        mark = true;
                        doc_id = device_id + " " + doc_id;
                        PushNews myPushNews = new PushNews();
                        myPushNews.setKEY_DOC_ID(doc_id);
                        myPushNews.setKEY_RECEIEVE_TIMESTAMP(Timestamp.now().getSeconds());
                        PushNewsDbHelper dbHandler = new PushNewsDbHelper(getApplicationContext());
                        dbHandler.UpdatePushNewsDetailsReceieve(myPushNews);
                        //insert
                        break;
                    }
                    case NOTIFICATION_TYPE_VALUE_DIARY: {//diary
                        collection_id = PUSH_DIARY_COLLECTION;
                        receieve_field = PUSH_DIARY_RECEIEVE_TIME;
                        mark = true;
                        doc_id = device_id + " " + doc_id;
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Calendar calendar = Calendar.getInstance();
                        int day_index = calendar.get(Calendar.DAY_OF_YEAR);
                        int diary_sum = sharedPrefs.getInt(DIARY_PUSH_TOTAL, 0);
                        int diary_day_sum = sharedPrefs.getInt(DIARY_DAY_PUSH_PREFIX + day_index, 0);
                        editor.putInt(DIARY_PUSH_TOTAL, diary_sum + 1);
                        editor.putInt(DIARY_DAY_PUSH_PREFIX + day_index, diary_day_sum + 1);
                        editor.apply();
                        Diary mydiary = new Diary();
                        mydiary.setKEY_DOC_ID(doc_id);
                        mydiary.setKEY_RECEIEVE_TIMESTAMP(Timestamp.now().getSeconds());;
                        DiaryDbHelper dbHandler = new DiaryDbHelper(getApplicationContext());
                        dbHandler.UpdatePushDiaryDetailsReceieve(mydiary);
                        break;
                    }
                    default: {//service
                        collection_id = PUSH_SERVICE_COLLECTION;
                        receieve_field = PUSH_SERVICE_RECEIEVE_TIME;
                        break;
                    }
                }
                //news
                //service
                //esm
                //diary
                if(mark){

//                    final DocumentReference rbRef = db.collection(TEST_USER_COLLECTION).document(device_id).collection(collection_id).document(doc_id);
                    final DocumentReference rbRef = db.collection(collection_id).document(doc_id);
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

                    DocumentReference rbRef_check = db.collection(USER_COLLECTION).document(device_id);
                    rbRef_check.update("check_last_" + type, current_now)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("log: firebase share", "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("log: firebase share", "Error updating document", e);
                                }
                            });
                }
            }
        } else if(is_target) {//new media
            boolean check_title = false, check_text = false;
            Log.d("checking", "NLService");
            Bundle extras = sbn.getNotification().extras;
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(sbn.getPostTime());
//            List<String> in_time_split = new ArrayList<String>(Arrays.asList(formatter.format(date).split(" ")));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Timestamp mytimestamp = Timestamp.now();//new Timestamp(System.currentTimeMillis());
            @SuppressLint("HardwareIds")
            String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            Map<String, Object> receieve_notification = new HashMap<>();
            receieve_notification.put(NOTIFICATION_BAR_NEWS_SOURCE, sbn.getPackageName());
            receieve_notification.put(NOTIFICATION_BAR_NEWS_NOTI_TIME, mytimestamp);
            receieve_notification.put(NOTIFICATION_BAR_NEWS_PACKAGE_ID, sbn.getKey());
            receieve_notification.put(NOTIFICATION_BAR_NEWS_DEVICE_ID, device_id);

            if (extras.containsKey("android.title")) {
                if(extras.getString("android.title")!=null){
                    receieve_notification.put(NOTIFICATION_BAR_NEWS_TITLE, Objects.requireNonNull(extras.getString("android.title")));
                    check_title = true;
                } else {
                    receieve_notification.put(NOTIFICATION_BAR_NEWS_TITLE,  "null");
                }
            } else {
                receieve_notification.put(NOTIFICATION_BAR_NEWS_TITLE,  "null");
            }
            if (extras.containsKey("android.text")) {
                if(extras.getCharSequence("android.text")!=null){
                    receieve_notification.put(NOTIFICATION_BAR_NEWS_TEXT,  extras.getCharSequence("android.text").toString());
                    check_text = true;
                } else {
                    receieve_notification.put(NOTIFICATION_BAR_NEWS_TEXT, "null");
                }
            } else {
                receieve_notification.put(NOTIFICATION_BAR_NEWS_TEXT, "null");
            }
            // if both is null then we don't need it
            if (check_title && check_text){

                boolean is_me = false;
                if (device_id.equals("318f4fea56e7070c") || device_id.equals("3e9e2a7c21cd7838")){
                    is_me = true;
                    receieve_notification.put("source", device_id);
                }
                if (is_me){
                    db.collection("compare")
                            .document(formatter.format(date))
                            .set(receieve_notification);
                }
                db.collection(NOTIFICATION_BAR_NEWS_MONITOR_COLLECTION)
                        .document(device_id + " " + sbn.getPostTime())
                        .set(receieve_notification);
            }
        } else {//other
            @SuppressLint("HardwareIds")
            String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            Map<String, Object> receieve_notification = new HashMap<>();
            receieve_notification.put(NOTIFICATION_BAR_PACKAGE_NAME, sbn.getPackageName());
            receieve_notification.put(NOTIFICATION_BAR_PACKAGE_ID, sbn.getKey());
            receieve_notification.put(NOTIFICATION_BAR_RECEIEVE_TIME, Timestamp.now());
            receieve_notification.put(NOTIFICATION_BAR_DEVICE_ID, device_id);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(NOTIFICATION_BAR_OTHER_APP_COLLECTION)
                    .document(device_id + " " + sbn.getPostTime())
                    .set(receieve_notification);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationRemoved(final StatusBarNotification sbn, NotificationListenerService.RankingMap rankingMap, int reason) {
        Log.i(TAG,"**********  onNotificationPosted7 ***********" + sbn.getKey());
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG,"********** onNOtificationRemoved" + reason);
        String remove_reason = "remove_reason";
        switch (reason) {
            case 1:
                //canceled by the status bar reporting a notification click.
                //click
                remove_reason = "REASON_CLICK";
                break;
            case 2:
                //swipe remove single
                //canceled by the status bar reporting a user dismissal.
                remove_reason = "REASON_CANCEL";
                break;
            case 3:
                //canceled by the status bar reporting a user dismiss all.
                //remove all
                remove_reason = "REASON_CANCEL_ALL";
                break;
            case 4:
                //canceled by the status bar reporting an inflation error.
                remove_reason = "REASON_ERROR";
                break;
            case 5:
                //canceled by the package manager modifying the package.
                remove_reason = "REASON_PACKAGE_CHANGED";
                break;
            case 6:
                //canceled by the owning user context being stopped.
                remove_reason = "REASON_USER_STOPPED";
                break;
            case 7:
                //canceled by the user banning the package.
                remove_reason = "REASON_PACKAGE_BANNED";
                break;
            case 8:
                //canceled by the app canceling this specific notification.
                remove_reason = "REASON_APP_CANCEL";
                break;
            case 9:
                //canceled by the app cancelling all its notifications.
                remove_reason = "REASON_APP_CANCEL_ALL";
                break;
            case 10:
                //canceled by a listener reporting a user dismissal.
                remove_reason = "REASON_LISTENER_CANCEL";
                break;
            case 11:
                //canceled by a listener reporting a user dismiss all.
                remove_reason = "REASON_LISTENER_CANCEL_ALL";
                break;
            case 12:
                //canceled because it was a member of a canceled group
                //remove group
                remove_reason = "REASON_GROUP_SUMMARY_CANCELED";
                break;
            case 13:
                //canceled because it was an invisible member of a group.
                remove_reason = "REASON_GROUP_OPTIMIZATION";
                break;
            case 14:
                //canceled by the device administrator suspending the package.
                remove_reason = "REASON_PACKAGE_SUSPENDED";
                break;
            case 15:
                //canceled by the owning managed profile being turned off.
                remove_reason = "REASON_PROFILE_TURNED_OFF";
                break;
            case 16:
                //Autobundled summary notification was canceled because its group was unbundled
                remove_reason = "REASON_UNAUTOBUNDLED";
                break;
            case 17:
                //canceled by the user banning the channel.
                remove_reason = "REASON_CHANNEL_BANNED";
                break;
            case 18:
                //Notification was snoozed.
                remove_reason = "REASON_SNOOZED";
                break;
            case 19:
                //Notification was canceled due to timeout
                remove_reason = "REASON_TIMEOUT";
                break;
            case 20:
                //canceled due to the backing channel being deleted
                remove_reason = "REASON_CHANNEL_REMOVED";
                break;
            case 21:
                // canceled due to the app's storage being cleared
                remove_reason = "REASON_CLEAR_DATA";
                break;
            default:
                remove_reason = "NO MATCH";
                break;
        }
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
                String remove_type_field = "";
                Boolean mark = false;
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                switch (type) {
                    case NOTIFICATION_TYPE_VALUE_ESM:{
                        collection_id = PUSH_ESM_COLLECTION;
                        remove_field = PUSH_ESM_REMOVE_TIME;
                        remove_type_field = PUSH_ESM_REMOVE_TYPE;
                        doc_id = device_id + " " + doc_id;
                        mark = true;
                        ESM myesm = new ESM();
                        myesm.setKEY_DOC_ID(doc_id);
                        myesm.setKEY_REMOVE_TIMESTAMP(Timestamp.now().getSeconds());
                        myesm.setKEY_REMOVE_TYPE(remove_reason);
                        ESMDbHelper dbHandler_e = new ESMDbHelper(getApplicationContext());
                        dbHandler_e.UpdatePushESMDetailsRemove(myesm);
                        break;
                    }
                    case NOTIFICATION_TYPE_VALUE_NEWS: {
                        collection_id = PUSH_NEWS_COLLECTION;
                        remove_field = PUSH_NEWS_REMOVE_TIME;
                        remove_type_field = PUSH_NEWS_REMOVE_TYPE;
                        doc_id = device_id + " " + doc_id;
                        mark = true;
                        PushNews myPushNews = new PushNews();
                        myPushNews.setKEY_DOC_ID(doc_id);
                        myPushNews.setKEY_REMOVE_TIMESTAMP(Timestamp.now().getSeconds());
                        myPushNews.setKEY_REMOVE_TYPE(remove_reason);
                        PushNewsDbHelper dbHandler_p = new PushNewsDbHelper(getApplicationContext());
                        dbHandler_p.UpdatePushNewsDetailsRemove(myPushNews);
                        break;
                    }
                    case NOTIFICATION_TYPE_VALUE_DIARY:{
                        collection_id = PUSH_DIARY_COLLECTION;
                        remove_field = PUSH_DIARY_REMOVE_TIME;
                        remove_type_field = PUSH_DIARY_REMOVE_TYPE;
                        doc_id = device_id + " " + doc_id;
                        mark = true;
                        Diary mydiary = new Diary();
                        mydiary.setKEY_DOC_ID(doc_id);
                        mydiary.setKEY_REMOVE_TIMESTAMP(Timestamp.now().getSeconds());
                        mydiary.setKEY_REMOVE_TYPE(remove_reason);
                        DiaryDbHelper dbHandler = new DiaryDbHelper(getApplicationContext());
                        dbHandler.UpdatePushESMDetailsRemove(mydiary);
                        break;
                    }

                }
                if(mark){
                    final DocumentReference rbRef = db.collection(collection_id).document(doc_id);
                    final String finalRemove_field = remove_field;
                    final String finalRemove_reason = remove_reason;
                    final String finalRemove_type_field = remove_type_field;
                    rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                assert document != null;
                                if (document.exists()) {
                                    rbRef.update(finalRemove_field, current_now,
                                            finalRemove_type_field, finalRemove_reason)//another field
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
        } else {
            final DocumentReference rbRef = db.collection(NOTIFICATION_BAR_OTHER_APP_COLLECTION).document(device_id + " " + sbn.getPostTime());
            final String finalRemove_reason1 = remove_reason;
            rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) {
                            rbRef.update(NOTIFICATION_BAR_REMOVE_TIME, current_now,
                                    NOTIFICATION_BAR_REMOVE_TYPE, finalRemove_reason1,
                                    NOTIFICATION_BAR_PACKAGE_ID, sbn.getKey())//another field
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
