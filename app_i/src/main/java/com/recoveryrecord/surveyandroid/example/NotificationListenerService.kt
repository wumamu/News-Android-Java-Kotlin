package com.recoveryrecord.surveyandroid.example

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.recoveryrecord.surveyandroid.example.Constants.CHINA_TIMES_PACKAGE_NAME
import com.recoveryrecord.surveyandroid.example.Constants.CNA_PACKAGE_NAME
import com.recoveryrecord.surveyandroid.example.Constants.CTS_PACKAGE_NAME
import com.recoveryrecord.surveyandroid.example.Constants.DOC_ID_KEY
import com.recoveryrecord.surveyandroid.example.Constants.EBC_PACKAGE_NAME
import com.recoveryrecord.surveyandroid.example.Constants.ETTODAY_PACKAGE_NAME
import com.recoveryrecord.surveyandroid.example.Constants.LTN_PACKAGE_NAME
import com.recoveryrecord.surveyandroid.example.Constants.MY_APP_PACKAGE_NAME
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_MONITOR_COLLECTION
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_NOTI_TIME
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_PACKAGE_ID
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_SOURCE
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_TEXT
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_TITLE
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_OTHER_APP_COLLECTION
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_PACKAGE_ID
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_PACKAGE_NAME
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_RECEIEVE_TIME
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_REMOVE_TIME
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_REMOVE_TYPE
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_KEY
import com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_NEWS
import com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_COLLECTION
import com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_RECEIEVE_TIME
import com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_REMOVE_TIME
import com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_REMOVE_TYPE
import com.recoveryrecord.surveyandroid.example.Constants.PUSH_SERVICE_COLLECTION
import com.recoveryrecord.surveyandroid.example.Constants.PUSH_SERVICE_RECEIEVE_TIME
import com.recoveryrecord.surveyandroid.example.Constants.SETS_PACKAGE_NAME
import com.recoveryrecord.surveyandroid.example.Constants.STORM_PACKAGE_NAME
import com.recoveryrecord.surveyandroid.example.Constants.UDN_PACKAGE_NAME
import com.recoveryrecord.surveyandroid.example.Constants.USER_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.PACKAGE_NAME
import java.text.DateFormat
import java.util.Date
import java.util.Objects

