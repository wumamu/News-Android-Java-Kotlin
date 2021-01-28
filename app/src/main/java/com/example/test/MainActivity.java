package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import android.app.AlarmManager ;
import android.app.Notification ;
import android.app.NotificationManager ;
import android.app.PendingIntent ;
import android.content.Context ;
import android.content.Intent ;
import android.os.Bundle ;
import android.os.SystemClock ;
//import android.support.v4.app.NotificationCompat ;
//import android.support.v7.app.AppCompatActivity ;
import android.view.Menu ;
import android.view.MenuItem ;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private Button btn_show;
    private Context mContext;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Homepage");
        setContentView(R.layout.activity_main);
        Button btn_to_news = (Button) findViewById(R.id.btn_to_news);
        Button btn_to_diary = (Button) findViewById(R.id.btn_to_diary);

        mContext = MainActivity.this;
        btn_to_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ExampleSurveyActivity.class);
//                intent.setClass(MainActivity.this, SampleNewsActivity.class);
                startActivity(intent);
                //MainActivity.this.finish();
            }
        });
        btn_to_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, DiaryAcitivity.class);
                intent.setClass(MainActivity.this, ExampleSurveyActivity.class);
                startActivity(intent);
                //MainActivity.this.finish();
            }
        });
    }
//    @Override
//    public boolean onCreateOptionsMenu (Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        //getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected (MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_5 :
//                scheduleNotification(getNotification("5 second delay" ), 5000 );
//                return true;
//            case R.id.action_10 :
//                scheduleNotification(getNotification("10 second delay" ), 10000 );
//                return true;
//            case R.id.action_30 :
//                scheduleNotification(getNotification("30 second delay" ), 30000 );
//                return true;
//            default :
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
