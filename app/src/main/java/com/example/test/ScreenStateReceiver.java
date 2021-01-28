//package com.example.test;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//public class ScreenStateReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//        if (Intent.ACTION_SCREEN_ON.equals(action)) {
//            //code
//            final String TAG = "MyScrollView";
//            Log.d(TAG, "Screen on");
//        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
//            //code
//            final String TAG = "MyScrollView";
//            Log.d(TAG, "Screen off");
//        }
//    }
//}