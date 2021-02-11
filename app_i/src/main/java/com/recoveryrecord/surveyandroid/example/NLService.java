package com.recoveryrecord.surveyandroid.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NLService extends NotificationListenerService {

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Log.i(TAG,"**********  onNotificationPosted");
//        Log.i(TAG, java.text.DateFormat.getDateTimeInstance().format(new Date()) + " ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Log.i(TAG, java.text.DateFormat.getDateTimeInstance().format(new Date()) + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
//        Intent i = new  Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
////        i.putExtra("notification_event","onNotificationPosted :" + sbn.getPackageName() + "\n");
//        String to_post = "Posted: " + java.text.DateFormat.getDateTimeInstance().format(new Date()) + " " + sbn.getPackageName() + ": " + sbn.getNotification().tickerText + "\n";
//        i.putExtra("notification_event",to_post);
//        sendBroadcast(i);
//
//        Bundle extras = sbn.getNotification().extras;
//
//        if (extras.containsKey("android.text")) {
//            if (extras.getCharSequence("android.text") != null) {
//                String text = extras.getCharSequence("android.text").toString();
//                Log.i(TAG, "------------------------- in onNotificationPosted(), Bundle.text != NULL, so here it is = " + text);
//            }
//        }
//        if (extras.containsKey("android.title")) {
//            Log.i(TAG, "------------------------- in onNotificationPosted(), Bundle android.title = " + extras.getString("android.title"));
//        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNOtificationRemoved");
//        Log.i(TAG, System.currentTimeMillis() + "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
        Log.i(TAG, java.text.DateFormat.getDateTimeInstance().format(new Date()) + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
//        Intent i = new  Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
////        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");
//        String to_post = "Removed: " + java.text.DateFormat.getDateTimeInstance().format(new Date()) + " " + sbn.getPackageName() + ": " + sbn.getNotification().tickerText + "\n";
////        i.putExtra("notification_event",to_post);
//
//        sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver{

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                NLService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                List<String> noti_list = new ArrayList<String>();
                String tmp = "=====================\n";
                noti_list.add(tmp);
                int i=1;
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    tmp = i + "\n";
                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//                    Date date = new Date(System.currentTimeMillis());
                    Date date = new Date(sbn.getPostTime());
//                    System.out.println(formatter.format(date));
//                    tmp = tmp + java.text.DateFormat.getDateTimeInstance().format(new Date()) + " " + sbn.getPackageName() + " " + sbn.getNotification().tickerText + "\n";
//                    tmp = tmp + " " + formatter.format(date) + "\n";
                    //now java.text.DateFormat.getDateTimeInstance().format(new Date())
                    tmp = tmp + formatter.format(date) + " " + sbn.getPackageName() + " " + sbn.getNotification().tickerText + "\n";
                    Bundle extras = sbn.getNotification().extras;
                    if (extras.containsKey("android.title")) {
                        tmp = tmp + "title " + extras.getString("android.title") + "\n";
//                        Log.i(TAG, "------------------------- in onNotificationPosted(), Bundle android.title = " + extras.getString("android.title"));
                        StringBuilder builder = new StringBuilder("Extras:\n");
                        for (String key : extras.keySet()) { //extras is the Bundle containing info
                            Object value = extras.get(key); //get the current object
                            builder.append(key).append(": ").append(value).append("\n"); //add the key-value pair to the
                        }
                        Log.i("Extras",builder.toString()); //log the data or use it as needed.
                    }
                    if (extras.containsKey("android.text")) {
                        if (extras.getCharSequence("android.text") != null) {
                            String text = extras.getCharSequence("android.text").toString();
//                            Log.i(TAG, "------------------------- in onNotificationPosted(), Bundle.text != NULL, so here it is = " + text);
                            tmp = tmp + "text " + text + "\n";
                        }
                    }

//                    tmp = tmp + "\n";
                    i++;
                    noti_list.add(tmp);
                }
                for (int index = noti_list.size()-1; index >=0; index--) {
//                    System.out.println(crunchifyList.get(i));
                    Intent i1 = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
                    i1.putExtra("notification_event",noti_list.get(index));
                    System.out.println(noti_list.get(index));
                    sendBroadcast(i1);
                }

//                Intent i1 = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
//                i1.putExtra("notification_event","=====================" + "\n");
//                sendBroadcast(i1);
//                int i=1;
//                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
//                    Intent i2 = new  Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
//                    i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "\n");
////                    i2.putExtra("notification_event",i +" " + sbn.getPackageName());
//                    sendBroadcast(i2);
//                    i++;
//                    Bundle extras = sbn.getNotification().extras;
//                    if (extras.containsKey("android.text")) {
//                        if (extras.getCharSequence("android.text") != null) {
//                            String text = extras.getCharSequence("android.text").toString();
//                            Log.i(TAG, "------------------------- in onNotificationPosted(), Bundle.text != NULL, so here it is = " + text);
//                        }
//                    }
//                    if (extras.containsKey("android.title")) {
//                        Log.i(TAG, "------------------------- in onNotificationPosted(), Bundle android.title = " + extras.getString("android.title"));
//                    }
//                }
//                Intent i3 = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
//                i3.putExtra("notification_event","===== Notification List ====");
//                sendBroadcast(i3);
            }

        }
    }

}
