package com.recoveryrecord.surveyandroid.example.config

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

object Config {
    //    const val SHAREPREFERENCE_TEST = "com.recoveryrecord.surveyandroid.example.SHAREPREFERENCE_TEST"
//    const val MILLISECONDS_PER_SECOND: Long = 1000
//    const val SECONDS_PER_MINUTE = 60
//    const val PACKAGE_NAME = "com.recoveryrecord.surveyandroid"
    const val DetectTime = 1 * 60 * 1000

    @JvmField
    var UsingApp = "NA"

    @JvmField
    var SessionID = 1

    //    const val SeesionCountDown = (3 * 60 * 1000).toLong()
//    var TimeLeftInMillis = SeesionCountDown
//    var LastPauseTime: SimpleDateFormat? = null
    const val DATE_FORMAT_for_storing = "yyyy-MM-dd HH:mm:ss"
    const val DATE_FORMAT_NOW = "yyyy/MM/dd HH:mm:ss" //yyyy-MM-dd HH:mm:ss Z

    //    const val MILLISECONDS_PER_MINUTE = SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND
    private const val DATE_FORMAT_NOW_SLASH = "yyyy/MM/dd HH:mm:ss Z"

    @JvmStatic
    val currentTimeInMillis: Long
        get() {
            //get timzone
            val tz = TimeZone.getDefault()
            val cal = Calendar.getInstance(tz)
            return cal.timeInMillis
        }
    const val PACKAGE_DIRECTORY_PATH =
        "/Android/data/com.recoveryrecord.surveyandroid/files/ActivityRecognition/"

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun getTimeString(time: Long): String {
        val sdfNow =
            SimpleDateFormat(DATE_FORMAT_NOW_SLASH)
        return sdfNow.format(time)
    }

    @JvmStatic
    fun getTimeString(time: Long, sdf: SimpleDateFormat): String {
        return sdf.format(time)
    }

//    val currentTimeString: String
//        get() = getTimeString(currentTimeInMillis)
//    const val ACTIVITY_DELIMITER = ";;"
//    const val sharedPrefString = "test"
}