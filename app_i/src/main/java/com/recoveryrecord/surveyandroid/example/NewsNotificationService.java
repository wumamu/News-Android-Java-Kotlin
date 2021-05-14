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

public class NewsNotificationService extends Service {
    //temp for notification
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
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

    private void listen_activity_cycle() {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        long LastAppStopOrDestroyTime = sharedPrefs.getLong("LastAppStopOrDestroyTime", 0L);
        db.collection("test_users")
                .document(device_id)
                .collection("notification_service")
                .orderBy("service_timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
//                            Log.d("lognewsselect", "listen:error", e);
                            return;
                        }
                        Timestamp right_now = Timestamp.now();
                        int count = 0;
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot documentSnapshot = dc.getDocument();
                            count++;
                            switch (dc.getType()) {
                                case ADDED:
                                    if(count<=1){
                                        Log.d("lognewsselect", dc.getDocument().getString("cycle"));
//                                        if (mTimerRunning) {
//                                            resetTimer();
//                                            startTimer();
//                                        } else {
//                                            startTimer();
//                                        }
                                        if((dc.getDocument().getString("cycle").equals("stop")) || (dc.getDocument().getString("cycle").equals("destroy"))){
                                            SharedPreferences.Editor editor = sharedPrefs.edit();
                                            editor.putLong("LastAppStopOrDestroyTime", dc.getDocument().getTimestamp("service_timestamp").getSeconds());
                                            editor.apply();
                                            if (mTimerRunning) {
                                                resetTimer();
//                                                startTimer();
                                            } else {
                                                startTimer();
                                            }
                                        }
//                                        else {
//                                            if (mTimerRunning) {
//                                                pauseTimer();
//                                            } else {
////                                                startTimer();
//                                            }
//                                        }
                                    }

                                    //before delete
                                    db.collection("test_users").document(device_id).collection("notification_service").document(dc.getDocument().getId())
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
                                    break;
                                case REMOVED:
                                    Log.d("lognewsselect", "Removed doc: " + dc.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });
    }

    private void listen_doc() {
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
    }

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
                                    record_noti.put("title", dc.getDocument().getString("news_title"));
                                    record_noti.put("id", dc.getDocument().getString("id"));
                                    record_noti.put("pubdate", dc.getDocument().getTimestamp("pubdate"));
                                    record_noti.put("noti_timestamp", Timestamp.now());
                                    record_noti.put("selections", Arrays.toString(new Set[]{selections}));
                                    record_noti.put("click", false);

