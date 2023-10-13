package com.recoveryrecord.surveyandroid.example.util

import com.google.firebase.Timestamp
import java.util.Date

fun String.toTimeStamp(): Timestamp {
    val date = Date(this.toLong() * 1000)
    return Timestamp(date)
}