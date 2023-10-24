package com.recoveryrecord.surveyandroid.example.model

import com.google.firebase.Timestamp
import com.recoveryrecord.surveyandroid.example.config.Constants.NO_VALUE

data class News(
    var title: String? = null,
    var media: String? = null,
    var id: String? = null,
    var image: String? = null,
    var pubDate: Timestamp? = null,
    var category: String? = null,
    var url: String? = null,
    var tags: List<String>? = null,
    var content: List<String>? = null,
    var wordCount: Int = 0,
    var update: Timestamp? = null,
) {
    val isValid: Boolean
        get() =
            title != null && media != null && id != null && pubDate != null &&
                title != NO_VALUE && media != NO_VALUE && id != NO_VALUE
}
