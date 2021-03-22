//package com.recoveryrecord.surveyandroid.example;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//import android.widget.Toast;
//
//public class NotificationReceiver extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
////            String temp = intent.getStringExtra("notification_event") + txtView.getText();
////            String temp = intent.getStringExtra("notification_list") + txtView.getText();
////            txtView.setText(temp);
//        String to_display_all = intent.getStringExtra("notification_list");
////            String to_display_post = intent.getStringExtra("notification_post");
//        if (to_display_all!=null){
//            Log.i("NLService","listen success");
//            String[] split = to_display_all.split("\n");
//            String packagename = "", time = "", tickertext = "", title = "", text = "";
//            Log.i("123", String.valueOf(split.length));
//            if(split.length==6){
//                time = time + split[1];
//                packagename = packagename + split[2];
//                tickertext = tickertext + split[3];
//                title = title + split[4];
//                text = text + split[5];
////                NotificationDbHelper dbHandler = new NotificationDbHelper(NotificationSettingActivity.this);
////                dbHandler.insertNotificationDetails(packagename, tickertext, time, title, text);
////                Toast.makeText(getApplicationContext(), "Details Inserted Successfully", Toast.LENGTH_SHORT).show();
//            }
//        }
////            if (to_display_post!=null){
////                Log.i("NLService","123");
////                String[] split_post = to_display_post.split("\n");
////                String packagename_post = "", time_post = "", tickertext_post = "", title_post = "", text_post = "";
////                if(split_post.length==6){
////                    time_post = time_post + split_post[1];
////                    packagename_post = packagename_post + split_post[2];
////                    tickertext_post = tickertext_post + split_post[3];
////                    title_post = title_post + split_post[4];
////                    text_post = text_post + split_post[5];
////                    NotificationDbHandler dbHandler = new NotificationDbHandler(NotificationListActivity.this);
////                    dbHandler.insertUserDetails(packagename_post, tickertext_post, time_post, title_post, text_post);
////                Toast.makeText(getApplicationContext(), "Post Inserted Successfully", Toast.LENGTH_SHORT).show();
////                }
////            } else {
////                Log.i("NLService","456");
////            }
//
//
//    }
//}