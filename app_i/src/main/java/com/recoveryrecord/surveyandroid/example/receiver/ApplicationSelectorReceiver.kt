package com.recoveryrecord.surveyandroid.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import timber.log.Timber

class ApplicationSelectorReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        var docId: String? = ""
        // Get package manager
        val packageManager = context.packageManager

        intent.extras?.keySet()?.forEach { key ->
            try {
                val packageName = intent.getStringExtra(key)
                Timber.d("Selected app: $packageName")
                packageName?.let {
                    val appInfo = packageManager.getApplicationInfo(it, 0)
                    val appName = packageManager.getApplicationLabel(appInfo)
                    Timber.d("Selected app: $appName")
                }
            } catch (e: NameNotFoundException) {
                Timber.d("Not found name")
            }
        }
    }
}
