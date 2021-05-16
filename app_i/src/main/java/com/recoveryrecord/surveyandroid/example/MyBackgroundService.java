//package com.recoveryrecord.surveyandroid.example;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.os.Handler;
//import android.os.IBinder;
//import android.provider.Settings;
//import android.util.Log;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.concurrent.Executors;
//
//import androidx.annotation.Nullable;
//
//public class MyBackgroundService extends Service{
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.d(TAG, "onCreate() executed");
//
////        Fabric.with(this, new Crashlytics());
//        StartForeground();
//
//        boolean getSettingPermission = getSharedPreferences("test", MODE_PRIVATE)
//                .getBoolean("SettingPermission", false);
//        Log.d(TAG, "SettingPermission: " + getSettingPermission);
//        pref = getSharedPreferences("test",MODE_PRIVATE);
////        boolean iskilled = pref.getBoolean("IsKilled", false);
////        if(iskilled){
////            Intent MainIntent = new Intent(this,MainActivity.class);
////            startActivity(MainIntent);
////        }
//        if(!getSettingPermission){
//            SharedPreferences p = getSharedPreferences("test", MODE_PRIVATE);
//            p.edit().putBoolean("SettingPermission", true).apply();
//
//            Intent AccessibilitySetting = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);// 協助工具
//            AccessibilitySetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            Intent UsageSetting = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS); //usage
//            UsageSetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            Intent NotiSetting = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);//notification
//            NotiSetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            Intent LocationSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);    //location
//            LocationSetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            getApplicationContext().startActivity(AccessibilitySetting);
//            getApplicationContext().startActivity(UsageSetting);
//            getApplicationContext().startActivity(NotiSetting);
//            getApplicationContext().startActivity(LocationSetting);
//        }
//        isBackgroundServiceRunning = false;
//        isBackgroundRunnableRunning = false;
//        streamManager = MinukuStreamManager.getInstance();
//        mScheduledExecutorService = Executors.newScheduledThreadPool(Constants.MAIN_THREAD_SIZE);
//
//        intentFilter = new IntentFilter();
//        intentFilter.addAction(CONNECTIVITY_ACTION);
//        intentFilter.addAction(Constants.ACTION_CONNECTIVITY_CHANGE);
//
//        for(int i = 0; i < Trigger_list.length; i++){
//            Last_Agree.put(Trigger_list[i], false);
//            Last_Dialog_Time.put(Trigger_list[i], 0L);
//        }
//        Random_session_num = (int)(Math.random()* 3 + 1);//random 1~3
//        CSVHelper.storeToCSV("ESM_random_number.csv", "In background reset random number to: " + Random_session_num);
//        Log.d(TAG, "Random session number = " + Random_session_num);
//        //IntentFilter checkRunnableFilter = new IntentFilter(CHECK_RUNNABLE_ACTION);
//        //registerReceiver(CheckRunnableReceiver, checkRunnableFilter);
//
//        /*mPhoneStateChecker= new PhoneStateChecker();
//        registerReceiver(mPhoneStateChecker,intentFilter);*/
//
//        IntentFilter itFilter = new IntentFilter("PhoneStateChecker");
//        mPhoneStateChecker = new PhoneStateChecker();
//        registerReceiver(mPhoneStateChecker, itFilter); //註冊廣播接收器
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
//        intentFilter.addAction("android.intent.action.RECEIVE_QUICKBOOT_POWEROFF");
//        intentFilter.addAction("com.htc.intent.action.QUICKBOOT_POWEROFF");
//        shutdownReceiver = new ShutDownReceiver();
//        registerReceiver(shutdownReceiver, intentFilter); //註冊廣播接收器
//
//        //LocalBroadcastManager.getInstance(this).registerReceiver(CheckRunnableReceiver,checkRunnableFilter);
//
//        //registerConnectivityNetworkMonitorForAPI21AndUp();
//
//        // 9/14
//        appDatabase db;
//        db = appDatabase.getDatabase(this);
//        UserDataRecord userRecord = db.userDataRecordDao().getLastRecord();
//
//        Long nowTime = new Date().getTime();
//        appStartHour = getReadableTimeLong(nowTime);
//
//        if(userRecord == null){
//            Log.d(TAG, "user data record null ");
//            UserDataRecord userFirst = new UserDataRecord("NA", "", "","0",
//                    "0", "9","22", "0",
//                    false, false, appStartHour, false);
//            db.userDataRecordDao().insertAll(userFirst);
//        }
//        else{
//            Log.d(TAG, "user data record Not null ");
//        }
//
//        if (!InstanceManager.isInitialized()) {
//            //  CSVHelper.storeToCSV(CSVHelper.CSV_RUNNABLE_CHECK, "Going to start the runnable.");
//            Log.d(TAG, "InstanceManager");
//            InstanceManager.getInstance(this);
//            //SessionManager.getInstance(this);
//            //MobilityManager.getInstance(this);
//        }
//        //mScheduledExecutorService.schedule(AlarmRunnable,0,TimeUnit.SECONDS);
//        handler = new Handler();
//    }
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TAG, "onStartCommand() executed");
//        CSVHelper.storeToCSV("TestService.csv", "Background service onStartCommand");
//
////        createNotificationChannel(this);
//
//        MyAccessibilityService mobileAccessibilityService;
//        mobileAccessibilityService = new MyAccessibilityService();
//        Intent in = new Intent(this, MyAccessibilityService.class);
//        this.startService(in);
//
//        appDatabase db;
//        db = appDatabase.getDatabase(this);
//        UserDataRecord userRecord = db.userDataRecordDao().getLastRecord();
//        if(userRecord != null) {
//            exec = userRecord.getExec();
//        }
//        else{
//            Log.d(TAG, "user record is null");
//        }
////        exec = getSharedPreferences("test",MODE_PRIVATE).getBoolean("execute", false);
//
//        // original
//        add_ServiceChecker(this);
//        updateNotificationAndStreamManagerThread();
//
//        //sure that these command execute only once even restart
//        if(!exec) {
//            //4/17
//            //SendESMnoti(this);
//            //add_reminder(this);
//
//            Long nowTime = new Date().getTime();
//            appStartHour = getReadableTimeLong(nowTime);
//
//            if(userRecord != null) {
//                long _id = userRecord.get_id();
//                db.userDataRecordDao().updateExec(_id, true);
//                db.userDataRecordDao().updateAppstart(_id, appStartHour);
//            }
////            getSharedPreferences("test", MODE_PRIVATE).edit().putLong("appStartHour", appStartHour).apply();
//
//
////            getSharedPreferences("test", MODE_PRIVATE).edit().putBoolean("execute", true).apply();
//        }
//
//        mMainThread = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    //  CSVHelper.storeToCSV(CSVHelper.CSV_RUNNABLE_CHECK, "updateRun_isBackgroundServiceRunning ? "+isBackgroundServiceRunning);
//                    //  CSVHelper.storeToCSV(CSVHelper.CSV_RUNNABLE_CHECK, "updateRun_isBackgroundServiceRunning ? "+isBackgroundRunnableRunning);
//                    Log.d(TAG, "Stream update runnable: " + System.currentTimeMillis());
//                    streamManager.updateStreamGenerators();
//                } catch (Exception e) {
//                    //   CSVHelper.storeToCSV(CSVHelper.CSV_RUNNABLE_CHECK, "Background, service update, stream, Exception");
//                    //   CSVHelper.storeToCSV(CSVHelper.CSV_RUNNABLE_CHECK, Utils.getStackTrace(e));
//                }
//                Log.d("BootCompleteReceiver", "In updateStreamManagerRunnable ");
//
//                mMainThread.postDelayed(this, 5*1000);
//            }
//        };
//        mMainThread.post(runnable);
//        //LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(CHECK_RUNNABLE_ACTION));
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    private void updateNotificationAndStreamManagerThread() {
//        try {
//            Thread.sleep(200);
//            Intent Isalive_intent = new Intent(this, TAlarmReceiver.class);
//            Isalive_intent.setAction(IS_ALIVE);
//
//            PendingIntent Isalive_pi = PendingIntent.getBroadcast(this, 200, Isalive_intent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//
//            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),30*60*1000,Isalive_pi);
//        }
//        catch(Exception e) {
//
//        }
//
//        try {
//            Thread.sleep(200);
//            Intent ESMalarm_intent = new Intent(this, TAlarmReceiver.class);
//            ESMalarm_intent.setAction(SCHEDULE_ALARM);
//
//            PendingIntent ESMalarm_pi = PendingIntent.getBroadcast(this, 300, ESMalarm_intent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//            Calendar cal = Calendar.getInstance(); //取得時間
//
//            if(!exec) cal.add(cal.DAY_OF_MONTH,0);
//            else cal.add(cal.DAY_OF_MONTH,0);
//
//            cal.set(Calendar.HOUR_OF_DAY, 0);
//            cal.set(Calendar.MINUTE, 0);
//            cal.set(Calendar.SECOND, 10);
//            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//
//            Log.d(TAG, "Set ESM alarm date: " + cal);
//            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),24*60*60*1000,ESMalarm_pi);
//        }
//        catch(Exception e) {
//
//        }
//    }
//}
