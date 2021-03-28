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

import java.util.Date;

import androidx.core.app.NotificationCompat;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

import static com.recoveryrecord.surveyandroid.example.config.SharedVariables.DIARY_ALARM;
import static com.recoveryrecord.surveyandroid.example.config.SharedVariables.ESM_ALARM;
import static com.recoveryrecord.surveyandroid.example.config.SharedVariables.NEWS_ALARM;

import static com.recoveryrecord.surveyandroid.example.config.Constants.SHAREPREFERENCE_TEST;

public class AlarmReceiever extends BroadcastReceiver {
    private static String TAG = "AlarmReceiver";
    private SharedPreferences pref;
    private long[] vibrate_effect = {100, 200, 300, 300, 500, 300, 300};
    NotificationManager ESM_manager;
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
            ESM_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            ESM_manager.createNotificationChannel(esmChannel);
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
            ESM_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            news_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            diary_manager =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }
    public void SendESMnoti(Context context) {
//        timeForQ = getReadableTime(System.currentTimeMillis());
//        CanFillEsm = true;
//        Log.d(TAG, "Send ESM");
//        String ESMtime = getReadableTime(System.currentTimeMillis());//2020-04-11 14:29:35
//        CSVHelper.storeToCSV("AlarmCreate.csv", "send ESM time: " + ESMtime);
//        Log.d(TAG, "TTime" + ESMtime);
////        pref.edit().putLong("Now_Esm_Time", System.currentTimeMillis()).apply();
//        pref.edit().putLong("ESM_send", System.currentTimeMillis()).apply();
////        if(pref.getBoolean("IsDestroy", true) || isFinish.equals("1")){
////            pref.edit().putLong("ESM_SendDestroy", System.currentTimeMillis()).apply();
////        }
//        pref.edit().putString("ESMtime", ESMtime).apply();//14:29:35
//        pref.edit().putBoolean("NewEsm", true).apply();
//
//        int EsmNum = 0;
//        int questionnaireID = 0;
//        String TodayResponseSet = "";
//        String[] TodayResponse = {};
//        String[] TotalEsmResponse = {};
//        if(userRecord != null){
//            long _id = userRecord.get_id();
//            String Tesm_str = userRecord.getTotal_ESM_number();
//            TodayResponseSet = userRecord.getESM_number();
//            TodayResponse = TodayResponseSet.split(",");
//            TotalEsmResponse = Tesm_str.split(",");
//            EsmNum = Integer.parseInt(TotalEsmResponse[TotalEsmResponse.length - 1]);
//            questionnaireID = userRecord.getquestionnaireID();
//            Log.d("QuestionActivity","questionnaireID: " + questionnaireID);
//            db.userDataRecordDao().updateQuestionnaireID(_id, questionnaireID + 1);
//            pref.edit().putInt("ESMID", questionnaireID + 1).apply();
//        }
//        Log.d(TAG, "ESM number = " + EsmNum + " ESM response size = " + TotalEsmResponse.length);
////        int EsmNum = pref.getInt("Esm_Num", 0);//今天有發幾封
//        EsmNum++;
//
//        if(userRecord != null){
//            String update_esm = "";
//            TotalEsmResponse[TotalEsmResponse.length - 1] = String.valueOf(EsmNum);
//            for(int i = 0; i < TotalEsmResponse.length; i++){
//                if(!TotalEsmResponse[i].equals("")){
//                    update_esm = update_esm + TotalEsmResponse[i] + ",";
//                }
//            }
//            long _id = userRecord.get_id();
//            db.userDataRecordDao().updateTotalESM(_id, update_esm);
//            Log.d(TAG, "ESM number = " + update_esm);
//        }
////        pref.edit().putInt("Esm_Num", EsmNum).apply();
//
//        // 12/16紀錄ESM的第三種狀態(沒回答消失)
//        int esmNum = 0;
//        for(int i = 0; i < TodayResponse.length; i++){
//            esmNum += Integer.parseInt(TodayResponse[i]);
//        }
//
//        int Total_ESM = 0;
//        for(int i = 0; i < TotalEsmResponse.length; i++){
//            Total_ESM += Integer.parseInt(TotalEsmResponse[i]);
//        }
//
//        int ESMID = pref.getInt("ESMID", 0);
//        FinalAnswerDataRecord finalAnswerDataRecord = new FinalAnswerDataRecord();
//        finalAnswerDataRecord.setGenerateTime(pref.getLong("ESM_send", 0));
//        finalAnswerDataRecord.setRespondTime(0L); //點進問卷的時間
//        finalAnswerDataRecord.setSubmitTime(0L);// onDestroy時間
//        finalAnswerDataRecord.setisFinish("0");
//        finalAnswerDataRecord.setQuesType("ESM");
//        finalAnswerDataRecord.setreplyCount(String.valueOf(esmNum));
//        finalAnswerDataRecord.settotalCount(String.valueOf(Total_ESM));
//        finalAnswerDataRecord.setAnswerChoicePos("0");
//        finalAnswerDataRecord.setAnswerChoiceState("1");
//        finalAnswerDataRecord.setanswerId(String.valueOf(0));
//        finalAnswerDataRecord.setdetectedTime(getReadableTime(System.currentTimeMillis()));
//        finalAnswerDataRecord.setQuestionId("0");
//        finalAnswerDataRecord.setsyncStatus(0);
//        finalAnswerDataRecord.setRelatedId(ESMID);
//        finalAnswerDataRecord.setAnswerChoice("");
//        finalAnswerDataRecord.setcreationIme(new Date().getTime());
//        db.finalAnswerDao().insertAll(finalAnswerDataRecord);


        String esm_id = String.valueOf(System.currentTimeMillis());
        Intent notificationIntent = new Intent(context, ExampleSurveyActivity.class); //Intent(this, 點下去會跳到ESM class)
        notificationIntent.putExtra("json_file_name", "ExampleQuestions.json");
        notificationIntent.putExtra("esm_id", esm_id);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //類似公式的東西 random
        PendingIntent pendingIntent = PendingIntent.getActivity(context, Integer.parseInt(esm_id), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder;
        Notification notification;
        builder = new NotificationCompat.Builder(context, ESM_CHANNEL_ID); //設定通知要有那些屬性
        builder.setContentTitle("YOUR ESM") // 通知的Title
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
        ESM_manager.notify((int)System.currentTimeMillis(), notification);                  //發送通知
    }

}
