package com.recoveryrecord.surveyandroid.example.config;

import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static java.security.AccessController.getContext;

public class Constants {
    public static final String SHAREPREFERENCE_TEST = "com.recoveryrecord.surveyandroid.example.SHAREPREFERENCE_TEST";
    public static final long MILLISECONDS_PER_SECOND = 1000;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int DetectTime = 1*60*1000;
    public static String UsingApp = "NA";
    public static int SessionID = 1;
    public static final long SeesionCountDown = 3*60*1000;
    public static long TimeLeftInMillis = SeesionCountDown;
    public static SimpleDateFormat LastPauseTime;
    public final static String DATE_FORMAT_for_storing = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_FORMAT_NOW = "yyyy/MM/dd HH:mm:ss";//yyyy-MM-dd HH:mm:ss Z
    public static final long MILLISECONDS_PER_MINUTE = SECONDS_PER_MINUTE*MILLISECONDS_PER_SECOND;
    public final static String DATE_FORMAT_NOW_SLASH = "yyyy/MM/dd HH:mm:ss Z";
    public static long getCurrentTimeInMillis(){
        //get timzone
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = Calendar.getInstance(tz);
        long t = cal.getTimeInMillis();
        return t;
    }
    public static final String PACKAGE_DIRECTORY_PATH= "/Android/data/com.recoveryrecord.surveyandroid/files/ActivityRecognition/";
    public static String getTimeString(long time){

        SimpleDateFormat sdf_now = new SimpleDateFormat(DATE_FORMAT_NOW_SLASH);
        String currentTimeString = sdf_now.format(time);

        return currentTimeString;
    }

    public static String getTimeString(long time, SimpleDateFormat sdf){

        String currentTimeString = sdf.format(time);

        return currentTimeString;
    }

    public static String getCurrentTimeString() {

        return getTimeString(getCurrentTimeInMillis());
    }
    public static final String ACTIVITY_DELIMITER = ";;";
    public static final String sharedPrefString = "test";
}
