package com.recoveryrecord.surveyandroid.example.receiever;//package com.example.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.provider.Settings;
import android.text.PrecomputedText;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.recoveryrecord.surveyandroid.example.config.Constants.DetectTime;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SessionID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.UsingApp;

public class ScreenStateReceiver implements StreamGenerator{
    private static final String TAG = "Main";
    private ScreenStateReceiver.ScreenStateBroadcastReceiver mReceiver;
    private final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String device_id;
    private static Handler mMainThread;
    ScreenStateReceiver _screenStateReceiver;
    private static String ScreenState = "Screen On";
    static Context  context;
    Map<String, Object> sensordb = new HashMap<>();
    final Timestamp current_end = Timestamp.now();
    Date date = new Date(System.currentTimeMillis());
    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    final String time_now = formatter.format(date);
    public void registerScreenStateReceiver(Context context) {
        if (mReceiver == null) {
            mReceiver = new ScreenStateReceiver.ScreenStateBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        context.registerReceiver(mReceiver, filter);
    }

    public class ScreenStateBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println(action);
            device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            final Timestamp current_end = Timestamp.now();
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            final String time_now = formatter.format(date);
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                //code
                //            final String TAG = "MyScrollView";
                Log.e("log: screen status", "Screen on");
//                Toast.makeText(context, "螢幕是開啟的", Toast.LENGTH_SHORT).show();
                ScreenState = "Screen On";
                sensordb.put("Time", time_now);
                sensordb.put("Screen", "Screen On");
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                //code
                Log.e("log: screen status", "Screen off");
//                Toast.makeText(context, "螢幕是關閉的", Toast.LENGTH_SHORT).show();
                ScreenState = "Screen Off";
                sensordb.put("Time", time_now);
                sensordb.put("Screen", "Screen Off");
            }
            sensordb.put("Using APP", UsingApp);
            if(UsingApp == "Using APP")
                sensordb.put("Session", SessionID);
            else
                sensordb.put("Session", -1);
            sensordb.put("device_id", device_id);
            sensordb.put("period", "Trigger Event");
            db.collection("Sensor collection")
                    .document("Sensor")
                    .collection("Screen")
                    .document(device_id + " " + time_now)
                    .set(sensordb);
        }
    }
    public void updateStream() {
        final Timestamp current_end = Timestamp.now();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        final String time_now = formatter.format(date);
        sensordb.put("Time", time_now);
        sensordb.put("Screen", ScreenState);
        sensordb.put("Using APP", UsingApp);
        if(UsingApp == "Using APP")
            sensordb.put("Session", SessionID);
        else
            sensordb.put("Session", -1);
        sensordb.put("device_id", device_id);
        sensordb.put("period", DetectTime);
        db.collection("Sensor collection")
                .document("Sensor")
                .collection("Screen")
                .document(device_id + " " + time_now)
                .set(sensordb);
    }
    public void unregisterScreenStateReceiver(Context context){
        if(mReceiver != null){
            context.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
}