package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.DbHelper.ESMDbHelper;
import com.recoveryrecord.surveyandroid.example.DbHelper.SessionDbHelper;
import com.recoveryrecord.surveyandroid.example.chinatimes.ChinatimesMainFragment;
import com.recoveryrecord.surveyandroid.example.cna.CnaMainFragment;
import com.recoveryrecord.surveyandroid.example.cts.CtsMainFragment;
import com.recoveryrecord.surveyandroid.example.ebc.EbcMainFragment;
import com.recoveryrecord.surveyandroid.example.ettoday.EttodayMainFragment;
import com.recoveryrecord.surveyandroid.example.ltn.LtnMainFragment;
import com.recoveryrecord.surveyandroid.example.receiever.AppUsageReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.LightSensorReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.MyBackgroudService;
import com.recoveryrecord.surveyandroid.example.receiever.NetworkChangeReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.RingModeReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.ScreenStateReceiver;
import com.recoveryrecord.surveyandroid.example.setn.SetnMainFragment;
import com.recoveryrecord.surveyandroid.example.sqlite.Session;
import com.recoveryrecord.surveyandroid.example.storm.StormMainFragment;
import com.recoveryrecord.surveyandroid.example.udn.UdnMainFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

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

import static com.recoveryrecord.surveyandroid.example.Constants.APP_VERSION_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.APP_VERSION_VALUE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_DONE_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_PUSH_TOTAL;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_ALARM_ACTION;
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


//public class NewsHybridActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
public class NewsHybridActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private FloatingActionButton myFab;
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
    //private long TimeLeftInMillis = SeesionCountDown;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"HardwareIds", "LongLogTag", "ApplySharedPref", "RestrictedApi", "BatteryLife"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_news_hybrid);

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

        myFab = findViewById(R.id.floatButton);
        ESMDbHelper esmdbHandler = new ESMDbHelper(getApplicationContext());
        Cursor cursor_esm = esmdbHandler.getRecentEsmRecord(Timestamp.now().getSeconds());
        if(cursor_esm.getCount() != 0){
            cursor_esm.moveToFirst();
            String result = cursor_esm.getString(cursor_esm.getColumnIndex("result"));
            //田過了
            long remove_timestamp = cursor_esm.getLong(cursor_esm.getColumnIndex("remove_timestamp"));
            long receieve_timestamp = cursor_esm.getLong(cursor_esm.getColumnIndex("receieve_timestamp"));
            long diff = Timestamp.now().getSeconds()-receieve_timestamp;
            //==0 還在通知藍
            if(result.equals("NA") && remove_timestamp!=0){
                if(diff<=900){//15 min
                    myFab.setOnClickListener(v -> sendEsm());
//                    myFab.setVisibility(View.GONE);
                } else {
                    myFab.setVisibility(View.GONE);
                }
            } else {
                myFab.setVisibility(View.GONE);
            }
            if (!cursor_esm.isClosed()) {
                cursor_esm.close();
            }
        } else {
            //first time database
            myFab.setOnClickListener(v -> sendEsm());
//            myFab.setVisibility(View.GONE);
        }

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> usageStats = mUsageStatsManager.queryAndAggregateUsageStats(System.currentTimeMillis()-1000*300, System.currentTimeMillis());
        long totalTimeUsageInMillis = usageStats.get(getPackageName()).getTotalTimeInForeground();
        Log.d("123456789", String.valueOf(totalTimeUsageInMillis));

        swipeRefreshLayout = findViewById(R.id.mainSwipeContainer);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue,R.color.red,R.color.black);
        //navi
        Toolbar toolbar = findViewById(R.id.main_toolbar_hy);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_hy);
        NavigationView navigationView = findViewById(R.id.nav_view_hy);
        View header = navigationView.getHeaderView(0);
        TextView user_phone = header.findViewById(R.id.textView_user_phone);
        user_phone.setText(Build.MODEL);
        TextView user_id = header.findViewById(R.id.textView_user_id);
        user_id.setText(device_id);
        TextView user_name = header.findViewById(R.id.textView_user_name);
//        signature = sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號");
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
        navigationView.setNavigationItemSelectedListener(this);

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

        //sensor
        //BLUETOOTH
