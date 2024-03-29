package com.recoveryrecord.surveyandroid.example.broadcastReceiver;

import static com.recoveryrecord.surveyandroid.example.config.Constants.DEFAULT_ESM_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.ESM_CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationListenerESM extends BroadcastReceiver {
    public void onReceive (Context context , Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE) ;
        Notification notification = intent.getParcelableExtra(DEFAULT_ESM_NOTIFICATION) ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(ESM_CHANNEL_ID, "ESM_CHANNEL", NotificationManager.IMPORTANCE_MAX) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert notificationManager != null;
        notificationManager.notify((int) System.currentTimeMillis() , notification) ;
//        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis() , notification);
    }

}