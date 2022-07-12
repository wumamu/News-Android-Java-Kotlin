package com.recoveryrecord.surveyandroid.example.receiever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.DbHelper.RingModeReceiverDbHelper;
import com.recoveryrecord.surveyandroid.example.sqlite.RingMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.recoveryrecord.surveyandroid.example.config.Constants.SHARE_PREFERENCE_USER_ID;
import static com.recoveryrecord.surveyandroid.example.config.ConstantsOld.DetectTime;
import static com.recoveryrecord.surveyandroid.example.config.ConstantsOld.SessionID;
import static com.recoveryrecord.surveyandroid.example.config.ConstantsOld.UsingApp;

public class RingModeReceiver implements StreamGenerator{
        private static final String TAG = "Main";
        private RingModeReceiver.RingModeBroadcastReceiver mReceiver;
        private final FirebaseFirestore db = FirebaseFirestore.getInstance();
        private static String device_id;
        private AudioManager myAudioManager;
        private static String RingerState = "NA";
        static Context  context;
        Map<String, Object> sensordb = new HashMap<>();
        final Timestamp current_end = Timestamp.now();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        final String time_now = formatter.format(date);
        public void registerRingModeReceiver(Context context){
            if(mReceiver == null){
                mReceiver = new RingModeReceiver.RingModeBroadcastReceiver();
            }
            IntentFilter filter = new IntentFilter();
            filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
            device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            context.registerReceiver(mReceiver, filter);
        }

    @Override
    public void updateStream(Context context) {
        final Timestamp current_end = Timestamp.now();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        final String time_now = formatter.format(date);
        sensordb.put("Time", time_now);
        sensordb.put("TimeStamp", Timestamp.now());
        sensordb.put("RingMode", RingerState);
        sensordb.put("Using APP", UsingApp);
        if(UsingApp == "Using APP")
            sensordb.put("Session", SessionID);
        else
            sensordb.put("Session", -1);
        sensordb.put("device_id", device_id);
        sensordb.put("period", DetectTime);
        db.collection("Sensor collection")
                .document("Sensor")
                .collection("Ring Mode")
                .document(device_id + " " + time_now)
                .set(sensordb);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        com.recoveryrecord.surveyandroid.example.sqlite.RingMode myring = new RingMode();//sqlite//add new to db
        myring.setKEY_TIMESTAMP(Timestamp.now().getSeconds());
        myring.setKEY_DOC_ID(device_id + " " + time_now);
        myring.setKEY_DEVICE_ID(device_id);
        myring.setKEY_USER_ID(sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
        myring.setKEY_SESSION(SessionID);
        myring.setKEY_USING_APP(UsingApp);
        myring.setKEY_RING(RingerState);
        RingModeReceiverDbHelper dbHandler = new RingModeReceiverDbHelper(context);
        dbHandler.insertRingModeDetailsCreate(myring);
    }

    class RingModeBroadcastReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                final Timestamp current_end = Timestamp.now();
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                final String time_now = formatter.format(date);
                Map<String, Object> sensordb = new HashMap<>();
                myAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int mod = myAudioManager.getRingerMode();
                if(mod == AudioManager.RINGER_MODE_NORMAL){
                    Log.e("log: Ring Mode", "NORMAL");
//                    Toast.makeText(context, "Ring Mode = NORMAL", Toast.LENGTH_LONG).show();
                    RingerState = "NORMAL";
                    sensordb.put("Time", time_now);
                    sensordb.put("TimeStamp", Timestamp.now());
                    sensordb.put("RingMode", "NORMAL");
                }
                else if(mod == AudioManager.RINGER_MODE_SILENT){
                    Log.e("log: Ring Mode", "SILENT");
//                    Toast.makeText(context, "Ring Mode = SILENT", Toast.LENGTH_LONG).show();
                    RingerState = "SILENT";
                    sensordb.put("Time", time_now);
                    sensordb.put("TimeStamp", Timestamp.now());
                    sensordb.put("RingMode", "SILENT");
                }
                else if(mod == AudioManager.RINGER_MODE_VIBRATE){
                    Log.e("log: Ring Mode", "VIBRATE");
//                    Toast.makeText(context, "Ring Mode = VIBRATE", Toast.LENGTH_LONG).show();
                    RingerState = "VIBRATE";
                    sensordb.put("Time", time_now);
                    sensordb.put("TimeStamp", Timestamp.now());
                    sensordb.put("RingMode", "VIBRATE");
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
                        .collection("Ring Mode")
                        .document(device_id + " " + time_now)
                        .set(sensordb);
                final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                com.recoveryrecord.surveyandroid.example.sqlite.RingMode myring = new RingMode();//sqlite//add new to db
                myring.setKEY_TIMESTAMP(Timestamp.now().getSeconds());
                myring.setKEY_DOC_ID(device_id + " " + time_now);
                myring.setKEY_DEVICE_ID(device_id);
                myring.setKEY_USER_ID(sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
                myring.setKEY_SESSION(SessionID);
                myring.setKEY_USING_APP(UsingApp);
                myring.setKEY_RING(RingerState);
                RingModeReceiverDbHelper dbHandler = new RingModeReceiverDbHelper(context);
                dbHandler.insertRingModeDetailsCreate(myring);
            }
        };
        public void unregisterBluetoothReceiver(Context context){
            if(mReceiver != null){
                context.unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        }
}
