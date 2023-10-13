package com.recoveryrecord.surveyandroid.example.receiver;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.CSVDataRecord.ActivityRecognitionDataRecord;
import com.recoveryrecord.surveyandroid.example.CSVDataRecord.DataRecord;
import com.recoveryrecord.surveyandroid.example.CSVDataRecord.StreamNotFoundException;
import com.recoveryrecord.surveyandroid.example.CSVDataRecord.TransportationModeDataRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


public class MyStreamManager{


    StreamGenerator streamGenerator1 = new ScreenStateReceiver();
//    StreamGenerator streamGenerator2 = new BlueToothReceiver();
    StreamGenerator streamGenerator3 = new RingModeReceiver();
    StreamGenerator streamGenerator4 = new NetworkChangeReceiver();
    //    StreamGenerator streamGenerator5 = new ActivityRecognitionReceiver();
    StreamGenerator streamGenerator6 = (StreamGenerator) new LightSensorReceiver();
//    StreamGenerator streamGenerator7 = new TransportationModeReceiver();

    private Context context;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String device_id;

    private Handler handler = new Handler();
    private static MyStreamManager instance;
    public static MyStreamManager getInstance() {
        if(MyStreamManager.instance == null) {
            try {
                MyStreamManager.instance = new MyStreamManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return MyStreamManager.instance;
    }
    public void updateStreamGenerators(Context context) {
        streamGenerator1.updateStream(context);
//        streamGenerator2.updateStream();
        streamGenerator3.updateStream(context);
        streamGenerator4.updateStream(context);
//        streamGenerator5.updateStream(context);
        streamGenerator6.updateStream(context);
//        streamGenerator7.updateStream(context);
    }
    //transportation

    private ActivityRecognitionDataRecord activityRecognitionDataRecord;
    private TransportationModeDataRecord transportationModeDataRecord;
    public static Map<Class, StreamGenerator> mRegisteredStreamGenerators;
    public MyStreamManager(){
        mRegisteredStreamGenerators = new HashMap<>();
    }
    public void setActivityRecognitionDataRecord(ActivityRecognitionDataRecord activityRecognitionDataRecord){
        this.activityRecognitionDataRecord = activityRecognitionDataRecord;
    }
    public ActivityRecognitionDataRecord getActivityRecognitionDataRecord(){
        return activityRecognitionDataRecord;
    }
    public void setTransportationModeDataRecord(TransportationModeDataRecord transportationModeDataRecord, final Context context, SharedPreferences prefs) {

        Log.d(TAG, "[test triggering] incoming transportation: " + transportationModeDataRecord.getConfirmedActivityString());

        Boolean addSessionFlag = false;

        //the first time we see incoming transportation mode data
        if (this.transportationModeDataRecord == null) {

            // this.transportationModeDataRecord = new TransportationModeDataRecord(TransportationModeStreamGenerator.TRANSPORTATION_MODE_NAME_NA);
            //  Log.d(TAG, "[test triggering] test trip original null updated to " + this.transportationModeDataRecord.getConfirmedActivityString());
        } else {

            Log.d(TAG, "[test triggering] NEW: " + transportationModeDataRecord.getConfirmedActivityString() + " vs OLD:" + this.transportationModeDataRecord.getConfirmedActivityString());

            /**
             *  check if the new activity label is different from the previous activity label. IF it is different, we should do something
             * **/

//            String currentWork = context.getResources().getString(R.string.current_task);
//
//            Log.d(TAG, "in setTransportationModeDataRecord, currentWork is " + currentWork);


        }
    }
    public <T extends DataRecord> void register(com.recoveryrecord.surveyandroid.example.CSVDataRecord.Stream s,
                                                Class<T> clazz,
                                                StreamGenerator aStreamGenerator){

        mRegisteredStreamGenerators.put(clazz, aStreamGenerator);
        Log.d(TAG, "Registered a new stream generator for " + clazz);
    }

    public void unregister(Stream s, StreamGenerator sg)
            throws StreamNotFoundException {
        mRegisteredStreamGenerators.remove(sg);
    }
    @SuppressLint("LongLogTag")
    public <T extends DataRecord> StreamGenerator<T> getStreamGeneratorFor(Class<T> clazz)
            throws StreamNotFoundException {
        if(mRegisteredStreamGenerators.containsKey(clazz)) {
            return mRegisteredStreamGenerators.get(clazz);
        } else {
            throw new StreamNotFoundException();
        }
    }
}

