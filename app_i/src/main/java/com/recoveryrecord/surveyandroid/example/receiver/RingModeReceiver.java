package com.recoveryrecord.surveyandroid.example.receiver;

import static com.recoveryrecord.surveyandroid.example.config.Constants.CURRENT_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.NORMAL;
import static com.recoveryrecord.surveyandroid.example.config.Constants.RING_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.RING_MODE;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SILENT;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USER_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USER_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USING_APP;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USING_APP_OR_NOT;
import static com.recoveryrecord.surveyandroid.example.config.Constants.VIBRATE;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.provider.Settings;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RingModeReceiver implements StreamGenerator {
    private static final String TAG = "Main";
    private RingModeReceiver.RingModeBroadcastReceiver mReceiver;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String device_id;
    private AudioManager myAudioManager;
    private static String RingerState = "NA";

    @SuppressLint("HardwareIds")
    public void registerRingModeReceiver(Context context) {
        if (mReceiver == null) {
            mReceiver = new RingModeReceiver.RingModeBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
        device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        context.registerReceiver(mReceiver, filter);
    }

    @Override
    public void updateStream(Context context) {
        Map<String, Object> sensordb = new HashMap<>();
        sensordb.put(CURRENT_TIME, Timestamp.now());
        sensordb.put(RING_MODE, RingerState);
        sensordb.put(USING_APP_OR_NOT, USING_APP);
        sensordb.put(USER_DEVICE_ID, device_id);
        sensordb.put(USER_ID, "");
        db.collection(RING_COLLECTION).add(sensordb);
    }

    class RingModeBroadcastReceiver extends BroadcastReceiver {

        @SuppressLint("HardwareIds")
        @Override
        public void onReceive(Context context, Intent intent) {
            device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            Map<String, Object> sensordb = new HashMap<>();
            myAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int mod = myAudioManager.getRingerMode();
            sensordb.put(CURRENT_TIME, Timestamp.now());
            if (mod == AudioManager.RINGER_MODE_NORMAL) {
                RingerState = NORMAL;
            } else if (mod == AudioManager.RINGER_MODE_SILENT) {
                RingerState = SILENT;
            } else if (mod == AudioManager.RINGER_MODE_VIBRATE) {
                RingerState = VIBRATE;
            }
            sensordb.put(RING_MODE, RingerState);
            sensordb.put(USING_APP_OR_NOT, USING_APP);
            sensordb.put(USER_DEVICE_ID, device_id);
            sensordb.put(USER_ID, "");
            db.collection(RING_COLLECTION).add(sensordb);
        }
        };
        public void unregisterRingModeReceiver(Context context) {
            if (mReceiver != null) {
                context.unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        }
}
