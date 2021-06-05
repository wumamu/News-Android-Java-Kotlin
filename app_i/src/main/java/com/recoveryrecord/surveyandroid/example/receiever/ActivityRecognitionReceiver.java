package com.recoveryrecord.surveyandroid.example.receiever;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static android.icu.lang.UCharacter.IndicPositionalCategory.NA;
import static com.recoveryrecord.surveyandroid.example.NotificationScheduler.TAG;

public class ActivityRecognitionReceiver extends IntentService implements StreamGenerator{
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String device_id;

    public ActivityRecognitionReceiver() {
        super("ActivityRecognitionReceiver");
    }
    public ActivityRecognitionReceiver(String name) {
        super(name);
    }
    public static final String STRING_DETECTED_ACTIVITY_IN_VEHICLE = "in_vehicle";
    public static final String STRING_DETECTED_ACTIVITY_ON_FOOT = "on_foot";
    public static final String STRING_DETECTED_ACTIVITY_WALKING = "walking";
    public static final String STRING_DETECTED_ACTIVITY_RUNNING = "running";
    public static final String STRING_DETECTED_ACTIVITY_TILTING = "tilting";
    public static final String STRING_DETECTED_ACTIVITY_STILL = "still";
    public static final String STRING_DETECTED_ACTIVITY_ON_BICYCLE = "on_bicycle";
    public static final String STRING_DETECTED_ACTIVITY_UNKNOWN = "unknown";
    public static final String STRING_DETECTED_ACTIVITY_NA = "NA";
    public static final int NO_ACTIVITY_TYPE = -1;
//    private ActivityRecognitionDataRecord activityRecognitionDataRecord;
    final Timestamp current_end = Timestamp.now();
    Date date = new Date(System.currentTimeMillis());
    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    final String time_now = formatter.format(date);
    Map<String, Object> sensordb = new HashMap<>();
    Intent intent;
    private static int Vehicle = 0;
    private static int Bicycle = 0;
    private static int Foot = 0;
    private static int Running = 0;
    private static int Still = 0;
    private static int Tilting = 0;
    private static int Walking = 0;
    private static int Unknown = 0;
    public void onCreate() {
        super.onCreate();
        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        sensordb.put("Time", time_now);
        sensordb.put("Activity Recognition", "NA");
        db.collection("test_users")
                .document(device_id)
                .collection("Sensor collection")
                .document("Sensor")
                .collection("Activity Recognition")
                .document(time_now)
                .set(sensordb);

        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
//        handleDetectedActivities(result.getProbableActivities());
    }
    @Override
    public void updateStream() {
//        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        final Timestamp current_end = Timestamp.now();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        final String time_now = formatter.format(date);
        sensordb.put("Time", time_now);
        sensordb.put("Activity Recognition 1", "In Vehicle: " + Vehicle);
        sensordb.put("Activity Recognition 2", "On Bicycle: " + Bicycle);
        sensordb.put("Activity Recognition 3", "On Foot: " + Foot);
        sensordb.put("Activity Recognition 4", "Running: " + Running);
        sensordb.put("Activity Recognition 5", "Still: " + Still);
        sensordb.put("Activity Recognition 6", "Tilting: " + Tilting);
        sensordb.put("Activity Recognition 7", "Walking: " + Walking);
        sensordb.put("Activity Recognition 8", "Unknown: " + Unknown);
        db.collection("test_users")
                .document(device_id)
                .collection("Sensor collection")
                .document("Sensor")
                .collection("Activity Recognition")
                .document(time_now)
                .set(sensordb);
    }
    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        final DocumentReference ref = db.collection("test_users")
                .document(device_id)
                .collection("Sensor collection")
                .document("Sensor")
                .collection("Activity Recognition")
                .document(time_now);
//        ref.update("Activity Recognition", "開始偵測");
        Handler handler = new Handler(Looper.getMainLooper());
        for (final DetectedActivity activity : probableActivities) {
            handler.post(new Runnable(){
                public void run(){
//                    Toast.makeText(getApplicationContext(), "開始偵測", Toast.LENGTH_LONG).show();
                }
            });
            switch (activity.getType()) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e("ActivityRecognition", "In Vehicle: " + activity.getConfidence());
//                    sensordb.put("ActivityRecognition", "In Vehicle: " + activity.getConfidence());
                    ref.update("Activity Recognition 1", "In Vehicle: " + activity.getConfidence());
                    Vehicle = activity.getConfidence();
                    Bicycle = 0;
                    Foot = 0;
                    Running = 0;
                    Still = 0;
                    Tilting = 0;
                    Walking = 0;
                    Unknown = 0;

                    handler.post(new Runnable(){
                        public void run(){
//                            Toast.makeText(getApplicationContext(), "在坐車啦" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Log.e("ActivityRecognition", "On Bicycle: " + activity.getConfidence());
                    ref.update("Activity Recognition 2", "On Bicycle: " + activity.getConfidence());
//                    sensordb.put("ActivityRecognition", "On Bicycle: " + activity.getConfidence());
                    Bicycle = activity.getConfidence();
                    Vehicle = 0;
                    Foot = 0;
                    Running = 0;
                    Still = 0;
                    Tilting = 0;
                    Walking = 0;
                    Unknown = 0;
                    handler.post(new Runnable(){
                        public void run(){
//                            Toast.makeText(getApplicationContext(), "騎腳踏車啦" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    Log.e("ActivityRecognition", "On Foot: " + activity.getConfidence());
                    ref.update("Activity Recognition 3", "On Foot: " + activity.getConfidence());
//                    sensordb.put("ActivityRecognition", "On Foot: " + activity.getConfidence());
                    Foot = activity.getConfidence();
                    Vehicle = 0;
                    Bicycle = 0;
                    Running = 0;
                    Still = 0;
                    Tilting = 0;
                    Walking = 0;
                    Unknown = 0;
                    handler.post(new Runnable(){
                        public void run(){
//                            Toast.makeText(getApplicationContext(), "走路啦" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e("ActivityRecognition", "Running: " + activity.getConfidence());
                    ref.update("Activity Recognition 4", "Running: " + activity.getConfidence());
//                    sensordb.put("ActivityRecognition", "Running: " + activity.getConfidence());
                    Running = activity.getConfidence();
                    Vehicle = 0;
                    Bicycle = 0;
                    Foot = 0;
                    Still = 0;
                    Tilting = 0;
                    Walking = 0;
                    Unknown = 0;
                    handler.post(new Runnable(){
                        public void run(){
//                            Toast.makeText(getApplicationContext(), "跑步啦" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                }

                case DetectedActivity.STILL: {
                    Log.e("ActivityRecognition", "Still: " + activity.getConfidence());
                    ref.update("Activity Recognition 5", "Still: " + activity.getConfidence());
//                    sensordb.put("ActivityRecognition", "Still: " + activity.getConfidence());
                    Still = activity.getConfidence();
                    Vehicle = 0;
                    Bicycle = 0;
                    Foot = 0;
                    Running = 0;
                    Tilting = 0;
                    Walking = 0;
                    Unknown = 0;
                    handler.post(new Runnable(){
                        public void run(){
//                            Toast.makeText(getApplicationContext(), "靜止不動啦" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                        }
                    });
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                    builder.setContentText("Are you walking?");
                    builder.setSmallIcon(R.mipmap.ic_launcher);
                    builder.setContentTitle(getString(R.string.app_name));
                    NotificationManagerCompat.from(this).notify(0, builder.build());

                    break;
                }
                case DetectedActivity.TILTING: {
                    Log.e("ActivityRecognition", "Tilting: " + activity.getConfidence());
                    ref.update("Activity Recognition 6", "Tilting: " + activity.getConfidence());
//                    sensordb.put("ActivityRecognition", "Tilting: " + activity.getConfidence());
                    Tilting = activity.getConfidence();
                    Vehicle = 0;
                    Bicycle = 0;
                    Foot = 0;
                    Running = 0;
                    Still = 0;
                    Walking = 0;
                    Unknown = 0;
                    handler.post(new Runnable(){
                        public void run(){
//                            Toast.makeText(getApplicationContext(), "傾斜的啦" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e("ActivityRecognition", "Walking: " + activity.getConfidence());
                    ref.update("Activity Recognition 7", "Walking: " + activity.getConfidence());
//                    sensordb.put("ActivityRecognition", "Walking: " + activity.getConfidence());
                    Walking = activity.getConfidence();
                    Vehicle = 0;
                    Bicycle = 0;
                    Foot = 0;
                    Running = 0;
                    Still = 0;
                    Tilting = 0;
                    Unknown = 0;
                    handler.post(new Runnable(){
                        public void run(){
//                            Toast.makeText(getApplicationContext(), "又是走路啦" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.e("ActivityRecognition", "Unknown: " + activity.getConfidence());
                    ref.update("Activity Recognition 8", "Unknown: " + activity.getConfidence());
//                    sensordb.put("ActivityRecognition", "Unknown: " + activity.getConfidence());
                    Unknown = activity.getConfidence();
                    Vehicle = 0;
                    Bicycle = 0;
                    Foot = 0;
                    Running = 0;
                    Still = 0;
                    Tilting = 0;
                    Walking = 0;
                    handler.post(new Runnable(){
                        public void run(){
//                            Toast.makeText(getApplicationContext(), "不知道啦" + activity.getConfidence(), Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                }
            }
        }

    }
    //    protected void Transport(ActivityRecognitionDataRecord activityRecognitionDataRecord){
//        TransportationMode transportationMode
//                = (TransportationMode) MinukuStreamManager.getInstance().getStreamGeneratorFor(TransportationModeDataRecord.class);
//
////        transportationMode.examineTransportation(activityRecognitionDataRecord);
//        String suspectedStartActivity = getActivityNameFromType(transportationMode.getSuspectedStartActivityType());
//        String suspectedEndActivity = getActivityNameFromType(transportationMode.getSuspectedStopActivityType());
//
//        TransportationModeDataRecord transportationModeDataRecord =
//                new TransportationModeDataRecord(transportationMode.getConfirmedActivityString(),
//                        transportationMode.getSuspectTime(),
//                        suspectedStartActivity, suspectedEndActivity);
//
//    }
    public static String getActivityNameFromType(int activityType) {
        switch(activityType) {
            case DetectedActivity.IN_VEHICLE:
                return STRING_DETECTED_ACTIVITY_IN_VEHICLE;
            case DetectedActivity.ON_BICYCLE:
                return STRING_DETECTED_ACTIVITY_ON_BICYCLE;
            case DetectedActivity.ON_FOOT:
                return STRING_DETECTED_ACTIVITY_ON_FOOT;
            case DetectedActivity.STILL:
                return STRING_DETECTED_ACTIVITY_STILL;
            case DetectedActivity.RUNNING:
                return STRING_DETECTED_ACTIVITY_RUNNING;
            case DetectedActivity.WALKING:
                return STRING_DETECTED_ACTIVITY_WALKING;
            case DetectedActivity.UNKNOWN:
                return STRING_DETECTED_ACTIVITY_UNKNOWN;
            case DetectedActivity.TILTING:
                return STRING_DETECTED_ACTIVITY_TILTING;
            case NO_ACTIVITY_TYPE:
                return STRING_DETECTED_ACTIVITY_NA;
        }
        return "NA";
    }


}

