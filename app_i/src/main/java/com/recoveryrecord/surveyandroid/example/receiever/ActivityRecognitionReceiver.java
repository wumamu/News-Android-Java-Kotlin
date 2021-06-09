package com.recoveryrecord.surveyandroid.example.receiever;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Nullable;

import static android.icu.lang.UCharacter.IndicPositionalCategory.NA;
import static com.recoveryrecord.surveyandroid.example.NotificationScheduler.TAG;
import static com.recoveryrecord.surveyandroid.example.config.Constants.DetectTime;
import static com.recoveryrecord.surveyandroid.example.config.Constants.MILLISECONDS_PER_SECOND;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SessionID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.UsingApp;

public class ActivityRecognitionReceiver extends IntentService implements StreamGenerator{
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String device_id = "NA";

    public ActivityRecognitionReceiver() {
        super("ActivityRecognitionReceiver");
    }
//    public ActivityRecognitionReceiver(String name) {
//        super(name);
//    }
//    public static final String STRING_DETECTED_ACTIVITY_IN_VEHICLE = "in_vehicle";
//    public static final String STRING_DETECTED_ACTIVITY_ON_FOOT = "on_foot";
//    public static final String STRING_DETECTED_ACTIVITY_WALKING = "walking";
//    public static final String STRING_DETECTED_ACTIVITY_RUNNING = "running";
//    public static final String STRING_DETECTED_ACTIVITY_TILTING = "tilting";
//    public static final String STRING_DETECTED_ACTIVITY_STILL = "still";
//    public static final String STRING_DETECTED_ACTIVITY_ON_BICYCLE = "on_bicycle";
//    public static final String STRING_DETECTED_ACTIVITY_UNKNOWN = "unknown";
//    public static final String STRING_DETECTED_ACTIVITY_NA = "NA";
//    public static final int NO_ACTIVITY_TYPE = -1;
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

    //Transportation Mode
    private String ConfirmedActvitiyString = "NA";

    /**ContextSourceType**/
    public static final int CONTEXT_SOURCE_TRANSPORTATION = 0;
    public static final int CONTEXT_SOURCE_DETECTION_STATE = 1;

    public static final String STRING_CONTEXT_SOURCE_TRANSPORTATION = "Transportation";
    public static final String STRING_CONTEXT_SOURCE_DETECTION_STATE = "DetectionState";

    /**Table Name**/
    public static final String RECORD_TABLE_NAME_TRANSPORTATION = "Record_Table_Transportation";

    public static final int STATE_INITIAL = -1;
    public static final int STATE_STATIC = 0;
    public static final int STATE_SUSPECTING_START = 1;
    public static final int STATE_CONFIRMED = 2;
    public static final int STATE_SUSPECTING_STOP = 3;

    //
    private static final float CONFIRM_START_ACTIVITY_THRESHOLD_IN_VEHICLE = (float) 0.8; //TODO origin為 0.6
    private static final float CONFIRM_START_ACTIVITY_THRESHOLD_ON_FOOT = (float)0.6;
    private static final float CONFIRM_START_ACTIVITY_THRESHOLD_ON_BICYCLE =(float) 0.6;
    private static final float CONFIRM_STOP_ACTIVITY_THRESHOLD_IN_VEHICLE = (float)0.3; //0.2
    private static final float CONFIRM_STOP_ACTIVITY_THRESHOLD_ON_FOOT = (float)0.3; //0.1
    private static final float CONFIRM_STOP_ACTIVITY_THRESHOLD_ON_BICYCLE =(float) 0.3; //0.2

    public static final int CONFIRM_START_ACTIVITY_Needed_Confidence = 40;
    public static final int CONFIRM_STOP_ACTIVITY_Needed_Confidence = 40;
    public static final int CANCEL_SUSPECT_Threshold = 95;
    public static final int SWTICH_TO_NEW_ACTIVITY_Threshold = 95;

    /**label **/
    public static final String STRING_DETECTED_ACTIVITY_IN_VEHICLE = "in_vehicle";
    public static final String STRING_DETECTED_ACTIVITY_ON_FOOT = "on_foot";
    public static final String STRING_DETECTED_ACTIVITY_WALKING = "walking";
    public static final String STRING_DETECTED_ACTIVITY_RUNNING = "running";
    public static final String STRING_DETECTED_ACTIVITY_TILTING = "tilting";
    public static final String STRING_DETECTED_ACTIVITY_STILL = "still";
    public static final String STRING_DETECTED_ACTIVITY_ON_BICYCLE = "on_bicycle";
    public static final String STRING_DETECTED_ACTIVITY_UNKNOWN = "unknown";
    public static final String STRING_DETECTED_ACTIVITY_NA = "NA";

