package com.recoveryrecord.surveyandroid.example.receiever;

import android.content.Context;

import com.recoveryrecord.surveyandroid.example.CSVDataRecord.DataRecord;

public interface StreamGenerator<T extends DataRecord>{
    public void updateStream(Context context);
}
