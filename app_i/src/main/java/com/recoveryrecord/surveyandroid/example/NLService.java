package com.recoveryrecord.surveyandroid.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Date;
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

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Log.i(TAG,"**********  onNotificationPosted");
//        Log.i(TAG, java.text.DateFormat.getDateTimeInstance().format(new Date()) + " ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Log.i(TAG, java.text.DateFormat.getDateTimeInstance().format(new Date()) + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new  Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
//        i.putExtra("notification_event","onNotificationPosted :" + sbn.getPackageName() + "\n");
        String to_post = "Posted: " + java.text.DateFormat.getDateTimeInstance().format(new Date()) + " " + sbn.getPackageName() + ": " + sbn.getNotification().tickerText + "\n";
        i.putExtra("notification_event",to_post);
        sendBroadcast(i);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNOtificationRemoved");
//        Log.i(TAG, System.currentTimeMillis() + "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
        Log.i(TAG, java.text.DateFormat.getDateTimeInstance().format(new Date()) + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new  Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
//        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");
        String to_post = "Removed: " + java.text.DateFormat.getDateTimeInstance().format(new Date()) + " " + sbn.getPackageName() + ": " + sbn.getNotification().tickerText + "\n";
        i.putExtra("notification_event",to_post);

        sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                NLService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                Intent i1 = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
                i1.putExtra("notification_event","=====================");
                sendBroadcast(i1);
                int i=1;
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    Intent i2 = new  Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
                    i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "\n");
                    sendBroadcast(i2);
                    i++;
                }
                Intent i3 = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
                i3.putExtra("notification_event","===== Notification List ====");
                sendBroadcast(i3);

            }

        }
    }

}