    public static final String TRANSPORTATION_MODE_NAME_IN_VEHICLE = STRING_DETECTED_ACTIVITY_IN_VEHICLE;
    public static final String TRANSPORTATION_MODE_NAME_ON_FOOT = STRING_DETECTED_ACTIVITY_ON_FOOT;
    public static final String TRANSPORTATION_MODE_NAME_ON_BICYCLE = STRING_DETECTED_ACTIVITY_ON_BICYCLE;
    public static final String TRANSPORTATION_MODE_NAME_NO_TRANSPORTATION = "static";
    public static final String TRANSPORTATION_MODE_NAME_NA = "NA";

    public static final String TRANSPORTATION_MODE_HASNT_DETECTED_FLAG = "未知";

    private static final long WINDOW_LENGTH_START_ACTIVITY_DEFAULT = 20 * MILLISECONDS_PER_SECOND;
    private static final long WINDOW_LENGTH_STOP_ACTIVITY_DEFAULT = 20 * MILLISECONDS_PER_SECOND;
    private static final long WINDOW_LENGTH_START_ACTIVITY_IN_VEHICLE = 60 * MILLISECONDS_PER_SECOND; //TODO origin為20s
    private static final long WINDOW_LENGTH_START_ACTIVITY_ON_FOOT = 10 * MILLISECONDS_PER_SECOND;
    private static final long WINDOW_LENGTH_START_ACTIVITY_ON_BICYCLE = 20 * MILLISECONDS_PER_SECOND;
    private static final long WINDOW_LENGTH_STOP_ACTIVITY_IN_VEHICLE = 150 * MILLISECONDS_PER_SECOND;
    private static final long WINDOW_LENGTH_STOP_ACTIVITY_ON_FOOT = 30 * MILLISECONDS_PER_SECOND;
    private static final long WINDOW_LENGTH_STOP_ACTIVITY_ON_BICYCLE = 90 * MILLISECONDS_PER_SECOND;

    private static final long WINDOW_LENGTH_TRANSITION_START_ACTIVITY_IN_VEHICLE = 5 * MILLISECONDS_PER_SECOND;
    private static final long WINDOW_LENGTH_TRANSITION_START_ACTIVITY_ON_FOOT = 10 * MILLISECONDS_PER_SECOND;
    private static final long WINDOW_LENGTH_TRANSITION_START_ACTIVITY_ON_BICYCLE = 10 * MILLISECONDS_PER_SECOND;
    private static final long WINDOW_LENGTH_TRANSITION_STOP_ACTIVITY_IN_VEHICLE = 75 * MILLISECONDS_PER_SECOND;
    private static final long WINDOW_LENGTH_TRANSITION_STOP_ACTIVITY_ON_FOOT = 10 * MILLISECONDS_PER_SECOND;
    private static final long WINDOW_LENGTH_TRANSITION_STOP_ACTIVITY_ON_BICYCLE = 45 * MILLISECONDS_PER_SECOND;

    //the frequency of requesting google activity from the google play service
    public static int ACTIVITY_RECOGNITION_DEFAULT_UPDATE_INTERVAL_IN_SECONDS = 5;

    public static long ACTIVITY_RECOGNITION_DEFAULT_UPDATE_INTERVAL =
            ACTIVITY_RECOGNITION_DEFAULT_UPDATE_INTERVAL_IN_SECONDS * MILLISECONDS_PER_SECOND;

    private static long sActivityRecognitionUpdateIntervalInSeconds = ACTIVITY_RECOGNITION_DEFAULT_UPDATE_INTERVAL_IN_SECONDS;

    private static long sActivityRecognitionUpdateIntervalInMilliseconds =
            sActivityRecognitionUpdateIntervalInSeconds * MILLISECONDS_PER_SECOND;

    /**Properties for Record**/
    public static final String RECORD_DATA_PROPERTY_TRANSPORTATION = "Transportation";


    public static final int NO_ACTIVITY_TYPE = -1;
    public static final int IN_VEHICLE = DetectedActivity.IN_VEHICLE;
    public static final int ON_FOOT = DetectedActivity.ON_FOOT;
    public static final int ON_BICYCLE = DetectedActivity.ON_BICYCLE;
    public static final int UNKNOWN = DetectedActivity.UNKNOWN;
    public static final int STILL = DetectedActivity.STILL;
    public static final int TILTING = DetectedActivity.TILTING;


//    private String LastImageName= "";

    /**Constant **/
    public static int mSuspectedStartActivityType = NO_ACTIVITY_TYPE;
    public static int mSuspectedStopActivityType = NO_ACTIVITY_TYPE;
    public static int mConfirmedActivityType = NO_ACTIVITY_TYPE;// the initial value of activity is STILL.
    public static long mSuspectTime = 0;
    public static int mCurrentState = STATE_STATIC;


