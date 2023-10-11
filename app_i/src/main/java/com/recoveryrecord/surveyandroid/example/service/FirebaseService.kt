package com.recoveryrecord.surveyandroid.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.recoveryrecord.surveyandroid.example.Constants.NEWS_ID_KEY
import com.recoveryrecord.surveyandroid.example.Constants.NEWS_MEDIA_KEY
import com.recoveryrecord.surveyandroid.example.Constants.NEWS_TITLE_KEY
import com.recoveryrecord.surveyandroid.example.Constants.PUSH_MEDIA_SELECTION
import com.recoveryrecord.surveyandroid.example.Constants.TRIGGER_BY_KEY
import com.recoveryrecord.surveyandroid.example.Constants.TRIGGER_BY_VALUE_NOTIFICATION
import com.recoveryrecord.surveyandroid.example.Constants.UPDATE_TIME
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.activity.NewsModuleActivity
import com.recoveryrecord.surveyandroid.example.ui.MediaType
import kotlin.random.Random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class FirebaseService : FirebaseMessagingService() {

    companion object {
        const val NEWS_CHANNEL = "News Channel"
        private const val FCM_TOKEN = "fcm_token"

        var sharedPref: SharedPreferences? = null
        var ref: DocumentReference? = null

        var token: String?
            get() {
                return sharedPref?.getString(FCM_TOKEN, "")
            }
            set(value) {
                sharedPref?.edit()?.putString(FCM_TOKEN, value)?.apply()
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.d("From: " + remoteMessage.from)
        Timber.d("From: " + remoteMessage.data)
        sendNotification(remoteMessage.data)
    }

    override fun onNewToken(newToken: String) {
        Timber.d("Refreshed token: " + newToken)
        token = newToken
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(messageBody: Map<String, String>?) {
        Timber.d("sendNotification: " + messageBody)

        val intent = Intent(this, NewsModuleActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(TRIGGER_BY_KEY, TRIGGER_BY_VALUE_NOTIFICATION)
        intent.putExtra(NEWS_ID_KEY, messageBody?.get(NEWS_ID_KEY))
        intent.putExtra(NEWS_MEDIA_KEY, messageBody?.get(NEWS_MEDIA_KEY))

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
            .setContentTitle(messageBody?.get(NEWS_TITLE_KEY))
            .setContentText(MediaType.getChinese(messageBody?.get(NEWS_MEDIA_KEY)))
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
                NEWS_CHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private suspend fun updateRemoteFcm(newToken: String) {
        try {
            ref?.apply {
                withContext(Dispatchers.IO) {
                    this@apply.update(
                        FCM_TOKEN, newToken,
                        PUSH_MEDIA_SELECTION, "",
                        UPDATE_TIME, Timestamp.now()
                    ).await()
                }
            }
        } catch (e: Exception) {
            Timber.d("get failed with " + e)

        }
    }


//    private suspend fun updateMediaOrder(newMediaOrder: String) {
//        try {
//            remoteMediaOrder?.apply {
//                add(newMediaOrder)
//                withContext(Dispatchers.IO) {
//                    ref.update(Constants.MEDIA_BAR_ORDER, this@apply).await()
//                }
//            }
//        } catch (e: Exception) {
//            Timber.d("get failed with " + e)
//
//        }
//    }
//
//    private suspend fun getRemoteMediaOrder() {
//        try {
//            val documentSnapshot = withContext(Dispatchers.IO) { ref.get().await() }
//            if (documentSnapshot.exists()) {
//                remoteMediaOrder =
//                    documentSnapshot[Constants.MEDIA_BAR_ORDER] as MutableList<String>?
//            }
//        } catch (e: Exception) {
//            Timber.d("get failed with " + e)
//        }
//    }
}