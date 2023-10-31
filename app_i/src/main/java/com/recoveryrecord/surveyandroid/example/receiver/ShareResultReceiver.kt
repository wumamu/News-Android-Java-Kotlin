package com.recoveryrecord.surveyandroid.example.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import androidx.preference.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.recoveryrecord.surveyandroid.example.config.Constants
import com.recoveryrecord.surveyandroid.example.config.Constants.NO_VALUE
import com.recoveryrecord.surveyandroid.example.config.Constants.READING_BEHAVIOR_COLLECTION
import com.recoveryrecord.surveyandroid.example.config.Constants.TMP_ACCESS_DOC_ID
import com.recoveryrecord.surveyandroid.example.util.fetchRemoteOne
import com.recoveryrecord.surveyandroid.example.util.updateRemote
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ShareResultReceiver : BroadcastReceiver() {
    @Inject
    lateinit var db: FirebaseFirestore

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        val componentName: ComponentName? = intent.getParcelableExtra(Intent.EXTRA_CHOSEN_COMPONENT)
        Timber.d("componentName: $componentName")
        try {
            componentName?.let {
                updateRemoteShare(context, it.shortClassName)
            }
            componentName?.packageName?.let { packageName ->
                val packageManager = context.packageManager
                val appInfo =
                    packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                val appName = packageManager.getApplicationLabel(appInfo).toString()

                Timber.d("Selected App Name: $appName")
            }
        } catch (e: NameNotFoundException) {
            Timber.d("Not found name")
        }
    }

    @SuppressLint("HardwareIds")
    private fun updateRemoteShare(
        context: Context,
        appName: String,
    ) {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val docId = sharedPrefs.getString(TMP_ACCESS_DOC_ID, NO_VALUE) ?: NO_VALUE
        if (docId.isBlank()) return
        val docRef = db.collection(READING_BEHAVIOR_COLLECTION).document(docId)
        CoroutineScope(Dispatchers.IO).launch {
            var remoteShareData = mutableListOf<String>()
            fetchRemoteOne(docRef) { documentSnapshot ->
                remoteShareData =
                    documentSnapshot.get(Constants.READING_BEHAVIOR_SHARE) as MutableList<String>
                if (remoteShareData.isNotEmpty()) remoteShareData.removeLast()
                remoteShareData.add(appName)
            }
            val updateData =
                hashMapOf<String, Any>(
                    Constants.READING_BEHAVIOR_SHARE to remoteShareData,
                )
            updateRemote(
                docRef,
                updateData,
                onSuccess = { Timber.d("Update app share") },
            ) { e ->
                Timber.w("Fail to update remote $e")
            }
        }
    }
}