    private static Context serviceInstance = null;
    private Context mContext;

    public static ScheduledExecutorService mScheduledExecutorService;
    public static final int TransportationMode_REFRESH_FREQUENCY = 5; //1s, 1000ms
    public static final int BACKGROUND_RECORDING_INITIAL_DELAY = 0;
    private final int TransportationMode_ThreadSize = 1;

    private SharedPreferences sharedPrefs;

    //-------------------------------------------------------------------------------------------------------------
    @SuppressLint("HardwareIds")
    public void onCreate() {
        super.onCreate();
        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        mScheduledExecutorService = Executors.newScheduledThreadPool(TransportationMode_ThreadSize);
//
//        sharedPrefs = mContext.getSharedPreferences("test", mContext.MODE_PRIVATE);
//
//        mCurrentState = sharedPrefs.getInt("CurrentState", STATE_STATIC);
//        mConfirmedActivityType = sharedPrefs.getInt("ConfirmedActivityType", NO_ACTIVITY_TYPE);
    }
    @SuppressLint("HardwareIds")
    @Override
    protected void onHandleIntent(Intent intent) {
        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        sensordb.put("Time", time_now);
        sensordb.put("Using APP", UsingApp);
        if(UsingApp == "Using APP")
            sensordb.put("Session", SessionID);
        else
            sensordb.put("Session", -1);
        sensordb.put("device_id", device_id);
//        db.collection("test_users")
//                .document(device_id)
//                .collection("Sensor collection")
//                .document("Sensor")
//                .collection("Activity Recognition")
//                .document(time_now)
//                .set(sensordb);
        db.collection("Sensor collection")
                .document("Sensor")
                .collection("Activity Recognition")
                .document(device_id + " " + time_now)
                .set(sensordb);
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        handleDetectedActivities(result.getProbableActivities());
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
//        db.collection("test_users")
//                .document(device_id)
//                .collection("Sensor collection")
//                .document("Sensor")
//                .collection("Activity Recognition")
//                .document(time_now)
//                .set(sensordb);
        sensordb.put("device_id", device_id);
        sensordb.put("Using APP", UsingApp);
        if(UsingApp == "Using APP")
            sensordb.put("Session", SessionID);
        else
            sensordb.put("Session", -1);
        sensordb.put("period", DetectTime);
//        db.collection("test_users")
//                .document(device_id)
//                .collection("Sensor collection")
//                .document("Sensor")
//                .collection("Activity Recognition")
//                .document(time_now)
//                .set(sensordb);
        db.collection("Sensor collection")
                .document("Sensor")
                .collection("Activity Recognition")
                .document(device_id + " " + time_now)
                .set(sensordb);
    }
    @SuppressLint("HardwareIds")
    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        sensordb.put("device_id", device_id);
        sensordb.put("Using APP", UsingApp);
        if(UsingApp == "Using APP")
            sensordb.put("Session", SessionID);
        else
            sensordb.put("Session", -1);
//        sensordb.put("period", "GPS Connected");
//        final DocumentReference ref = db.collection("test_users")
//                .document(device_id)
//                .collection("Sensor collection")
//                .document("Sensor")
//                .collection("Activity Recognition")
//                .document(time_now);
        final DocumentReference ref = db.collection("Sensor collection")
                .document("Sensor")
                .collection("Activity Recognition")
                .document(device_id + " " + time_now);
        ref.update("period", "GPS Connected");
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
   //Transportation Mode
//   public int examineTransportation(){
//       //if there's no existing activity type, we need to get activity from the shared preference
//       if (mConfirmedActivityType == NO_ACTIVITY_TYPE){
//           mConfirmedActivityType = sharedPrefs.getInt("ConfirmedActivityType", NO_ACTIVITY_TYPE);
//       }
//       if(mCurrentState == STATE_INITIAL){
//           mCurrentState = sharedPrefs.getInt("CurrentState", STATE_INITIAL);
//       }
//       final DocumentReference ref = db.collection("Sensor collection")
//               .document("Sensor")
//               .collection("Activity Recognition")
//               .document(device_id + " " + time_now);
//
//       if(activityRecognitionDataRecord.getProbableActivities()==null || activityRecognitionDataRecord.getProbableActivities().isEmpty()){
//           return -1;
//       }
//
//       List<DetectedActivity> probableActivities = activityRecognitionDataRecord.getProbableActivities();
//
//       //Log.d(TAG, "[test replay] examine the incoming record.....for transportation " + activityRecognitionDataRecord.getDetectedtime()  +" : "+ activityRecognitionDataRecord.getProbableActivities().toString());
//
//       long detectionTime = activityRecognitionDataRecord.getCreationTime();
//
//       if ((probableActivities.get(0).getType() == DetectedActivity.ON_BICYCLE ||
//               probableActivities.get(0).getType() == DetectedActivity.IN_VEHICLE ||
//               probableActivities.get(0).getType() == DetectedActivity.ON_FOOT) &&
//               probableActivities.get(0).getConfidence() >= CANCEL_SUSPECT_Threshold) {
//
//           //back to static, cancel the suspect
//           setCurrentState(STATE_CONFIRMED);
//
//           setSuspectedStartActivityType(NO_ACTIVITY_TYPE);
//
//           setConfirmedActivityType(probableActivities.get(0).getType());
//
//           CSVHelper.TransportationState_StoreToCSV(new Date().getTime(), "STATE_CONFIRMED", getConfirmedActivityString());
//
//           return getConfirmedActivityType();
//       }
//
//       //if in the static state or initial state, we try to suspect new activity
//       if (getCurrentState()==STATE_STATIC || getCurrentState()==STATE_INITIAL) {
//
//           //if the detected activity is vehicle, bike or on foot, then we suspect the activity from now
//           if (probableActivities.get(0).getType()== DetectedActivity.ON_BICYCLE ||
//                   probableActivities.get(0).getType()== DetectedActivity.IN_VEHICLE ||
//                   probableActivities.get(0).getType()== DetectedActivity.ON_FOOT) {
//
//               //set current state to suspect stop
//               setCurrentState(STATE_SUSPECTING_START);
//
//               //set suspected Activity type
//               setSuspectedStartActivityType(probableActivities.get(0).getType());
//
//               //set suspect time
//               setSuspectTime(detectionTime);
//
//               CSVHelper.TransportationState_StoreToCSV(new Date().getTime(), "STATE_SUSPECTING_START", getConfirmedActivityString());
//           }
//
//       }
//       else if (getCurrentState()==STATE_SUSPECTING_START) {
//
//           //Log.d(TAG,"[test replay] in Suspect start, the suspected AR is " +getActivityNameFromType(getSuspectedStartActivityType()) );
//           boolean isTimeToConfirm = checkTimeElapseOfLatestActivityFromSuspectPoint(detectionTime, getSuspectTime(), getWindowLengh(getSuspectedStartActivityType(), getCurrentState()) );
//
//           //StoreToCSV(isTimeToConfirm, detectionTime);
//
//           if (isTimeToConfirm) {
//
//               long startTime = detectionTime - getWindowLengh(getSuspectedStartActivityType(), getCurrentState());
//               long endTime = detectionTime;
//               boolean isNewTransportationModeConfirmed = confirmStartPossibleTransportation(getSuspectedStartActivityType(), getWindowData(startTime, endTime),
//                       getWindowLengh(getSuspectedStartActivityType(), getCurrentState()));
//
//               if (isNewTransportationModeConfirmed) {
//
//                   //change the state to Confirmed
//                   setCurrentState(STATE_CONFIRMED);
//                   //set confirmed activity type
//                   setConfirmedActivityType(getSuspectedStartActivityType());
//                   //no suspect
//                   setSuspectedStartActivityType(NO_ACTIVITY_TYPE);
//
//                   //set the suspect time so that other class can access it.(Trip_startTime is when we think the transportation starts)
//                   setSuspectTime(startTime);
//
//                   CSVHelper.TransportationState_StoreToCSV(new Date().getTime(), "STATE_CONFIRMED", getConfirmedActivityString());
//
//                   return getConfirmedActivityType();
//               }
//               //if the suspection is wrong, back to the static state
//               else {
//
//                   //change the state to Confirmed
//                   setCurrentState(STATE_STATIC);
//                   //set confirmed activity type
//                   setConfirmedActivityType(NO_ACTIVITY_TYPE);
//
//                   setSuspectTime(0);
//
//                   CSVHelper.TransportationState_StoreToCSV(new Date().getTime(), "STATE_STATIC", getConfirmedActivityString());
//
//                   return getConfirmedActivityType();
//
//               }
//           }
//       }
//       //if in the confirmed state, we suspect whether users exit the activity
//       else if (getCurrentState()==STATE_CONFIRMED) {
//
//           //Log.d(TAG,"[test replay] in confirm, the confirm AR is " + getActivityNameFromType(getConfirmedActivityType()));
//           /** if the detected activity is vehicle, bike or on foot, then we suspect the activity from now **/
//
//           //if the latest activity is not the currently confirmed activity nor tilting nor unkown
//           if (probableActivities.get(0).getType() != getConfirmedActivityType() &&
//                   probableActivities.get(0).getType() != DetectedActivity.TILTING &&
//                   probableActivities.get(0).getType() != DetectedActivity.UNKNOWN) {
//
//               //set current state to suspect stop
//               setCurrentState(STATE_SUSPECTING_STOP);
//               //set suspected Activity type to the confirmed activity type
//               setSuspectedStopActivityType(getConfirmedActivityType());
//               //set suspect time
//               setSuspectTime(detectionTime);
//
//               CSVHelper.TransportationState_StoreToCSV(new Date().getTime(), "STATE_SUSPECTING_STOP", getConfirmedActivityString());
//
//           }
//       }
//       else if (getCurrentState()==STATE_SUSPECTING_STOP) {
//
//           boolean isTimeToConfirm = checkTimeElapseOfLatestActivityFromSuspectPoint(detectionTime, getSuspectTime(),
//                   getWindowLengh(getSuspectedStopActivityType(),
//                           getCurrentState())
//           );
//
//           if (isTimeToConfirm) {
//               long startTime =detectionTime -
//                       getWindowLengh(getSuspectedStartActivityType(),
//                               getCurrentState())
//                       ;
//               long endTime = detectionTime;
//               boolean isExitingTransportationMode =
//                       confirmStopPossibleTransportation(getSuspectedStopActivityType(), getWindowData(startTime, endTime),
//                               getWindowLengh(getSuspectedStartActivityType(), getCurrentState()));
//
//
//               if (isExitingTransportationMode) {
//
//                   //back to static
//                   setCurrentState(STATE_STATIC);
//
//                   setConfirmedActivityType(NO_ACTIVITY_TYPE);
//
//                   setSuspectedStopActivityType(NO_ACTIVITY_TYPE);
//
//                   //set the suspect time so that other class can access it.(Trip_startTime is when we think the transportation starts)
//                   setSuspectTime(startTime);
//
//                   CSVHelper.TransportationState_StoreToCSV(new Date().getTime(), "STATE_STATIC", getConfirmedActivityString());
//
//               }
//               //not exiting the confirmed activity
//               else {
//                   //back to static, cancel the suspection
//                   setCurrentState(STATE_CONFIRMED);
//
//                   setSuspectedStartActivityType(NO_ACTIVITY_TYPE);
//
//                   CSVHelper.TransportationState_StoreToCSV(new Date().getTime(), "STATE_CONFIRMED", getConfirmedActivityString());
//
//               }
//
//               setSuspectTime(0);
//           }
//
//           /**
//            * switch activity
//            */
//
//           //or directly enter suspecting activity: if the current record is other type of transportation mode
//           if (probableActivities.get(0).getType()!=getSuspectedStopActivityType() &&
//                   probableActivities.get(0).getType()!=DetectedActivity.TILTING &&
//                   probableActivities.get(0).getType()!=DetectedActivity.STILL &&
//                   probableActivities.get(0).getType()!=DetectedActivity.UNKNOWN &&
//                   //NA = 9
//                   probableActivities.get(0).getType()!=9) {
//
//               //if the new Activity having the confidence over 95, changing to state confirm
//               if(probableActivities.get(0).getConfidence() > SWTICH_TO_NEW_ACTIVITY_Threshold){
//
//                   //change the state to Confirmed
//                   setCurrentState(STATE_CONFIRMED);
//                   //set confirmed activity type
//                   setConfirmedActivityType(probableActivities.get(0).getType());
//                   //no suspect
//                   setSuspectedStartActivityType(NO_ACTIVITY_TYPE);
//
//                   //set the suspect time so that other class can access it.(Trip_startTime is when we think the transportation starts)
//                   setSuspectTime(detectionTime);
//
//                   CSVHelper.TransportationState_StoreToCSV(new Date().getTime(), "STATE_CONFIRMED", getConfirmedActivityString());
//
//                   return getConfirmedActivityType();
//               }
//
//               //no label with high confidence, we need to check labels in a window time
//               else {
//
//                   isTimeToConfirm = checkTimeElapseOfLatestActivityFromSuspectPoint(
//                           detectionTime,
//                           getSuspectTime(),
//                           getWindowLengh(probableActivities.get(0).getType(),
//                                   STATE_SUSPECTING_START) );
//
//                   if (isTimeToConfirm) {
//                       long startTime = detectionTime -
//                               getWindowLengh(probableActivities.get(0).getType(),
//                                       STATE_SUSPECTING_START) ;
//                       long endTime = detectionTime;
//                       boolean isActuallyStartingAnotherActivity = changeSuspectingTransportation(
//                               probableActivities.get(0).getType(),
//                               getWindowData(startTime, endTime));
//
//                       if (isActuallyStartingAnotherActivity) {
//
//                           //back to static
//                           setCurrentState(STATE_SUSPECTING_START);
//
//                           setSuspectedStopActivityType(NO_ACTIVITY_TYPE);
//
//                           setSuspectedStartActivityType(probableActivities.get(0).getType());
//
//                           //start suspecting new activity
//                           setSuspectTime(detectionTime);
//
//                           CSVHelper.TransportationState_StoreToCSV(new Date().getTime(), "STATE_SUSPECTING_START", getConfirmedActivityString());
//                       }
//                   }
//               }
//           }
//       }
//       return getConfirmedActivityType();
//   }
//    public static int getConfirmedActivityType() {
//        return mConfirmedActivityType;
//    }
//
//    public void setConfirmedActivityType(int confirmedActivityType) {
//        mConfirmedActivityType = confirmedActivityType;
//    }
//
//    public static int getSuspectedStartActivityType() {
//        return mSuspectedStartActivityType;
//    }
//
//    public static long getSuspectTime() {
//        return mSuspectTime;
//    }
//
//    public static int getCurrentState() {
//        return mCurrentState;
//    }
//
//    public static int getSuspectedStopActivityType() {
//        return mSuspectedStopActivityType;
//    }
//
//    public static void setSuspectedStopActivityType(int suspectedStopActivityType) {
//        mSuspectedStopActivityType = suspectedStopActivityType;
//    }
//
//    public static void setCurrentState(int state) {
//        mCurrentState = state;
//    }
//
//    public static void setSuspectedStartActivityType(int suspectedStartActivityType) {
//        mSuspectedStartActivityType = suspectedStartActivityType;
//    }
//
//    public static void setSuspectTime(long suspectTime) {
//        mSuspectTime = suspectTime;
//    }
//
//    public static String getConfirmedActivityString() {
//        return getActivityNameFromType(mConfirmedActivityType);
//    }
//
//    public static boolean checkTimeElapseOfLatestActivityFromSuspectPoint( long lastestActivityTime, long suspectTime, long windowLenth) {
//        boolean flag = (lastestActivityTime - suspectTime > windowLenth); //(lastestActivityTime - suspectTime)*1000
//        //StoreToCSV(new Date().getTime(), lastestActivityTime, suspectTime, lastestActivityTime - suspectTime ,windowLenth, flag);
//        if (flag)
//            //wait for long enough
//            return true;
//        else
//            //still need to wait
//            return false;
//    }
//    public static long getTransitionWindowLength (int activityType, int state) {
//        if (state==STATE_SUSPECTING_START) {
//            switch (activityType) {
//                case DetectedActivity.IN_VEHICLE:
//                    return WINDOW_LENGTH_TRANSITION_START_ACTIVITY_IN_VEHICLE;
//                case DetectedActivity.ON_FOOT:
//                    return WINDOW_LENGTH_TRANSITION_START_ACTIVITY_ON_FOOT;
//                case DetectedActivity.ON_BICYCLE:
//                    return WINDOW_LENGTH_TRANSITION_START_ACTIVITY_ON_BICYCLE;
//                default:
//                    return WINDOW_LENGTH_START_ACTIVITY_DEFAULT;
//            }
//        }
//        else if (state==STATE_SUSPECTING_STOP) {
//            switch (activityType) {
//                case DetectedActivity.IN_VEHICLE:
//                    return WINDOW_LENGTH_TRANSITION_STOP_ACTIVITY_IN_VEHICLE;
//                case DetectedActivity.ON_FOOT:
//                    return WINDOW_LENGTH_TRANSITION_STOP_ACTIVITY_ON_FOOT;
//                case DetectedActivity.ON_BICYCLE:
//                    return WINDOW_LENGTH_TRANSITION_STOP_ACTIVITY_ON_BICYCLE;
//                default:
//                    return WINDOW_LENGTH_STOP_ACTIVITY_DEFAULT;
//            }
//        }else {
//            return WINDOW_LENGTH_STOP_ACTIVITY_DEFAULT;
//        }
//    }
//
//    public static long getWindowLengh (int activityType, int state) {
//        if (state==STATE_SUSPECTING_START) {
//            switch (activityType) {
//                case DetectedActivity.IN_VEHICLE:
//                    return WINDOW_LENGTH_START_ACTIVITY_IN_VEHICLE;
//                case DetectedActivity.ON_FOOT:
//                    return WINDOW_LENGTH_START_ACTIVITY_ON_FOOT;
//                case DetectedActivity.ON_BICYCLE:
//                    return WINDOW_LENGTH_START_ACTIVITY_ON_BICYCLE;
//                default:
//                    return WINDOW_LENGTH_START_ACTIVITY_DEFAULT;
//            }
//        }
//        else if (state==STATE_SUSPECTING_STOP) {
//            switch (activityType) {
//                case DetectedActivity.IN_VEHICLE:
//                    return WINDOW_LENGTH_STOP_ACTIVITY_IN_VEHICLE;
//                case DetectedActivity.ON_FOOT:
//                    return WINDOW_LENGTH_STOP_ACTIVITY_ON_FOOT;
//                case DetectedActivity.ON_BICYCLE:
//                    return WINDOW_LENGTH_STOP_ACTIVITY_ON_BICYCLE;
//                default:
//                    return WINDOW_LENGTH_STOP_ACTIVITY_DEFAULT;
//            }
//        }else {
//            return WINDOW_LENGTH_STOP_ACTIVITY_DEFAULT;
//        }
//    }
//
//    private boolean confirmStartPossibleTransportation(int activityType, ArrayList<ActivityRecognitionDataRecord> windowData, long windowLength) {
//
//        float threshold = getConfirmStartThreshold(activityType);
//
//        /** check if in the window data the number of the possible activity exceeds the threshold**/
//
//        //get number of targeted data
//        int count = 0;
//        int inRecentCount = 0;
//
//        for (int i=0; i<windowData.size(); i++) {
//
//            List<DetectedActivity> detectedActivities = windowData.get(i).getProbableActivities();
//
//            //in the recent 6 there are more than 3
//            if (i >= windowData.size()-5) {
//
//                if (detectedActivities.get(0).getType()==activityType ) {
//                    inRecentCount +=1;
//                }
//            }
////            if (detectedActivities.get(0).getType()==activityType ) {
////                count +=1;
////            }
//            for (int activityIndex = 0; activityIndex<detectedActivities.size(); activityIndex++) {
//                //if probable activities contain the target activity, we count! (not simply see the most probable one)
//                if (detectedActivities.get(activityIndex).getType()==activityType
//                    //turned into getting the first two labels
//                    //also, we only care about the label which is much confidence to
//                    //prevent the low confidence ones would affect the result
////                        && detectedActivities.get(activityIndex).getConfidence() >= CONFIRM_START_ACTIVITY_Needed_Confidence
//                ){
//                    count +=1;
//                    break;
//                }
//                //only consider the first two labels
//                if(activityIndex >= 1){
//                    break;
//                }
//            }
//        }
//        if (windowData.size()!=0) {
//            float percentage = (float)count/windowData.size();
//            //StoreToCSV(new Date().getTime(), String.valueOf(percentage), "start", windowData, threshold, windowLength);
//            //if the percentage > threshold
//            if ( threshold <= percentage || inRecentCount >= 2)
//                return true;
//            else
//                return false;
//        }
//        else{
//            //StoreToCSV(new Date().getTime(), "no float", "start",new ArrayList<ActivityRecognitionDataRecord>(), threshold, windowLength);
//
//            //if there's no data in the windowdata, we should not confirm the possible activity
//            return false;
//        }
//    }
//    private static float getConfirmStartThreshold(int activityType) {
//        switch (activityType) {
//            case DetectedActivity.IN_VEHICLE:
//                return CONFIRM_START_ACTIVITY_THRESHOLD_IN_VEHICLE;
//            case DetectedActivity.ON_FOOT:
//                return CONFIRM_START_ACTIVITY_THRESHOLD_ON_FOOT;
//            case DetectedActivity.ON_BICYCLE:
//                return CONFIRM_START_ACTIVITY_THRESHOLD_ON_BICYCLE;
//            default:
//                return (float) 0.5;
//        }
//    }
//
//    private ArrayList<ActivityRecognitionDataRecord> getWindowData(long startTime, long endTime) {
//        ArrayList<ActivityRecognitionDataRecord> windowData = new ArrayList<ActivityRecognitionDataRecord>();
//        //TODO: get activity records from the database
//        //windowData = DataHandler.getActivityRecognitionRecordsBetweenTimes(Trip_startTime, Trip_endTime);
//        ///for testing: get data from the testData
//        ArrayList<ActivityRecognitionDataRecord> recordPool = getLocalRecordPool();
////        //Log.d(LOG_TAG, " examineTransportation you find " + recordPool.size() + " records in the activity recognition pool");
//        for (int i=0; i<recordPool.size(); i++) {
//            ActivityRecognitionDataRecord record = (ActivityRecognitionDataRecord) recordPool.get(i);
//            //       //Log.d(LOG_TAG, " record.getTimestamp() " + record.getTimestamp() +
//            //             " windwo Trip_startTime " + Trip_startTime + " windwo Trip_endTime " + Trip_endTime);
//            if (record.getTimestamp() >= startTime && record.getTimestamp() <= endTime)
//                windowData.add(record);
//        }
//        return windowData;
//    }
//
//    private boolean confirmStopPossibleTransportation(int activityType, ArrayList<ActivityRecognitionDataRecord> windowData, long windowLength) {
//
//        float threshold = getConfirmStopThreshold(activityType);
//
//        /** check if in the window data the number of the possible activity exceeds the threshold**/
//
//        //get number of targeted data
//        int count = 0;
//        int inRecentCount = 0;
//        for (int i=0; i<windowData.size(); i++) {
//            List<DetectedActivity> detectedActivities = windowData.get(i).getProbableActivities();
//            //in the recent 6 there are more than 3
//            if (i >= windowData.size()-5) {
//                if (detectedActivities.get(0).getType()==activityType ) {
//                    inRecentCount +=1;
//                }
//            }
//            for (int activityIndex = 0; activityIndex<detectedActivities.size(); activityIndex++) {
//                if(activityIndex == 0){
//
//                    if (detectedActivities.get(activityIndex).getType()==activityType){
//                        count +=1;
//                    }
//                }else if(activityIndex == 1){
//
//                    if (detectedActivities.get(activityIndex).getType()==activityType &&
//                            detectedActivities.get(activityIndex).getConfidence() > 35){
//                        count +=1;
//                    }
//                }else{
//                    break;
//                }
//            }
//        }
//        float percentage = (float)count/windowData.size();
//        if (windowData.size()!=0) {
//            //if the percentage > threshold
//            //StoreToCSV(new Date().getTime(), "stop", String.valueOf(percentage), windowData, threshold, windowLength);
//            if ( threshold >= percentage && inRecentCount <= 2)
//                return true;
//            else
//                return false;
//        }
//        else{
//            //StoreToCSV(new Date().getTime(), "no float", "stop", new ArrayList<ActivityRecognitionDataRecord>(), threshold, windowLength);
//            //if there's no data in the windowdata, we should not confirm the possible activity
//            return false;
//        }
//    }
//
//    private static boolean changeSuspectingTransportation(int activityType, ArrayList<ActivityRecognitionDataRecord> windowData) {
//        float threshold = getConfirmStartThreshold(activityType);
//        /** check if in the window data the number of the possible activity exceeds the threshold**/
//        int inRecentCount = 0;
//        for (int i=windowData.size()-1; i>=0; i--) {
//            List<DetectedActivity> detectedActivities = windowData.get(i).getProbableActivities();
//            //in the recent 6 there are more than 3
//            if (i >= windowData.size()-3) {
//                if (detectedActivities.get(0).getType()==activityType ) {
//                    inRecentCount +=1;
//                }
//            }
//        }
//        if (windowData.size()!=0) {
//            //if the percentage > threshold
////            //Log.d(LOG_TAG, "[changeSuspectingTransportation] examineTransportation changing transportation recentCount " +inRecentCount + " within " + windowData.size()  + "  data");
//            if ( inRecentCount >= 2)
//                return true;
//            else
//                return false;
//        }
//        else
//            //if there's no data in the windowdata, we should not confirm the possible activity
//            return false;
//
//    }
//    private static float getConfirmStopThreshold(int activityType) {
//
//        switch (activityType) {
//            case DetectedActivity.IN_VEHICLE:
//                return CONFIRM_STOP_ACTIVITY_THRESHOLD_IN_VEHICLE;
//            case DetectedActivity.ON_FOOT:
//                return CONFIRM_STOP_ACTIVITY_THRESHOLD_ON_FOOT;
//            case DetectedActivity.ON_BICYCLE:
//                return CONFIRM_STOP_ACTIVITY_THRESHOLD_ON_BICYCLE;
//            default:
//                return (float) 0.5;
//
//        }
//    }
//
//    /**
//     * Map detected activity types to strings
//     */
//    public static String getActivityNameFromType(int activityType) {
//        switch(activityType) {
//            case DetectedActivity.IN_VEHICLE:
//                return STRING_DETECTED_ACTIVITY_IN_VEHICLE;
//            case DetectedActivity.ON_BICYCLE:
//                return STRING_DETECTED_ACTIVITY_ON_BICYCLE;
//            case DetectedActivity.ON_FOOT:
//                return STRING_DETECTED_ACTIVITY_ON_FOOT;
//            case DetectedActivity.STILL:
//                return STRING_DETECTED_ACTIVITY_STILL;
//            case DetectedActivity.RUNNING:
//                return STRING_DETECTED_ACTIVITY_RUNNING;
//            case DetectedActivity.WALKING:
//                return STRING_DETECTED_ACTIVITY_WALKING;
//            case DetectedActivity.UNKNOWN:
//                return STRING_DETECTED_ACTIVITY_UNKNOWN;
//            case DetectedActivity.TILTING:
//                return STRING_DETECTED_ACTIVITY_TILTING;
//            case NO_ACTIVITY_TYPE:
//                return TRANSPORTATION_MODE_NAME_NO_TRANSPORTATION;
//        }
//        return TRANSPORTATION_MODE_NAME_NO_TRANSPORTATION;
//    }
}

