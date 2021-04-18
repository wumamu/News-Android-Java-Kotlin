package com.recoveryrecord.surveyandroid.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ShowNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //判断app进程是否存活
        if (Helper.isAppRunning(context, context.getPackageName())) {
            // App is running
            Log.d("apprunning", "1");

        } else {
            // App is not running
            Log.d("apprunning", "2");
        }
    }
}
