package com.recoveryrecord.surveyandroid.example;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_NEWS_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.TestBasicActivity.NOTIFICATION_CHANNEL_ID;

public class NotificationListenerNews extends BroadcastReceiver {
    public void onReceive (Context context , Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        Notification notification = intent.getParcelableExtra(DEFAULT_NEWS_NOTIFICATION) ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel( NEWS_CHANNEL_ID , "NEWS_CHANNEL" , NotificationManager.IMPORTANCE_HIGH) ;
//            NotificationChannel notificationChannel = new NotificationChannel( ESM_CHANNEL_ID , "NEWS_CHANNEL" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert notificationManager != null;
        notificationManager.notify((int) System.currentTimeMillis() , notification) ;
    }
}