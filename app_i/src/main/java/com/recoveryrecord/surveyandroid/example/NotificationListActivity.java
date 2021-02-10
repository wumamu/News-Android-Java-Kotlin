package com.recoveryrecord.surveyandroid.example;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import static com.recoveryrecord.surveyandroid.example.MainActivity.NOTIFICATION_CHANNEL_ID;

public class NotificationListActivity extends Activity {

    private TextView txtView;
    private NotificationReceiver nReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
//        Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//        startActivity(intent);
        txtView = (TextView) findViewById(R.id.textView);
        Button btnCreateNotify = (Button) findViewById(R.id.btnCreateNotify);
        Button btnClearNotify = (Button) findViewById(R.id.btnClearNotify);
        Button btnListNotify = (Button) findViewById(R.id.btnListNotify);
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver,filter);
        btnCreateNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Log.e("log: NLService", "21231231");
                Intent i = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
                i.putExtra("command","clearall");
                sendBroadcast(i);
            }
        });
        btnListNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("log: NLService", "4654645");
                Intent i = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
                i.putExtra("command","list");
                sendBroadcast(i);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }



//    public void buttonClicked(View v){
//
//        if(v.getId() == R.id.btnCreateNotify){
////            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//////            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
//////            ncomp.setContentTitle("My Notification");
//////            ncomp.setContentText("Notification Listener Service Example");
//////            ncomp.setTicker("Notification Listener Service Example");
//////            ncomp.setSmallIcon(R.drawable.ic_launcher);
//////            ncomp.setAutoCancel(true);
//////            nManager.notify((int)System.currentTimeMillis(),ncomp.build());
////            String NOTIFICATION_ID = "notification-id" ;
////            String NOTIFICATION = "notification" ;
//////            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
//////            Notification notification = intent.getParcelableExtra( NOTIFICATION ) ;
////            if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
////                int importance = NotificationManager. IMPORTANCE_HIGH ;
////                NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
////                assert nManager != null;
////                nManager.createNotificationChannel(notificationChannel) ;
////            }
//////        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
//////        int id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
////            assert nManager != null;
////            nManager.notify((int) System.currentTimeMillis(), ) ;
//        }
//        else if(v.getId() == R.id.btnClearNotify){
//            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            nManager.cancelAll();
//            Log.e("log: bluetooth device", "123131321");
//            Intent i = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
//            i.putExtra("command","clearall");
//            sendBroadcast(i);
//        }
//        else if(v.getId() == R.id.btnListNotify){
//            Intent i = new Intent("com.recoveryrecord.surveyandroid.example.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
//            i.putExtra("command","list");
//            sendBroadcast(i);
//        }
//
//    }

    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event") + "n" + txtView.getText();
            txtView.setText(temp);
        }
    }

}