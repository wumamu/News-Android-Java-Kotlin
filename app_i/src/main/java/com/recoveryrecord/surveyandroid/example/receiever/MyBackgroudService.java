package com.recoveryrecord.surveyandroid.example.receiever;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import static com.recoveryrecord.surveyandroid.example.config.Constants.DetectTime;

public class MyBackgroudService extends Service{
    MyStreamManager mystreamManager = new MyStreamManager();
    ScreenStateReceiver screenStateReceiver = new ScreenStateReceiver();
    @Override
    public void onCreate() {
        super.onCreate();
        mystreamManager = MyStreamManager.getInstance();
        Toast.makeText(this, "STREAM", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private static Handler mMainThread = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mystreamManager.updateStreamGenerators();
//            screenStateReceiver.updateStream();
//            Log.e("MyBackgroundService", "123");
            mMainThread.postDelayed(runnable, DetectTime);
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "OnStartCommand", Toast.LENGTH_SHORT).show();
        mMainThread.postDelayed(runnable, DetectTime);
        return super.onStartCommand(intent, flags, startId);
    }

}
