package com.recoveryrecord.surveyandroid.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import static java.security.AccessController.getContext;

//import static com.recoveryrecord.surveyandroid.example.NetworkCheckerActivity.dialog;


public class NetworkChangeReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if (isOnline(context)) {
//                dialog(true);
                //network speed ####################################################################
                ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connManager.getActiveNetworkInfo();
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWifi.isConnected()) {
                    // Do whatever
                    Log.e("log: network status", "connected by wifi");
                } else {
                    Log.e("log: network status", "connected by mobile data");
                }
                //should check null because in airplane mode it will be null
                NetworkCapabilities nc = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    nc = connManager.getNetworkCapabilities(connManager.getActiveNetwork());
                }
                int downSpeed = nc.getLinkDownstreamBandwidthKbps()/1000;
                int upSpeed = nc.getLinkUpstreamBandwidthKbps()/1000;
                Log.e("log: network speed down", downSpeed + " Mbps");
                Log.e("log: network speed up", upSpeed + " Mbps");
            } else {
//                dialog(false);
                Log.e("log: ", "disconnected");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}