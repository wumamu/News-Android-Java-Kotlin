package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListenerService extends android.service.notification.NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private NotificationListenerServiceReceiver nlservicereciver;
    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NotificationListenerServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG,"**********  onNotificationPosted ***********");
//        Log.i(TAG,sbn.getPackageName());
        boolean is_target = false;
        String p_name = sbn.getPackageName();
        switch (sbn.getPackageName()) {
//            case "com.recoveryrecord.surveyandroid":
//            case "com.facebook.orca":
            case "cc.nexdoor.ct.activity":
            case "m.cna.com.tw.App":
            case "com.udn.news":
            case "com.ltnnews.news":
            case "net.ettoday.phone":
            case "com.news.ctsapp":
            case "com.ebc.news":
            case "cc.nexdoor.stormmedia":
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

        //content intent
//        Notification notification = sbn.getNotification();
//        PendingIntent contentIntent = notification.contentIntent;

//        no need to add to sql
//        String on_noti_post = "";
        //keep length 6
//        on_noti_post = "hello" + "\n";
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        Date date = new Date(sbn.getPostTime());
//        on_noti_post = on_noti_post + formatter.format(date) + "\n";
//        on_noti_post = on_noti_post + sbn.getPackageName() + "\n";
//        on_noti_post = on_noti_post + "tickertext: " + sbn.getNotification().tickerText + "\n";
//        Bundle extras = sbn.getNotification().extras;
//        if (extras.containsKey("android.title")) {
//            on_noti_post = on_noti_post + "title: " + extras.getString("android.title") + "\n";
//        } else {
//            on_noti_post = on_noti_post + "title: null\n";
//        }
//        if (extras.containsKey("android.text")) {
//            if (extras.getCharSequence("android.text") != null) {
//                String text = extras.getCharSequence("android.text").toString();
////                            Log.i(TAG, "------------------------- in onNotificationPosted(), Bundle.text != NULL, so here it is = " + text);
//                on_noti_post = on_noti_post + "text: " + text + "\n";
//            } else {
//                on_noti_post = on_noti_post + "text: null\n";
//            }
//        } else {
//            on_noti_post = on_noti_post + "text: null\n";
//        }
//
//        //add to sql
//        Intent i_post = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
//        if (is_target){
//            i_post.putExtra("notification_list", on_noti_post);
//            sendBroadcast(i_post);
//        }
        //add to fire base
        boolean receieve_to_firestore = false;
        String document_name ="";
        switch (sbn.getPackageName()) {
            case "com.recoveryrecord.surveyandroid":
                receieve_to_firestore = true;
                document_name = "send_notifications";
                break;
//            case "com.facebook.orca":
            case "cc.nexdoor.ct.activity":
            case "m.cna.com.tw.App":
            case "com.udn.news":
            case "com.ltnnews.news":
            case "net.ettoday.phone":
            case "com.news.ctsapp":
            case "com.ebc.news":
            case "cc.nexdoor.stormmedia":
                receieve_to_firestore = true;
                document_name = "receieve_notifications";
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
            receieve_notification.put("media", sbn.getPackageName());
            receieve_notification.put("noti_date", in_time_split.get(0));
            receieve_notification.put("noti_time", in_time_split.get(2));
            receieve_notification.put("noti_timestamp", mytimestamp);
            if (extras.containsKey("android.title")) {
                if(extras.getString("android.title")!=null){
                    receieve_notification.put("title", Objects.requireNonNull(extras.getString("android.title")));
                    //check if is news or esm
                    if(extras.getString("android.title").equals("ESM")){
                        document_name = "esms";
                    }
                    check_title = true;
                } else {
                    receieve_notification.put("title",  "null");
                }
            } else {
                receieve_notification.put("title",  "null");
            }
            if (extras.containsKey("android.text")) {
                if(extras.getCharSequence("android.text")!=null){
                    receieve_notification.put("text",  extras.getCharSequence("android.text").toString());
                    check_text = true;
                } else {
                    receieve_notification.put("text", "null");
                }
            } else {
                receieve_notification.put("text", "null");
            }
            // if both is null then we don't need it
            if (check_title && check_text){
//                Log.d("checking", "13");
                @SuppressLint("HardwareIds")
                String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                boolean is_me = false;
                if (device_id.equals("564da153307f5547")){
                    is_me = true;
                }
                if (document_name.equals("receieve_notificaions") && is_me){
                    db.collection("compare")
                            .document(formatter.format(date))
                            .set(receieve_notification);
                    Toast.makeText(getApplicationContext(), "compare Inserted Successfully", Toast.LENGTH_SHORT).show();
                }
                db.collection("test_users")
                        .document(device_id)
                        .collection(document_name)
                        .document(formatter.format(date))
                        .set(receieve_notification);
                Toast.makeText(getApplicationContext(), "firebase Inserted Successfully", Toast.LENGTH_SHORT).show();
//                Log.d("checking", "55");
            }

        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG, java.text.DateFormat.getDateTimeInstance().format(new Date()) + "\t" + "\t" + sbn.getPackageName());
    }

    class NotificationListenerServiceReceiver extends BroadcastReceiver{
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"**********  onNotificationonReceive");
            if(intent.getStringExtra("command").equals("clearall")){
                NotificationListenerService.this.cancelAllNotifications();
            } else if(intent.getStringExtra("command").equals("list")){
                Log.i("log: NLService", "NLService");
                List<String> noti_list = new ArrayList<String>();
//                String tmp = "=====================\n";
                String tmp = "";
//                noti_list.add(tmp);
                int count=1;
                for (StatusBarNotification sbn : NotificationListenerService.this.getActiveNotifications()) {
                    tmp = "";
                    tmp = count + "\n";
                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//                    Date date = new Date(System.currentTimeMillis());
                    Date date = new Date(sbn.getPostTime());
//                    java.text.DateFormat.getDateTimeInstance().format(new Date())
                    tmp = tmp + formatter.format(date) + "\n";
                    tmp = tmp + sbn.getPackageName() + "\n";
                    tmp = tmp + "tickertext: " + sbn.getNotification().tickerText + "\n";
                    Bundle extras = sbn.getNotification().extras;
                    if (extras.containsKey("android.title")) {
                        tmp = tmp + "title: " + extras.getString("android.title") + "\n";
//                        Log.i(TAG, "------------------------- in onNotificationPosted(), Bundle android.title = " + extras.getString("android.title"));
//                        StringBuilder builder = new StringBuilder("Extras:\n");
//                        for (String key : extras.keySet()) { //extras is the Bundle containing info
//                            Object value = extras.get(key); //get the current object
//                            builder.append(key).append(": ").append(value).append("\n"); //add the key-value pair to the
//                        }
//                        Log.i("Extras",builder.toString()); //log the data or use it as needed.
                    } else {
                        tmp = tmp + "title: null\n";
                    }
                    if (extras.containsKey("android.text")) {
                        if (extras.getCharSequence("android.text") != null) {
                            String text = extras.getCharSequence("android.text").toString();
//                            Log.i(TAG, "------------------------- in onNotificationPosted(), Bundle.text != NULL, so here it is = " + text);
                            tmp = tmp + "text: " + text + "\n";
                        } else {
                            tmp = tmp + "text: null\n";
                        }
                    } else {
                        tmp = tmp + "text: null\n";
                    }
                    count++;
                    noti_list.add(tmp);
                }
                for (int index = noti_list.size()-1; index >=0; index--) {
                    Intent i1 = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
                    i1.putExtra("notification_list",noti_list.get(index));
                    System.out.println(noti_list.get(index));
                    sendBroadcast(i1);
                }
                Toast.makeText(getApplicationContext(), "Catch Successfully", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
