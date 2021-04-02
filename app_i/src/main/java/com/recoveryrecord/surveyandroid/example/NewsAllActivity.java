 package com.recoveryrecord.surveyandroid.example;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.recoveryrecord.surveyandroid.example.activity.NotificationDbViewActivity;
import com.recoveryrecord.surveyandroid.example.model.Pagers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

//import com.google.firebase.FirebaseApp;

public class NewsAllActivity extends AppCompatActivity {
//    private RecyclerView courseRV;
//    private ArrayList<NewsModel> dataModalArrayList;
//    private NewsRecycleViewAdapter dataRVAdapter;
//    private FirebaseFirestore db;
//temp for notification
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.document("server_push_notifications/start");
    private CollectionReference noteRefqq = db.collection("server_push_notifications");
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FirebaseApp.initializeApp();
        setContentView(R.layout.activity_all_news);
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
    protected void onStart() {
        super.onStart();
        noteRefqq.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("onstart", "listen:error", e);
                    return;
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot documentSnapshot = dc.getDocument();
//                    String id = documentSnapshot.getId();
//                    int oldIndex = dc.getOldIndex();
//                    int newIndex = dc.getNewIndex();
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d("onstart", "New city: " + dc.getDocument().getData());
                            String news_id = dc.getDocument().getString("news_id");
                            String media  = dc.getDocument().getString("media");
                            String title = dc.getDocument().getString("title");
                            scheduleNotification(getNotification(news_id, media, title), 1000 );
                            scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 30000 );
                            break;
                        case MODIFIED:
                            Log.d("onstart", "Modified city: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            Log.d("onstart", "Removed city: " + dc.getDocument().getData());
                            break;
                    }
                }
            }
        });
//        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                if (e != null) {
//                    Toast.makeText(NewsMainActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
////                    Log.d(TAG, e.toString());
//                    return;
//                }
//                if (documentSnapshot.exists()) {
//                    scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 30000 );
//                    String news_id = documentSnapshot.getString("news_id");
//                    scheduleNotification(getNotification(news_id, "wahaha" ), 5000 );
//                    Toast.makeText(NewsMainActivity.this, news_id, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_news, menu);
        //getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_5 :
                scheduleNotification(getNotification("0143b739b1c33d46cd18b6af12b2d5b2", "ettoday", "wa_title" ), 5000 );
                scheduleNotification_repeat(getNotification_esm("time"));
                return true;
            case R.id.action_10 :
                Intent intent_ems = new Intent(NewsAllActivity.this, ExampleSurveyActivity.class);
                startActivity(intent_ems);
                return true;
            case R.id.action_30 :
                Intent intent_noti_db = new Intent(NewsAllActivity.this, NotificationDbViewActivity.class);
                startActivity(intent_noti_db);
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
    private Notification getNotification (String news_id, String media, String title) {
        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "news id" + nid);
        Intent intent_news = new Intent();
        intent_news.setClass(NewsAllActivity.this, NewsModuleActivity.class);
        intent_news.putExtra("trigger_from", "Notification");
        intent_news.putExtra("news_id", news_id);
        intent_news.putExtra("media", media);
//        intent_news.putExtra("media", "Notification");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_news, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle(title);
        builder.setContentText(media);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build() ;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification_repeat (Notification notification) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 0);
        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "news id" + nid);
        Intent notificationIntent = new Intent(this, MyNotificationPublisherNews.class);
        notificationIntent.putExtra(MyNotificationPublisherESM.NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisherESM.NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 3600, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 10, pendingIntent);//60 min
//        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 * 20, 1000 * 20, pendingIntent);
    }
    private Notification getNotification_esm (String content) {
        //replace content with time
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String time_noti = formatter.format(date);

        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "esm id" + nid);
        Intent intent_esm = new Intent();
        intent_esm.setClass(NewsAllActivity.this, ExampleSurveyActivity.class);
        intent_esm.putExtra("trigger_from", "Notification");
        intent_esm.putExtra("esm_id", System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_esm, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle("Please fill out the questionnaire");
        builder.setContentText(time_noti);
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
