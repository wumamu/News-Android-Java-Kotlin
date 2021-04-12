package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NewsService extends Service {
    //temp for notification
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    // declaring object of MediaPlayer
    private MediaPlayer player;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final DocumentReference noteRef = db.document("server_push_notifications/start");
//    private CollectionReference noteRefqq = db.collection("test_users/" + device_id + "/compare_result");

//    private FirebaseFirestore firestore;
//    @Override
//    public void onCreate(){
//        FirebaseApp.initializeApp(this);
//        this.firestore = FirebaseFirestore.getInstance();
////        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
////                .setTimestampsInSnapshotsEnabled(true)
////                .build();
////        firestore.setFirestoreSettings(settings);
//    }
    @Override

    // execution of service will start
    // on calling this method
    public int onStartCommand(Intent intent, int flags, int startId) {
        @SuppressLint("HardwareIds")
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
        db.collection("test_users")
                .document(device_id)
                .collection("compare_result")
                .orderBy("pubdate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("onstart", "listen:error", e);
                    return;
                }
                Timestamp right_now = Timestamp.now();
                int count = 0;
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot documentSnapshot = dc.getDocument();
                    count++;
                    switch (dc.getType()) {
                        case ADDED:
                            if(count<20){
                                Log.d("onstart", "New doc: " + dc.getDocument().getData());
                                String news_id = dc.getDocument().getString("id");
                                String media  = dc.getDocument().getString("media");
                                String title = dc.getDocument().getString("news_title");
                                String doc_id = dc.getDocument().getId();
                                scheduleNotification(getNotification(news_id, media, title), 1000 );
                                Log.d("onstart", "doc id" + doc_id);
                            }
                            db.collection("test_users").document(device_id).collection("compare_result").document(dc.getDocument().getId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("onstart", "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("onstart", "Error deleting document", e);
                                        }
                                    });
//                            scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 30000 );
                            break;
                        case MODIFIED:
                            Log.d("onstart", "Modified doc: " + dc.getDocument().getData());
                            String news_id_m = dc.getDocument().getString("id");
                            String media_m  = dc.getDocument().getString("media");
                            String title_m = dc.getDocument().getString("news_title");
                            title_m = "新! " + title_m;
                            scheduleNotification(getNotification(news_id_m, media_m, title_m), 1000 );
                            break;
                        case REMOVED:
                            Log.d("onstart", "Removed doc: " + dc.getDocument().getData());
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

    @Override

    // execution of the service will
    // stop on calling this method
    public void onDestroy() {
        super.onDestroy();

        // stopping the process
        player.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        intent_news.setClass(NewsService.this, NewsModuleActivity.class);
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

    private Notification getNotification_esm (String content) {
        //replace content with time
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String time_noti = formatter.format(date);

        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "esm id" + nid);
        Intent intent_esm = new Intent();
        intent_esm.setClass(NewsService.this, ExampleSurveyActivity.class);
        intent_esm.putExtra("trigger_from", "Notification");
        intent_esm.putExtra("esm_id", System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_esm, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle("ESM");
        builder.setContentText("是時候填寫問卷咯~");
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
