package com.recoveryrecord.surveyandroid.example;

import static com.recoveryrecord.surveyandroid.example.Constants.APP_VERSION_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.APP_VERSION_VALUE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_DONE_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_PUSH_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_DONE_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_PUSH_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.MEDIA_BAR_ORDER;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_VALUE_FAILED_RESTART;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_VALUE_MAIN_PAGE;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_VALUE_RESTART;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_VALUE_RUNNING;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.OUR_EMAIL;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_MEDIA_SELECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_CLEAR_CACHE;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_USER_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.TEST_USER_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.UPDATE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_ANDROID_RELEASE;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_ANDROID_SDK;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_FIRESTORE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_NUM;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_PHONE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_SURVEY_NUMBER;
import static com.recoveryrecord.surveyandroid.example.config.Constants.LastPauseTime;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SeesionCountDown;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SessionID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.TimeLeftInMillis;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.DbHelper.SessionDbHelper;
import com.recoveryrecord.surveyandroid.example.receiever.AppUsageReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.LightSensorReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.MyBackgroudService;
import com.recoveryrecord.surveyandroid.example.receiever.NetworkChangeReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.RingModeReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.ScreenStateReceiver;
import com.recoveryrecord.surveyandroid.example.sqlite.Session;
import com.recoveryrecord.surveyandroid.example.ui.MainFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;


public class NewsHybridActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private String signature;
    private DrawerLayout drawerLayout;
//    private SwipeRefreshLayout swipeRefreshLayout;
    private MySwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    boolean doubleBackToExitPressedOnce = false;

    Intent mServiceIntent;

    String device_id = "";

    public GoogleApiClient mApiClient;
