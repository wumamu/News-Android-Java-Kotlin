package com.recoveryrecord.surveyandroid.example;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_DIARY_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_NEWS_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_CHANNEL_ID;

public class NotificationListenerDiary extends BroadcastReceiver {
    public void onReceive (Context context , Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        Notification notification = intent.getParcelableExtra(DEFAULT_DIARY_NOTIFICATION) ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( DIARY_CHANNEL_ID , "DIARY_CHANNEL" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert notificationManager != null;
        notificationManager.notify((int) System.currentTimeMillis() , notification) ;
    }
}