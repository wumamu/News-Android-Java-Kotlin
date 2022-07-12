package com.recoveryrecord.surveyandroid.example.broadcastReceiver;

import static com.recoveryrecord.surveyandroid.example.config.Constants.DEFAULT_NEWS_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationListenerNews extends BroadcastReceiver {
    public void onReceive (Context context , Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        Notification notification = intent.getParcelableExtra(DEFAULT_NEWS_NOTIFICATION) ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel( NEWS_CHANNEL_ID , "NEWS_CHANNEL" , NotificationManager.IMPORTANCE_HIGH) ;
//            notificationChannel.setGroup(GROUP_NEWS);
//            notificationChannel.setDescription("新聞通知");
//            NotificationChannel notificationChannel = new NotificationChannel( ESM_CHANNEL_ID , "NEWS_CHANNEL" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert notificationManager != null;
        notificationManager.notify((int) System.currentTimeMillis() , notification) ;
//        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis() , notification);
//        Notification summaryNotification = new NotificationCompat.Builder(context, DEFAULT_NEWS_CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setStyle(new NotificationCompat.InboxStyle()
//                        .setBigContentTitle("2 new messages")
//                        .setSummaryText("user@example.com"))
//                .setPriority(NotificationCompat.PRIORITY_LOW)
//                .setGroup(GROUP_NEWS)
//                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
//                .setGroupSummary(true)
//                .build();
//        notificationManager.notify(501, summaryNotification);
    }
}