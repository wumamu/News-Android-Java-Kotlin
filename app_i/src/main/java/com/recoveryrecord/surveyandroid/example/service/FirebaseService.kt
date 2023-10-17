package com.recoveryrecord.surveyandroid.example.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.recoveryrecord.surveyandroid.example.R
import com.recoveryrecord.surveyandroid.example.activity.NewsModuleActivity
import com.recoveryrecord.surveyandroid.example.config.Constants
import com.recoveryrecord.surveyandroid.example.config.Constants.FCM_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.FCM_TOKEN
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_CHANNEL_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_ID_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.NEWS_MEDIA_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_ADD
import com.recoveryrecord.surveyandroid.example.config.Constants.NOTIFICATION_SKIP
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
import com.recoveryrecord.surveyandroid.example.config.Constants.TRIGGER_BY_KEY
import com.recoveryrecord.surveyandroid.example.config.Constants.TRIGGER_BY_VALUE_NOTIFICATION
import com.recoveryrecord.surveyandroid.example.config.Constants.UPDATE_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.USER_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.VIBRATE_EFFECT
import com.recoveryrecord.surveyandroid.example.model.MediaType
import com.recoveryrecord.surveyandroid.example.model.NotificationData
import com.recoveryrecord.surveyandroid.example.util.createNotificationChannel
import com.recoveryrecord.surveyandroid.example.util.insertRemote
import com.recoveryrecord.surveyandroid.example.util.toTimeStamp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FirebaseService : FirebaseMessagingService() {
    private lateinit var sharedPref: SharedPreferences

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

//    private var mediaPushResult: Set<String> = emptySet()


    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var auth: FirebaseAuth

    companion object {
        private var notificationId = 0
        private val ZERO_TIME = Timestamp(0, 0)

        var deviceId: String = ""

        fun getPushNewsDocId(curNewsId: String): String {
            return "$deviceId$curNewsId"
        }
    }

//    private val sharedPrefChangeListener =
//        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
//            if (key == SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION) {
//                // Update mediaPushResult when the shared preference changes
//                mediaPushResult = sharedPreferences.getStringSet(
//                    SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION,
//                    emptySet()
//                ) ?: emptySet()
//            }
//        }

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        sharedPref = getSharedPreferences(this.packageName + "_preferences", Context.MODE_PRIVATE)
//        sharedPref.registerOnSharedPreferenceChangeListener(sharedPrefChangeListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
        // Unregister the listener when the service is destroyed
//        sharedPref.unregisterOnSharedPreferenceChangeListener(sharedPrefChangeListener)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.d("onMessageReceived")
        Timber.d("From: " + remoteMessage.from)
        Timber.d("Data: " + remoteMessage.data)
//        if (remoteMessage.data.isNotEmpty()) {
//            handleRemoteMessage(remoteMessage.data)
//        }
    }

    override fun onNewToken(newToken: String) {
        Timber.d("Refreshed token: $newToken")
//        token = newToken
        try {
            sharedPref.edit()?.putString(FCM_TOKEN, newToken)?.apply()
            Timber.d("token saved success")
            // Save was successful
        } catch (e: Exception) {
            // Save failed
            Timber.d("token saved failed")
        }
        CoroutineScope(Dispatchers.IO).launch {
            addRemoteFcm(newToken)
        }
    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
        try {
            Timber.i("handleIntent:${intent.toString()}")
            val data = intent?.extras as Bundle
            val remoteMessage = RemoteMessage(data)
            Timber.d("From: " + remoteMessage.from)
            Timber.d("Data: " + remoteMessage.data)

            if (remoteMessage.data.isNotEmpty()) {
                handleRemoteMessage(remoteMessage.data)
            }
        } catch (e: Exception) {
            Timber.e("Error handling intent: ${e.message}")
        }
    }

    private fun handleRemoteMessage(dataMap: MutableMap<String, String>) {
        try {
            val curNoti = NotificationData(
                title = dataMap[Constants.NEWS_TITLE_KEY]!!,
                media = dataMap[NEWS_MEDIA_KEY]!!,
                newsId = dataMap[NEWS_ID_KEY]!!,
                pubDate = dataMap.getOrDefault(Constants.NEWS_PUBDATE, null),
            )
            // filter selected media
            val trigger =
                true // (curNoti.media in mediaPushResult)//checkNotificationPreference(curNoti.media)
//            Timber.d(mediaPushResult.toString())
            CoroutineScope(Dispatchers.IO).launch {
                insertRemotePushNews(curNoti, trigger)
            }
            if (trigger) {
                sendNotification(curNoti)
            } else {
                Timber.d("Did not select current media")
            }
        } catch (e: Exception) {
            Timber.w("Send notification failed $e")
        }
    }

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
        val notificationBuilder = NotificationCompat
            .Builder(this, NEWS_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notification.title)
            .setContentText(MediaType.getChinese(notification.media))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVibrate(VIBRATE_EFFECT)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_RECOMMENDATION)
            .setOnlyAlertOnce(true)
            .setExtras(extras)
        // Do not set group manually, it automatically done for you
        //    .setGroup(GROUP_NEWS)

        createNotificationChannel(notificationManager, NEWS_CHANNEL_ID)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private suspend fun insertRemotePushNews(notification: NotificationData, trigger: Boolean) {
        val docRef =
            db.collection(PUSH_NEWS_COLLECTION).document(getPushNewsDocId(notification.newsId))
        val updateData = hashMapOf<String, Any>(
            PUSH_NEWS_DOC_ID to getPushNewsDocId(notification.newsId),
            PUSH_NEWS_USER_ID to (auth.currentUser?.uid ?: ""),
            PUSH_NEWS_DEVICE_ID to deviceId,
            PUSH_NEWS_TYPE to if (trigger) NOTIFICATION_ADD else NOTIFICATION_SKIP, //NO_VALUE,
            PUSH_NEWS_ID to notification.newsId,
            PUSH_NEWS_TITLE to notification.title,
            PUSH_NEWS_MEDIA to notification.media,
            PUSH_NEWS_PUBDATE to (notification.pubDate?.toTimeStamp() ?: ZERO_TIME),
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

    private suspend fun addRemoteFcm(newToken: String) {
        // TODO sync with ios
        val newData = hashMapOf(
            USER_DEVICE_ID to deviceId,
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