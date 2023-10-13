//package com.recoveryrecord.surveyandroid.example.receiever;
//
//import android.annotation.SuppressLint;
//import android.app.IntentService;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.provider.Settings;
//import android.util.Log;
//
//import com.google.android.gms.location.ActivityRecognitionResult;
//import com.google.android.gms.location.DetectedActivity;
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.recoveryrecord.surveyandroid.example.CSVDataRecord.ActivityRecognitionDataRecord;
//import com.recoveryrecord.surveyandroid.example.CSVDataRecord.ActivityRecognitionStream;
//import com.recoveryrecord.surveyandroid.example.CSVDataRecord.CSVHelper;
//import com.recoveryrecord.surveyandroid.example.CSVDataRecord.StreamNotFoundException;
//import com.recoveryrecord.surveyandroid.example.CSVDataRecord.TransportationModeDataRecord;
//import com.recoveryrecord.surveyandroid.example.DbHelper.ActivityRecognitionReceiverDbHelper;
//import com.recoveryrecord.surveyandroid.example.R;
//import com.recoveryrecord.surveyandroid.example.sqlite.ActivityRecognition;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.TimeZone;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//import androidx.preference.PreferenceManager;
//
//import static com.recoveryrecord.surveyandroid.example.config.Constants.SHARE_PREFERENCE_USER_ID;
//import static com.recoveryrecord.surveyandroid.example.NotificationScheduler.TAG;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.ACTIVITY_DELIMITER;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.DATE_FORMAT_NOW_SLASH;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.DetectTime;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.MILLISECONDS_PER_MINUTE;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.SessionID;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.UsingApp;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.getCurrentTimeString;
//import static com.recoveryrecord.surveyandroid.example.config.Constants.sharedPrefString;
//import static com.recoveryrecord.surveyandroid.example.receiever.TransportationModeReceiver.getConfirmedActivityString;
//
//public class ActivityRecognitionReceiver extends IntentService implements StreamGenerator{
//    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
//    public static String device_id = "NA";
//
//    public ActivityRecognitionReceiver() {
//        super("ActivityRecognitionReceiver");
//    }
////    public ActivityRecognitionReceiver(String name) {
////        super(name);
////    }
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
////    private ActivityRecognitionDataRecord activityRecognitionDataRecord;
////    final Timestamp current_end = Timestamp.now();
//    Date date = new Date(System.currentTimeMillis());
//    @SuppressLint("SimpleDateFormat")
//    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//    final String time_now = formatter.format(date);
//    Map<String, Object> sensordb = new HashMap<>();
////    Intent intent;
//    private static int Vehicle = 0;
//    private static int Bicycle = 0;
//    private static int Foot = 0;
//    private static int Running = 0;
//    private static int Still = 0;
//    private static int Tilting = 0;
//    private static int Walking = 0;
//    private static int Unknown = 0;
//
//    public static final String RECORD_DATA_PROPERTY_NAME = "DetectedActivities";
//
//    protected long recordCount;
//
//    private Context mContext;
//    private ActivityRecognitionStream mStream;
//
//    private ActivityRecognitionDataRecord activityRecognitionDataRecord;
//
//    /** KeepAlive **/
//    protected int KEEPALIVE_MINUTE = 3;
//    protected long sKeepalive = KEEPALIVE_MINUTE * MILLISECONDS_PER_MINUTE;
//
//    public static List<DetectedActivity> sProbableActivities;
//    public static DetectedActivity sMostProbableActivity;
////    public static boolean ReadNews;
//    private static long sLatestDetectionTime;
//
////    public static int ACTIVITY_RECOGNITION_DEFAULT_UPDATE_INTERVAL_IN_SECONDS = 0;
////    public static long ACTIVITY_RECOGNITION_DEFAULT_UPDATE_INTERVAL = ACTIVITY_RECOGNITION_DEFAULT_UPDATE_INTERVAL_IN_SECONDS * MILLISECONDS_PER_SECOND;
//
//    public static ArrayList<ActivityRecognitionDataRecord> mLocalRecordPool;
//    TransportationModeReceiver transportationModeStreamGenerator = new TransportationModeReceiver();
////    private static ActivityRecognitionStreamGenerator instance;
//
//    private SharedPreferences sharedPrefs;
//    //-------------------------------------------------------------------------------------------------------------
//    @SuppressLint("HardwareIds")
//    public void onCreate() {
////        Log.e("ActivityRecognition", "onCreate");
//        super.onCreate();
//        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        mLocalRecordPool = new ArrayList<>();
//        sLatestDetectionTime = -1;
//        recordCount = 0;
//        sKeepalive = KEEPALIVE_MINUTE * MILLISECONDS_PER_MINUTE;
//        this.mStream = new ActivityRecognitionStream(50);
//        sharedPrefs = this.getSharedPreferences(sharedPrefString, MODE_PRIVATE);
//
//        transportationModeStreamGenerator.TransportationModeStreamGenerator(this);
//        this.register();
//        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        com.recoveryrecord.surveyandroid.example.sqlite.ActivityRecognition myactivityrecognition = new ActivityRecognition();//sqlite//add new to db
//        myactivityrecognition.setKEY_TIMESTAMP(Timestamp.now().getSeconds());
//        myactivityrecognition.setKEY_DOC_ID(device_id + " " + time_now);
//        myactivityrecognition.setKEY_DEVICE_ID(device_id);
//        myactivityrecognition.setKEY_USER_ID(sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
//        myactivityrecognition.setKEY_SESSION(SessionID);
//        myactivityrecognition.setKEY_USING_APP(UsingApp);
//        Log.e("activity using app", UsingApp);
//        myactivityrecognition.setKEY_ACTIVITYRECOGNITION(getConfirmedActivityString());
//        ActivityRecognitionReceiverDbHelper dbHandler = new ActivityRecognitionReceiverDbHelper(this);
//        dbHandler.insertActivityRecognitionDetailsCreate(myactivityrecognition);
//    }
//    public void register() {
////        Log.e("TAG", "Registering with StreamManager.");
//        MyStreamManager.getInstance().register(mStream, ActivityRecognitionDataRecord.class, this);
//    }
//    @SuppressLint("HardwareIds")
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        sensordb.put("Time", time_now);
//        sensordb.put("TimeStamp", Timestamp.now());
//        sensordb.put("Using APP", UsingApp);
//        if(UsingApp.equals("Using APP"))
//            sensordb.put("Session", SessionID);
//        else
//            sensordb.put("Session", -1);
//        sensordb.put("device_id", device_id);
//        db.collection("Sensor collection")
//                .document("Sensor")
//                .collection("Activity Recognition")
//                .document(device_id + " " + time_now)
//                .set(sensordb);
//        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
//        handleDetectedActivities(result.getProbableActivities());
//    }
//    @SuppressLint("HardwareIds")
//    @Override
//    public void updateStream(Context context) {
//        device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
////        Log.e("Activity Recognution", "update");
////        final Timestamp current_end = Timestamp.now();
//        Date date = new Date(System.currentTimeMillis());
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        final String time_now = formatter.format(date);
//        sensordb.put("Time", time_now);
//        sensordb.put("TimeStamp", Timestamp.now());
//        sensordb.put("Activity Recognition 1", "In Vehicle: " + Vehicle);
//        sensordb.put("Activity Recognition 2", "On Bicycle: " + Bicycle);
//        sensordb.put("Activity Recognition 3", "On Foot: " + Foot);
//        sensordb.put("Activity Recognition 4", "Running: " + Running);
//        sensordb.put("Activity Recognition 5", "Still: " + Still);
//        sensordb.put("Activity Recognition 6", "Tilting: " + Tilting);
//        sensordb.put("Activity Recognition 7", "Walking: " + Walking);
//        sensordb.put("Activity Recognition 8", "Unknown: " + Unknown);
//        sensordb.put("device_id", device_id);
//        sensordb.put("Using APP", UsingApp);
//        if(UsingApp.equals("Using APP"))
//            sensordb.put("Session", SessionID);
//        else
//            sensordb.put("Session", -1);
//        sensordb.put("period", DetectTime);
//        db.collection("Sensor collection")
//                .document("Sensor")
//                .collection("Activity Recognition")
//                .document(device_id + " " + time_now)
//                .set(sensordb);
//
//        activityRecognitionDataRecord = new ActivityRecognitionDataRecord(sMostProbableActivity, sProbableActivities, sLatestDetectionTime);
//
//        //if there don't have any updates for 10 minutes, add the NA one to represent it
//        if((getCurrentTimeInMillis() - sLatestDetectionTime) >= MILLISECONDS_PER_MINUTE * 10
//                && (sLatestDetectionTime != -1)){
//
//            DetectedActivity initialDetectedActivity = getInitialDetectedActivity();
//
//            ArrayList<DetectedActivity> initialDetectedActivities = new ArrayList<>();
//            initialDetectedActivities.add(initialDetectedActivity);
//
//            ActivityRecognitionDataRecord activityRecognitionDataRecord;
//            activityRecognitionDataRecord = new ActivityRecognitionDataRecord(initialDetectedActivity, initialDetectedActivities, getCurrentTimeInMillis());
//            if(mStream != null)
//                mStream.add(activityRecognitionDataRecord);
//
//        }
//
//        MyStreamManager.getInstance().setActivityRecognitionDataRecord(activityRecognitionDataRecord);
////        transportationModeStreamGenerator.examineTransportation(activityRecognitionDataRecord);
//        if(activityRecognitionDataRecord!=null && mStream != null) {
////            CSVHelper.storeToCSV(CSV_Act, "In Activity update stream");
//            mStream.add(activityRecognitionDataRecord);
////            Log.e(TAG, "Activity to be sent to event bus" + activityRecognitionDataRecord);
//        }
//    }
//    @SuppressLint("HardwareIds")
//    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
//        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        sensordb.put("device_id", device_id);
//        sensordb.put("Using APP", UsingApp);
//        if(UsingApp.equals("Using APP"))
//            sensordb.put("Session", SessionID);
//        else
//            sensordb.put("Session", -1);
//
//        final DocumentReference ref = db.collection("Sensor collection")
//                .document("Sensor")
//                .collection("Activity Recognition")
//                .document(device_id + " " + time_now);
//        ref.update("period", "GPS Connected");
////        Handler handler = new Handler(Looper.getMainLooper());
//        int largerconfidence = 0;
//        for (final DetectedActivity activity : probableActivities) {
//            switch (activity.getType()) {
//                case DetectedActivity.IN_VEHICLE: {
//                    Log.e("ActivityRecognition", "In Vehicle: " + activity.getConfidence());
////                    sensordb.put("ActivityRecognition", "In Vehicle: " + activity.getConfidence());
//                    ref.update("Activity Recognition 1", "In Vehicle: " + activity.getConfidence());
//                    Vehicle = activity.getConfidence();
//                    Bicycle = 0;
//                    Foot = 0;
//                    Running = 0;
//                    Still = 0;
//                    Tilting = 0;
//                    Walking = 0;
//                    Unknown = 0;
//                    if(activity.getConfidence() > largerconfidence){
//                        largerconfidence = activity.getConfidence();
//                        sMostProbableActivity = activity;
//                    }
//                    break;
//                }
//                case DetectedActivity.ON_BICYCLE: {
//                    Log.e("ActivityRecognition", "On Bicycle: " + activity.getConfidence());
//                    ref.update("Activity Recognition 2", "On Bicycle: " + activity.getConfidence());
////                    sensordb.put("ActivityRecognition", "On Bicycle: " + activity.getConfidence());
//                    Bicycle = activity.getConfidence();
//                    Vehicle = 0;
//                    Foot = 0;
//                    Running = 0;
//                    Still = 0;
//                    Tilting = 0;
//                    Walking = 0;
//                    Unknown = 0;
//                    if(activity.getConfidence() > largerconfidence){
//                        largerconfidence = activity.getConfidence();
//                        sMostProbableActivity = activity;
//                    }
//                    break;
//                }
//                case DetectedActivity.ON_FOOT: {
//                    Log.e("ActivityRecognition", "On Foot: " + activity.getConfidence());
//                    ref.update("Activity Recognition 3", "On Foot: " + activity.getConfidence());
////                    sensordb.put("ActivityRecognition", "On Foot: " + activity.getConfidence());
//                    Foot = activity.getConfidence();
//                    Vehicle = 0;
//                    Bicycle = 0;
//                    Running = 0;
//                    Still = 0;
//                    Tilting = 0;
//                    Walking = 0;
//                    Unknown = 0;
//                    break;
//                }
//                case DetectedActivity.RUNNING: {
//                    Log.e("ActivityRecognition", "Running: " + activity.getConfidence());
//                    ref.update("Activity Recognition 4", "Running: " + activity.getConfidence());
////                    sensordb.put("ActivityRecognition", "Running: " + activity.getConfidence());
//                    Running = activity.getConfidence();
//                    Vehicle = 0;
//                    Bicycle = 0;
//                    Foot = 0;
//                    Still = 0;
//                    Tilting = 0;
//                    Walking = 0;
//                    Unknown = 0;
//                    if(activity.getConfidence() > largerconfidence){
//                        largerconfidence = activity.getConfidence();
//                        sMostProbableActivity = activity;
//                    }
//                    break;
//                }
//
//                case DetectedActivity.STILL: {
//                    Log.e("ActivityRecognition", "Still: " + activity.getConfidence());
//                    ref.update("Activity Recognition 5", "Still: " + activity.getConfidence());
////                    sensordb.put("ActivityRecognition", "Still: " + activity.getConfidence());
//                    Still = activity.getConfidence();
//                    Vehicle = 0;
//                    Bicycle = 0;
//                    Foot = 0;
//                    Running = 0;
//                    Tilting = 0;
//                    Walking = 0;
//                    Unknown = 0;
//                    if(activity.getConfidence() > largerconfidence){
//                        largerconfidence = activity.getConfidence();
//                        sMostProbableActivity = activity;
//                    }
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//                    builder.setContentText("Are you walking?");
//                    builder.setSmallIcon(R.mipmap.ic_launcher);
//                    builder.setContentTitle(getString(R.string.app_name));
//                    NotificationManagerCompat.from(this).notify(0, builder.build());
//
//                    break;
//                }
//                case DetectedActivity.TILTING: {
//                    Log.e("ActivityRecognition", "Tilting: " + activity.getConfidence());
//                    ref.update("Activity Recognition 6", "Tilting: " + activity.getConfidence());
////                    sensordb.put("ActivityRecognition", "Tilting: " + activity.getConfidence());
//                    Tilting = activity.getConfidence();
//                    Vehicle = 0;
//                    Bicycle = 0;
//                    Foot = 0;
//                    Running = 0;
//                    Still = 0;
//                    Walking = 0;
//                    Unknown = 0;
//                    if(activity.getConfidence() > largerconfidence){
//                        largerconfidence = activity.getConfidence();
//                        sMostProbableActivity = activity;
//                    }
//                    break;
//                }
//                case DetectedActivity.WALKING: {
//                    Log.e("ActivityRecognition", "Walking: " + activity.getConfidence());
//                    ref.update("Activity Recognition 7", "Walking: " + activity.getConfidence());
////                    sensordb.put("ActivityRecognition", "Walking: " + activity.getConfidence());
//                    Walking = activity.getConfidence();
//                    Vehicle = 0;
//                    Bicycle = 0;
//                    Foot = 0;
//                    Running = 0;
//                    Still = 0;
//                    Tilting = 0;
//                    Unknown = 0;
//                    if(activity.getConfidence() > largerconfidence){
//                        largerconfidence = activity.getConfidence();
//                        sMostProbableActivity = activity;
//                    }
//                    break;
//                }
//                case DetectedActivity.UNKNOWN: {
//                    Log.e("ActivityRecognition", "Unknown: " + activity.getConfidence());
//                    ref.update("Activity Recognition 8", "Unknown: " + activity.getConfidence());
////                    sensordb.put("ActivityRecognition", "Unknown: " + activity.getConfidence());
//                    Unknown = activity.getConfidence();
//                    Vehicle = 0;
//                    Bicycle = 0;
//                    Foot = 0;
//                    Running = 0;
//                    Still = 0;
//                    Tilting = 0;
//                    Walking = 0;
//                    if(activity.getConfidence() > largerconfidence){
//                        largerconfidence = activity.getConfidence();
//                        sMostProbableActivity = activity;
//                    }
//                    break;
//                }
//            }
//        }
//        sProbableActivities = probableActivities;
//
//        try {
//            setActivitiesandDetectedtime(sProbableActivities, sMostProbableActivity, getCurrentTimeInMillis());
//        } catch (StreamNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
////    public long getUpdateFrequency() {
////        return 1;
////    }
////    public void sendStateChangeEvent() {
////
////    }
//
////    public void offer(ActivityRecognitionDataRecord dataRecord) {
////        Log.e(TAG, "Offer for ActivityRecognition data record does nothing!");
////    }
//
//    @SuppressLint("LongLogTag")
//    public void setActivitiesandDetectedtime (List<DetectedActivity> probableActivities, DetectedActivity mostProbableActivity, long detectedtime) throws StreamNotFoundException {
//        //set activities
////        Log.e("ActivityRecognition", "setActivitiesandDetectedTime");
//        //set a list of probable activities
//        setProbableActivities(probableActivities);
//        //set the most probable activity
//        setMostProbableActivity(mostProbableActivity);
//
//        setDetectedtime(detectedtime);
//
//        Log.d(TAG,detectedtime+"||"+ mostProbableActivity);
////        CSVHelper.storeToCSV(CSV_Act,"set Activity and detect time");
//
////        Log.e("probableActivities", String.valueOf(probableActivities));
////        Log.e("mostProbableActivity", String.valueOf(mostProbableActivity));
//        // Assume isRequested.
//        if(probableActivities!=null&&mostProbableActivity!=null)
//            saveRecordToLocalRecordPool(mostProbableActivity,detectedtime);
//
//}
//    public void saveRecordToLocalRecordPool(DetectedActivity MostProbableActivity,long Detectedtime) throws StreamNotFoundException {
////        /** create a Record to save timestamp, session it belongs to, and Data**/
//
////        Log.e("ActivityRecognition", "saveRecordToLocalRecordPool");
//        ActivityRecognitionDataRecord record;
//        record = new ActivityRecognitionDataRecord(MostProbableActivity, Detectedtime);
//
//        record.setProbableActivities(sProbableActivities);
//
//        JSONObject data = new JSONObject();
//
//        //also set data:
//        JSONArray activitiesJSON = new JSONArray();
//
//        //add all activities to JSONArray
//        for (int i=0; i<sProbableActivities.size(); i++){
//            DetectedActivity detectedActivity =  sProbableActivities.get(i);
//            String activityAndConfidence = getActivityNameFromType(detectedActivity.getType()) + ACTIVITY_DELIMITER + detectedActivity.getConfidence();
//            activitiesJSON.put(activityAndConfidence);
//        }
//
//        //add activityJSON Array to data
//        try {
//            data.put(RECORD_DATA_PROPERTY_NAME, activitiesJSON);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        record.setData(data);
//        record.setTimestamp(sLatestDetectionTime);
//
//        Log.d(TAG, "testing saving records at " + record.getTimeString() + " data: " + record.getData());
//
////        CSVHelper.storeToCSV(CSV_Act,"saveRecordToLocalRecordPool data: " + record.getData().toString());
//
//        addRecord(record);
//
//    }
//
//    private DetectedActivity getInitialDetectedActivity(){
//
//        return new DetectedActivity(-1, 100);
//    }
//    @SuppressLint("LongLogTag")
//    protected void addRecord(ActivityRecognitionDataRecord activityRecognitionDataRecord) throws StreamNotFoundException {
////        Log.e("add record", "start");
//        //        long id = recordCount++;
////        activityRecognitionDataRecord.setID(id);
////        Log.d(TAG,"CreateTime:" + activityRecognitionDataRecord.getCreationTime()+ " MostProbableActivity:"+activityRecognitionDataRecord.getMostProbableActivity());
//
//        mLocalRecordPool.add(activityRecognitionDataRecord); //it's working.
//        Log.d(TAG, "[test logging]add record " + "logged at " + activityRecognitionDataRecord.getTimeString() );
////        Log.e("mlocalrecord size before remove", String.valueOf(mLocalRecordPool.size()));
//        removeOutDatedRecord();
//        //**** update the latest ActivityRecognitionDataRecord in mLocalRecordPool to MinukuStreamManager;
////        Log.e("mlocalrecord size after remove", String.valueOf(mLocalRecordPool.size()));
//        mLocalRecordPool.get(mLocalRecordPool.size()-1).setID(999);
//        Log.d(TAG,"size : "+mLocalRecordPool.size());
//        MyStreamManager.getInstance().setActivityRecognitionDataRecord(mLocalRecordPool.get(mLocalRecordPool.size()-1));
//        Log.d(TAG,"CreateTime:" + mLocalRecordPool.get(mLocalRecordPool.size()-1).getCreationTime()+ " MostProbableActivity:"+mLocalRecordPool.get(mLocalRecordPool.size()-1).getMostProbableActivity());
//
//
//        this.activityRecognitionDataRecord = activityRecognitionDataRecord;
//
//        //TODO: now update the value to the TransportationMode every 5 seconds
//
//
////            Log.e("TransportationMode", "Start");
//            transportationModeStreamGenerator = (TransportationModeReceiver) MyStreamManager.getInstance().getStreamGeneratorFor(TransportationModeDataRecord.class);
//
//            transportationModeStreamGenerator.examineTransportation(activityRecognitionDataRecord);
//
//            sharedPrefs.edit().putInt("CurrentState", TransportationModeReceiver.mCurrentState).apply();
//            sharedPrefs.edit().putInt("ConfirmedActivityType", TransportationModeReceiver.mConfirmedActivityType).apply();
////            Log.e("TransportationMode store in CSV", "Start");
//            CSVHelper.storeToCSV(CSVHelper.CSV_CHECK_TRANSPORTATION,
//                    getCurrentTimeString(),
//                    getConfirmedActivityString(),
//                    getTimeString(TransportationModeReceiver.getSuspectTime()),
//                    getActivityNameFromType(TransportationModeReceiver.getSuspectedStartActivityType()),
//                    getActivityNameFromType(TransportationModeReceiver.getSuspectedStopActivityType()),
//                    activityRecognitionDataRecord.getMostProbableActivity().toString(),
//                    activityRecognitionDataRecord.getProbableActivities().toString());
//
//            String suspectedStartActivity = getActivityNameFromType(TransportationModeReceiver.getSuspectedStartActivityType());
//            String suspectedEndActivity = getActivityNameFromType(TransportationModeReceiver.getSuspectedStopActivityType());
//
//            TransportationModeDataRecord transportationModeDataRecord =
//                    new TransportationModeDataRecord(getConfirmedActivityString(),
//                            TransportationModeReceiver.getSuspectTime(),
//                            suspectedStartActivity, suspectedEndActivity);
//
//            MyStreamManager.getInstance().setTransportationModeDataRecord(transportationModeDataRecord, mContext, sharedPrefs);
////            Log.e("TransportationMode", "End");
//    }
//    /**
//     * this function remove old record (depending on the maximum size of the local pool)
//     */
//    protected void removeOutDatedRecord() {
////        Log.e("remove out data record","start");
//        for (int i=0; i<mLocalRecordPool.size(); i++) {
//
//            ActivityRecognitionDataRecord record = mLocalRecordPool.get(i);
//
//            //calculate time difference
//            long diff =  getCurrentTimeInMillis() - mLocalRecordPool.get(i).getTimestamp();
//
//            //remove outdated records.
//            if (diff >= sKeepalive){//3 secs
//                mLocalRecordPool.remove(record);
//                //Log.d(TAG, "[test logging]remove record " + record.getSource() + record.getID() + " logged at " + record.getTimeString() + " to " + this.getName());
////                Log.e(TAG,"sKeepalive");
//                i--;
//            }
//        }
//    }
//
//    //TODO might be useless
//    public static ArrayList<ActivityRecognitionDataRecord> getLocalRecordPool(){
//        return mLocalRecordPool;
//    }
//
////    public static ActivityRecognitionDataRecord getLastSavedRecord(){
////        if(mLocalRecordPool==null){
////            Log.e("getLastSavedRecord","null");
////            return null;
////        }
////        if (mLocalRecordPool.size()>0)
////            return mLocalRecordPool.get(mLocalRecordPool.size()-1);
////        else{
////            Log.e("getLastSavedRecord","mLocalRecordPool.size()<0");
////            return null;
////        }
////    }
//
//    /**get the current time in milliseconds**/
//    public static long getCurrentTimeInMillis(){
//        //get timzone
//        TimeZone tz = TimeZone.getDefault();
//        Calendar cal = Calendar.getInstance(tz);
//        return cal.getTimeInMillis();
//    }
//
//
//    public void setProbableActivities(List<DetectedActivity> probableActivities) {
//        sProbableActivities = probableActivities;
//
//    }
//
//    public void setMostProbableActivity(DetectedActivity mostProbableActivity) {
//        sMostProbableActivity = mostProbableActivity;
//
//    }
//
//    public void setDetectedtime(long detectedtime){
//        sLatestDetectionTime = detectedtime;
//
//    }
//
////    public void onConnectionSuspended(int i) {
////
////    }
//
//    @SuppressLint("LongLogTag")
//
////    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
////        if (connectionResult.hasResolution()) {
////            // Log.d(LOG_TAG,"[onConnectionFailed] Conntection to Google Play services is failed");
////
////        } else {
////            Log.e(TAG, "[onConnectionFailed] No Google Play services is available, the error code is "
////                    + connectionResult.getErrorCode());
////        }
////    }
//
////    public static int getActivityTypeFromName(String activityName) {
////
////        switch (activityName) {
////            case STRING_DETECTED_ACTIVITY_IN_VEHICLE:
////                return DetectedActivity.IN_VEHICLE;
////            case STRING_DETECTED_ACTIVITY_ON_BICYCLE:
////                return DetectedActivity.ON_BICYCLE;
////            case STRING_DETECTED_ACTIVITY_ON_FOOT:
////                return DetectedActivity.ON_FOOT;
////            case STRING_DETECTED_ACTIVITY_STILL:
////                return DetectedActivity.STILL;
////            case STRING_DETECTED_ACTIVITY_UNKNOWN:
////                return DetectedActivity.UNKNOWN;
////            case STRING_DETECTED_ACTIVITY_RUNNING:
////                return DetectedActivity.RUNNING;
////            case STRING_DETECTED_ACTIVITY_WALKING:
////                return DetectedActivity.WALKING;
////            case STRING_DETECTED_ACTIVITY_TILTING:
////                return DetectedActivity.TILTING;
////            default:
////                return NO_ACTIVITY_TYPE;
////        }
////
////    }
//
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
//                return STRING_DETECTED_ACTIVITY_NA;
//        }
//        return "NA";
//    }
//    public static String getTimeString(long time){
//
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf_now = new SimpleDateFormat(DATE_FORMAT_NOW_SLASH);
//
//        return sdf_now.format(time);
//    }
//}
//
