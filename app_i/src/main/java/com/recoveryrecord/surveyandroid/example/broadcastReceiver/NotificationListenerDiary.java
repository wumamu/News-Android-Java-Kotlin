package com.recoveryrecord.surveyandroid.example.broadcastReceiver;

import static com.recoveryrecord.surveyandroid.example.config.Constants.DEFAULT_DIARY_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.DIARY_CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

public class NotificationListenerDiary extends BroadcastReceiver {
    public void onReceive (Context context , Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        Notification notification = intent.getParcelableExtra(DEFAULT_DIARY_NOTIFICATION) ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_HIGH ;
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel( DIARY_CHANNEL_ID , "DIARY_CHANNEL" , NotificationManager.IMPORTANCE_MAX) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert notificationManager != null;
        notificationManager.notify((int) System.currentTimeMillis() , notification) ;
//        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis() , notification);
//        manager.notify(GROUP_NEWS, notification);
//        manager.notify(id, builder.build());
    }
}