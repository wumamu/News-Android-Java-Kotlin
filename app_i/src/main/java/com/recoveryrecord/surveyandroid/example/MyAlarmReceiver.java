//package com.recoveryrecord.surveyandroid.example;
//
//import android.app.AlarmManager;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.util.Log;
//
//import com.google.firebase.Timestamp;
//
//import java.util.Calendar;
//import java.util.Date;
//
//import androidx.annotation.RequiresApi;
//import androidx.core.app.NotificationCompat;
//
//import static android.content.Context.ALARM_SERVICE;
//import static android.content.Context.MODE_PRIVATE;
//import static android.content.Context.NOTIFICATION_SERVICE;
//
//import static com.recoveryrecord.surveyandroid.example.Constants.CHECK_SERVICE_ACTION;
//import static com.recoveryrecord.surveyandroid.example.Constants.DAIRY_ALARM_ACTION;
//import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_TEXT;
//import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_TITLE_CONTENT;
//import static com.recoveryrecord.surveyandroid.example.Constants.ESM_ALARM_ACTION;
//import static com.recoveryrecord.surveyandroid.example.Constants.SCHEDULE_ALARM_ACTION;
//import static com.recoveryrecord.surveyandroid.example.config.SharedVariables.ESM_ALARM;
//
//import static com.recoveryrecord.surveyandroid.example.config.Constants.SHAREPREFERENCE_TEST;
//
//public class MyAlarmReceiver extends BroadcastReceiver {
//    private static String TAG = "TAlarmReceiver";
//    private SharedPreferences pref;
//    private long[] vibrate_effect = {100, 200, 300, 300, 500, 300, 300};
//    NotificationManager esm_manager;
//    NotificationManager news_manager;
//    NotificationManager diary_manager;
//    NotificationManager mNotificationManager;
//    Context mContext;
//    public static final String ESM_CHANNEL_ID = "esmChannel";
//    public static final String NEWS_CHANNEL_ID = "newsChannel";
//    public static final String DIARY_CHANNEL_ID = "diaryChannel";
//
//    public static final int ESM_ID = 1;
////    public static final int REMIND_ID = 2;
//    public static final int DIARY_ID = 2;
//    private static final int DIARY_REQUEST = 6;
//
//    public void onReceive(Context context, Intent intent) {
//        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
////        createNotificationChannel(context);
//        String action = intent.getAction();
//
////        if(action.equals(ESM_ALARM_ACTION)) {
////            Log.d(TAG, "Alarm Activate esm");
//////            SendESMnoti(context);//發問卷通知
////        }
////        if(action.equals(DAIRY_ALARM_ACTION)){
//////            SendDiaryNoti(context);
////        }
//        if(action.equals(SCHEDULE_ALARM_ACTION)) {
////            CallScheduleAlarm(context);
//        }
//        if(action.equals(CHECK_SERVICE_ACTION)) {
//            Log.d(TAG, "Checker");
//            isServiceAlive(context);
//        }
//    }
//    private void createNotificationChannel(Context context) {
//        //do NotificationListenerNews
//        Log.d(TAG, "createNotificationChannel");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //for esm
//            NotificationChannel esmChannel = new NotificationChannel(
//                    ESM_CHANNEL_ID,
//                    "esm Service Channel",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            esmChannel.setVibrationPattern(vibrate_effect);
//            esmChannel.enableVibration(true);
//            esm_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//            esm_manager.createNotificationChannel(esmChannel);
//            //for news
//            NotificationChannel newsChannel = new NotificationChannel(
//                    NEWS_CHANNEL_ID,
//                    "news Service Channel",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            newsChannel.setVibrationPattern(vibrate_effect);
//            newsChannel.enableVibration(true);
//            news_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//            news_manager.createNotificationChannel(newsChannel);
//            //for diary
//            NotificationChannel dairyChannel = new NotificationChannel(
//                    DIARY_CHANNEL_ID,
//                    "Dairy Service Channel",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            dairyChannel.setVibrationPattern(vibrate_effect);
//            dairyChannel.enableVibration(true);
//            diary_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//            diary_manager.createNotificationChannel(dairyChannel);
//        } else {
//            //without channel??????????????
//            esm_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//            news_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//            diary_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        }
//    }
//    public void SendESMnoti(Context context) {
//        //do getNotification scheduleNotification
//        Date date = new Date(System.currentTimeMillis());
//        String esm_id = "";
//        String json_file_name = "3.json";
//        esm_id = String.valueOf(date);
//        Intent notificationIntent = new Intent(context, SurveyActivity.class); //Intent(this, 點下去會跳到ESM class)
//        notificationIntent.putExtra("json_file_name", "diary.json");
//        notificationIntent.putExtra("esm_id", esm_id);
//        notificationIntent.putExtra("noti_timestamp", Timestamp.now());
//        notificationIntent.putExtra("json_file_name", json_file_name);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        //類似公式的東西 random
//        int time_int = (int) System.currentTimeMillis();
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, time_int, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder builder;
//        Notification notification;
//
//        builder = new NotificationCompat.Builder(context, ESM_CHANNEL_ID); //設定通知要有那些屬性
//        builder.setContentTitle("ESM") // 通知的Title
//                .setContentText("Please fill out the questionnaire\n"+ esm_id)                        //通知的內容
//                .setSmallIcon(R.drawable.ic_launcher_foreground)//通知的icon
//                .setContentIntent(pendingIntent)                //點下去會跳到ESM class
//                //.setOngoing(true)                             //使用者滑不掉
//                .setAutoCancel(true)                            //點擊之後通知消失
//                .setVibrate(vibrate_effect)                     //震動模式
//                .setTimeoutAfter(900000)                        //幾毫秒之後自動消失 15min
//                .build();
//        notification = builder.build();
//        try {
//            Thread.sleep(1000);
//        } catch (Exception e) {
//        }
////        esm_manager.notify((int)System.currentTimeMillis(), notification);                  //發送通知
//        esm_manager.notify(ESM_ID, notification);                  //發送通知
//    }
//    public void SendDiaryNoti(Context context) {
//        Log.d(TAG, "Diary Here");
//
//        Date date = new Date(System.currentTimeMillis());
//        String diary_id = "";
//        String json_file_name = "3.json";
//        diary_id = String.valueOf(date);
//        Intent notificationIntent = new Intent(context, SurveyActivity.class); //Intent(this, 點下去會跳到ESM class)
//        notificationIntent.putExtra("json_file_name", "diary.json");
//        notificationIntent.putExtra("esm_id", diary_id);
//        notificationIntent.putExtra("noti_timestamp", Timestamp.now());
//        notificationIntent.putExtra("json_file_name", json_file_name);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, DIARY_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder builder;
//        Notification notification;
//
//        builder = new NotificationCompat.Builder(context, ESM_CHANNEL_ID); //設定通知要有那些屬性
//        builder.setContentTitle(DIARY_TITLE_CONTENT)            //通知的Title
//                .setContentText(DIARY_TEXT)                     //通知的內容
//                .setSmallIcon(R.drawable.ic_launcher_foreground)//通知的icon
//                .setContentIntent(pendingIntent)                //點下去會跳到ESM class
//                .setOngoing(true)                               //使用者滑不掉
//                .setAutoCancel(true)                            //點擊之後通知消失
//                .setVibrate(vibrate_effect)                     //震動模式
//                .build();
//        notification = builder.build();
//        try {
//            Thread.sleep(1000);
//        } catch (Exception e) {
//        }
//        diary_manager.notify(DIARY_ID, notification);                  //發送通知
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void CallScheduleAlarm(Context context) {
//        Log.d(TAG,"Alarm runnable: " + System.currentTimeMillis());
//        Calendar dairy_cal = Calendar.getInstance(); //取得時間
//        dairy_cal.set(Calendar.HOUR_OF_DAY, 22);
//        dairy_cal.set(Calendar.MINUTE, 0);
//        dairy_cal.set(Calendar.SECOND, 0);
//        add_diary(context, dairy_cal);
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public static void add_diary(Context context, Calendar cal) {
//        Log.d(TAG, "Add diary alarm");
//        Log.d(TAG, "diary time: " + String.valueOf(cal.get(Calendar.MONTH)) + "." + String.valueOf(cal.get(Calendar.DATE)) + " " + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
////        if(userRecord != null)
////        {
////            db.userDataRecordDao().updateDiaryClick(userRecord.get_id(), false);
////        }
////        userRecord.setDiaryClick(false);
//        Log.d(TAG, "Diary click is false");
////        context.getSharedPreferences("test", MODE_PRIVATE).edit().putBoolean("DiaryClick", false).apply();
////        CSVHelper.storeToCSV("AlarmCreate.csv", "Diary alarm time: " + stampToDate(cal.getTimeInMillis()));
//        Intent intent = new Intent(context, TAlarmReceiver.class);
//        // 以日期字串組出不同的 category 以添加多個鬧鐘
//        //intent.addCategory("ID." + String.valueOf(cal.get(Calendar.MONTH)) + "." + String.valueOf(cal.get(Calendar.DATE)) + "-" + String.valueOf((cal.get(Calendar.HOUR_OF_DAY) )) + "." + String.valueOf(cal.get(Calendar.MINUTE)) + "." + String.valueOf(cal.get(Calendar.SECOND)));
//        //String AlarmTimeTag = "Alarmtime " + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(cal.get(Calendar.MINUTE)) + ":" + String.valueOf(cal.get(Calendar.SECOND));
//
//        intent.setAction(DAIRY_ALARM_ACTION);
//        //intent.putExtra("time", AlarmTimeTag);
//
//        PendingIntent pi = PendingIntent.getBroadcast(context, DIARY_REQUEST, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//        am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);       //註冊鬧鐘
//    }
//    public static void add_ServiceChecker(Context context)
//    {
//        Log.d(TAG, "add service checker");
//        //Log.d(TAG, "remind time: " + String.valueOf(cal.get(Calendar.MONTH)) + "." + String.valueOf(cal.get(Calendar.DATE)) + " " + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
//        try {
//            Thread.sleep(200);
//        }
//        catch(Exception e) {
//
//        }
//        Intent intent = new Intent(context, TAlarmReceiver.class);
//        intent.setAction(SERVICE_CHECKER);
//        //intent.putExtra("time", AlarmTimeTag);
//        PendingIntent pi = PendingIntent.getBroadcast(context, 20, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//        long time_fired = System.currentTimeMillis() + CheckInterval*1000*60;
//        Log.d(TAG, "Service Checker fire time: " + stampToDate(time_fired));
//        CSVHelper.storeToCSV("AlarmCreate.csv", "ServiceChecker alarm time: " + stampToDate(time_fired));
//        am.setExact(AlarmManager.RTC_WAKEUP, time_fired, pi);       //註冊鬧鐘
//    }
//    public void isServiceAlive(Context context)
//    {
//        Log.d(TAG, "Background service running: " + isMyServiceRunning(com.example.accessibility_detect.MyBackgroundService.class, context));
//        if(!isMyServiceRunning(com.example.accessibility_detect.MyBackgroundService.class, context))
//        {
////            CSVHelper.storeToCSV("AlarmCreate.csv", "isServiceAlive");
//            mNotificationManager.cancel(1);
//            mNotificationManager.cancel(2);
//            mNotificationManager.cancel(3);
//            mNotificationManager.cancel(4);
//            mNotificationManager.cancel(9);
//            if(userRecord != null){
//                db.userDataRecordDao().updateIsKilled(userRecord.get_id(), true);
//            }
////            userRecord.setIsKilled(true);
//            Log.d(TAG, "is killed is true");
////            pref.edit().putBoolean("IsKilled", true).apply();
//            context.startService(new Intent(context, MyBackgroundService.class));
//        }
//        add_ServiceChecker(context);
//    }
//
//}
