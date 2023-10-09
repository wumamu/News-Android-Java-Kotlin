package com.recoveryrecord.surveyandroid.example.receiever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.recoveryrecord.surveyandroid.example.Constants

class ApplicationSelectorReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        var docTime: String? = ""
        var deviceId: String? = ""
        intent.extras?.apply {
            deviceId = getString("device_id")
            docTime = getString("doc_time")
            for (key in keySet()) {
                try {
                    val packageName = intent.getStringExtra(key)
                    val packageManager = context.packageManager
                    val appName =
                        packageName?.let {
                            packageManager.getApplicationInfo(
                                it,
                                PackageManager.GET_META_DATA
                            )
                        }
                            ?.let {
                                packageManager.getApplicationLabel(it).toString()
                            }
                    appName?.let {
                        Log.d("ApplicationSelector: ", "Selected App Name $it")
                        updateSharingBehavior(deviceId, docTime, it)
                    } ?: run {
                        Log.e("ApplicationSelector: ", "AppName is null for package ")
                    }


                } catch (e: Exception) {
                    Log.d("ApplicationSelector: ", "Share failed ${e.toString()}");
                }
            }
        }
    }

    private fun updateSharingBehavior(
        deviceId: String?,
        docTime: String?,
        appName: String,
//        onSuccess: () -> Unit,
//        onFailure: (Exception?) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val rbRef =
            db.collection(Constants.READING_BEHAVIOR_COLLECTION).document("$deviceId $docTime")

        rbRef.get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    Log.d("log: firebase", "Success")
                    val shareResult = document["share"] as MutableList<String>?
                    shareResult?.removeAt(shareResult.size - 1)
                    shareResult?.add(appName)

                    rbRef.update("share", shareResult)
                        .addOnSuccessListener { _ ->
                            Log.d("log: firebase share", "DocumentSnapshot successfully updated!")
//                            onSuccess.invoke() // Invoke the success callback
                        }
                        .addOnFailureListener { e ->
                            Log.w("log: firebase share", "Error updating document", e)
                        }
                } else {
                    Log.d("log: firebase share", "No such document")
                }
            } else {
                Log.d("log: firebase share", "get failed with ", task.exception)
            }
        }
    }
}