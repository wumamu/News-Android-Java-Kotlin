package com.recoveryrecord.surveyandroid.example.receiever;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.core.content.ContextCompat.getSystemService;

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
    public void updateStream() {
        final Timestamp current_end = Timestamp.now();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        final String time_now = formatter.format(date);
        sensordb.put("Time", time_now);
        sensordb.put("Screen", RingerState);
        db.collection("test_users")
                .document(device_id)
                .collection("Sensor collection")
                .document("Sensor")
                .collection("RingMode")
                .document(time_now)
                .set(sensordb);
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
                    sensordb.put("RingMode", "NORMAL");
                }
                else if(mod == AudioManager.RINGER_MODE_SILENT){
                    Log.e("log: Ring Mode", "SILENT");
//                    Toast.makeText(context, "Ring Mode = SILENT", Toast.LENGTH_LONG).show();
                    RingerState = "SILENT";
                    sensordb.put("Time", time_now);
                    sensordb.put("RingMode", "SILENT");
                }
                else if(mod == AudioManager.RINGER_MODE_VIBRATE){
                    Log.e("log: Ring Mode", "VIBRATE");
//                    Toast.makeText(context, "Ring Mode = VIBRATE", Toast.LENGTH_LONG).show();
                    RingerState = "VIBRATE";
                    sensordb.put("Time", time_now);
                    sensordb.put("RingMode", "VIBRATE");
                }
                db.collection("test_users")
                        .document(device_id)
                        .collection("Sensor collection")
                        .document("Sensor")
                        .collection("RingMode")
                        .document(time_now)
                        .set(sensordb);
            }
        };
        public void unregisterBluetoothReceiver(Context context){
            if(mReceiver != null){
                context.unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        }
}
