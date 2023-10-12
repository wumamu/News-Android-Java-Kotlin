package com.recoveryrecord.surveyandroid.example.receiever;

import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_USER_NAME;
import static com.recoveryrecord.surveyandroid.example.config.Constants.DetectTime;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SessionID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.UsingApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.DbHelper.LightSensorReceiverDbHelper;
import com.recoveryrecord.surveyandroid.example.sqlite.LightSensor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LightSensorReceiver implements StreamGenerator{
    private static final boolean DEBUG = true;
    private static final String TAG = "LightSensor";

    private static LightSensorReceiver instance;
    private SensorManager sensorManager;
    private LightSensorListener lightSensorListener;
    private boolean mHasStarted = false;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String device_id;
    private AudioManager myAudioManager;
    private static float LightState = (float) 0.0;
    static Context  context;
    Map<String, Object> sensordb = new HashMap<>();
    final Timestamp current_end = Timestamp.now();
    Date date = new Date(System.currentTimeMillis());
    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    final String time_now = formatter.format(date);

    public LightSensorReceiver(){}

    public static LightSensorReceiver getInstance(){
        if(instance == null){
            instance = new LightSensorReceiver();
        }
        return instance;
    }

    public void registerLightSensorReceiver(Context context){
        if(mHasStarted){
            return;
        }
        mHasStarted = true;
        sensorManager = (SensorManager) context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor LightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //獲取光線感測器
        if(LightSensor != null){
            lightSensorListener = new LightSensorListener();
            sensorManager.registerListener(lightSensorListener, LightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    public void unregisterLightSensorReceiver(Context context){
        if(!mHasStarted || sensorManager == null){
            return;
        }
        mHasStarted = false;
        sensorManager.unregisterListener(lightSensorListener);
    }
    public float getLux(){
        if(lightSensorListener != null){
            return lightSensorListener.lux;
        }
        return -1.0f;
    }


    @Override

    public void updateStream(Context context) {
        Log.e("lux", String.valueOf(LightState));
        final Timestamp current_end = Timestamp.now();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        final String time_now = formatter.format(date);
        sensordb.put("Time", time_now);
        sensordb.put("TimeStamp", Timestamp.now());
        sensordb.put("Light Sensor", LightState);
        sensordb.put("Using APP", UsingApp);
        if(UsingApp == "Using APP")
            sensordb.put("Session", SessionID);
        else
            sensordb.put("Session", -1);
        sensordb.put("device_id", device_id);
        sensordb.put("period", DetectTime);
        db.collection("Sensor collection")
                .document("Sensor")
                .collection("Light Sensor")
                .document(device_id + " " + time_now)
                .set(sensordb);
//        db.collection("test_users")
//                .document(device_id)
//                .collection("Sensor collection")
//                .document("Sensor")
//                .collection("LightSensor")
//                .document(time_now)
//                .set(sensordb);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        com.recoveryrecord.surveyandroid.example.sqlite.LightSensor mylight = new LightSensor();//sqlite//add new to db
        mylight.setKEY_TIMESTAMP(Timestamp.now().getSeconds());
        mylight.setKEY_DOC_ID(device_id + " " + time_now);
        mylight.setKEY_DEVICE_ID(device_id);
        mylight.setKEY_USER_ID(sharedPrefs.getString(SHARE_PREFERENCE_USER_NAME, "尚未設定實驗編號"));
        mylight.setKEY_SESSION(SessionID);
        mylight.setKEY_USING_APP(UsingApp);
        mylight.setKEY_LIGHT(LightState);
        LightSensorReceiverDbHelper dbHandler = new LightSensorReceiverDbHelper(context);
        dbHandler.insertLightDetailsCreate(mylight);
    }

    private class LightSensorListener implements SensorEventListener{
        private float lux; //光線強度
        @Override
        public void onSensorChanged(SensorEvent event) {
//            device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            final Timestamp current_end = Timestamp.now();
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            final String time_now = formatter.format(date);
            Map<String, Object> sensordb = new HashMap<>();
            if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                lux = event.values[0];

                //sensordb.put("Light Sensor", lux);
                LightState = lux;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
