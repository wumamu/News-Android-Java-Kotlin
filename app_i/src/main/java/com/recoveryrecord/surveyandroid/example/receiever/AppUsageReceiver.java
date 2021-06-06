package com.recoveryrecord.surveyandroid.example.receiever;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static com.recoveryrecord.surveyandroid.example.config.Constants.DetectTime;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SessionID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.UsingApp;

public class
AppUsageReceiver extends Service {
    private Context context;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String device_id;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Handler handler = new Handler();
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            final Timestamp current_end = Timestamp.now();
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            final String time_now = formatter.format(date);
            Map<String, Object> sensordb = new HashMap<>();
            String foregroundActivityName = ForegroundAppUtil.getForegroundActivityName(getApplicationContext());
            Log.e("AppUsage", foregroundActivityName);
            sensordb.put("Time", time_now);
            sensordb.put("AppUsage", foregroundActivityName);
//            Toast.makeText(getApplicationContext(), foregroundActivityName, Toast.LENGTH_SHORT).show();
            handler.postDelayed(r, DetectTime);
            sensordb.put("device_id", device_id);
            sensordb.put("period", DetectTime);
            db.collection("Sensor collection")
                    .document("Sensor")
                    .collection("App Usage")
                    .document(device_id + " " + time_now)
                    .set(sensordb);
//            DocumentReference ref = db.collection("test_users")
//                    .document(device_id)
//                    .collection("Sensor collection")
//                    .document("Sensor");
//
            if(foregroundActivityName.equals("com.recoveryrecord.surveyandroid") == true){
                Log.e("Using NewsMoment?", "YES");
                UsingApp = "Using APP";
//                sensordb.put("Using APP", "Y");

            }else{
                Log.e("Using NewsMoment?", "NO ");
                UsingApp = "Not Using APP";
//                sensordb.put("Using APP", "N");
            }
//            ref.collection("BlueTooth").document(time_now).set(sensordb);
//            ref.collection("LightSensor").document(time_now).set(sensordb);
//            ref.collection("Network").document(time_now).set(sensordb);
//            ref.collection("RingMode").document(time_now).set(sensordb);
//            ref.collection("Screen").document(time_now).set(sensordb);
//            ref.collection("Activity Recognition").document(time_now).set(sensordb);
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(r, DetectTime);
        device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        final Timestamp current_end = Timestamp.now();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        final String time_now = formatter.format(date);
        Map<String, Object> sensordb = new HashMap<>();
        String foregroundActivityName = ForegroundAppUtil.getForegroundActivityName(getApplicationContext());
        Log.e("AppUsage", foregroundActivityName);
        sensordb.put("Time", time_now);
        sensordb.put("AppUsage", foregroundActivityName);
//            Toast.makeText(getApplicationContext(), foregroundActivityName, Toast.LENGTH_SHORT).show();
        sensordb.put("device_id", device_id);
        if(UsingApp == "Using APP")
            sensordb.put("Session", SessionID);
        else
            sensordb.put("Session", -1);
        sensordb.put("period", "Trigger Event");
        db.collection("Sensor collection")
                .document("Sensor")
                .collection("App Usage")
                .document(device_id + " " + time_now)
                .set(sensordb);
        return START_STICKY;
    }

}

