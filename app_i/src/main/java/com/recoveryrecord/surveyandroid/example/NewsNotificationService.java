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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

public class NewsNotificationService extends Service {
    //temp for notification
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    // declaring object of MediaPlayer
    private MediaPlayer player;
    public int counter=0;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final DocumentReference noteRef = db.document("server_push_notifications/start");
//    private CollectionReference noteRefqq = db.collection("test_users/" + device_id + "/compare_result");

//    private FirebaseFirestore firestore;
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("lognewsselect", "onCreate");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
//        FirebaseApp.initializeApp(this);
//        this.firestore = FirebaseFirestore.getInstance();
////        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
////                .setTimestampsInSnapshotsEnabled(true)
////                .build();
////        firestore.setFirestoreSettings(settings);
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
        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        // creating a media player which
//        // will play the audio of Default
//        // ringtone in android device
//        player = MediaPlayer.create( this, Settings.System.DEFAULT_RINGTONE_URI );
//
//        // providing the boolean
//        // value as true to play
//        // the audio on loop
//        player.setLooping( true );
//
//        // starting the process
//        player.start();
//
//        // returns the status
//        // of the program
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        final Set<String> selections = sharedPrefs.getStringSet("media_select", new HashSet<String>());
//        Log.d("lognewsselect_", "ss " + Arrays.toString(new Set[]{selections}));
//        if (selections==null || selections.isEmpty()){
//            selections.add("中時");
//            selections.add("中央社");
//            selections.add("華視");
//            selections.add("東森");
//            selections.add("自由時報");
//            selections.add("風傳媒");
//            selections.add("聯合");
//            selections.add("ettoday");
//        }
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
                    count++;
                    switch (dc.getType()) {
                        case ADDED:
                            //add record
                            Map<String, Object> record_noti = new HashMap<>();
                            String news_id = "", media = "", title = "", doc_id = "";
//                            if(count<20 && selections.contains(dc.getDocument().getString("media"))){
                            if(count<20){
                                if(selections.contains(dc.getDocument().getString("media"))){
                                    Log.d("lognewsselect", "New doc: " + dc.getDocument().getData());
                                    news_id = dc.getDocument().getString("id");
                                    media  = dc.getDocument().getString("media");
                                    title = dc.getDocument().getString("news_title");
                                    doc_id = dc.getDocument().getId();
                                    scheduleNotification(getNotification(news_id, media, title), 1000 );
                                    Log.d("lognewsselect", "doc id" + doc_id);
                                    record_noti.put("type", "add success");
                                } else {
                                    record_noti.put("type", "not select");
                                }
                            } else {
                                record_noti.put("type", "too much");
                            }
                            record_noti.put("media", dc.getDocument().getString("media"));
                            record_noti.put("news_title", dc.getDocument().getString("news_title"));
                            record_noti.put("doc_id", dc.getDocument().getString("id"));
                            record_noti.put("timestamp", Timestamp.now());
                            record_noti.put("selections", Arrays.toString(new Set[]{selections}));

                            db.collection("test_users")
                                    .document(device_id)
                                    .collection("push_noti_backup")
                                    .document(String.valueOf(Timestamp.now()))
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
//        noteRef.addSnapshotListener((Executor) this, new EventListener<DocumentSnapshot>() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                if (e != null) {
//                    Toast.makeText(NewService.this, "Error while loading!", Toast.LENGTH_SHORT).show();
////                    Log.d(TAG, e.toString());
//                    return;
//                }
//                if (documentSnapshot.exists()) {
//                    Toast.makeText(NewService.this, "Success", Toast.LENGTH_SHORT).show();
////                    scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 30000 );
////                    String news_id = documentSnapshot.getString("news_id");
//////                    scheduleNotification(getNotification(news_id, "wahaha" ), 5000 );
////                    Toast.makeText(NewService.this, news_id, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        return START_STICKY;
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
                .collection("notification_service")
                .document(String.valueOf(Timestamp.now()))
                .set(log_service);
        // stopping the process
//        player.stop();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, NewsNotificationRestarter.class);
        this.sendBroadcast(broadcastIntent);
    }
//    private Timer timer;
//    private TimerTask timerTask;
//    public void startTimer() {
//        timer = new Timer();
//        timerTask = new TimerTask() {
//            public void run() {
//                Log.i("Count", "=========  "+ (counter++));
//            }
//        };
//        timer.schedule(timerTask, 1000, 1000); //
//    }
//
//    public void stoptimertask() {
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification (Notification notification, int delay) {
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
    private Notification getNotification (String news_id, String media, String title) {
        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "news id" + nid);
        Intent intent_news = new Intent();
        intent_news.setClass(NewsNotificationService.this, NewsModuleActivity.class);
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

//    private Notification getNotification_esm (String content) {
//        //replace content with time
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        String time_noti = formatter.format(date);
//
//        int nid = (int) System.currentTimeMillis();
//        Log.d("log: notification", "esm id" + nid);
//        Intent intent_esm = new Intent();
//        intent_esm.setClass(NewsNotificationService.this, ESMActivity.class);
//        intent_esm.putExtra("trigger_from", "Notification");
//        intent_esm.putExtra("esm_id", System.currentTimeMillis());
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_esm, 0);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
//        builder.setContentTitle("ESM");
//        builder.setContentText("是時候填寫問卷咯~");
//        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
//        builder.setContentIntent(pendingIntent);
//        builder.setAutoCancel(true);
//        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
//        return builder.build() ;
//    }
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void scheduleNotification_esm (Notification notification, int delay) {
////        int nid = (int) System.currentTimeMillis();
////        Log.d("log: notification", "news id" + nid);
//        Intent notificationIntent = new Intent(this, NotificationListenerNews.class);
//        notificationIntent.putExtra(NotificationListenerESM.NOTIFICATION_ID, 1 ) ;
//        notificationIntent.putExtra(NotificationListenerESM.NOTIFICATION, notification) ;
//        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        assert alarmManager != null;
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//    }
}
