package com.recoveryrecord.surveyandroid.example.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.config.Constants

@RequiresApi(Build.VERSION_CODES.O)
fun createNotificationChannel(notificationManager: NotificationManager) {
    val channel = NotificationChannel(
        Constants.NEWS_CHANNEL_ID,
        Constants.DEFAULT_NEWS_CHANNEL_ID,
        NotificationManager.IMPORTANCE_DEFAULT,
    )
    notificationManager.createNotificationChannel(channel)
}

fun showDummyNotification(context: Context) {
    val builder = NotificationCompat.Builder(context, Constants.TEST_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Congratulations! 🎉🎉🎉")
        .setContentText("You have post a notification to Android 13!!!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    with(NotificationManagerCompat.from(context)) {
        notify(1, builder.build())
    }
}