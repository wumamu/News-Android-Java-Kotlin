package com.recoveryrecord.surveyandroid.example.receiver;

import static com.recoveryrecord.surveyandroid.example.config.Constants.CURRENT_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.DISCONNECT;
import static com.recoveryrecord.surveyandroid.example.config.Constants.MOBILE;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NETWORK_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NETWORK_STATUE;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USER_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USER_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USING_APP;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USING_APP_OR_NOT;
import static com.recoveryrecord.surveyandroid.example.config.Constants.WIFI;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NetworkChangeReceiver implements StreamGenerator{
    private NetworkChangeReceiver.NetworkChangeBroadcastReceiver mReceiver;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String device_id;
    private static String NetworkState = "NA";


    @SuppressLint("HardwareIds")
    public void registerNetworkReceiver(Context context) {
        if (mReceiver == null) {
            mReceiver = new NetworkChangeReceiver.NetworkChangeBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        context.registerReceiver(mReceiver, filter);
    }

    @Override
    public void updateStream(Context context) {
        Map<String, Object> sensordb = new HashMap<>();
        sensordb.put(CURRENT_TIME, Timestamp.now());
        sensordb.put(NETWORK_COLLECTION, NetworkState);
        sensordb.put(USING_APP_OR_NOT, USING_APP);
        sensordb.put(USER_ID, "");
        sensordb.put(USER_DEVICE_ID, device_id);
        db.collection(NETWORK_COLLECTION).add(sensordb);
    }

    public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {
        @SuppressLint("HardwareIds")
        @Override
        public void onReceive(Context context, Intent intent) {
            device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            Map<String, Object> sensordb = new HashMap<>();
            try {
                if (isOnline(context)) {
                    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    assert mWifi != null;
                    if (mWifi.isConnected()) {
                        NetworkState = WIFI;
                    } else {
                        NetworkState = MOBILE;
                    }
                } else {
                    NetworkState = DISCONNECT;
                }
                sensordb.put(CURRENT_TIME, Timestamp.now());
                sensordb.put(NETWORK_STATUE, NetworkState);
                sensordb.put(USING_APP_OR_NOT, USING_APP);
                sensordb.put(USER_DEVICE_ID, device_id);
                sensordb.put(USER_ID, "");
                db.collection(NETWORK_COLLECTION).add(sensordb);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                assert cm != null;
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    public void unregisterNetworkReceiver(Context context){
        if(mReceiver != null){
            context.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
}