//package com.recoveryrecord.surveyandroid.example.receiever;
//
//import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.media.AudioManager;
//import android.provider.Settings;
//import android.util.Log;
//
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.recoveryrecord.surveyandroid.example.config.Constants.UsingApp;
//
//public class OrientationReceiver implements StreamGenerator{
//    private static final String TAG = "Orientation";
//
//    private static OrientationReceiver instance;
//    private SensorManager sensorManager;
//
//    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private static String device_id;
//    private AudioManager myAudioManager;
//    private static float LightState = (float) 0.0;
//    static Context context;
//    Map<String, Object> sensordb = new HashMap<>();
//    final Timestamp current_end = Timestamp.now();
//    Date date = new Date(System.currentTimeMillis());
//    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//    final String time_now = formatter.format(date);
//
//
//    public static OrientationReceiver getInstance(){
//        if(instance == null){
//            instance = new OrientationReceiver();
//        }
//        return instance;
//    }
//
//    public void registerOrientationReceiver(Context context){
//
//        sensorManager = (SensorManager) context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
//        Sensor LightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //獲取光線感測器
//        if(LightSensor != null){
//
//            sensorManager.registerListener(, , SensorManager.SENSOR_DELAY_NORMAL);
//        }
//        device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//    }
//
//    public void unregisterOrientationReceiver(Context context){
//
//        sensorManager.unregisterListener();
//    }
//
//
//    @Override
//
//    public void updateStream() {
//        Log.e("lux", String.valueOf(LightState));
//        final Timestamp current_end = Timestamp.now();
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        final String time_now = formatter.format(date);
//        sensordb.put("Time", time_now);
//        sensordb.put("Orientation", LightState);
//        sensordb.put("Using APP", UsingApp);
//        db.collection("test_users")
//                .document(device_id)
//                .collection("Sensor collection")
//                .document("Sensor")
//                .collection("Orientation")
//                .document(time_now)
//                .set(sensordb);
//    }
//
//}
