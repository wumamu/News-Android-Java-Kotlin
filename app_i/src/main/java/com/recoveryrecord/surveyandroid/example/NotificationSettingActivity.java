package com.recoveryrecord.surveyandroid.example;
import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.recoveryrecord.surveyandroid.example.activity.NotificationDbViewActivity;

import androidx.core.app.ActivityCompat;

public class NotificationSettingActivity extends Activity {
//    private TextView txtView;
    private NotificationReceiver nReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
        if(ActivityCompat.checkSelfPermission(NotificationSettingActivity.this, Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)!= PackageManager.PERMISSION_GRANTED){
            Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }
//        txtView = (TextView) findViewById(R.id.textView);
        Button btnCreateNotify = (Button) findViewById(R.id.btnCreateNotify);
        Button btnClearNotify = (Button) findViewById(R.id.btnClearNotify);
        Button btnCatchNotify = (Button) findViewById(R.id.btnCatchNotify);
        Button btnListNotify = (Button) findViewById(R.id.btnListNotify);
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver,filter);
        btnCreateNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationSettingActivity.this, AddNotificationActivity.class);
                startActivity(intent);
//                NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
//                ncomp.setContentTitle("My Notification");
//                ncomp.setContentText("Notification Listener Service Example");
//                ncomp.setTicker("Notification Listener Service Example");
//                ncomp.setSmallIcon(R.drawable.ic_launcher);
//                ncomp.setAutoCancel(true);
//                nManager.notify((int)System.currentTimeMillis(),ncomp.build());
            }
        });
        btnClearNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("log: NLService", "NotificationListActivity");
                Intent i = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
                i.putExtra("command","clearall");
                sendBroadcast(i);
            }
        });
        btnCatchNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("log: NLService", "NotificationListActivity");
                Intent i = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
                i.putExtra("command","list");
                sendBroadcast(i);
            }
        });
        btnListNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationSettingActivity.this, NotificationDbViewActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            String temp = intent.getStringExtra("notification_event") + txtView.getText();
//            String temp = intent.getStringExtra("notification_list") + txtView.getText();
//            txtView.setText(temp);
            String to_display_all = intent.getStringExtra("notification_list");
//            String to_display_post = intent.getStringExtra("notification_post");
            if (to_display_all!=null){
                Log.i("NLService","listen success");
                String[] split = to_display_all.split("\n");
                String packagename = "", time = "", tickertext = "", title = "", text = "";
                Log.i("123", String.valueOf(split.length));
                if(split.length==6){
                    time = time + split[1];
                    packagename = packagename + split[2];
                    tickertext = tickertext + split[3];
                    title = title + split[4];
                    text = text + split[5];
                    NotificationDbHelper dbHandler = new NotificationDbHelper(NotificationSettingActivity.this);
                    dbHandler.insertNotificationDetails(packagename, tickertext, time, title, text);
                    Toast.makeText(getApplicationContext(), "Details Inserted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
//            if (to_display_post!=null){
//                Log.i("NLService","123");
//                String[] split_post = to_display_post.split("\n");
//                String packagename_post = "", time_post = "", tickertext_post = "", title_post = "", text_post = "";
//                if(split_post.length==6){
//                    time_post = time_post + split_post[1];
//                    packagename_post = packagename_post + split_post[2];
//                    tickertext_post = tickertext_post + split_post[3];
//                    title_post = title_post + split_post[4];
//                    text_post = text_post + split_post[5];
//                    NotificationDbHandler dbHandler = new NotificationDbHandler(NotificationListActivity.this);
//                    dbHandler.insertUserDetails(packagename_post, tickertext_post, time_post, title_post, text_post);
//                Toast.makeText(getApplicationContext(), "Post Inserted Successfully", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Log.i("NLService","456");
//            }


        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NotificationSettingActivity.this, TestActivity.class);
        startActivity(intent);
    }

}