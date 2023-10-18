//package com.recoveryrecord.surveyandroid.example.receiever;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//
//public class ShutDownReceiver extends BroadcastReceiver {
//
//    public ShutDownReceiver() {
//    }
//
//    //    private appDatabase db;
////    UserDataRecord userRecord;
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        // TODO: This method is called when the BroadcastReceiver is receiving
//        // an Intent broadcast.
////        throw new UnsupportedOperationException("Not yet implemented");
////        pref = context.getSharedPreferences("test", MODE_PRIVATE);
////        db = appDatabase.getDatabase(context);
////        userRecord = db.userDataRecordDao().getLastRecord();
//        String intent_action = intent.getAction();
////        CSVHelper.storeToCSV("AlarmCreate.csv", "intent_action: " + intent_action);
//        /* 同一個接收者可以收多個不同行為的廣播，所以可以判斷收進來的行為為何，再做不同的動作 */
//        assert intent_action != null;
//        if (intent_action.equals("android.intent.action.ACTION_SHUTDOWN") ||
//                intent_action.equals("android.intent.action.QUICKBOOT_POWEROFF") ||
//                intent_action.equals("com.htc.intent.action.QUICKBOOT_POWEROFF") ) {
//            Log.d("BootOrShutDown", "shutdown service");
//            context.stopService(new Intent(context, NewsNotificationService.class));
//        }
//    }
//}
