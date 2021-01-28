package com.recoveryrecord.surveyandroid.example;//package com.example.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.PrecomputedText;
import android.util.Log;

public class ScreenStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println(action);
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            //code
//            final String TAG = "MyScrollView";
            Log.d("log: screen status", "Screen on");
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            //code
//            final String TAG = "MyScrollView";
            Log.d("log: screen status", "Screen off");
        }
    }
}