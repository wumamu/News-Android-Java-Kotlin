package com.recoveryrecord.surveyandroid.example;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MyNotificationListenerService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;
    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
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
        Log.i(TAG,"**********  onNotificationPosted");
        //cancel notification
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                cancelNotification(sbn.getKey());
//                Log.i(TAG, "++++++++++++++++++++");
//            }
//        }
        //content intent
        Notification notification = sbn.getNotification();
        PendingIntent contentIntent = notification.contentIntent;
//        Log.i(TAG, String.valueOf(contentIntent.describeContents()));
//        Log.i(TAG, contentIntent.getTargetPackage());
        String on_noti_post = "";
        //keep length 6
        on_noti_post = "hello" + "\n";
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(sbn.getPostTime());
        on_noti_post = on_noti_post + formatter.format(date) + "\n";
        on_noti_post = on_noti_post + sbn.getPackageName() + "\n";
        on_noti_post = on_noti_post + "tickertext: " + sbn.getNotification().tickerText + "\n";
        Bundle extras = sbn.getNotification().extras;
        if (extras.containsKey("android.title")) {
            on_noti_post = on_noti_post + "title: " + extras.getString("android.title") + "\n";
        } else {
            on_noti_post = on_noti_post + "title: null\n";
        }
        if (extras.containsKey("android.text")) {
            if (extras.getCharSequence("android.text") != null) {
                String text = extras.getCharSequence("android.text").toString();
//                            Log.i(TAG, "------------------------- in onNotificationPosted(), Bundle.text != NULL, so here it is = " + text);
                on_noti_post = on_noti_post + "text: " + text + "\n";
            } else {
                on_noti_post = on_noti_post + "text: null\n";
            }
        } else {
            on_noti_post = on_noti_post + "text: null\n";
        }
        Intent i_post = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
        i_post.putExtra("notification_list", on_noti_post);
        sendBroadcast(i_post);
        Toast.makeText(getApplicationContext(), "Post Inserted Successfully", Toast.LENGTH_SHORT).show();
        boolean receieve_to_firestore = false;
        String document_name ="";
        switch (sbn.getPackageName()) {
            case "com.recoveryrecord.surveyandroid":
                receieve_to_firestore = true;
                document_name = "send_notificaions";
                break;
            case "cc.nexdoor.ct.activity":
            case "m.cna.com.tw.App":
            case "com.udn.news":
            case "com.ltnnews.news":
            case "net.ettoday.phone":
            case "com.news.ctsapp":
            case "com.ebc.news":
                receieve_to_firestore = true;
                document_name = "receieve_notificaions";
                break;
            default:
                receieve_to_firestore = false;
        }
        if(receieve_to_firestore){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> receieve_notification = new HashMap<>();
            receieve_notification.put("media", sbn.getPackageName());
            receieve_notification.put("time", formatter.format(date));
            if (extras.containsKey("android.title")) {
                receieve_notification.put("title",  extras.getString("android.title"));
//                StringBuilder builder = new StringBuilder("Extras:\n");

//                for (String key : extras.keySet()) { //extras is the Bundle containing info
//                    Object value = extras.get(key); //get the current object
////                    builder.append(key).append(": ").append(value).append("\n"); //add the key-value pair to the
//                    receieve_notification.put(key, value);
//                }
//                Bundle extras = getIntent().getExtras();
                for (String key : extras.keySet()) {
                    Log.d(TAG, "Extra '" + key + "': '" + extras.getString(key) + "'");
                    receieve_notification.put(key, extras.getString(key));
                }
//                receieve_notification.put("bundle", builder.toString());
            } else {
                receieve_notification.put("title",  "null");
            }
            if (extras.containsKey("android.text")) {
                receieve_notification.put("text",  extras.getCharSequence("android.text").toString());
            } else {
                receieve_notification.put("text", "null");
            }
            String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            db.collection("test_users")
                    .document(device_id)
                    .collection(document_name)
                    .document(formatter.format(date))
                    .set(receieve_notification);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG, java.text.DateFormat.getDateTimeInstance().format(new Date()) + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
    }

    class NLServiceReceiver extends BroadcastReceiver{
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                MyNotificationListenerService.this.cancelAllNotifications();
            } else if(intent.getStringExtra("command").equals("list")){
                Log.i("log: NLService", "NLService");
                List<String> noti_list = new ArrayList<String>();
//                String tmp = "=====================\n";
                String tmp = "";
//                noti_list.add(tmp);
                int count=1;
                for (StatusBarNotification sbn : MyNotificationListenerService.this.getActiveNotifications()) {
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