//    public ActivityRecognitionClient mActivityRecognitionClient;
    NetworkChangeReceiver _NetworkChangeReceiver;
    ScreenStateReceiver _ScreenStateReceiver;
    RingModeReceiver _RingModeReceiver;
    LightSensorReceiver _LightSensorReceiver;

    //session
    //計時離開app的區間
    private CountDownTimer countDownTimer;
    private boolean TimerRunning;

    public NewsHybridActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"HardwareIds", "LongLogTag", "ApplySharedPref", "RestrictedApi", "BatteryLife"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_news_hybrid);
        FirebaseCrashlytics.getInstance().setUserId(device_id);

        //initial value for user (first time)
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //first in app
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean clear = sharedPrefs.getBoolean(SHARE_PREFERENCE_CLEAR_CACHE, true);
        Map<String, Object> first = new HashMap<>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            signature = extras.getString(USER_NUM);
            //The key argument here must match that used in the other activity
        } else {
            signature = sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號");
        }


        if (clear) {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(SHARE_PREFERENCE_DEVICE_ID, device_id);
            editor.putBoolean(SHARE_PREFERENCE_CLEAR_CACHE, false);
            editor.apply();
            //initial media list
            Set<String> ranking = sharedPrefs.getStringSet(SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, Collections.emptySet());
            if (ranking.isEmpty()){
                Set<String> set = new HashSet<>();
                set.add("中時 1");
                set.add("中央社 2");
                set.add("華視 3");
                set.add("東森 4");
                set.add("自由時報 5");
                set.add("風傳媒 6");
                set.add("聯合 7");
                set.add("ettoday 8");
                set.add("三立 9");
                SharedPreferences.Editor edit = sharedPrefs.edit();
//                edit.clear();
                edit.putStringSet(SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, set);
                edit.apply();
            }
            List<String> media_bar_result = new ArrayList<>();
            List<String> media_push_result = new ArrayList<>();
//        List<String> media_bar_result = (List<String>) document.get(READING_BEHAVIOR_SHARE);
            media_bar_result.add(sharedPrefs.getStringSet(SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, Collections.emptySet()).toString());
//            media_push_result.add(sharedPrefs.getStringSet(SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION, Collections.<String>emptySet()).toString());
            media_push_result.add(String.join(",", sharedPrefs.getStringSet(SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION, Collections.emptySet()).toString()));
            first.put(MEDIA_BAR_ORDER, media_bar_result);
            first.put(USER_DEVICE_ID, device_id);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            assert user != null;
            first.put(USER_FIRESTORE_ID, user.getUid());
            first.put(USER_SURVEY_NUMBER, sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
            first.put(USER_PHONE_ID, Build.MODEL);
            first.put(USER_ANDROID_SDK, Build.VERSION.SDK_INT);
            first.put(USER_ANDROID_RELEASE, Build.VERSION.RELEASE);
            first.put(UPDATE_TIME, Timestamp.now());
            first.put(APP_VERSION_KEY, APP_VERSION_VALUE);
            first.put(PUSH_MEDIA_SELECTION, media_push_result);

            first.put("check_last_service", new Timestamp(0, 0));
            first.put("check_last_schedule", new Timestamp(0, 0));
            first.put("check_last_news", new Timestamp(0, 0));
            first.put("check_last_diary", new Timestamp(0, 0));
            first.put("check_last_esm", new Timestamp(0, 0));

            db.collection(USER_COLLECTION)
                    .document(device_id)
                    .set(first);

            db.collection(TEST_USER_COLLECTION)
                    .document(device_id)
                    .set(first);
        } else {
            final DocumentReference rbRef = db.collection(USER_COLLECTION).document(device_id);
            rbRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
//                        Log.d("log: firebase", "Success");
                        List<String> media_push_result = (List<String>) document.get(PUSH_MEDIA_SELECTION);
                        assert media_push_result != null;
//                        Log.d("l1231313", media_push_result.get(media_push_result.size() - 1));
                        //different from last one
//                            for (int)
                        String tmp = String.join(",", sharedPrefs.getStringSet(SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION, Collections.emptySet()).toString());
//                        Log.d("l1231313", tmp);
//                            media_push_result.add(tmp);
                        if(!media_push_result.get(media_push_result.size() - 1).equals(tmp)){
                            media_push_result.add(tmp);
                        }
//                            media_push_result.add(String.valueOf(media_push_result));
                        rbRef.update(PUSH_MEDIA_SELECTION, media_push_result,
                                USER_SURVEY_NUMBER, sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"),
                                UPDATE_TIME, Timestamp.now(),
                                APP_VERSION_KEY, APP_VERSION_VALUE,
                                "check_last_service", Timestamp.now(),
                                ESM_PUSH_TOTAL, sharedPrefs.getInt(ESM_PUSH_TOTAL, 0),
                                ESM_DONE_TOTAL, sharedPrefs.getInt(ESM_DONE_TOTAL, 0),
                                DIARY_PUSH_TOTAL, sharedPrefs.getInt(DIARY_PUSH_TOTAL, 0),
                                DIARY_DONE_TOTAL, sharedPrefs.getInt(DIARY_DONE_TOTAL, 0),

                                USER_DEVICE_ID, device_id,
                                USER_ANDROID_SDK, Build.VERSION.SDK_INT,
                                USER_ANDROID_RELEASE, Build.VERSION.RELEASE)
                                .addOnSuccessListener(aVoid -> Log.d("log: firebase share", "DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.w("log: firebase share", "Error updating document", e));
                    } else {

                        Log.d("log: firebase share", "No such document");
                    }
                } else {
                    Log.d("log: firebase share", "get failed with ", task.getException());
                }
            });
        }



        swipeRefreshLayout = findViewById(R.id.mainSwipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue,R.color.red,R.color.black);
        //navi
        Toolbar toolbar = findViewById(R.id.main_toolbar_hy);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_hy);
        NavigationView navigationView = findViewById(R.id.nav_view_hy);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView user_phone = header.findViewById(R.id.textView_user_phone);
        user_phone.setText(Build.MODEL);
        TextView user_id = header.findViewById(R.id.textView_user_id);
        user_id.setText(device_id);
        TextView user_name = header.findViewById(R.id.textView_user_name);
        user_name.setText(signature);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setTitle("新聞列表");

        //check notification service running
        NewsNotificationService mYourService = new NewsNotificationService();
        mServiceIntent = new Intent(this, NewsNotificationService.class);
        Map<String, Object> log_service = new HashMap<>();
        if (!isMyServiceRunning(mYourService.getClass())) {
            log_service.put(NEWS_SERVICE_STATUS_KEY, NEWS_SERVICE_STATUS_VALUE_RESTART);
            log_service.put(NEWS_SERVICE_CYCLE_KEY, NEWS_SERVICE_CYCLE_VALUE_FAILED_RESTART);
            startService(mServiceIntent);
        } else {
            log_service.put(NEWS_SERVICE_STATUS_KEY, NEWS_SERVICE_STATUS_VALUE_RUNNING);
            log_service.put(NEWS_SERVICE_CYCLE_KEY, NEWS_SERVICE_CYCLE_VALUE_MAIN_PAGE);
        }
        log_service.put(NEWS_SERVICE_TIME, Timestamp.now());
        log_service.put(NEWS_SERVICE_DEVICE_ID, device_id);
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

        db.collection(NEWS_SERVICE_COLLECTION)
                .document(device_id + " " + formatter.format(date))
                .set(log_service);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = findViewById(R.id.container_hy);
        tabLayout = findViewById(R.id.tabs_hy);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        //Detected Activity
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApiClient.connect();

        //Network
        _NetworkChangeReceiver = new NetworkChangeReceiver();
        _NetworkChangeReceiver.registerNetworkReceiver(this);

        //Screen
        _ScreenStateReceiver = new ScreenStateReceiver();
        _ScreenStateReceiver.registerScreenStateReceiver(this);

        //RingMode
        _RingModeReceiver = new RingModeReceiver();
        _RingModeReceiver.registerRingModeReceiver(this);

        //LightSensor
        _LightSensorReceiver = new LightSensorReceiver();
        _LightSensorReceiver.registerLightSensorReceiver(this);

        //AppUsage
        startService(new Intent(getApplicationContext(), AppUsageReceiver.class));
        //Sessions
        Map<String, Object> sessiontime = new HashMap<>();
        if(SessionID == 1){
            sessiontime.put("session", SessionID);
            sessiontime.put("state", 0);
            sessiontime.put("time", formatter.format(date));
            sessiontime.put("timestamp", Timestamp.now());
            sessiontime.put("device_id", device_id);
            db.collection("Session_List")
                    .document(device_id + " " + formatter.format(date))
                    .set(sessiontime);
            final SharedPreferences sharedPrefs1 = PreferenceManager.getDefaultSharedPreferences(context);
            com.recoveryrecord.surveyandroid.example.sqlite.Session mysession = new Session();//sqlite//add new to db
            mysession.setKEY_TIMESTAMP(Timestamp.now().getSeconds());
            mysession.setKEY_DOC_ID(device_id + " " + formatter.format(date));
            mysession.setKEY_DEVICE_ID(device_id);
            mysession.setKEY_USER_ID(sharedPrefs1.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
            mysession.setKEY_SESSION(SessionID);
            mysession.setKEY_STATE(0);
            SessionDbHelper dbHandler = new SessionDbHelper(context);
            dbHandler.insertSessionDetailsCreate(mysession);
        }

        //Accessibility Request
        if(!checkAccessibilityPermission()){
            Toast.makeText(NewsHybridActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean checkAccessibilityPermission () {
        int accessEnabled = 0;
        try {
            accessEnabled = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessEnabled == 0) {
            // if not construct intent to request permission
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // request permission via start activity for result
            startActivity(intent);
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onResume() {
        Log.e("onResume", "onResume");
        super.onResume();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Map<String, Object> sessiontime = new HashMap<>();
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        //Sessions

        if(TimerRunning) {
            pauseTimer();

            sessiontime.put("session", SessionID);
            sessiontime.put("state", 2);
            sessiontime.put("time", formatter.format(date));
            sessiontime.put("timestamp", Timestamp.now());
            sessiontime.put("device_id", device_id);
            db.collection("Session_List")
                    .document(device_id + " " + formatter.format(date))
                    .set(sessiontime);
            final SharedPreferences sharedPrefs1 = PreferenceManager.getDefaultSharedPreferences(context);
            com.recoveryrecord.surveyandroid.example.sqlite.Session mysession = new Session();//sqlite//add new to db
            mysession.setKEY_TIMESTAMP(Timestamp.now().getSeconds());
            mysession.setKEY_DOC_ID(device_id + " " + formatter.format(date));
            mysession.setKEY_DEVICE_ID(device_id);
            mysession.setKEY_USER_ID(sharedPrefs1.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
            mysession.setKEY_SESSION(SessionID);
            mysession.setKEY_STATE(2);
            SessionDbHelper dbHandler = new SessionDbHelper(context);
            dbHandler.insertSessionDetailsCreate(mysession);
        }else{
            if(TimeLeftInMillis < 1000){
                SessionID++;
                sessiontime.put("session", SessionID);
                sessiontime.put("state", 0);
                sessiontime.put("time", formatter.format(date));
                sessiontime.put("timestamp", Timestamp.now());
                sessiontime.put("device_id", device_id);
                db.collection("Session_List")
                        .document(device_id + " " + formatter.format(date))
                        .set(sessiontime);
                final SharedPreferences sharedPrefs1 = PreferenceManager.getDefaultSharedPreferences(context);
                com.recoveryrecord.surveyandroid.example.sqlite.Session mysession = new Session();//sqlite//add new to db
                mysession.setKEY_TIMESTAMP(Timestamp.now().getSeconds());
                mysession.setKEY_DOC_ID(device_id + " " + formatter.format(date));
                mysession.setKEY_DEVICE_ID(device_id);
                mysession.setKEY_USER_ID(sharedPrefs1.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
                mysession.setKEY_SESSION(SessionID);
                mysession.setKEY_STATE(0);
                SessionDbHelper dbHandler = new SessionDbHelper(context);
                dbHandler.insertSessionDetailsCreate(mysession);
            }
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onStart() {
        super.onStart();
        //regular timestated period
        startService(new Intent(getApplicationContext(), MyBackgroudService.class));
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onPause() {
        Log.d("log: activity cycle", "NewsHybridActivity On Pause");
        super.onPause();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Map<String, Object> sessiontime = new HashMap<>();
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        resetTimer();
        startTimer();
        LastPauseTime = formatter;
        sessiontime.put("session", -1);
        sessiontime.put("state", 1);
        sessiontime.put("time", formatter.format(date));
        sessiontime.put("timestamp", Timestamp.now());
        sessiontime.put("device_id", device_id);
        db.collection("Session_List")
                .document(device_id + " " + formatter.format(date))
                .set(sessiontime);
        final SharedPreferences sharedPrefs1 = PreferenceManager.getDefaultSharedPreferences(context);
        com.recoveryrecord.surveyandroid.example.sqlite.Session mysession = new Session();//sqlite//add new to db
        mysession.setKEY_TIMESTAMP(Timestamp.now().getSeconds());
        mysession.setKEY_DOC_ID(device_id + " " + formatter.format(date));
        mysession.setKEY_DEVICE_ID(device_id);
        mysession.setKEY_USER_ID(sharedPrefs1.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
        mysession.setKEY_SESSION(-1);
        mysession.setKEY_STATE(1);
        SessionDbHelper dbHandler = new SessionDbHelper(context);
        dbHandler.insertSessionDetailsCreate(mysession);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        _BluetoothReceiver.unregisterBluetoothReceiver(this);
        _NetworkChangeReceiver.unregisterNetworkReceiver(this);
        _ScreenStateReceiver.unregisterScreenStateReceiver(this);
        _RingModeReceiver.unregisterBluetoothReceiver(this);
        _LightSensorReceiver.unregisterLightSensorReceiver(this);
        super.onDestroy();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_setting :
                Intent intent_setting = new Intent(NewsHybridActivity.this, SettingsActivity.class);
                startActivity(intent_setting);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_history :
                Intent intent_rbh = new Intent(NewsHybridActivity.this, ReadHistoryActivity.class);
                startActivity(intent_rbh);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_reschedule :
                Intent intent_notih = new Intent(NewsHybridActivity.this, PushHistoryActivity.class);
                startActivity(intent_notih);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_contact:
                Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
                selectorIntent.setData(Uri.parse("mailto:"));

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{OUR_EMAIL});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "NewsMoment App 問題回報");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, 我的 user id 是" + signature + "，" + "\ndevice id 是" + device_id +"，\n我有問題要回報(以文字描述發生的問題)：\n以下是相關問題截圖(如有截圖或是錄影，可以幫助我們更快了解問題)：");
                emailIntent.setSelector(selectorIntent);
                if (emailIntent.resolveActivity(getPackageManager())!=null){
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } else {
                    Toast.makeText(this,"Gmail App is not installed",Toast.LENGTH_LONG).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            default :
                return false;
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
        }
    }
    @Override
    public void onRefresh() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), context);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        swipeRefreshLayout.setRefreshing(false);
    }



    public static class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final Context context;
        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            //super.destroyItem(container, position, object);
//        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
//            Log.d ("mediaselect", String.valueOf(position));
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Set<String> ranking = sharedPrefs.getStringSet(SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, Collections.emptySet());
            String media_name = "";
            if (!ranking.isEmpty()){
                switch (position) {
                    case 0:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==1){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 1:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==2){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 2:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==3){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 3:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==4){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 4:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==5){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 5:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==6){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 6:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==7){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 7:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==8){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 8:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==9){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                }
                Log.d ("mediaselect", media_name+ "   565");
                switch (media_name) {
                    case "中時":
                        return MainFragment.newInstance("chinatimes");
                    case "中央社":
                        return MainFragment.newInstance("cna");
                    case "華視":
                        return MainFragment.newInstance("cts");
                    case "東森":
                        return MainFragment.newInstance("ebc");
                    case "ettoday":
                        return MainFragment.newInstance("ettoday");
                    case "聯合":
                        return MainFragment.newInstance("udn");
                    case "自由時報":
                        return MainFragment.newInstance("ltn");
                    case "風傳媒":
                        return MainFragment.newInstance("storm");
                    case "三立":
                        return MainFragment.newInstance("setn");
                    default:
                        return TestTab3Fragment.newInstance();
                }
            } else {
                switch (position) {
                    case 0:
                        return MainFragment.newInstance("chinatimes");
                    case 1:
                        return MainFragment.newInstance("cna");
                    case 2:
                        return MainFragment.newInstance("cts");
                    case 3:
                        return MainFragment.newInstance("ebc");
                    case 4:
                        return MainFragment.newInstance("ettoday");
                    case 5:
                        return MainFragment.newInstance("udn");
                    case 6:
                        return MainFragment.newInstance("ltn");
                    case 7:
                        return MainFragment.newInstance("storm");
                    case 8:
                        return MainFragment.newInstance("setn");
                    default:
                        return TestTab3Fragment.newInstance();
                }
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 9;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Set<String> ranking = sharedPrefs.getStringSet(SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, Collections.emptySet());
            if (!ranking.isEmpty()){
                switch (position) {
                    case 0:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==1){
                                return out.get(0);
//                                break;
                            }
                        }
                    case 1:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==2){
                                return out.get(0);
                            }
                        }
                    case 2:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==3){
                                return out.get(0);
                            }
                        }
                    case 3:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==4){
                                return out.get(0);
                            }
                        }
                    case 4:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==5){
                                return out.get(0);
                            }
                        }
                    case 5:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==6){
                                return out.get(0);
                            }
                        }
                    case 6:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==7){
                                return out.get(0);
                            }
                        }
                    case 7:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==8){
                                return out.get(0);
                            }
                        }
                    case 8:
                        for (String r : ranking){
                            List<String> out= new ArrayList<>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==9){
                                return out.get(0);
                            }
                        }
                    default:
