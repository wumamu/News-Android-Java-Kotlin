/*
 * Copyright (c) 2021 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.recoveryrecord.surveyandroid.example.detectedactivity

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.recoveryrecord.surveyandroid.example.config.Constants.ACTIVITY_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.CURRENT_TIME
import com.recoveryrecord.surveyandroid.example.config.Constants.USER_DEVICE_ID
import com.recoveryrecord.surveyandroid.example.config.Constants.USER_ID
import com.recoveryrecord.surveyandroid.example.model.ActivityType
import com.recoveryrecord.surveyandroid.example.util.addRemote
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DetectedActivityReceiver : BroadcastReceiver() {
    @Inject
    lateinit var db: FirebaseFirestore

    private var deviceId = ""

    companion object {
        private const val DETECTED_PENDING_INTENT_REQUEST_CODE = 100
        private const val RELIABLE_CONFIDENCE = 75

        val activityTypes =
            lazy {
                listOf(
                    ActivityType("automotive", DetectedActivity.IN_VEHICLE),
                    ActivityType("cycling", DetectedActivity.ON_BICYCLE),
                    ActivityType("walking", DetectedActivity.ON_FOOT),
                    ActivityType("running", DetectedActivity.RUNNING),
                    ActivityType("stationary", DetectedActivity.STILL),
                    ActivityType("tilting", DetectedActivity.TILTING),
                    ActivityType("Walking", DetectedActivity.WALKING),
                    ActivityType("Unknown", DetectedActivity.UNKNOWN),
                )
            }

        fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, DetectedActivityReceiver::class.java)
            return PendingIntent.getBroadcast(
                context,
                DETECTED_PENDING_INTENT_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }
    }

    @SuppressLint("HardwareIds")
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        if (deviceId.isEmpty()) {
            deviceId =
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        if (ActivityRecognitionResult.hasResult(intent)) {
            val result = ActivityRecognitionResult.extractResult(intent)
            result?.let { handleDetectedActivities(it.probableActivities) }
        }
    }

    private fun handleDetectedActivities(
        detectedActivities: List<DetectedActivity>,
//        context: Context
    ) {
        detectedActivities
            .filter {
                it.type == DetectedActivity.STILL ||
                    it.type == DetectedActivity.WALKING ||
                    it.type == DetectedActivity.RUNNING
            }
            .filter { it.confidence > RELIABLE_CONFIDENCE }
            .run {
                if (isNotEmpty()) {
                    updateActivityData(this[0])
//            showNotification(this[0], context)
                }
            }
    }

    private fun updateActivityData(activity: DetectedActivity) {
        val updateData =
            HashMap<String, Any>().apply {
                activityTypes.value.forEach { put(it.key, false) }
            }.apply {
                put(USER_DEVICE_ID, deviceId)
                put(USER_ID, "")
                put(CURRENT_TIME, Timestamp.now())
            }

        val activityTypeKey = activityTypes.value.find { it.type == activity.type }?.key

        if (activityTypeKey != null) {
            updateData[activityTypeKey] = true
        }
        CoroutineScope(Dispatchers.IO).launch {
            addRemote(db.collection(ACTIVITY_COLLECTION), updateData) {
                Timber.d("Activity Recognition Update Success")
            }
        }
    }

//  private fun showNotification(detectedActivity: DetectedActivity, context: Context) {
//    createNotificationChannel(context)
//    val intent = Intent(context, MainActivity::class.java).apply {
//      putExtra(SUPPORTED_ACTIVITY_KEY, SupportedActivity.fromActivityType(detectedActivity.type))
//    }
//    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent,
//        PendingIntent.FLAG_UPDATE_CURRENT)
//
//    val activity = SupportedActivity.fromActivityType(detectedActivity.type)
//
//    val builder = NotificationCompat.Builder(context, DETECTED_ACTIVITY_CHANNEL_ID)
//        .setSmallIcon(R.drawable.ic_launcher_foreground)
//        .setContentTitle(context.getString(activity.activityText))
//        .setContentText("Your pet is ${detectedActivity.confidence}% sure of it")
//        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        .setContentIntent(pendingIntent)
//        .setOnlyAlertOnce(true)
//        .setAutoCancel(true)
//
//    with(NotificationManagerCompat.from(context)) {
//      notify(DETECTED_ACTIVITY_NOTIFICATION_ID, builder.build())
//    }
//  }

//  private fun createNotificationChannel(context: Context) {
//    // Create the NotificationChannel, but only on API 26+ because
//    // the NotificationChannel class is new and not in the support library
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//      val name = "detected_activity_channel_name"
//      val descriptionText = "detected_activity_channel_description"
//      val importance = NotificationManager.IMPORTANCE_DEFAULT
//      val channel = NotificationChannel(DETECTED_ACTIVITY_CHANNEL_ID, name, importance).apply {
//        description = descriptionText
//        enableVibration(false)
//      }
//      // Register the channel with the system
//      val notificationManager: NotificationManager =
//          context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//      notificationManager.createNotificationChannel(channel)
//    }
//  }
}
