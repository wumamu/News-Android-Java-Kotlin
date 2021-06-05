package com.recoveryrecord.surveyandroid.example.receiever;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.$Gson$Preconditions;

import java.nio.file.SecureDirectoryStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MyStreamManager{
//    private static ScreenStateReceiver screenStateReceiver = new ScreenStateReceiver();
////    Context context;
////    Intent intent = new Intent(context, ScreenStateReceiver.class);

    StreamGenerator streamGenerator1 = new ScreenStateReceiver();
    StreamGenerator streamGenerator2 = new BlueToothReceiver();
    StreamGenerator streamGenerator3 = new RingModeReceiver();
    StreamGenerator streamGenerator4 = new NetworkChangeReceiver();
    StreamGenerator streamGenerator5 = new ActivityRecognitionReceiver();
    StreamGenerator streamGenerator6 = (StreamGenerator) new LightSensorReceiver();

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
    public void updateStreamGenerators() {
        streamGenerator1.updateStream();
        streamGenerator2.updateStream();
        streamGenerator3.updateStream();
        streamGenerator4.updateStream();
        streamGenerator5.updateStream();
        streamGenerator6.updateStream();
        }
}