//        _BluetoothReceiver = new BlueToothReceiver();
//        _BluetoothReceiver.registerBluetoothReceiver(this);
//        //BLUETOOTH--要偵測附近裝置需要啟動位置權限
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//            //Android M Permission check
//            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
////                builder.setTitle("This app needs location access");
////                builder.setMessage("Please grant location access so this app can detect beacons.");
////                builder.setPositiveButton(android.R.string.ok, null);
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//                    }
//                });
//                builder.show();
//            }
//        }
//        //若沒開啟藍芽則跳出要求
//        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter( );
//        if( !mBtAdapter.isEnabled( ) ) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, 1);
//        }
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

        //ActivityRecognition
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
//                // Permission is not granted
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
//            }
//        }
//            //Android Q Permission check
//            if(this.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION)!= PackageManager.PERMISSION_GRANTED){
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
////                builder.setTitle("This app needs activity recognition access");
////                builder.setMessage("Please g
////                rant activity recognition access so this app can detect physical activity.");
////                builder.setPositiveButton(android.R.string.ok, null);
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
//                    }
//                });
//                builder.show();
//            }
//        }


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
    }

    @SuppressLint("RestrictedApi")
    private void sendEsm() {
        Toast.makeText(this, "正在為您生成問卷，請稍等", Toast.LENGTH_SHORT).show();
        myFab.setVisibility(View.GONE);
        Intent intent_esm = new Intent(context, AlarmReceiver.class);
        intent_esm.setAction(ESM_ALARM_ACTION);
        PendingIntent pendingIntent_esm = PendingIntent.getBroadcast(context, 77, intent_esm, 0);
        AlarmManager alarmManager_esm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar cal_esm = Calendar.getInstance();
        cal_esm.add(Calendar.SECOND, 1);
        assert alarmManager_esm != null;
        alarmManager_esm.setExact(AlarmManager.RTC_WAKEUP, cal_esm.getTimeInMillis() , pendingIntent_esm);
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
//            Log.e("TimerRunning", "FALSE");
//            Log.e("TimeLeftInMillis", String.valueOf(TimeLeftInMillis));
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

//        Toast.makeText(this, "SESSION:"+SessionID, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onStart() {
//        Log.d("log: activity cycle", "NewsHybridActivity On Start");
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
//        Log.d("log: activity cycle", "NewsHybridActivity On Restart");
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        switch (item.getItemId()) {
            case R.id.nav_setting :
//                Log.d("log: navigation", "nav_setting " + item.getItemId());
                Intent intent_setting = new Intent(NewsHybridActivity.this, SettingsActivity.class);
                startActivity(intent_setting);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_progressing :
//                Log.d("log: navigation", "nav_progressing " + item.getItemId());
                Intent intent_db = new Intent(NewsHybridActivity.this, SurveyProgressActivity.class);
                startActivity(intent_db);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_history :
//                Log.d("log: navigation", "nav_history " + item.getItemId());
                Intent intent_rbh = new Intent(NewsHybridActivity.this, ReadHistoryActivity.class);
                startActivity(intent_rbh);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_reschedule :
//                Log.d("log: navigation", "nav_reschedule " + item.getItemId());
                Intent intent_notih = new Intent(NewsHybridActivity.this, PushHistoryActivity.class);
                startActivity(intent_notih);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.upload:
                Intent intent_up = new Intent(NewsHybridActivity.this, UploadPagesActivity.class);
                startActivity(intent_up);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_update:
                Intent intent_update = new Intent(NewsHybridActivity.this, CheckUpdateActivity.class);
                startActivity(intent_update);
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
            case R.id.nav_listen:
                if(signature.equals("test")){
                    Intent intent_listen = new Intent(NewsHybridActivity.this, ListenActivity.class);
                    startActivity(intent_listen);
                } else {
                    Toast.makeText(this, "抱歉您沒有權限",Toast.LENGTH_LONG).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
//            case R.id.nav_esm :
//                Intent intent_esm = new Intent(context, AlarmReceiver.class);
//                intent_esm.setAction(ESM_ALARM_ACTION);
//                PendingIntent pendingIntent_esm = PendingIntent.getBroadcast(context, 77, intent_esm, 0);
//                AlarmManager alarmManager_esm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//                Calendar cal_esm = Calendar.getInstance();
//                cal_esm.add(Calendar.SECOND, 1);
//                assert alarmManager_esm != null;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    alarmManager_esm.setExact(AlarmManager.RTC_WAKEUP, cal_esm.getTimeInMillis() , pendingIntent_esm);
//                }else {
//                    alarmManager_esm.set(AlarmManager.RTC_WAKEUP, cal_esm.getTimeInMillis() , pendingIntent_esm);
//                }
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
//            case R.id.nav_tmp :
//                Intent intent_diary = new Intent(context, AlarmReceiver.class);
//                intent_diary.setAction(DIARY_ALARM_ACTION);
//                PendingIntent pendingIntent_diary = PendingIntent.getBroadcast(context, 78, intent_diary, 0);
//                AlarmManager alarmManager_diary = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//                Calendar cal_diary = Calendar.getInstance();
//                cal_diary.add(Calendar.SECOND, 1);
//                assert alarmManager_diary != null;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    alarmManager_diary.setExact(AlarmManager.RTC_WAKEUP, cal_diary.getTimeInMillis() , pendingIntent_diary);
//                }else {
//                    alarmManager_diary.set(AlarmManager.RTC_WAKEUP, cal_diary.getTimeInMillis() , pendingIntent_diary);
//                }
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
            default :
                return false;
        }

//        return false;
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
//                Log.d ("Servicestatus", "Running");
                return true;
            }
        }
//        Log.d ("Servicestatus", "Not running");
        return false;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
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
                        return new ChinatimesMainFragment();
                    case "中央社":
                        return new CnaMainFragment();
                    case "華視":
                        return new CtsMainFragment();
                    case "東森":
                        return new EbcMainFragment();
                    case "ettoday":
                        return new EttodayMainFragment();
                    case "聯合":
                        return new UdnMainFragment();
                    case "自由時報":
                        return new LtnMainFragment();
                    case "風傳媒":
                        return new StormMainFragment();
                    case "三立":
                        return new SetnMainFragment();
                    default:
                        return TestTab3Fragment.newInstance();
                }
            } else {
                switch (position) {
                    case 0:
                        return new ChinatimesMainFragment();
                    case 1:
                        return new CnaMainFragment();
                    case 2:
                        return new CtsMainFragment();
                    case 3:
                        return new EbcMainFragment();
                    case 4:
                        return new EttodayMainFragment();
                    case 5:
                        return new UdnMainFragment();
                    case 6:
                        return new LtnMainFragment();
                    case 7:
                        return new StormMainFragment();
                    case 8:
                        return new SetnMainFragment();
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