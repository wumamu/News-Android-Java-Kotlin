package com.recoveryrecord.surveyandroid.example;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.recoveryrecord.surveyandroid.example.activity.NotificationDbViewActivity;
import com.recoveryrecord.surveyandroid.example.model.Pagers;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

//import com.google.firebase.FirebaseApp;

public class NewsMainActivity extends AppCompatActivity {
//    private RecyclerView courseRV;
//    private ArrayList<NewsModel> dataModalArrayList;
//    private NewsRecycleViewAdapter dataRVAdapter;
//    private FirebaseFirestore db;
//temp for notification
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FirebaseApp.initializeApp();
        setContentView(R.layout.activity_main_news);
        setTitle("即時新聞");

        ArrayList<View> mPages = new ArrayList<>();
//        for (int i=0;i<5;i++) {
//            mPages.add(new Pagers(this, (i + 1)));
//        }
        int i = 0;
        mPages.add(new Pagers(this, (i + 1), "中時", "chinatimes"));
        mPages.add(new Pagers(this, (i + 1), "中央社", "cna"));
        mPages.add(new Pagers(this, (i + 1), "華視", "cts"));//cts
        mPages.add(new Pagers(this, (i + 1), "東森", "ebc"));
        mPages.add(new Pagers(this, (i + 1), "自由時報", "ltn"));
        mPages.add(new Pagers(this, (i + 1), "風傳媒", "storm"));
        mPages.add(new Pagers(this, (i + 1), "聯合", "udn"));
        mPages.add(new Pagers(this, (i + 1), "ettoday", "ettoday"));

        ViewPager viewPager = findViewById(R.id.mViewPager);
        TabLayout tab = findViewById(R.id.tab);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(mPages);

        tab.setupWithViewPager(viewPager);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setCurrentItem(0);//指定跳到某頁，一定得設置在setAdapter後面
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.noti_menu, menu);
        //getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_5 :
                scheduleNotification(getNotification("news (5 second delay)" ), 5000 );
                scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 30000 );
                return true;
            case R.id.action_10 :
                scheduleNotification(getNotification("news (10 second delay)" ), 10000 );
                scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 30000 );
                return true;
            case R.id.action_30 :
                Intent intent = new Intent(NewsMainActivity.this, NotificationDbViewActivity.class);
                startActivity(intent);
//                scheduleNotification(getNotification("news (30 second delay)" ), 30000 );
//                scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 30000 );
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification (Notification notification, int delay) {
//        int nid = (int) System.currentTimeMillis();
//        Log.d("log: notification", "news id" + nid);
        Intent notificationIntent = new Intent(this, MyNotificationPublisherNews.class);
        notificationIntent.putExtra(MyNotificationPublisherNews.NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisherNews.NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
    private Notification getNotification (String content) {
        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "news id" + nid);
        Intent intent_news = new Intent();
        intent_news.setClass(NewsMainActivity.this, NewsModuleActivity.class);
        intent_news.putExtra("trigger_from", "Notification");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_news, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle("Scheduled NEWS");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build() ;
    }
    //    @SuppressLint("ShortAlarm")
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void scheduleNotification_repeat (Notification notification, Calendar cc) {
//        int nid = (int) System.currentTimeMillis();
//        Log.d("log: notification", "news id" + nid);
//        Intent notificationIntent = new Intent(this, MyNotificationPublisherNews.class);
//        notificationIntent.putExtra(MyNotificationPublisherESM.NOTIFICATION_ID, 1 ) ;
//        notificationIntent.putExtra(MyNotificationPublisherESM.NOTIFICATION, notification) ;
//        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        assert alarmManager != null;
////        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cc.getTimeInMillis(), 1000 * 60, pendingIntent);
////        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 * 20, 1000 * 20, pendingIntent);
//    }
    private Notification getNotification_esm (String content) {
        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "esm id" + nid);
        Intent intent_esm = new Intent();
        intent_esm.setClass(NewsMainActivity.this, ExampleSurveyActivity.class);
        intent_esm.putExtra("trigger_from", "Notification");
        intent_esm.putExtra("esm_id", System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_esm, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle("Scheduled ESM");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build() ;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification_esm (Notification notification, int delay) {
//        int nid = (int) System.currentTimeMillis();
//        Log.d("log: notification", "news id" + nid);
        Intent notificationIntent = new Intent(this, MyNotificationPublisherNews.class);
        notificationIntent.putExtra(MyNotificationPublisherESM.NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisherESM.NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
