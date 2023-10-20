package com.recoveryrecord.surveyandroid.example.receiver

import android.content.Context
import com.recoveryrecord.surveyandroid.example.CSVDataRecord.DataRecord

interface StreamGenerator<T : DataRecord?> {
    fun updateStream(context: Context?)
}
