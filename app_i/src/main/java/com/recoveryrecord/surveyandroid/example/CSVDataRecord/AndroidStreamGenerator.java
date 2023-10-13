package com.recoveryrecord.surveyandroid.example.CSVDataRecord;

import android.content.Context;

import com.recoveryrecord.surveyandroid.example.receiver.StreamGenerator;


public class AndroidStreamGenerator<T extends DataRecord>
        implements StreamGenerator<T> {
    protected Context mApplicationContext;

    public AndroidStreamGenerator(Context aApplicationContext) {
        this.mApplicationContext = aApplicationContext;
    }

    public AndroidStreamGenerator() {

    }

    @Override
    public void updateStream(Context context) {

    }
}
