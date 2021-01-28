//package com.example.test;
//
//import android.app.Activity;
//import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.util.Log;
//
//public class MySensorActivity extends Activity implements SensorEventListener {
//    private SensorManager mSensorManager;
//    private Sensor mPressure;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sample_news);
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
//
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        float millibars_of_pressure = event.values[0];
//        Log.d("sensorValues", String.valueOf(millibars_of_pressure));
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mSensorManager.unregisterListener(this);
//    }
//}