package com.recoveryrecord.surveyandroid.example.model

import com.recoveryrecord.surveyandroid.example.config.Constants

data class ReadingBehavior(
    var docId: String = "NA",
    var deviceId: String = "NA",
    var userId: String = "NA",
    var selectEsmId: String = "NA",
    var triggerBy: String = "NA",
    var newsId: String = "NA",
    var title: String = "NA",
    var media: String = "NA",
    var hasImg: Int = 0,
    var pubdate: Long = 0,
    var rowSpacing: Int = 0,
    var bytePerLine: Int = 0,
    var fontSize: String = "NA",
    var contentLength: Int = 0,
    var displayWidth: Float = 0f,
    var displayHeight: Float = 0f,
    var inTimestamp: Long = 0,
    var outTimestamp: Long = 0,
    var timeOnPage: Long = 0,
    var pauseOnPage: Int = 0,
    var viewPortNum: Int = 0,
    var viewPortRecord: String = "NA",
    var flingNum: Int = 0,
    var flingRecord: String = "NA",
    var dragNum: Int = 0,
    var dragRecord: String = "NA",
    var share: String = "NA",
    var timeSeries: String = "NA",
) {
    fun isSelfTrigger() = triggerBy != Constants.READING_BEHAVIOR_TRIGGER_BY_NOTIFICATION

    fun hasImage() = (hasImg == 1)
}
