package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_NEWS_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.VIBRATE_EFFECT;

public class NewsNotificationService extends Service {
    //temp for notification
//    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
//    private final static String default_notification_channel_id = "default" ;
    // declaring object of MediaPlayer
    private MediaPlayer player;
    public int counter=0;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final DocumentReference noteRef = db.document("server_push_notifications/start");
    String device_id = "";

    //timer count down
//    private static final long START_TIME_IN_MILLIS = 1000;//30 * 60 * 1000;//20 min
//    private static final long INTERVAL = 60 * 1000;//30 * 60 * 1000;//one hour
    private static final long START_TIME_IN_MILLIS = 3 * 60 * 1000;//3 * 60 * 1000;//3 min
    private static final long INTERVAL =  60 * 60 * 1000;//60 * 60 * 1000;//one hour
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate(){
        super.onCreate();
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("lognewsselect", "onCreate");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        Log.d("lognewsselect", "startMyOwnForeground");
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//        Bundle extras = new Bundle();
//        extras.putString("id", news_id);
//        extras.putString("type", "news");
//        builder.setExtras(extras);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
    @Override
    // execution of service will start
    // on calling this method
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("lognewsselect", "onStartCommand");
//        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        listen_compare_result();//news
//        listen_activity_cycle();//esm
//        listen_reading_behavior_result();
//        listen_doc();
        return START_STICKY;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void listen_compare_result() {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        db.collection("test_users")
                .document(device_id)
                .collection("compare_result")
                .orderBy("pubdate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("lognewsselect", "listen:error", e);
                            return;
                        }
                        Timestamp right_now = Timestamp.now();
                        Set<String> selections = sharedPrefs.getStringSet("media_select", new HashSet<String>());
                        Log.d("lognewsselect_", "ss " + Arrays.toString(new Set[]{selections}));
                        int count = 0;
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot documentSnapshot = dc.getDocument();
                            switch (dc.getType()) {
                                case ADDED:
                                    //add record
                                    Map<String, Object> record_noti = new HashMap<>();
                                    String news_id = "", media = "", title = "", doc_id = "";
//                            if(count<20 && selections.contains(dc.getDocument().getString("media"))){
                                    if(selections.contains(dc.getDocument().getString("media"))){
                                        if(count<20){
                                            Log.d("lognewsselect", "New doc: " + dc.getDocument().getData());
                                            news_id = dc.getDocument().getString("id");
                                            media  = dc.getDocument().getString("media");
                                            title = dc.getDocument().getString("news_title");
                                            doc_id = dc.getDocument().getId();
                                            scheduleNotification(getNotification(news_id, media, title), 1000 );
                                            Log.d("lognewsselect", "doc id" + doc_id);
                                            record_noti.put("type", "target add");
                                            record_noti.put("click", 0);
                                            count++;
                                        } else {
                                            record_noti.put("type", "target too much");
                                            record_noti.put("click", 2);
                                        }
                                    } else {
                                        record_noti.put("type", "not target");
                                        record_noti.put("click", 3);
                                    }
                                    record_noti.put("media", dc.getDocument().getString("media"));
                                    record_noti.put("title", dc.getDocument().getString("news_title"));
                                    record_noti.put("id", dc.getDocument().getString("id"));
                                    record_noti.put("pubdate", dc.getDocument().getTimestamp("pubdate"));
                                    record_noti.put("noti_timestamp", Timestamp.now());
                                    record_noti.put("selections", Arrays.toString(new Set[]{selections}));


                                    db.collection("test_users")
                                            .document(device_id)
                                            .collection("push_news")
                                            .document(news_id)
                                            .set(record_noti);
                                    //before delete
                                    db.collection("test_users").document(device_id).collection("compare_result").document(dc.getDocument().getId())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("lognewsselect", "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("lognewsselect", "Error deleting document", e);
                                                }
                                            });
                                    break;
                                case MODIFIED:
                                    if(count<20 && selections.contains(dc.getDocument().getString("media"))){
                                        Log.d("lognewsselect", "Modified doc: " + dc.getDocument().getData());
                                        String news_id_m = dc.getDocument().getString("id");
                                        String media_m  = dc.getDocument().getString("media");
                                        String title_m = dc.getDocument().getString("news_title");
                                        title_m = "新! " + title_m;
                                        scheduleNotification(getNotification(news_id_m, media_m, title_m), 1000 );
                                    }
                                    db.collection("test_users").document(device_id).collection("compare_result").document(dc.getDocument().getId())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("lognewsselect", "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("lognewsselect", "Error deleting document", e);
                                                }
                                            });
                                    break;
                                case REMOVED:
                                    Log.d("lognewsselect", "Removed doc: " + dc.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });
    }



    @SuppressLint("HardwareIds")
    @Override
    // execution of the service will
    // stop on calling this method
    public void onDestroy() {
        super.onDestroy();
        Log.d("lognewsselect", "onDestroy service");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> log_service = new HashMap<>();
        log_service.put("noti_timestamp", Timestamp.now());
        log_service.put("cycle", "destroy(service)");
        log_service.put("status", "failed");
        db.collection("test_users")
                .document(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID))
                .collection("notification_service_dead")
                .document(String.valueOf(Timestamp.now()))
                .set(log_service);
        // stopping the process
//        player.stop();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, NewsNotificationRestarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification (Notification notification, int delay) {
//        Intent notificationIntent = new Intent(this, NotificationListenerNews.class);
        Intent notificationIntent = new Intent(this, NotificationListenerNews.class);
        notificationIntent.putExtra(NotificationListenerNews.NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(NotificationListenerNews.NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private Notification getNotification(String news_id, String media, String title) {
        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "news id" + nid);
//        Intent mainIntent = new Intent(context, MainActivity.class);

        Intent intent_news = new Intent();
        intent_news.setClass(this, NewsModuleActivity.class);
        intent_news.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent_news.putExtra("trigger_by", "Notification");
        intent_news.putExtra("news_id", news_id);
        intent_news.putExtra("media", media);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_news, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, DEFAULT_NEWS_CHANNEL_ID);
        builder.setContentTitle(title);
        builder.setContentText(media);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setChannelId(NEWS_CHANNEL_ID);
        builder.setVibrate(VIBRATE_EFFECT);              //震動模式
        builder.setPriority(NotificationManager.IMPORTANCE_DEFAULT);
        builder.setCategory(Notification.CATEGORY_MESSAGE);
        Bundle extras = new Bundle();
        extras.putString("id", news_id);
        extras.putString("type", "news");
        builder.setExtras(extras);

        return builder.build() ;
    }

}