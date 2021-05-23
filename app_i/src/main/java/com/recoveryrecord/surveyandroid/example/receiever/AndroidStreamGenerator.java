package com.recoveryrecord.surveyandroid.example.receiever;

import android.content.Context;
import android.util.Log;

import com.recoveryrecord.surveyandroid.example.receiever.StreamGenerator;

public abstract class AndroidStreamGenerator implements StreamGenerator{
    protected Context mApplicationContext;

    public AndroidStreamGenerator(Context aApplicationContext) {
        this.mApplicationContext = aApplicationContext;
    }

    public AndroidStreamGenerator() {
    }
}
