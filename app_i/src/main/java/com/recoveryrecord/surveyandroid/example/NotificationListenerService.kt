package com.recoveryrecord.surveyandroid.example

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Build
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.recoveryrecord.surveyandroid.example.Constants.NEWS_ID_KEY
import com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_COLLECTION
import com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_RECEIEVE_TIME
import com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_REMOVE_TIME
import com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_REMOVE_TYPE
import com.recoveryrecord.surveyandroid.util.updateRemote
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class NotificationListenerService : NotificationListenerService() {

    @Inject
    lateinit var db: FirebaseFirestore

    companion object {
        private val notificationRemoveReason = lazy {
            mapOf(
                1 to "REASON_CLICK",
                2 to "REASON_CANCEL",
                3 to "REASON_CANCEL_ALL",
                4 to "REASON_ERROR",
                5 to "REASON_PACKAGE_CHANGED",
                6 to "REASON_USER_STOPPED",
                7 to "REASON_PACKAGE_BANNED",
                8 to "REASON_APP_CANCEL",
                9 to "REASON_APP_CANCEL_ALL",
                10 to "REASON_LISTENER_CANCEL",
                11 to "REASON_LISTENER_CANCEL_ALL",
                12 to "REASON_GROUP_SUMMARY_CANCELED",
                13 to "REASON_GROUP_OPTIMIZATION",
                14 to "REASON_PACKAGE_SUSPENDED",
                15 to "REASON_PROFILE_TURNED_OFF",
                16 to "REASON_UNAUTOBUNDLED",
                17 to "REASON_CHANNEL_BANNED",
                18 to "REASON_SNOOZED",
                19 to "REASON_TIMEOUT",
                20 to "REASON_CHANNEL_REMOVED",
                21 to "REASON_CLEAR_DATA"
            )
        }
        private val notificationTarget = lazy {
            setOf(
                Constants.CHINA_TIMES_PACKAGE_NAME,
                Constants.CNA_PACKAGE_NAME,
                Constants.UDN_PACKAGE_NAME,
                Constants.LTN_PACKAGE_NAME,
                Constants.ETTODAY_PACKAGE_NAME,
                Constants.CTS_PACKAGE_NAME,
                Constants.EBC_PACKAGE_NAME,
                Constants.STORM_PACKAGE_NAME,
                Constants.SETS_PACKAGE_NAME
            )
        }
        var deviceId: String = ""
        fun getPushNewsDocId(curNewsId: String): String {
            return "$deviceId$curNewsId"
        }
    }

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter()
        filter.addAction("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_SERVICE_EXAMPLE")
        deviceId = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onNotificationPosted(sbn: StatusBarNotification, rankingMap: RankingMap) {
        val isTarget = notificationTarget.value.contains(sbn.packageName)
        // cancel notification
        if (isTarget) {
            cancelNotification(sbn.key)
            Timber.d(sbn.packageName + "being canceled")
        }
        // my notification
        if (sbn.packageName == PACKAGE_NAME) {
            val notificationNewId = sbn.notification.extras?.getString(NEWS_ID_KEY)
            notificationNewId?.let { newsId ->
                val updateData = hashMapOf<String, Any>(
                    PUSH_NEWS_RECEIEVE_TIME to Timestamp.now(),
                )
                CoroutineScope(Dispatchers.IO).launch {
                    updateRemote(
                        db.collection(PUSH_NEWS_COLLECTION).document(getPushNewsDocId(newsId)),
                        updateData
                    ) {
                        Timber.d("onNotificationReceived update success")
                    }
                }
            }
        } else if (isTarget) {
            // TODO Save monitor data to firstore
            Timber.d(
                "onNotificationReceived is target ${sbn.packageName}" +
                        " ${sbn.notification.extras?.getString("android.title")}" +
                        " ${sbn.notification.extras?.getString("android.text")}"
            )

            //new media
//            var check_title = false
//            var check_text = false
//            Log.d("checking", "NLService")
//            val extras = sbn.notification.extras
//
//            val db = FirebaseFirestore.getInstance()
//            val mytimestamp = Timestamp.now() //new Timestamp(System.currentTimeMillis());
//            @SuppressLint("HardwareIds") val device_id = Settings.Secure.getString(
//                applicationContext.contentResolver, Settings.Secure.ANDROID_ID
//            )
//            val receieve_notification: MutableMap<String, Any> = HashMap()
//            receieve_notification[NOTIFICATION_BAR_NEWS_SOURCE] = sbn.packageName
//            receieve_notification[NOTIFICATION_BAR_NEWS_NOTI_TIME] = mytimestamp
//            receieve_notification[NOTIFICATION_BAR_NEWS_PACKAGE_ID] = sbn.key
//            receieve_notification[NOTIFICATION_BAR_NEWS_DEVICE_ID] = device_id
//            if (extras.containsKey("android.title")) {
//                if (extras.getString("android.title") != null) {
//                    receieve_notification[NOTIFICATION_BAR_NEWS_TITLE] =
//                        Objects.requireNonNull<String?>(extras.getString("android.title"))
//                    check_title = true
//                } else {
//                    receieve_notification[NOTIFICATION_BAR_NEWS_TITLE] = "null"
//                }
//            } else {
//                receieve_notification[NOTIFICATION_BAR_NEWS_TITLE] = "null"
//            }
//            if (extras.containsKey("android.text")) {
//                if (extras.getCharSequence("android.text") != null) {
//                    receieve_notification[NOTIFICATION_BAR_NEWS_TEXT] =
//                        extras.getCharSequence("android.text").toString()
//                    check_text = true
//                } else {
//                    receieve_notification[NOTIFICATION_BAR_NEWS_TEXT] = "null"
//                }
//            } else {
//                receieve_notification[NOTIFICATION_BAR_NEWS_TEXT] = "null"
//            }
//            // if both is null then we don't need it
//            if (check_title && check_text) {
//                var is_me = false
//                if ((device_id == "318f4fea56e7070c") || (device_id == "c067c6c688c792b2") || (device_id == "37824129045c645a")) {
//                    // || device_id.equals("3f726664ceaad94f")
//                    is_me = true
//                    receieve_notification["source"] = device_id
//                }
//                if (is_me) {
//                    db.collection("compare") //.document(formatter.format(date))
//                        .add(receieve_notification)
//                }
//                db.collection(NOTIFICATION_BAR_NEWS_MONITOR_COLLECTION)
//                    .document(device_id + " " + sbn.postTime)
//                    .set(receieve_notification)
//            }
        }
    }

    override fun onNotificationRemoved(
        sbn: StatusBarNotification,
        rankingMap: RankingMap,
        reason: Int
    ) {
        val removeReason = notificationRemoveReason.value[reason] ?: "UNKNOWN"
        Timber.d("onNotificationRemoved $removeReason ${sbn.packageName}")

        if (sbn.packageName == PACKAGE_NAME) {
            val notificationNewId = sbn.notification.extras?.getString(NEWS_ID_KEY)
            notificationNewId?.let { newsId ->
                val updateData = hashMapOf<String, Any>(
                    PUSH_NEWS_REMOVE_TIME to Timestamp.now(),
                    PUSH_NEWS_REMOVE_TYPE to removeReason
                )
                CoroutineScope(Dispatchers.IO).launch {
                    updateRemote(
                        db.collection(PUSH_NEWS_COLLECTION).document(getPushNewsDocId(newsId)),
                        updateData
                    ) {
                        Timber.d("onNotificationRemoved update success")
                    }
                }
            }
        }
    }
}