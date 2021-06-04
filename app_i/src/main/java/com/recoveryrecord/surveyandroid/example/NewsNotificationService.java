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
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import static com.recoveryrecord.surveyandroid.example.Constants.APP_VERSION_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.APP_VERSION_VALUE;
import static com.recoveryrecord.surveyandroid.example.Constants.CHECK_SERVICE_ACTION;
import static com.recoveryrecord.surveyandroid.example.Constants.COMPARE_RESULT_CLICK;
import static com.recoveryrecord.surveyandroid.example.Constants.COMPARE_RESULT_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.COMPARE_RESULT_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.COMPARE_RESULT_MEDIA;
import static com.recoveryrecord.surveyandroid.example.Constants.COMPARE_RESULT_NEW_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.COMPARE_RESULT_PUBDATE;
//import static com.recoveryrecord.surveyandroid.example.Constants.COMPARE_RESULT_TYPE;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_NEWS_CHANNEL_ID;
//import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_NEWS_PARCELABLE;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_NEWS_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_NEWS_NOTIFICATION_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.DOC_ID_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.GROUP_NEWS;
import static com.recoveryrecord.surveyandroid.example.Constants.GROUP_NEWS_SERVICE;
import static com.recoveryrecord.surveyandroid.example.Constants.INITIAL_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.LAST_LAUNCH_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_CHANNEL_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_ID_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_MEDIA_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_VALUE_SERVICE_PAGE;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_VALUE_INITIAL;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_NEWS;
import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_TYPE_VALUE_SERVICE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_MEDIA;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_USER_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.READING_BEHAVIOR_USER_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_NOTIFICATION_FIRST_CREATE;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_NOTI_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_PUBDATE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_SELECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_TITLE;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_NEWS_TYPE;
import static com.recoveryrecord.surveyandroid.example.Constants.SERVICE_CHECKER_INTERVAL;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_USER_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.TEST_USER_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.TRIGGER_BY_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.TRIGGER_BY_VALUE_NOTIFICATION;
import static com.recoveryrecord.surveyandroid.example.Constants.UPDATE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_PHONE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.VIBRATE_EFFECT;

public class NewsNotificationService extends Service {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String device_id = "";
    Boolean first_group = true;
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
        Bundle extras = new Bundle();
        extras.putString(DOC_ID_KEY, "####");
        extras.putString(NOTIFICATION_TYPE_KEY, NOTIFICATION_TYPE_VALUE_SERVICE);
        notificationBuilder.setExtras(extras);



