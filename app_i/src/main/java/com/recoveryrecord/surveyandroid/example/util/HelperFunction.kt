package com.recoveryrecord.surveyandroid.example.util

import com.google.firebase.Timestamp
import java.util.Date

fun String.toTimeStamp(): Timestamp? {
    return try {
        val date = Date(this.toLong() * 1000)
        Timestamp(date) // Firebase Timestamp
    } catch (e: NumberFormatException) {
        // Handle the case where the input string is not a valid long
        null
    } catch (e: Exception) {
        // Handle other exceptions that may occur
        null
    }
}