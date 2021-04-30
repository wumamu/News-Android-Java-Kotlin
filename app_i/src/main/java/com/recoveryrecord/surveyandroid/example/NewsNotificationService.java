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
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.recoveryrecord.surveyandroid.example.model.NewsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import java.util.concurrent.TimeUnit;

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
    private static final long START_TIME_IN_MILLIS = 3 * 60 * 1000;//30 * 60 * 1000;//20 min
    private static final long INTERVAL =  60 * 60 * 1000;//30 * 60 * 1000;//one hour
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
        listen_activity_cycle();//esm
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

    private Notification getNotification_esm (String content) throws JSONException {
        //replace content with time
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String LastSelectedNewsTitle = sharedPrefs.getString("LastSelectedNewsTitle", "");
//        String LastSelectedNewsTitle = select_news();
        Log.d("lognewsselect", "getNotification_esm()" + LastSelectedNewsTitle);
        String json_file_name = "ESM.json";
        if (!LastSelectedNewsTitle.equals("zero_result")){
            json_file_name = createEsmJson(LastSelectedNewsTitle);
        }
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

    private String createEsmJson(String news_title) throws JSONException {
        String output_file_name = "";
        String strJsons;
        strJsons = "{\n" +
                "  \"questions\": [\n" +
                "    {\n" +
                "      \"id\": \"base_1\",\n" +
                "      \"header\": \"Question base_1\",\n" +
                "      \"question\": \"請問您最近30分鐘內有印象在Time_Me上閱讀新聞嗎？\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"有\",\n" +
                "        \"沒有\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"not_read_1\",\n" +
                "      \"header\": \"Question not_read_1\",\n" +
                "      \"question\": \"請問您在最近30分鐘內有看到Time_Me的新聞通知標題嗎？\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"有\",\n" +
                "        \"沒有\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"id\": \"base_1\",\n" +
                "        \"operation\": \"equals\",\n" +
                "        \"value\": \"沒有\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"not_read_title_1\",\n" +
                "      \"header\": \"Question not_read_title_1\",\n" +
                "      \"question\": \"請問您未在此時段看到新聞標題的原因，若無合適選項，請簡短描述。\",\n" +
                "      \"question_type\": \"multi_select\",\n" +
                "      \"options\": [\n" +
                "        \"沒看到新聞通知\",\n" +
                "        \"沒有在使用手機\",\n" +
                "        \"手機不在身邊\",\n" +
                "        {\n" +
                "          \"title\": \"其他\",\n" +
                "          \"type\": \"freeform\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"not_read_title_2\",\n" +
                "      \"header\": \"Question not_read_title_2\",\n" +
                "      \"question\": \"開始閱讀前，您是否正在從事以下活動？\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"處理家務\",\n" +
                "        \"移動中\",\n" +
                "        \"工作或學習\",\n" +
                "        \"用餐\",\n" +
                "        \"從事娛樂或休閒運動\",\n" +
                "        \"與朋友聊天\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_title_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"not_read_title_3\",\n" +
                "      \"header\": \"Question not_read_title_3\",\n" +
                "      \"question\": \"承上題，該項活動您主觀複雜程度？\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不複雜\",\n" +
                "      \"high_tag\": \"非常複雜\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_title_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_title_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"not_read_title_4\",\n" +
                "      \"header\": \"Question not_read_title_4\",\n" +
                "      \"question\": \"請選擇最符合您閱讀當下所處地點\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"工作或學習場所\",\n" +
                "        \"家裡\",\n" +
                "        \"移動中(如：在交通工具上)\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_title_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_title_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_title_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_title_1\",\n" +
                "      \"header\": \"Question read_title_1\",\n" +
                "      \"question\": \"請問您只有閱讀標題，而未點開閱讀的原因為何。\",\n" +
                "      \"question_type\": \"multi_select\",\n" +
                "      \"options\": [\n" +
                "        \"我認為標題已經給我足夠資訊\",\n" +
                "        \"我當下沒有足夠時間閱讀\",\n" +
                "        \"我當下沒有興趣閱讀\",\n" +
                "        \"我當下沒有足夠注意力(認知資源)可以閱讀\",\n" +
                "        \"這是則不重要的新聞\",\n" +
                "        {\n" +
                "          \"title\": \"其他\",\n" +
                "          \"type\": \"freeform\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_title_2\",\n" +
                "      \"header\": \"Question read_title_2\",\n" +
                "      \"question\": \"對您來說，這篇新聞是真實的。\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不符合\",\n" +
                "      \"high_tag\": \"非常符合\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_title_3\",\n" +
                "      \"header\": \"Question read_title_3\",\n" +
                "      \"question\": \"對您來說，這篇新聞是正確的。\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不符合\",\n" +
                "      \"high_tag\": \"非常符合\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_title_4\",\n" +
                "      \"header\": \"Question read_title_4\",\n" +
                "      \"question\": \"對您來說，這則新聞是可信任的。\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不符合\",\n" +
                "      \"high_tag\": \"非常符合\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_title_5\",\n" +
                "      \"header\": \"Question read_title_5\",\n" +
                "      \"question\": \"開始閱讀前，您是否正在從事以下活動？\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"處理家務\",\n" +
                "        \"移動中\",\n" +
                "        \"工作或學習\",\n" +
                "        \"用餐\",\n" +
                "        \"從事娛樂或休閒運動\",\n" +
                "        \"與朋友聊天\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_title_6\",\n" +
                "      \"header\": \"Question read_title_6\",\n" +
                "      \"question\": \"承上題，該項活動您主觀複雜程度？\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不複雜\",\n" +
                "      \"high_tag\": \"非常複雜\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_title_7\",\n" +
                "      \"header\": \"Question read_title_7\",\n" +
                "      \"question\": \"請選擇最符合您閱讀當下所處地點\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"工作或學習場所\",\n" +
                "        \"家裡\",\n" +
                "        \"移動中(如：在交通工具上)\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"not_read_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_title_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_1\",\n" +
                "      \"header\": \"Question read_1\",\n" +
                "      \"question\": \"請問您此次新聞閱讀，透過甚麼方式進到新聞文章頁面？\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"看到「Time_Me新聞通知」點入閱讀\",\n" +
                "        \"點開「Time_Me」進行新聞閱讀\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"id\": \"base_1\",\n" +
                "        \"operation\": \"equals\",\n" +
                "        \"value\": \"有\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_2\",\n" +
                "      \"header\": \"Question read_2\",\n" +
                "      \"question\": \"請回答關於「某某news title」的問題\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"了解\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_3\",\n" +
                "      \"header\": \"Question read_3\",\n" +
                "      \"question\": \"在閱讀這則新聞前，是否看過其他媒體或其他平台引用相同新聞稿，或闡述同一特定事件的新聞？\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"有\",\n" +
                "        \"沒有\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_4\",\n" +
                "      \"header\": \"Question read_4\",\n" +
                "      \"question\": \"關於這篇新聞，主觀來說，您認為您閱讀多少比例的新聞內容？\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"0%\",\n" +
                "      \"high_tag\": \"100%\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_5\",\n" +
                "      \"header\": \"Question read_5\",\n" +
                "      \"question\": \"關於此次新聞閱讀，你投入程度為何？\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不投入\",\n" +
                "      \"high_tag\": \"非常投入\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_6\",\n" +
                "      \"header\": \"Question read_6\",\n" +
                "      \"question\": \"關於這篇新聞內容，你理解程度為何？\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不理解\",\n" +
                "      \"high_tag\": \"非常理解\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_7\",\n" +
                "      \"header\": \"Question read_7\",\n" +
                "      \"question\": \"關於這篇新聞，在閱讀文章內容時感到困難程度。\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不困難\",\n" +
                "      \"high_tag\": \"非常困難\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_8\",\n" +
                "      \"header\": \"Question read_8\",\n" +
                "      \"question\": \"關於這篇新聞，與您自身相關程度。\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不相關\",\n" +
                "      \"high_tag\": \"非常相關\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_9\",\n" +
                "      \"header\": \"Question read_9\",\n" +
                "      \"question\": \"關於這篇新聞，您感興趣程度。\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不感興趣\",\n" +
                "      \"high_tag\": \"非常感興趣\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_10\",\n" +
                "      \"header\": \"Question read_10\",\n" +
                "      \"question\": \"對您來說，這篇新聞是真實的。\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不符合\",\n" +
                "      \"high_tag\": \"非常符合\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_11\",\n" +
                "      \"header\": \"Question read_11\",\n" +
                "      \"question\": \"對您來說，這篇新聞是正確的。\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不符合\",\n" +
                "      \"high_tag\": \"非常符合\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_12\",\n" +
                "      \"header\": \"Question read_12\",\n" +
                "      \"question\": \"對您來說，這則新聞是可信任的。\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不符合\",\n" +
                "      \"high_tag\": \"非常符合\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_11\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_13\",\n" +
                "      \"header\": \"Question read_13\",\n" +
                "      \"question\": \"關於這次的新聞閱讀的時段，對您來說屬於何種性質的閱讀？\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"預期中的閱讀活動(我平時就計畫/習慣在於此時刻閱讀)\",\n" +
                "        \"未預期、意外地閱讀活動(我未規劃/沒有習慣於此時刻閱讀)\",\n" +
                "        {\n" +
                "          \"title\": \"其他\",\n" +
                "          \"type\": \"freeform\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_11\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_12\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_14\",\n" +
                "      \"header\": \"Question read_14\",\n" +
                "      \"question\": \"關於這次新聞閱讀，您的閱讀目標最接近以下何種？\",\n" +
                "      \"question_type\": \"multi_select\",\n" +
                "      \"options\": [\n" +
                "        \"了解整個新聞事件細節\",\n" +
                "        \"鎖定文章特定段落\",\n" +
                "        \"想看新聞圖片\",\n" +
                "        \"大致掌握新聞事件概要\",\n" +
                "        {\n" +
                "          \"title\": \"其他\",\n" +
                "          \"type\": \"freeform\"\n" +
                "        },\n" +
                "        \"無\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_11\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_12\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_13\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_15\",\n" +
                "      \"header\": \"Question read_15\",\n" +
                "      \"question\": \"承上題，甚麼因素讓你採取此閱讀方式(策略)？\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"當下可花費時間多寡\",\n" +
                "        \"內容吸引我程度\",\n" +
                "        \"當下注意力(認知資源)多寡\",\n" +
                "        {\n" +
                "          \"title\": \"其他\",\n" +
                "          \"type\": \"freeform\"\n" +
                "        },\n" +
                "        \"無\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_11\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_12\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_13\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_14\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_16\",\n" +
                "      \"header\": \"Question read_16\",\n" +
                "      \"question\": \"開始閱讀前與其間，您是否正在從事以下活動？\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"處理家務\",\n" +
                "        \"移動中\",\n" +
                "        \"工作或學習\",\n" +
                "        \"用餐\",\n" +
                "        \"從事娛樂或休閒運動\",\n" +
                "        \"與朋友聊天\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_11\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_12\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_13\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_14\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_15\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_17\",\n" +
                "      \"header\": \"Question read_17\",\n" +
                "      \"question\": \"承上題，該項活動您主觀複雜程度？\",\n" +
                "      \"question_type\": \"segment_select\",\n" +
                "      \"low_tag\": \"非常不複雜\",\n" +
                "      \"high_tag\": \"非常複雜\",\n" +
                "      \"values\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\",\n" +
                "        \"4\",\n" +
                "        \"5\",\n" +
                "        \"6\",\n" +
                "        \"7\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_11\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_12\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_13\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_14\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_15\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_16\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_18\",\n" +
                "      \"header\": \"Question read_18\",\n" +
                "      \"question\": \"請選擇最符合您閱讀當下所處地點\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"工作或學習場所\",\n" +
                "        \"家裡\",\n" +
                "        \"移動中(如：在交通工具上)\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_11\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_12\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_13\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_14\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_15\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_16\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_17\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_19\",\n" +
                "      \"header\": \"Question read_20\",\n" +
                "      \"question\": \"對我來說，這是個好的新聞通知時機。\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"我覺得是好時機\",\n" +
                "        \"我覺得不是好時機\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_11\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_12\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_13\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_14\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_15\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_16\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_17\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_18\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"read_20\",\n" +
                "      \"header\": \"Question read_20\",\n" +
                "      \"question\": \"關於這篇新聞，您是否有分享給其他人？\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"有\",\n" +
                "        \"沒有\"\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_11\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_12\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_13\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_14\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_15\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_16\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_17\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_18\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_19\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"not_share_1\",\n" +
                "      \"header\": \"Question not_share_1\",\n" +
                "      \"question\": \"不分享此則新聞原因為何？\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"看起來不太可信\",\n" +
                "        \"無聊\",\n" +
                "        \"不重要\",\n" +
                "        \"跟我無關\",\n" +
                "        \"已過時\",\n" +
                "        {\n" +
                "          \"title\": \"其他\",\n" +
                "          \"type\": \"freeform\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_11\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_12\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_13\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_14\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_15\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_16\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_17\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_18\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_19\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_20\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"沒有\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"share_1\",\n" +
                "      \"header\": \"Question share_1\",\n" +
                "      \"question\": \"您分享方式為何？\",\n" +
                "      \"question_type\": \"multi_select\",\n" +
                "      \"options\": [\n" +
                "        \"社群軟體(如fb,ig)貼文\",\n" +
                "        \"通訊軟體(如line, whatsapp)貼文\",\n" +
                "        \"私人訊息給單一好友\",\n" +
                "        \"私人訊息給多人群組\",\n" +
                "        \"實體聊天時口述\",\n" +
                "        \"截圖\",\n" +
                "        {\n" +
                "          \"title\": \"其他\",\n" +
                "          \"type\": \"freeform\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_11\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_12\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_13\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_14\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_15\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_16\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_17\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_18\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_19\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_20\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"share_2\",\n" +
                "      \"header\": \"Question share_2\",\n" +
                "      \"question\": \"分享此則新聞原因為何？\",\n" +
                "      \"question_type\": \"single_select\",\n" +
                "      \"options\": [\n" +
                "        \"這則新聞很重要\",\n" +
                "        \"覺得有趣\",\n" +
                "        \"嘲諷此則新聞立場\",\n" +
                "        \"此則新聞含有錯誤資訊，需澄清\",\n" +
                "        \"倡議某議題\",\n" +
                "        {\n" +
                "          \"title\": \"其他\",\n" +
                "          \"type\": \"freeform\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"show_if\": {\n" +
                "        \"operation\": \"and\",\n" +
                "        \"subconditions\": [\n" +
                "          {\n" +
                "            \"id\": \"base_1\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_2\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_3\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_4\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_5\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_6\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_7\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_8\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_9\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_10\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_11\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_12\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_13\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_14\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_15\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_16\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_17\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_18\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_19\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"read_20\",\n" +
                "            \"operation\": \"equals\",\n" +
                "            \"value\": \"有\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": \"share_1\",\n" +
                "            \"operation\": \"not equals\",\n" +
                "            \"value\": \"***\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"submit\": {\n" +
                "    \"button_title\": \"Submit Answers\",\n" +
                "    \"url\": \"https://www.example.com\"\n" +
                "  },\n" +
                "  \"auto_focus_text\": true\n" +
                "}";
        JSONObject jsonRootObject = new JSONObject(strJsons);
        JSONArray jsonQuestionObject = jsonRootObject.optJSONArray("questions");
        for (int i =0 ; i < jsonQuestionObject.length();i++){
            JSONObject one = jsonQuestionObject.getJSONObject(i);
            if(one.optString("id").equals("read_2")){
                Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + one.optString("question"));
                one.putOpt ("question", "請回答關於新聞「" + news_title + "」的問題");
                Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + one.optString("question"));
                break;
            }
        }
        File file;
        file = new File(getFilesDir(),"test.json");
        try{
            FileOutputStream fos = new FileOutputStream(file);//创建一个文件输出流
            fos.write(jsonRootObject.toString().getBytes());//将生成的JSON数据写出
            fos.close();//关闭输出流
            Toast.makeText(getApplicationContext(),"创建成功！",Toast.LENGTH_SHORT).show();
            Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.d("lognewsselect", "!!!!!!!!!!!!!!!!!!!()" + getApplicationContext().getFilesDir());

        return file.getAbsolutePath();
    }
}