                                    db.collection("test_users")
                                            .document(device_id)
                                            .collection("push_news")
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
    }

    private void listen_reading_behavior_result() {
        db.collection("test_users")
                .document(device_id)
                .collection("reading_behaviors_result")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
//                            Log.d("lognewsselect", "listen:error", e);
                            return;
                        }
                        Timestamp right_now = Timestamp.now();
                        int count = 0;
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot documentSnapshot = dc.getDocument();
                            count++;
                            switch (dc.getType()) {
                                case ADDED:
//                                    check_time_range(dc.getDocument().getTimestamp("timestamp"));
//                                    Timestamp my_timestamp = dc.getDocument().getTimestamp("timestamp");
                                    if(count<=1){
                                        if(check_daily_time_range()){
                                            try {
                                                scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 1000 );
                                            } catch (JSONException jsonException) {
                                                jsonException.printStackTrace();
                                            }
                                        } else {
                                            Log.d("lognewsselect", "check_daily_time_range failed");

                                        }

                                    } else {
//                                        record_noti.put("type", "too much");
                                    }
                                    //before delete
                                    db.collection("test_users").document(device_id).collection("reading_behaviors_result").document(dc.getDocument().getId())
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
    private Notification getNotification (String news_id, String media, String title) {
        int nid = (int) System.currentTimeMillis();
        Log.d("log: notification", "news id" + nid);
//        Intent mainIntent = new Intent(context, MainActivity.class);

        Intent intent_news = new Intent();
        intent_news.setClass(this, NewsModuleActivity.class);
        intent_news.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    private Notification getNotification_esm(String content) throws JSONException {
        //replace content with time
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String LastSelectedNewsTitle = sharedPrefs.getString("LastSelectedNewsTitle", "");
//        String LastSelectedNewsTitle = select_news();
        Log.d("lognewsselect", "getNotification_esm()" + LastSelectedNewsTitle);
        String json_file_name = "3.json";
//        if (!LastSelectedNewsTitle.equals("zero_result")){
//            json_file_name = createEsmJson(LastSelectedNewsTitle);
//        }
        Date date = new Date(System.currentTimeMillis());
        String esm_id = "";
//        esm_id = String.valueOf(date);
//        long esm_id = Timestamp.now().getSeconds();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String time_now = formatter.format(date);
        esm_id = time_now;
        int nid = (int) System.currentTimeMillis();
        Log.d("logesm", "esm id " + nid + " " + Timestamp.now());
        Intent intent_esm = new Intent();
        intent_esm.setClass(this, ESMActivity.class);
        intent_esm.putExtra("trigger_from", "Notification");
        intent_esm.putExtra("status", "foreground");
        intent_esm.putExtra("esm_id", esm_id);
//        intent_esm.putExtra("json_file_name", "t123.json");
        intent_esm.putExtra("json_file_name", json_file_name);
        intent_esm.putExtra("noti_timestamp", Timestamp.now());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_esm, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle("ESM");
        builder.setContentText("是時候填寫問卷咯~");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Map<String, Object> esm = new HashMap<>();
        esm.put("noti_time", time_now);
        esm.put("target_news", LastSelectedNewsTitle);
        esm.put("noti_timestamp", Timestamp.now());
        db.collection("test_users")
                .document(device_id)
                .collection("esms")
                .document(esm_id)
                .set(esm);
        return builder.build() ;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification_esm (Notification notification, int delay) {
        Intent notificationIntent = new Intent(this,NotificationListenerNews.class);
        notificationIntent.putExtra(NotificationListenerESM.NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(NotificationListenerESM.NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private void select_news() {
        Log.d("lognewsselect", "select_news()");
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String LastSelectedNewsTime = sharedPrefs.getLong("LastSelectedNewsTime", "");
        final String[] SelectedNewsTitle = {""};
        db.collection("test_users")
                .document(device_id)
                .collection("reading_behaviors")
                .whereEqualTo("select", false)
//                .whereEqualTo("category", "體育")
//                .whereArrayContains("category", "社會")
                .orderBy("out_timestamp", Query.Direction.DESCENDING)
//                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> news_title_array = new ArrayList<>();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                if(!(d.getString("title") ==null)){
                                    if(!d.getString("title").equals("NA")){
                                        news_title_array.add(d.getString("title"));
                                    }
                                }

                                Log.d("lognewsselect", "title " + d.getString("title"));
                                //mark as check
                                db.collection("test_users")
                                        .document(device_id)
                                        .collection("reading_behaviors")
                                        .document(d.getId())
                                        .update("select", true)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("lognewsselect", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("lognewsselect", "Error updating document", e);
                                            }
                                        });
                            }
                        } else {
                        }
                        if(news_title_array.size()!=0){
                            Random r=new Random();
                            int randomNumber=r.nextInt(news_title_array.size());
                            SelectedNewsTitle[0] = news_title_array.get(randomNumber);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putString("LastSelectedNewsTitle", news_title_array.get(randomNumber));
                            editor.apply();
//                            return news_title_array.get(randomNumber);
                            Log.d("lognewsselect", "select news title " + news_title_array.get(randomNumber));
                            Log.d("lognewsselect", "randomNumber " + randomNumber);
                        } else {
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putString("LastSelectedNewsTitle", "zero_result");
                            editor.apply();
                            Log.d("lognewsselect", "news_title_array.size()!=0 ");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("lognewsselect", String.valueOf(e));
                // if we do not get any data or any error we are displaying
                // a toast message that we do not get any data
//                Toast.makeText(TestNewsOneActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("lognewsselect", "select news finish " + SelectedNewsTitle[0]);
        return;
//        return SelectedNewsTitle[0];
    }

    private boolean check_daily_time_range(){
        Long now = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now);
        int MinHour = 9;//09:00:00
        int MaxHour = 22;//23:00:00
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        long LastEsmTime = sharedPrefs.getLong("LastEsmTime", 0L);
//        Log.d("lognewsselect", String.valueOf(c.get(Calendar.HOUR_OF_DAY)));
        if(c.get(Calendar.HOUR_OF_DAY) >= MinHour && c.get(Calendar.HOUR_OF_DAY) < MaxHour) {
            Log.d("lognewsselect", "in interval");
//            return true;
//            if(now - LastEsmTime > 60* 60 * 1000){
            if(now - LastEsmTime > INTERVAL){
                Log.d("lognewsselect", "in hour interval");
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putLong("LastEsmTime", now);
                editor.apply();
                return true;
            } else {
                Log.d("lognewsselect", "not in hour interval");
                return false;
            }
        } else {
            Log.d("lognewsselect", "not in interval");
            return false;
        }
    }

    private void startTimer() {
        Log.d("lognewsselect", "startTimer");
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
//                updateCountDownText();
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFinish() {
                Log.d("lognewsselect", "onFinish");
                mTimerRunning = false;
                if(check_daily_time_range()){
//                    select_news();
                    select_news();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // Actions to do after 10 seconds
                            try {
                                scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 1000 );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 3*1000);

                } else {
                    Log.d("lognewsselect", "check_daily_time_range failed");

                }
//                mButtonStartPause.setText("Start");
//                mButtonStartPause.setVisibility(View.INVISIBLE);
//                mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();
        mTimerRunning = true;
//        mButtonStartPause.setText("pause");
//        mButtonReset.setVisibility(View.INVISIBLE);
    }
    private void pauseTimer() {
        Log.d("lognewsselect", "pauseTimer");
        mCountDownTimer.cancel();
        mTimerRunning = false;
//        mButtonStartPause.setText("Start");
//        mButtonReset.setVisibility(View.VISIBLE);
    }
    private void resetTimer() {
        Log.d("lognewsselect", "resetTimer");
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
//        updateCountDownText();
//        mButtonReset.setVisibility(View.INVISIBLE);
//        mButtonStartPause.setVisibility(View.VISIBLE);
    }

//    private String createEsmJson(String news_title) throws JSONException {
//        String output_file_name = "";
//        String strJsons;
//        JSONObject jsonRootObject = new JSONObject(strJsons);
//        JSONArray jsonQuestionObject = jsonRootObject.optJSONArray("questions");
//        for (int i =0 ; i < jsonQuestionObject.length();i++){
//            JSONObject one = jsonQuestionObject.getJSONObject(i);
//            if(one.optString("id").equals("read_2")){
//                Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + one.optString("question"));
//                one.putOpt ("question", "請回答關於新聞「" + news_title + "」的問題");
//                Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + one.optString("question"));
//                break;
//            }
//        }
//        File file;
//        file = new File(getFilesDir(),"test.json");
//        try{
//            FileOutputStream fos = new FileOutputStream(file);//创建一个文件输出流
//            fos.write(jsonRootObject.toString().getBytes());//将生成的JSON数据写出
//            fos.close();//关闭输出流
//            Toast.makeText(getApplicationContext(),"创建成功！",Toast.LENGTH_SHORT).show();
//            Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + file.getAbsolutePath());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + getApplicationContext().getFilesDir());
//
//        return file.getAbsolutePath();
//    }
}