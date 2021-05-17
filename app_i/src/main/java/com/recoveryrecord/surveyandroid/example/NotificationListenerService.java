package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.CHINA_TIMES_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.CNA_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.CTS_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.DOC_ID_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.EBC_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_DAY_PUSH_PREFIX;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_PUSH_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_START_TIME_HOUR;
import static com.recoveryrecord.surveyandroid.example.Constants.ETTODAY_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.LTN_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.MY_APP_PACKAGE_NAME;
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
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_ESM_REMOVE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.SETS_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.STORM_PACKAGE_NAME;
import static com.recoveryrecord.surveyandroid.example.Constants.TEST_USER_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.UDN_PACKAGE_NAME;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListenerService extends android.service.notification.NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
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

        //add to fire base
        boolean receieve_to_firestore = false;
        String document_name ="";
        switch (sbn.getPackageName()) {
            case MY_APP_PACKAGE_NAME:
                receieve_to_firestore = true;
                document_name = NOTIFICATION_BAR_SERVICE_COLLECTION;
                break;
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
//            receieve_notification.put("noti_date", in_time_split.get(0));
//            receieve_notification.put("noti_time", in_time_split.get(2));
            receieve_notification.put(NOTIFICATION_BAR_NOTI_TIME, mytimestamp);
            if (extras.containsKey("android.title")) {
                if(extras.getString("android.title")!=null){
                    receieve_notification.put(NOTIFICATION_BAR_TITLE, Objects.requireNonNull(extras.getString("android.title")));
                    //check if is news or esm
                    if(extras.getString("android.title").equals(ESM_NOTIFICATION_CONTENT_TITLE)){
                        document_name = NOTIFICATION_BAR_ESM_COLLECTION;
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Calendar calendar = Calendar.getInstance();
                        int day_index = calendar.get(Calendar.DAY_OF_YEAR);
                        int esm_sum = sharedPrefs.getInt(ESM_PUSH_TOTAL, 0);
                        int esm_day_sum = sharedPrefs.getInt(ESM_DAY_PUSH_PREFIX+ day_index, 0);
                        editor.putInt(ESM_PUSH_TOTAL, esm_sum+1);
                        editor.putInt(ESM_DAY_PUSH_PREFIX + day_index, esm_day_sum+1);
                        editor.apply();
                    }
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
                if (document_name.equals("notification_bar_news(not app)") && is_me){
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
            String type = Objects.requireNonNull(extras.getString(NOTIFICATION_TYPE_KEY));
            String doc_id = Objects.requireNonNull(extras.getString(DOC_ID_KEY));
            String collection_id = "";
            if(type.equals(NOTIFICATION_TYPE_VALUE_ESM)){
                collection_id = PUSH_ESM_COLLECTION;
            } else if(type.equals(NOTIFICATION_TYPE_VALUE_NEWS)){
                collection_id = PUSH_NEWS_COLLECTION;
            } else if(type.equals(NOTIFICATION_TYPE_VALUE_DIARY)){
                collection_id = PUSH_DIARY_COLLECTION;
            }
            Log.i(TAG,"********** onNOtificationRemoved type" + collection_id);
            Log.i(TAG,"********** onNOtificationRemoved id" + doc_id);
            if(!collection_id.equals("")){
                final DocumentReference rbRef = db.collection(TEST_USER_COLLECTION).document(device_id).collection(PUSH_ESM_COLLECTION).document(doc_id);
                rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if (document.exists()) {
                                rbRef.update(PUSH_ESM_REMOVE_TIME, current_now)//another field
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