@SuppressLint("OverrideAbstract")
class NotificationListenerService : NotificationListenerService() {
    private val TAG = this.javaClass.simpleName
    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter()
        filter.addAction("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_SERVICE_EXAMPLE")
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onNotificationPosted(sbn: StatusBarNotification, rankingMap: RankingMap) {
        var is_target = false
        when (sbn.packageName) {
            CHINA_TIMES_PACKAGE_NAME, CNA_PACKAGE_NAME, UDN_PACKAGE_NAME, LTN_PACKAGE_NAME, ETTODAY_PACKAGE_NAME, CTS_PACKAGE_NAME, EBC_PACKAGE_NAME, STORM_PACKAGE_NAME, SETS_PACKAGE_NAME -> is_target =
                true

            else -> {}
        }
        //cancel notification
        if (is_target) {
            cancelNotification(sbn.key)
            Log.i(TAG, sbn.packageName + "being canceled")
        } else {
            Log.i(TAG, sbn.packageName + " not target")
        }
        //MY APP
        if ((sbn.packageName == MY_APP_PACKAGE_NAME)) {
            val current_now = Timestamp.now()
            val db = FirebaseFirestore.getInstance()
            @SuppressLint("HardwareIds") val device_id = Settings.Secure.getString(
                applicationContext.contentResolver, Settings.Secure.ANDROID_ID
            )
            //Log.e(TAG,"********** DEVICEID ***********" + device_id);
            val extras = sbn.notification.extras
            //            String type = "", doc_id = "";
            if (extras.getString(NOTIFICATION_TYPE_KEY) != null && extras.getString(DOC_ID_KEY) != null) {
                val type = Objects.requireNonNull<String?>(extras.getString(NOTIFICATION_TYPE_KEY))
                var doc_id = Objects.requireNonNull<String?>(extras.getString(DOC_ID_KEY))
                var collection_id: String? = ""
                var receieve_field = ""
                var mark = false
                //news
                //service
                if ((type == NOTIFICATION_TYPE_VALUE_NEWS)) {
                    collection_id = PUSH_NEWS_COLLECTION
                    receieve_field = PUSH_NEWS_RECEIEVE_TIME
                    mark = true
                    doc_id = "$device_id $doc_id"
                    //insert
                } else {
                    collection_id = PUSH_SERVICE_COLLECTION
                    receieve_field = PUSH_SERVICE_RECEIEVE_TIME
                }
                //news
                //service
                //esm
                //diary
                if (mark) {
                    val rbRef = db.collection((collection_id)!!).document(doc_id)
                    val finalReceieve_field = receieve_field
                    rbRef.get().addOnCompleteListener(OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            assert(document != null)
                            if (document!!.exists()) {
                                rbRef.update(finalReceieve_field, current_now) //another field
                                    .addOnSuccessListener {
                                        Log.d(
                                            "lognewsselect",
                                            "DocumentSnapshot successfully updated!"
                                        )
                                    }
                                    .addOnFailureListener { e: Exception? ->
                                        Log.w(
                                            "lognewsselect",
                                            "Error updating document",
                                            e
                                        )
                                    }
                            } else {
                                Log.d("lognewsselect", "No such document")
                            }
                        } else {
                            Log.d("lognewsselect", "get failed with ", task.exception)
                        }
                    })
                    val rbRef_check = db.collection(USER_COLLECTION).document(device_id)
                    rbRef_check.update("check_last_$type", current_now)
                        .addOnSuccessListener {
                            Log.d(
                                "log: firebase share",
                                "DocumentSnapshot successfully updated!"
                            )
                        }
                        .addOnFailureListener { e: Exception? ->
                            Log.w(
                                "log: firebase share",
                                "Error updating document",
                                e
                            )
                        }
                }
            }
        } else if (is_target) { //new media
            var check_title = false
            var check_text = false
            Log.d("checking", "NLService")
            val extras = sbn.notification.extras

            val db = FirebaseFirestore.getInstance()
            val mytimestamp = Timestamp.now() //new Timestamp(System.currentTimeMillis());
            @SuppressLint("HardwareIds") val device_id = Settings.Secure.getString(
                applicationContext.contentResolver, Settings.Secure.ANDROID_ID
            )
            val receieve_notification: MutableMap<String, Any> = HashMap()
            receieve_notification[NOTIFICATION_BAR_NEWS_SOURCE] = sbn.packageName
            receieve_notification[NOTIFICATION_BAR_NEWS_NOTI_TIME] = mytimestamp
            receieve_notification[NOTIFICATION_BAR_NEWS_PACKAGE_ID] = sbn.key
            receieve_notification[NOTIFICATION_BAR_NEWS_DEVICE_ID] = device_id
            if (extras.containsKey("android.title")) {
                if (extras.getString("android.title") != null) {
                    receieve_notification[NOTIFICATION_BAR_NEWS_TITLE] =
                        Objects.requireNonNull<String?>(extras.getString("android.title"))
                    check_title = true
                } else {
                    receieve_notification[NOTIFICATION_BAR_NEWS_TITLE] = "null"
                }
            } else {
                receieve_notification[NOTIFICATION_BAR_NEWS_TITLE] = "null"
            }
            if (extras.containsKey("android.text")) {
                if (extras.getCharSequence("android.text") != null) {
                    receieve_notification[NOTIFICATION_BAR_NEWS_TEXT] =
                        extras.getCharSequence("android.text").toString()
                    check_text = true
                } else {
                    receieve_notification[NOTIFICATION_BAR_NEWS_TEXT] = "null"
                }
            } else {
                receieve_notification[NOTIFICATION_BAR_NEWS_TEXT] = "null"
            }
            // if both is null then we don't need it
            if (check_title && check_text) {
                var is_me = false
                if ((device_id == "318f4fea56e7070c") || (device_id == "c067c6c688c792b2") || (device_id == "37824129045c645a")) {
                    // || device_id.equals("3f726664ceaad94f")
                    is_me = true
                    receieve_notification["source"] = device_id
                }
                if (is_me) {
                    db.collection("compare") //.document(formatter.format(date))
                        .add(receieve_notification)
                }
                db.collection(NOTIFICATION_BAR_NEWS_MONITOR_COLLECTION)
                    .document(device_id + " " + sbn.postTime)
                    .set(receieve_notification)
            }
        } else { //other
            @SuppressLint("HardwareIds") val device_id = Settings.Secure.getString(
                applicationContext.contentResolver, Settings.Secure.ANDROID_ID
            )
            val receieve_notification: MutableMap<String, Any> = HashMap()
            receieve_notification[NOTIFICATION_BAR_PACKAGE_NAME] = sbn.packageName
            receieve_notification[NOTIFICATION_BAR_PACKAGE_ID] = sbn.key
            receieve_notification[NOTIFICATION_BAR_RECEIEVE_TIME] = Timestamp.now()
            receieve_notification[NOTIFICATION_BAR_DEVICE_ID] = device_id
            val db = FirebaseFirestore.getInstance()
            db.collection(NOTIFICATION_BAR_OTHER_APP_COLLECTION)
                .document(device_id + " " + sbn.postTime)
                .set(receieve_notification)
        }
    }

    override fun onNotificationRemoved(
        sbn: StatusBarNotification,
        rankingMap: RankingMap,
        reason: Int
    ) {
        Log.i(TAG, "********** onNOtificationRemoved$reason")
        val removeReason = when (reason) {
            1 -> "REASON_CLICK"
            2 -> "REASON_CANCEL"
            3 -> "REASON_CANCEL_ALL"
            4 -> "REASON_ERROR"
            5 -> "REASON_PACKAGE_CHANGED"
            6 -> "REASON_USER_STOPPED"
            7 -> "REASON_PACKAGE_BANNED"
            8 -> "REASON_APP_CANCEL"
            9 -> "REASON_APP_CANCEL_ALL"
            10 -> "REASON_LISTENER_CANCEL"
            11 -> "REASON_LISTENER_CANCEL_ALL"
            12 -> "REASON_GROUP_SUMMARY_CANCELED"
            13 -> "REASON_GROUP_OPTIMIZATION"
            14 -> "REASON_PACKAGE_SUSPENDED"
            15 -> "REASON_PROFILE_TURNED_OFF"
            16 -> "REASON_UNAUTOBUNDLED"
            17 -> "REASON_CHANNEL_BANNED"
            18 -> "REASON_SNOOZED"
            19 -> "REASON_TIMEOUT"
            20 -> "REASON_CHANNEL_REMOVED"
            21 -> "REASON_CLEAR_DATA"
            else -> "NO MATCH"
        }

        Log.i(TAG, DateFormat.getDateTimeInstance().format(Date()) + "\t" + "\t" + sbn.packageName)
        val currentNow = Timestamp.now()
        val db = FirebaseFirestore.getInstance()
        @SuppressLint("HardwareIds") val deviceId = Settings.Secure.getString(
            applicationContext.contentResolver, Settings.Secure.ANDROID_ID
        )
        if (sbn.packageName == PACKAGE_NAME) {
            val extras = sbn.notification.extras
            if (extras.getString(NOTIFICATION_TYPE_KEY) != null && extras.getString(DOC_ID_KEY) != null) {
                val type = Objects.requireNonNull<String?>(extras.getString(NOTIFICATION_TYPE_KEY))
                var doc_id = Objects.requireNonNull<String?>(extras.getString(DOC_ID_KEY))
                var collection_id: String? = ""
                var remove_field = ""
                var remove_type_field = ""
                var mark = false
                if ((type == NOTIFICATION_TYPE_VALUE_NEWS)) {
                    collection_id = PUSH_NEWS_COLLECTION
                    remove_field = PUSH_NEWS_REMOVE_TIME
                    remove_type_field = PUSH_NEWS_REMOVE_TYPE
                    doc_id = "$deviceId $doc_id"
                    mark = true
                }
                if (mark) {
                    val rbRef = db.collection((collection_id)!!).document(doc_id)
                    val finalRemove_field = remove_field
                    val finalRemove_reason: String = removeReason
                    val finalRemove_type_field = remove_type_field
                    rbRef.get().addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                        if (task.isSuccessful) {
                            val document: DocumentSnapshot? = task.result
                            assert(document != null)
                            if (document!!.exists()) {
                                rbRef.update(
                                    finalRemove_field, currentNow,
                                    finalRemove_type_field, finalRemove_reason
                                ) //another field
                                    .addOnSuccessListener { aVoid: Void? ->
                                        Log.d(
                                            "lognewsselect",
                                            "DocumentSnapshot successfully updated!"
                                        )
                                    }
                                    .addOnFailureListener { e: Exception? ->
                                        Log.w(
                                            "lognewsselect",
                                            "Error updating document",
                                            e
                                        )
                                    }
                            } else {
                                Log.d("lognewsselect", "No such document")
                            }
                        } else {
                            Log.d("lognewsselect", "get failed with ", task.getException())
                        }
                    }
                }
            }
        } else {
            val rbRef = db.collection(NOTIFICATION_BAR_OTHER_APP_COLLECTION)
                .document(deviceId + " " + sbn.postTime)
            val removeReason1: String = removeReason
            rbRef.get().addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val document: DocumentSnapshot? = task.result
                    assert(document != null)
                    if (document!!.exists()) {
                        rbRef.update(
                            NOTIFICATION_BAR_REMOVE_TIME, currentNow,
                            NOTIFICATION_BAR_REMOVE_TYPE, removeReason1,
                            NOTIFICATION_BAR_PACKAGE_ID, sbn.getKey()
                        ) //another field
                            .addOnSuccessListener {
                                Log.d(
                                    "lognewsselect",
                                    "DocumentSnapshot successfully updated!"
                                )
                            }
                            .addOnFailureListener { e: Exception? ->
                                Log.w(
                                    "lognewsselect",
                                    "Error updating document",
                                    e
                                )
                            }
                    } else {
                        Log.d("lognewsselect", "No such document")
                    }
                } else {
                    Log.d("lognewsselect", "get failed with ", task.getException())
                }
            }
        }
    }
}