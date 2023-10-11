package com.recoveryrecord.surveyandroid.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.recoveryrecord.surveyandroid.example.Constants.NEWS_ID_KEY
import com.recoveryrecord.surveyandroid.example.Constants.NEWS_MEDIA_KEY
import com.recoveryrecord.surveyandroid.example.Constants.TRIGGER_BY_KEY
import com.recoveryrecord.surveyandroid.example.Constants.TRIGGER_BY_VALUE_NOTIFICATION
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.activity.NewsModuleActivity
import com.recoveryrecord.surveyandroid.example.model.NotificationData
import com.recoveryrecord.surveyandroid.example.ui.MediaType
import kotlin.random.Random

class FirebaseService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        var sharedPref: SharedPreferences? = null

        var token: String?
            get() {
                return sharedPref?.getString("token", "")
            }
            set(value) {
                sharedPref?.edit()?.putString("token", value)?.apply()
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "From: ${remoteMessage.data}")
        sendNotification(remoteMessage.data)
    }

    override fun onNewToken(newToken: String) {
        Log.d(TAG, "Refreshed token: $newToken")
        token = newToken
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(messageBody: Map<String, String>?) {
        Log.d(TAG, "sendNotification: $messageBody")

        val notificationData = NotificationData(
            title = "殺警犯服外役監爆重大弊案　涉隱匿易寶宏前科", //messageBody["title"],
            media = MediaType.Setn.englishId,
            newsId = "000114e70fbc1b3a88d7f0d46ab7354b59cf429d"
        )
        val intent = Intent(this, NewsModuleActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(TRIGGER_BY_KEY, TRIGGER_BY_VALUE_NOTIFICATION)
        intent.putExtra(NEWS_ID_KEY, notificationData.newsId)
        intent.putExtra(NEWS_MEDIA_KEY, notificationData.media)

        val notificationId = Random.nextInt()
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notificationData.title)
            .setContentText(MediaType.getChinese(notificationData.media))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "News Channel",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}