//                        media_name = String.valueOf(position);
                        return null;
                }
            } else {
                switch (position) {
                    case 0:
                        return "中時";
                    case 1:
                        return "中央社";
                    case 2:
                        return "華視";
                    case 3:
                        return "東森";
                    case 4:
                        return "ettoday";
                    case 5:
                        return "聯合";
                    case 6:
                        return "自由";
                    case 7:
                        return "風傳媒";
                    case 8:
                        return "三立";
                }
                return null;
            }
//            return null;
//            Log.d ("mediaselect", media_name);
//
        }


    }
    //sensor
    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        Intent intent = new Intent( this, ActivityRecognitionReceiver.class );
//        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
//        Task<Void> task = ActivityRecognition.getClient(this).requestActivityUpdates(DetectTime, pendingIntent);
        //mApiClient.disconnect();
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    @SuppressLint("HardwareIds")
    public void startTimer(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        final Map<String, Object> sessiontime = new HashMap<>();
        final Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        countDownTimer = new CountDownTimer(TimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeftInMillis = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                TimerRunning = false;
                sessiontime.put("session", SessionID);
                sessiontime.put("state", 1);
                sessiontime.put("time", formatter.format(date));
                sessiontime.put("timestamp", Timestamp.now());
                sessiontime.put("device_id", device_id);
                db.collection("Session_List")
                        .document(device_id + " " + formatter.format(date))
                        .set(sessiontime);
                final SharedPreferences sharedPrefs1 = PreferenceManager.getDefaultSharedPreferences(context);
                com.recoveryrecord.surveyandroid.example.sqlite.Session mysession = new Session();//sqlite//add new to db
                mysession.setKEY_TIMESTAMP(Timestamp.now().getSeconds());
                mysession.setKEY_DOC_ID(device_id + " " + formatter.format(date));
                mysession.setKEY_DEVICE_ID(device_id);
                mysession.setKEY_USER_ID(sharedPrefs1.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
                mysession.setKEY_SESSION(SessionID);
                mysession.setKEY_STATE(1);
                SessionDbHelper dbHandler = new SessionDbHelper(context);
                dbHandler.insertSessionDetailsCreate(mysession);
            }
        }.start();
        TimerRunning = true;
    }
    public void pauseTimer(){
        countDownTimer.cancel();
        TimerRunning = false;
    }
    public void resetTimer(){
        TimeLeftInMillis = SeesionCountDown;
    }
}