package com.recoveryrecord.surveyandroid.example;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import static com.recoveryrecord.surveyandroid.example.MainActivity.NOTIFICATION_CHANNEL_ID;

public class NotificationListActivity extends Activity {
//    private TextView txtView;
    private NotificationReceiver nReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        if(ActivityCompat.checkSelfPermission(NotificationListActivity.this, Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)!= PackageManager.PERMISSION_GRANTED){
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
                Intent intent = new Intent(NotificationListActivity.this, TmpMainActivity.class);
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
                Intent intent = new Intent(NotificationListActivity.this, NotificationDetailsActivity.class);
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
            String to_display = intent.getStringExtra("notification_list");
            String[] split = to_display.split("\n");
            String packagename = "", time = "", tickertext = "", title = "", text = "";
            Log.i("123", String.valueOf(split.length));
            if(split.length==6){
                time = time + split[1];
                packagename = packagename + split[2];
                tickertext = tickertext + split[3];
                title = title + split[4];
                text = text + split[5];
                NotificationDbHandler dbHandler = new NotificationDbHandler(NotificationListActivity.this);
                dbHandler.insertUserDetails(packagename, tickertext, time, title, text);
//                Toast.makeText(getApplicationContext(), "Details Inserted Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NotificationListActivity.this, MainActivity.class);
        startActivity(intent);
    }

}