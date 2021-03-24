package com.recoveryrecord.surveyandroid.example.receiever;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

public class BlueToothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("log: bluetooth device", "123");
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//            mDeviceList.add(device.getName() + "\n" + device.getAddress());
            Log.e("log: bluetooth device", device.getName() + " " + device.getAddress());
//            Log.e("BT", device.getName() + "\n" + device.getAddress());
//            listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, mDeviceList));
        }
    }
}
