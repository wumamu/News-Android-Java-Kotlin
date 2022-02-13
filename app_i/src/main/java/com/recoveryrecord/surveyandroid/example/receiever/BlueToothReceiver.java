package com.recoveryrecord.surveyandroid.example.receiever;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.recoveryrecord.surveyandroid.example.config.Constants.DetectTime;
import static com.recoveryrecord.surveyandroid.example.config.Constants.UsingApp;

public class BlueToothReceiver implements StreamGenerator{
    private static final String TAG = "Main";
    private BluetoothStateBroadcastReceiver mReceiver;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String device_id;
    Map<String, Object> sensordb = new HashMap<>();
    private static String BlueToothState = "NA";
    final Timestamp current_end = Timestamp.now();
    Date date = new Date(System.currentTimeMillis());
    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    final String time_now = formatter.format(date);
    private final static int REQUEST_ENABLE_BT = 1;
//    public void getBlueToothDevice() {
//        //獲取藍芽配對裝置
//        mBluetoothadapter = BluetoothAdapter.getDefaultAdapter();
//    }
//    public void getBlueToothState() {
//        //獲取藍芽狀態
//
//    }

    public void registerBluetoothReceiver(Context context){
        if(mReceiver == null){
            mReceiver = new BluetoothStateBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("BlueTooth", "Register");
        context.registerReceiver(mReceiver, filter);

    }


    class BluetoothStateBroadcastReceiver extends BroadcastReceiver {
        int devicecount = 0;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            final Timestamp current_end = Timestamp.now();
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            final String time_now = formatter.format(date);
            sensordb.put("Time", time_now);
            sensordb.put("TimeStamp", Timestamp.now());
//            BluetoothDevice device = (BluetoothDevice) intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
                Log.e("log: bluetooth device", "ACTION_DISCOVERY_STARTED");
//                Toast.makeText(context, "開始偵測藍芽", Toast.LENGTH_LONG).show();
                BlueToothState = "BlueTooth Is Opened";
//                sensordb.put("Time", time_now);
//                sensordb.put("BlueTooth", "Start Detect BlueTooth");
                devicecount = 0;
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
                Log.e("log: bluetooth device", "ACTION_DISCOVERY_FINISHED");
//                Toast.makeText(context, "結束偵測藍芽", Toast.LENGTH_LONG).show();
                BlueToothState = "Stop Detect BlueTooth";
                sensordb.put("Time", time_now);
                sensordb.put("TimeStamp", Timestamp.now());
                sensordb.put("BlueTooth", "Stop Detect BlueTooth");

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                Log.e("log: bluetooth device", "ACTION_FOUND");
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                Toast("Found device " + device.getName());
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.e("log: bluetooth device", deviceName + " " + deviceHardwareAddress);
//                sensordb.put("BlueTooth Device", deviceHardwareAddress);
//                sensordb.put("BlueTooth", "Founding");
                devicecount = devicecount + 1 ;
            }
            Log.e("Device number", String.valueOf(devicecount));
            sensordb.put("BlueTooth Device Number", devicecount);
            sensordb.put("Using APP", UsingApp);
            if(BlueToothState == "Stop Detect BlueTooth") {
                sensordb.put("device_id", device_id);
                sensordb.put("period", "Trigger Event");
                db.collection("Sensor collection")
                        .document("Sensor")
                        .collection("BlueTooth")
                        .document(device_id + " " + time_now)
                        .set(sensordb);
            }
        }
    };

    @Override
    public void updateStream(Context context) {
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter( );
        if( !mBtAdapter.isEnabled( ) ) {
            BlueToothState = "BlueTooth Is Closed";
        }else{
            BlueToothState = "BlueTooth Is Opened";
            if (mBtAdapter.isDiscovering()) {
                mBtAdapter.cancelDiscovery();
            }
            mBtAdapter.startDiscovery();
        }
        final Timestamp current_end = Timestamp.now();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        final String time_now = formatter.format(date);
        sensordb.put("Time", time_now);
        sensordb.put("TimeStamp", Timestamp.now());
        sensordb.put("BlueTooth", BlueToothState);
        sensordb.put("Using APP", UsingApp);
        sensordb.put("device_id", device_id);
        sensordb.put("period", DetectTime);
        db.collection("Sensor collection")
                .document("Sensor")
                .collection("BlueTooth")
                .document(device_id + " " + time_now)
                .set(sensordb);
    }


    public void unregisterBluetoothReceiver(Context context){
        if(mReceiver != null){
            context.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
}
