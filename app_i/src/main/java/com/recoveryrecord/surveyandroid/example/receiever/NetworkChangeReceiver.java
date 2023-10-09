package com.recoveryrecord.surveyandroid.example.receiever;

import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_USER_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.DetectTime;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SessionID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.UsingApp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.DbHelper.NetworkChangeReceiverDbHelper;
import com.recoveryrecord.surveyandroid.example.sqlite.Network;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NetworkChangeReceiver implements StreamGenerator{
//    private static final String TAG = "Main";
    private NetworkChangeReceiver.NetworkChangeBroadcastReceiver mReceiver;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String device_id;
    private static String NetworkState = "NA";
    private static double down_speed = 0;
    private static double up_speed = 0;
    @SuppressLint("StaticFieldLeak")
//    static Context  context;
    Map<String, Object> sensordb = new HashMap<>();
//    final Timestamp current_end = Timestamp.now();
//    Date date = new Date(System.currentTimeMillis());
//    @SuppressLint("SimpleDateFormat")
//    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//    final String time_now = formatter.format(date);
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
//        final Timestamp current_end = Timestamp.now();
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        final String time_now = formatter.format(date);
        sensordb.put("Time", time_now);
        sensordb.put("TimeStamp", Timestamp.now());
        sensordb.put("Network", NetworkState);
        sensordb.put("Using APP", UsingApp);
        sensordb.put("down_speed", down_speed);
        sensordb.put("up_speed", up_speed);
        if(UsingApp.equals("Using APP"))
            sensordb.put("Session", SessionID);
        else
            sensordb.put("Session", -1);
        sensordb.put("device_id", device_id);
        sensordb.put("period", DetectTime);
        db.collection("Sensor collection")
                .document("Sensor")
                .collection("Network")
                .document(device_id + " " + time_now)
                .set(sensordb);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        com.recoveryrecord.surveyandroid.example.sqlite.Network mynetwork = new Network();//sqlite//add new to db
        mynetwork.setKEY_TIMESTAMP(Timestamp.now().getSeconds());
        mynetwork.setKEY_DOC_ID(device_id + " " + time_now);
        mynetwork.setKEY_DEVICE_ID(device_id);
        mynetwork.setKEY_USER_ID(sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
        mynetwork.setKEY_SESSION(SessionID);
        mynetwork.setKEY_USING_APP(UsingApp);
        mynetwork.setKEY_NETWORK(NetworkState);
        NetworkChangeReceiverDbHelper dbHandler = new NetworkChangeReceiverDbHelper(context);
        dbHandler.insertNetworkDetailsCreate(mynetwork);
    }

    public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {
        @SuppressLint("HardwareIds")
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {
            device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//            final Timestamp current_end = Timestamp.now();
            Date date = new Date(System.currentTimeMillis());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            final String time_now = formatter.format(date);

//            DocumentReference ref = db.collection("test_users").document(device_id).collection("Sensor collection").document("BlurTooth");
            try {
                if (isOnline(context)) {
                    //                dialog(true);
                    //network speed ####################################################################
                    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    assert connManager != null;
//                    NetworkInfo netInfo = connManager.getActiveNetworkInfo();
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    assert mWifi != null;
                    if (mWifi.isConnected()) {
                        // Do whatever
                        Log.e("log: network status", "connected by wifi");
//                        Toast.makeText(context, "用WiFi連的", Toast.LENGTH_SHORT).show();
                        NetworkState = "Connected by Wifi";
                        sensordb.put("Time", time_now);
                        sensordb.put("TimeStamp", Timestamp.now());
                        sensordb.put("Network", "Connected by Wifi");

                    } else {
                        Log.e("log: network status", "connected by mobile data");
//                        Toast.makeText(context, "用手機網路連的", Toast.LENGTH_SHORT).show();
                        NetworkState = "Connected by Mobile";
                        sensordb.put("Time", time_now);
                        sensordb.put("TimeStamp", Timestamp.now());
                        sensordb.put("Network", "Connected by Mobile");
                    }
                    //should check null because in airplane mode it will be null
                    NetworkCapabilities nc = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        nc = connManager.getNetworkCapabilities(connManager.getActiveNetwork());
                    }
                    assert nc != null;
                    down_speed = nc.getLinkDownstreamBandwidthKbps() / 1000;
                    up_speed = nc.getLinkUpstreamBandwidthKbps() / 1000;
                    sensordb.put("down_speed", down_speed);
                    sensordb.put("up_speed", up_speed);
//                    int downSpeed = nc.getLinkDownstreamBandwidthKbps() / 1000;
//                    int upSpeed = nc.getLinkUpstreamBandwidthKbps() / 1000;
//                    down_speed = downSpeed;
//                    up_speed = upSpeed;
//                    Log.e("log: network speed down", downSpeed + " Mbps");
//                    Log.e("log: network speed up", upSpeed + " Mbps");
                } else {
                    //                dialog(false);
                    Log.e("log: ", "disconnected");
//                    Toast.makeText(context, "斷網了", Toast.LENGTH_SHORT).show();
                    NetworkState = "Disconnected";
                    sensordb.put("Time", time_now);
                    sensordb.put("TimeStamp", Timestamp.now());
                    sensordb.put("Network", "Disconnected");
                }
                sensordb.put("Using APP", UsingApp);
                if(UsingApp.equals("Using APP"))
                    sensordb.put("Session", SessionID);
                else
                    sensordb.put("Session", -1);
                sensordb.put("device_id", device_id);
                sensordb.put("period", "Trigger Event");
                db.collection("Sensor collection")
                        .document("Sensor")
                        .collection("Network")
                        .document(device_id + " " + time_now)
                        .set(sensordb);
                final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                com.recoveryrecord.surveyandroid.example.sqlite.Network mynetwork = new Network();//sqlite//add new to db
                mynetwork.setKEY_TIMESTAMP(Timestamp.now().getSeconds());
                mynetwork.setKEY_DOC_ID(device_id + " " + time_now);
                mynetwork.setKEY_DEVICE_ID(device_id);
                mynetwork.setKEY_USER_ID(sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
                mynetwork.setKEY_SESSION(SessionID);
                mynetwork.setKEY_USING_APP(UsingApp);
                mynetwork.setKEY_NETWORK(NetworkState);
                NetworkChangeReceiverDbHelper dbHandler = new NetworkChangeReceiverDbHelper(context);
                dbHandler.insertNetworkDetailsCreate(mynetwork);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
//            db.collection("test_users")
//                        .document(device_id)
//                        .collection("Sensor collection")
//                        .document(time_now)
//                        .delete();
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