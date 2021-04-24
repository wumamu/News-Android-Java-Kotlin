package com.recoveryrecord.surveyandroid.example;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.Timestamp;

import java.util.Date;

import static com.recoveryrecord.surveyandroid.example.TestBasicActivity.NOTIFICATION_CHANNEL_ID;

public class NotificationListenerESM extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;
    public void onReceive (Context context , Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        Notification notification = intent.getParcelableExtra( NOTIFICATION ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
//        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
//        int id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        assert notificationManager != null;
        notificationManager.notify((int) System.currentTimeMillis() , notification) ;
    }

}