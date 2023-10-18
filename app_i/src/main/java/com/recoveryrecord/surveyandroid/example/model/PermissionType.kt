package com.recoveryrecord.surveyandroid.example.model

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

enum class PermissionType(
    val code: Int, val string: String, val minSDK: Int
) {
    @RequiresApi(Build.VERSION_CODES.Q)
    ACTIVITY_RECOGNITION(1000, Manifest.permission.ACTIVITY_RECOGNITION, Build.VERSION_CODES.Q),

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    NOTIFICATION_PERMISSION(
        112,
        Manifest.permission.POST_NOTIFICATIONS,
        Build.VERSION_CODES.TIRAMISU
    )
}