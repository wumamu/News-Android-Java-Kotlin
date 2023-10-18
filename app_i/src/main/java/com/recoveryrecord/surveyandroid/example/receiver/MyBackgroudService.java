//package com.recoveryrecord.surveyandroid.example.receiver;
//
//import static com.recoveryrecord.surveyandroid.example.config.Config.DetectTime;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.IBinder;
//
//import androidx.annotation.Nullable;
//
//public class MyBackgroudService extends Service{
//    MyStreamManager mystreamManager = new MyStreamManager();
//    ScreenStateReceiver screenStateReceiver = new ScreenStateReceiver();
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mystreamManager = MyStreamManager.getInstance();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//    private static Handler mMainThread = new Handler();
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            mystreamManager.updateStreamGenerators(getApplicationContext());
////            screenStateReceiver.updateStream();
////            Log.e("MyBackgroundService", "123");
//            mMainThread.postDelayed(runnable, DetectTime);
//        }
//    };
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
////        Toast.makeText(this, "OnStartCommand", Toast.LENGTH_SHORT).show();
////        mystreamManager.updateStreamGenerators();
//        mMainThread.postDelayed(runnable, DetectTime);
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//}
