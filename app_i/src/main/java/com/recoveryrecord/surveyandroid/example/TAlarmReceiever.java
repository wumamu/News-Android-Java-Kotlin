package com.recoveryrecord.surveyandroid.example;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.firebase.Timestamp;

import java.util.Date;

import androidx.core.app.NotificationCompat;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

import static com.recoveryrecord.surveyandroid.example.config.SharedVariables.ESM_ALARM;

import static com.recoveryrecord.surveyandroid.example.config.Constants.SHAREPREFERENCE_TEST;

public class TAlarmReceiever extends BroadcastReceiver {
    private static String TAG = "AlarmReceiver";
    private SharedPreferences pref;
    private long[] vibrate_effect = {100, 200, 300, 300, 500, 300, 300};
    NotificationManager esm_manager;
    NotificationManager news_manager;
    NotificationManager diary_manager;
    NotificationManager mNotificationManager;
    Context mContext;
    public static final String ESM_CHANNEL_ID = "esmChannel";
    public static final String NEWS_CHANNEL_ID = "newsChannel";
    public static final String DIARY_CHANNEL_ID = "diaryChannel";
    public void onReceive(Context context, Intent intent) {
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel(context);
        String action = intent.getAction();

        //Fabric.with(context, new Crashlytics());

        //創建SharedPreferences，索引為 SHAREPREFERENCE_TEST
        pref = context.getSharedPreferences(SHAREPREFERENCE_TEST, MODE_PRIVATE);
        // second para is default value if did not exist
        int esmCount = pref.getInt("esm_count", 0);

//        db = appDatabase.getDatabase(context);
//        userRecord = db.userDataRecordDao().getLastRecord();

        if(action.equals(ESM_ALARM)) {
            Log.d(TAG, "Alarm Activate esm");
            SendESMnoti(context);//發問卷通知
        }
//        if(action.equals(NEWS_ALARM)){
//            Log.d(TAG, "Alarm Activate news");
//            SendNewsNoti(context);
//        }
//        if(action.equals(DIARY_ALARM)){
//            SendDiaryNoti(context);
//        }
//        if(action.equals(SERVICE_CHECKER)) {
//            Log.d(TAG, "Checker");
//            isServiceAlive(context);
//        }
//        if(action.equals(PHONE_STATE)) {
//            CallPhoneStateChecker(context);
//        }
//        if(action.equals(IS_ALIVE)) {
//            CallIsAlive(context);
//        }
//        if(action.equals(SCHEDULE_ALARM)) {
//            CallScheduleAlarm(context);
//        }

//        if(action.equals(CLEAN_DIARYNOTI)){
//            RemoveDiaryNoti();
//        }
    }
    private void createNotificationChannel(Context context) {
        //do NotificationListenerNews
        Log.d(TAG, "createNotificationChannel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //for esm
            NotificationChannel esmChannel = new NotificationChannel(
                    ESM_CHANNEL_ID,
                    "esm Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            esmChannel.setVibrationPattern(vibrate_effect);
            esmChannel.enableVibration(true);
            esm_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            esm_manager.createNotificationChannel(esmChannel);
            //for news
            NotificationChannel newsChannel = new NotificationChannel(
                    NEWS_CHANNEL_ID,
                    "news Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            newsChannel.setVibrationPattern(vibrate_effect);
            newsChannel.enableVibration(true);
            news_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            news_manager.createNotificationChannel(newsChannel);
            //for diary
            NotificationChannel dairyChannel = new NotificationChannel(
                    DIARY_CHANNEL_ID,
                    "Dairy Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            dairyChannel.setVibrationPattern(vibrate_effect);
            dairyChannel.enableVibration(true);
            diary_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            diary_manager.createNotificationChannel(dairyChannel);
        } else {
            //without channel??????????????
            esm_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            news_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            diary_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }
    public void SendESMnoti(Context context) {
        //do getNotification scheduleNotification
        Date date = new Date(System.currentTimeMillis());
        String esm_id = "";
        esm_id = String.valueOf(date);
        Intent notificationIntent = new Intent(context, ESMActivity.class); //Intent(this, 點下去會跳到ESM class)
        notificationIntent.putExtra("json_file_name", "ExampleQuestions.json");
        notificationIntent.putExtra("esm_id", esm_id);
        notificationIntent.putExtra("noti_timestamp", Timestamp.now());

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //類似公式的東西 random
        int time_int = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, time_int, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder;
        Notification notification;
        builder = new NotificationCompat.Builder(context, ESM_CHANNEL_ID); //設定通知要有那些屬性
        builder.setContentTitle("ESM") // 通知的Title
                .setContentText("Please fill out the questionnaire\n"+ esm_id)                        //通知的內容
                .setSmallIcon(R.drawable.ic_launcher_foreground)            //通知的icon
                .setContentIntent(pendingIntent)               //點下去會跳到ESM class
                //.setOngoing(true)                              //使用者滑不掉
                .setAutoCancel(true)                           //點擊之後通知消失
                .setVibrate(vibrate_effect)                    //震動模式
                .setTimeoutAfter(900000)                    //幾毫秒之後自動消失 15min
                .build();
        notification = builder.build();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        esm_manager.notify((int)System.currentTimeMillis(), notification);                  //發送通知
    }

}
