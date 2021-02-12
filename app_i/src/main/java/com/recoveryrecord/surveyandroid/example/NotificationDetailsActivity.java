package com.recoveryrecord.surveyandroid.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationDetailsActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_details);
        NotificationDbHandler db = new NotificationDbHandler(this);
        ArrayList<HashMap<String, String>> notificationList = db.GetNotifications();
        ListView lv = (ListView) findViewById(R.id.notification_list);
        ListAdapter adapter = new SimpleAdapter(NotificationDetailsActivity.this, notificationList, R.layout.list_row,new String[]{"packagename","time","tickertext", "notititle", "notitext"}, new int[]{R.id.packagename, R.id.time, R.id.tickertext, R.id.notititle, R.id.notitext});
        lv.setAdapter(adapter);
//        Button back = (Button)findViewById(R.id.btnBack);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent = new Intent(NotificationDetailsActivity.this, TmpMainActivity.class);
//                startActivity(intent);
//            }
//        });
    }
    @Override
    public void onBackPressed() {
        intent = new Intent(NotificationDetailsActivity.this, NotificationListActivity.class);
        startActivity(intent);
    }

}