package com.recoveryrecord.surveyandroid.example.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.config.Constants.DEFAULT_NEWS_CHANNEL_NAME
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_CHANNEL_ID

fun createNotificationChannel(notificationManager: NotificationManager, channelId: String) {
    val channel = NotificationChannel(
        channelId,
        DEFAULT_NEWS_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT,
    )
    notificationManager.createNotificationChannel(channel)
}

@SuppressLint("MissingPermission")
fun Activity.showDummyNotification(title: String, text: String) {
    val builder = NotificationCompat.Builder(this, NEWS_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(text)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    with(NotificationManagerCompat.from(this)) {
        notify(1, builder.build())
    }
}