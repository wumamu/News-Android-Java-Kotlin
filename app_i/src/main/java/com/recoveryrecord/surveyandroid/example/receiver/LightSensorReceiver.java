package com.recoveryrecord.surveyandroid.example.receiver;

import static com.recoveryrecord.surveyandroid.example.config.Constants.CURRENT_TIME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.LIGHT_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.config.Constants.LIGHT_LEVEL;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USER_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USER_ID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USING_APP;
import static com.recoveryrecord.surveyandroid.example.config.Constants.USING_APP_OR_NOT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class LightSensorReceiver implements StreamGenerator {

    private static LightSensorReceiver instance;
    private SensorManager sensorManager;
    private LightSensorListener lightSensorListener;
    private boolean mHasStarted = false;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String device_id;
    private static float lightLevel = (float) 0.0;

    public LightSensorReceiver() {
    }

    public static LightSensorReceiver getInstance() {
        if (instance == null) {
            instance = new LightSensorReceiver();
        }
        return instance;
    }

    @SuppressLint("HardwareIds")
    public void registerLightSensorReceiver(Context context) {
        if (mHasStarted) {
            return;
        }
        mHasStarted = true;
        sensorManager = (SensorManager) context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor LightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //獲取光線感測器
        if (LightSensor != null) {
            lightSensorListener = new LightSensorListener();
            sensorManager.registerListener(lightSensorListener, LightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    public void unregisterLightSensorReceiver() {
        if (!mHasStarted || sensorManager == null) {
            return;
        }
        mHasStarted = false;
        sensorManager.unregisterListener(lightSensorListener);
    }

    public float getLux() {
        if (lightSensorListener != null) {
            return lightSensorListener.lux;
        }
        return -1.0f;
    }


    @Override

    public void updateStream(Context context) {
        Map<String, Object> sensordb = new HashMap<>();

        sensordb.put(CURRENT_TIME, Timestamp.now());
        sensordb.put(LIGHT_LEVEL, lightLevel);
        sensordb.put(USING_APP_OR_NOT, USING_APP);
        sensordb.put(USER_DEVICE_ID, device_id);
        // TODO
        sensordb.put(USER_ID, "");
//        sensordb.put("period", DetectTime);
        db.collection(LIGHT_COLLECTION)
                .add(sensordb);

    }

    private static class LightSensorListener implements SensorEventListener {
        private float lux; //光線強度

        @Override
        public void onSensorChanged(SensorEvent event) {
            Timber.d("onSensorChanged %s", event);
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                lux = event.values[0];
                lightLevel = lux;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }
}
