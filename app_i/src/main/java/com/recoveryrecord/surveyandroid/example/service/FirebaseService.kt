package com.recoveryrecord.surveyandroid.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.activity.NewsModuleActivity
import com.recoveryrecord.surveyandroid.example.config.Constants.DEFAULT_NEWS_CHANNEL_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.GROUP_NEWS
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_CHANNEL_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_ID_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_MEDIA_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_TITLE_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.NO_VALUE
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_MEDIA_SELECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_DOC_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_MEDIA
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_NOTI_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_OPEN_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_PUBDATE
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_RECEIEVE_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_REMOVE_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_REMOVE_TYPE
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_TITLE
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_TYPE
import com.recoveryrecord.surveyandroid.example.config.Constants.PUSH_NEWS_USER_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.TRIGGER_BY_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.TRIGGER_BY_VALUE_NOTIFICATION
import com.recoveryrecord.surveyandroid.example.config.Constants.UPDATE_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.VIBRATE_EFFECT
import com.recoveryrecord.surveyandroid.example.model.MediaType
import com.recoveryrecord.surveyandroid.example.model.NotificationData
import com.recoveryrecord.surveyandroid.util.insertRemote
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var auth: FirebaseAuth

    companion object {
        private var notificationId = 0
        private const val FCM_TOKEN = "fcm_token"
        private const val FCM_COLLECTION = "FCMToken"
        private val ZERO_TIME = Timestamp(0, 0)

        const val NOTIFICATION_ADD = "target add"
        const val NOTIFICATION_SKIP = "not target"


        var sharedPref: SharedPreferences? = null
        var deviceId: String = ""

        var token: String?
            get() {
                return sharedPref?.getString(FCM_TOKEN, "")
            }
            set(value) {
                try {
                    sharedPref?.edit()?.putString(FCM_TOKEN, value)?.apply()
                    Timber.d("token saved success")
                    // Save was successful
                } catch (e: Exception) {
                    // Save failed
                    Timber.d("token saved failed")
                }
            }

        fun getPushNewsDocId(curNewsId: String): String {
            return "$deviceId$curNewsId"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.d("From: " + remoteMessage.from)
        Timber.d("From: " + remoteMessage.data)
        try {
            val curNoti = NotificationData(
                title = remoteMessage.data[NEWS_TITLE_KEY]!!,
                media = remoteMessage.data[NEWS_MEDIA_KEY]!!,
                newsId = remoteMessage.data[NEWS_ID_KEY]!!
            )
            // filter selected media
            val trigger = checkNotificationPreference(curNoti.media)
            CoroutineScope(Dispatchers.IO).launch {
                insertRemotePushNews(curNoti, trigger)
            }
            if (trigger) {
                sendNotification(curNoti)
            } else {
                Timber.d("Did not select current media")
            }
        } catch (e: Exception) {
            Timber.d("Cast to NotificationData failed $e")
        }
    }

    private fun checkNotificationPreference(curMedia: String): Boolean {
        // english
        val mediaPushResult =
            sharedPref?.getStringSet(SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION, emptySet())
                ?: emptySet()
        return (curMedia in mediaPushResult)
    }


    override fun onNewToken(newToken: String) {
        Timber.d("Refreshed token: $newToken")
        token = newToken
        CoroutineScope(Dispatchers.IO).launch {
            updateRemoteFcm(newToken)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(notification: NotificationData) {
        Timber.d("sendNotification: $notification")

        val intent = Intent(this, NewsModuleActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(TRIGGER_BY_KEY, TRIGGER_BY_VALUE_NOTIFICATION)
        intent.putExtra(NEWS_ID_KEY, notification.newsId)
        intent.putExtra(NEWS_MEDIA_KEY, notification.media)
        val notificationId = getUniqueNotificationId()
        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )
        val extras = Bundle()
        extras.putString(NEWS_ID_KEY, notification.newsId)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, NEWS_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notification.title)
            .setContentText(MediaType.getChinese(notification.media))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVibrate(VIBRATE_EFFECT)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_RECOMMENDATION)
            .setGroup(GROUP_NEWS)
            .setExtras(extras)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NEWS_CHANNEL_ID,
                DEFAULT_NEWS_CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private suspend fun insertRemotePushNews(notification: NotificationData, noti: Boolean) {
        val docRef =
            db.collection(PUSH_NEWS_COLLECTION).document(getPushNewsDocId(notification.newsId))
        val updateData = hashMapOf<String, Any>(
            PUSH_NEWS_DOC_ID to getPushNewsDocId(notification.newsId),
            PUSH_NEWS_USER_ID to (auth.currentUser?.uid ?: ""),
            PUSH_NEWS_DEVICE_ID to deviceId,
            PUSH_NEWS_TYPE to if (noti) NOTIFICATION_ADD else NOTIFICATION_SKIP,
            PUSH_NEWS_ID to notification.newsId,
            PUSH_NEWS_TITLE to notification.title,
            PUSH_NEWS_MEDIA to notification.media,
            // TODO add to python
            PUSH_NEWS_PUBDATE to ZERO_TIME,
            PUSH_NEWS_NOTI_TIME to Timestamp.now(),
            PUSH_NEWS_RECEIEVE_TIME to ZERO_TIME,
            PUSH_NEWS_OPEN_TIME to ZERO_TIME,
            PUSH_NEWS_REMOVE_TIME to ZERO_TIME,
            PUSH_NEWS_REMOVE_TYPE to NO_VALUE,
        )
        insertRemote(docRef, updateData) {
            Timber.d("insertRemotePushNews success")
        }
    }

    private suspend fun updateRemoteFcm(newToken: String) {
        // TODO sync with ios
        val newData = hashMapOf(
//            "device_name" to deviceId,
            FCM_TOKEN to newToken,
            PUSH_MEDIA_SELECTION to List(9) { "${it + 1}" },
            UPDATE_TIME to Timestamp.now(),
//            "u_id" to (auth.currentUser?.uid ?: "")
        )
        insertRemote(db.collection(FCM_COLLECTION).document(newToken), newData) {
            Timber.d("FCM token update success")
        }
    }

    private fun getUniqueNotificationId(): Int {
        return notificationId++
    }
}