        Notification notification = notificationBuilder
                .setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setNotificationSilent()
                .setSmallIcon(R.drawable.ic_launcher_foreground, 0)

//                .setGroup(GROUP_NEWS_SERVICE)
//                .setGroupSummary(true)
                .build();
        startForeground(2, notification);
    }
    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    // execution of service will start
    // on calling this method
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("lognewsselect", "onStartCommand");
        listen_compare_result();//news
        add_ServiceChecker();//service checker
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void add_ServiceChecker() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {

        }
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.setAction(CHECK_SERVICE_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 50, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        long time_fired = System.currentTimeMillis() + SERVICE_CHECKER_INTERVAL;
//        am.setExact(AlarmManager.RTC_WAKEUP, time_fired, pi);       //註冊鬧鐘
        //用于设置一次性闹铃，执行时间更为精准，为精确闹铃。
        assert am != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, time_fired, pi);       //註冊鬧鐘
        } else {
            am.set(AlarmManager.RTC_WAKEUP, time_fired, pi);       //註冊鬧鐘
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void listen_compare_result() {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, Object> service_check = new HashMap<>();
        service_check.put(NEWS_SERVICE_STATUS_KEY, NEWS_SERVICE_STATUS_VALUE_INITIAL);
        service_check.put(NEWS_SERVICE_TIME, Timestamp.now());
        service_check.put(NEWS_SERVICE_CYCLE_KEY, NEWS_SERVICE_CYCLE_VALUE_SERVICE_PAGE);
        service_check.put(NEWS_SERVICE_DEVICE_ID, device_id);

        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

//        db.collection(TEST_USER_COLLECTION)
//                .document(device_id)
//                .collection(NEWS_SERVICE_COLLECTION)
////                .document(String.valueOf(Timestamp.now().toDate()))
//                .document(formatter.format(date))
//                .set(service_check);
        db.collection(NEWS_SERVICE_COLLECTION)
//                .document(String.valueOf(Timestamp.now().toDate()))
                .document(device_id + " " + formatter.format(date))
                .set(service_check);

        db.collection(TEST_USER_COLLECTION)
                .document(device_id)
                .collection(COMPARE_RESULT_COLLECTION)
                .orderBy(COMPARE_RESULT_PUBDATE, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("lognewsselect", "listen:error", e);
                            return;
                        }
//                        Timestamp right_now = Timestamp.now();
                        Set<String> selections = sharedPrefs.getStringSet(SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION, new HashSet<String>());
                        Log.d("lognewsselect_", "ss " + Arrays.toString(new Set[]{selections}));
                        int count = 0;
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot documentSnapshot = dc.getDocument();
                            switch (dc.getType()) {
                                case ADDED:
                                    //add record
//                                    Date date = new Date(System.currentTimeMillis());
//                                    @SuppressLint("SimpleDateFormat")
//                                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//                                    String doc_name = device_id + " " + formatter.format(date);
                                    Map<String, Object> record_noti = new HashMap<>();
                                    String news_id = "", media = "", title = "", doc_id = "";
                                    if(selections.contains(dc.getDocument().getString(COMPARE_RESULT_MEDIA))){
                                        if(count<20){
                                            Log.d("lognewsselect", "New doc: " + dc.getDocument().getData());
                                            news_id = dc.getDocument().getString(COMPARE_RESULT_ID);
                                            media  = dc.getDocument().getString(COMPARE_RESULT_MEDIA);
                                            title = dc.getDocument().getString(COMPARE_RESULT_NEW_TITLE);
                                            doc_id = dc.getDocument().getId();
                                            scheduleNotification(getNotification(news_id, media, title), 1000 );
                                            Log.d("lognewsselect", "doc id" + doc_id);
                                            record_noti.put(PUSH_NEWS_TYPE, "target add");
                                            record_noti.put(COMPARE_RESULT_CLICK, 0);
                                            count++;
                                        } else {
                                            record_noti.put(PUSH_NEWS_TYPE, "target too much");
                                            record_noti.put(COMPARE_RESULT_CLICK, 2);
                                        }
                                    } else {
                                        record_noti.put(PUSH_NEWS_TYPE, "not target");
                                        record_noti.put(COMPARE_RESULT_CLICK, 3);
                                    }
                                    record_noti.put(PUSH_NEWS_MEDIA, dc.getDocument().getString(COMPARE_RESULT_MEDIA));
                                    record_noti.put(PUSH_NEWS_TITLE, dc.getDocument().getString(COMPARE_RESULT_NEW_TITLE));
                                    record_noti.put(PUSH_NEWS_ID, dc.getDocument().getString(COMPARE_RESULT_ID));
                                    record_noti.put(PUSH_NEWS_PUBDATE, dc.getDocument().getTimestamp(COMPARE_RESULT_PUBDATE));
                                    record_noti.put(PUSH_NEWS_NOTI_TIME, Timestamp.now());
                                    record_noti.put(PUSH_NEWS_DEVICE_ID,  device_id);
                                    record_noti.put(PUSH_NEWS_USER_ID,  sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
//                                    record_noti.put(PUSH_NEWS_SELECTION, Arrays.toString(new Set[]{selections}));



//                                    db.collection(TEST_USER_COLLECTION)
//                                            .document(device_id)
//                                            .collection(PUSH_NEWS_COLLECTION)
//                                            .document(device_id + " " + dc.getDocument().getString(COMPARE_RESULT_ID))
////                                            .document(dc.getDocument().getId())
//                                            .set(record_noti);
                                    db.collection(PUSH_NEWS_COLLECTION)
                                            .document(device_id + " " + dc.getDocument().getString(COMPARE_RESULT_ID))
                                            .set(record_noti);

                                    //delete
                                    db.collection(TEST_USER_COLLECTION)
                                            .document(device_id)
                                            .collection(COMPARE_RESULT_COLLECTION)
                                            .document(dc.getDocument().getId())
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendBroadcast(new Intent(this, NewsNotificationRestarter.class).setAction(Constants.CHECK_SERVICE_ACTION));
        } else {
            Intent checkServiceIntent = new Intent(Constants.CHECK_SERVICE_ACTION);
            sendBroadcast(checkServiceIntent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleNotification (Notification notification, int delay) {
        Intent notificationIntent = new Intent(this, NotificationListenerNews.class);
        notificationIntent.putExtra(DEFAULT_NEWS_NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(DEFAULT_NEWS_NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private Notification getNotification(String news_id, String media, String title) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Boolean first_group = sharedPrefs.getBoolean(SHARE_NOTIFICATION_FIRST_CREATE, true);

        int nid = (int) System.currentTimeMillis();
        Intent intent_news = new Intent();
        intent_news.setClass(this, NewsModuleActivity.class);
        intent_news.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent_news.putExtra(TRIGGER_BY_KEY, TRIGGER_BY_VALUE_NOTIFICATION);
        intent_news.putExtra(NEWS_ID_KEY, news_id);
        intent_news.putExtra(NEWS_MEDIA_KEY, media);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, nid, intent_news, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, DEFAULT_NEWS_CHANNEL_ID);
        builder.setContentTitle(title);
        builder.setContentText(media);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setChannelId(NEWS_CHANNEL_ID);
        builder.setVibrate(VIBRATE_EFFECT);              //震動模式
        builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        builder.setCategory(Notification.CATEGORY_RECOMMENDATION);
        builder.setGroup(GROUP_NEWS);
        if(first_group){
            builder.setGroupSummary(true);
            first_group = false;
//            SharedPreferences.Editor editor = sharedPrefs.edit();
//            editor.putBoolean(SHARE_NOTIFICATION_FIRST_CREATE, false);
//            editor.apply();
        }
        Bundle extras = new Bundle();
        extras.putString(DOC_ID_KEY, news_id);
        extras.putString(NOTIFICATION_TYPE_KEY, NOTIFICATION_TYPE_VALUE_NEWS);
        builder.setExtras(extras);

        return builder.build() ;
    }



}


