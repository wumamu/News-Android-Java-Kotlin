package com.recoveryrecord.surveyandroid.example;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.chinatimes.ChinatimesMainFragment;
import com.recoveryrecord.surveyandroid.example.cna.CnaMainFragment;
import com.recoveryrecord.surveyandroid.example.cts.CtsMainFragment;
import com.recoveryrecord.surveyandroid.example.ebc.EbcMainFragment;
import com.recoveryrecord.surveyandroid.example.ettoday.EttodayMainFragment;
import com.recoveryrecord.surveyandroid.example.ltn.LtnMainFragment;
//import com.recoveryrecord.surveyandroid.example.receiever.ActivityRecognitionReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.ActivityRecognitionReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.AppUsageReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.BlueToothReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.LightSensorReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.MyBackgroudService;
import com.recoveryrecord.surveyandroid.example.receiever.NetworkChangeReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.RingModeReceiver;
import com.recoveryrecord.surveyandroid.example.receiever.ScreenStateReceiver;
import com.recoveryrecord.surveyandroid.example.setn.SetnMainFragment;
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
import java.util.Timer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import javax.annotation.Nullable;

import static com.recoveryrecord.surveyandroid.example.Constants.APP_VERSION_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.APP_VERSION_VALUE;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_ALARM_ACTION;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_LAST_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.DIARY_TIME_OUT;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_ALARM_ACTION;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_INTERVAL;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_LAST_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.ESM_TIME_OUT;
import static com.recoveryrecord.surveyandroid.example.Constants.MEDIA_BAR_ORDER;
import static com.recoveryrecord.surveyandroid.example.Constants.MY_DEVICE;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.PUSH_MEDIA_SELECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_CLEAR_CACHE;
//import static com.recoveryrecord.surveyandroid.example.Constants.DEFAULT_ESM_PARCELABLE;
import static com.recoveryrecord.surveyandroid.example.Constants.OUR_EMAIL;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_VALUE_FAILED_RESTART;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_CYCLE_VALUE_MAIN_PAGE;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_KEY;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_VALUE_RESTART;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_STATUS_VALUE_RUNNING;
import static com.recoveryrecord.surveyandroid.example.Constants.NEWS_SERVICE_TIME;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_USER_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.UPDATE_TIME;
//import static com.recoveryrecord.surveyandroid.example.Constants.USER_ANDROID;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_ANDROID_RELEASE;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_ANDROID_SDK;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_COLLECTION;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_DEVICE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_PHONE_ID;
import static com.recoveryrecord.surveyandroid.example.Constants.USER_SURVEY_NUMBER;
import static com.recoveryrecord.surveyandroid.example.config.Constants.DetectTime;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SeesionCountDown;
import static com.recoveryrecord.surveyandroid.example.config.Constants.SessionID;
import static com.recoveryrecord.surveyandroid.example.config.Constants.TimeLeftInMillis;

//public class NewsHybridActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
public class NewsHybridActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "TestNewsAllActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.document("server_push_notifications/start");
    private CollectionReference noteRefqq = db.collection("server_push_notifications");
    private String signature = "尚未設定實驗編號";

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
//    private SwipeRefreshLayout swipeRefreshLayout;
    private MySwipeRefreshLayout swipeRefreshLayout;
    private NavigationView navigationView;
    private Context context;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    boolean doubleBackToExitPressedOnce = false;
    private TextView user_phone, user_id, user_name;

    Intent mServiceIntent;
    private NewsNotificationService mYourService;

    String device_id = "";

//    boolean esm_range_flag = false, diary_range_flag = false;

    private static final HashMap<String, String> media_hash = new HashMap<String, String>();
    static{
        media_hash.put("中時", "chinatimes");
        media_hash.put("中央社", "cna");
        media_hash.put("華視", "cts");
        media_hash.put("東森", "ebc");
        media_hash.put("自由時報", "ltn");
        media_hash.put("風傳媒", "storm");
        media_hash.put("聯合", "udn");
        media_hash.put("ettoday", "ettoday");
        media_hash.put("三立", "setn");
    }
    //sensor part
    BlueToothReceiver _BluetoothReceiver;
    //    RingModeReceiver _RingModeReceiver;
    private AudioManager myAudioManager;
    private boolean activityTrackingEnabled;
    private List<ActivityTransition> activityTransitionList;
    //DETECTED ACTIVITY
    public GoogleApiClient mApiClient;
    public ActivityRecognitionClient mActivityRecognitionClient;
    NetworkChangeReceiver _NetworkChangeReceiver;
    ScreenStateReceiver _ScreenStateReceiver;
    RingModeReceiver _RingModeReceiver;
    LightSensorReceiver _LightSensorReceiver;

    //session
    //計時離開app的區間
    private CountDownTimer countDownTimer;
    private boolean TimerRunning;
    //private long TimeLeftInMillis = SeesionCountDown;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_news_hybrid);
        //initial value for user (first time)
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //first in app
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean clear = sharedPrefs.getBoolean(SHARE_PREFERENCE_CLEAR_CACHE, true);
        if (clear) {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(SHARE_PREFERENCE_DEVICE_ID, device_id);
            editor.putBoolean(SHARE_PREFERENCE_CLEAR_CACHE, false);
            editor.apply();
            clear = false;

            //initial media list
            Set<String> ranking = sharedPrefs.getStringSet(SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, Collections.<String>emptySet());
            if (ranking.isEmpty()){
                Set<String> set = new HashSet<String>();
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
                edit.clear();
                edit.putStringSet(SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, set);
                edit.apply();
            }
        }
        Map<String, Object> first = new HashMap<>();
        first.put(UPDATE_TIME, Timestamp.now());
        first.put(USER_DEVICE_ID, device_id);
        first.put(USER_PHONE_ID, Build.MODEL);
        first.put(USER_ANDROID_SDK, Build.VERSION.SDK_INT);
        first.put(USER_ANDROID_RELEASE, Build.VERSION.RELEASE);
        first.put(APP_VERSION_KEY, APP_VERSION_VALUE);
        first.put(USER_SURVEY_NUMBER, sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
        first.put(PUSH_MEDIA_SELECTION, sharedPrefs.getStringSet(SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION, Collections.<String>emptySet()).toString());
        first.put(MEDIA_BAR_ORDER, sharedPrefs.getStringSet(SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, Collections.<String>emptySet()).toString());
        db.collection(USER_COLLECTION)
                .document(device_id)
                .set(first);
        //notification media_select
//        Set<String> selections = sharedPrefs.getStringSet(SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION, Collections.<String>emptySet());
//        if (selections==null){
////            Toast.makeText(this, "趕快去設定選擇想要收到推播的媒體吧~", Toast.LENGTH_SHORT).show();
//        } else {
////            String[] selected = selections.toArray(new String[] {});
//            Log.d("lognewsselect", Arrays.toString(new Set[]{selections}));
//        }
        swipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.mainSwipeContainer);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue,R.color.red,R.color.black);
        //navi
        toolbar = findViewById(R.id.main_toolbar_hy);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_hy);
        navigationView = findViewById(R.id.nav_view_hy);
        View header = navigationView.getHeaderView(0);
        user_phone = (TextView) header.findViewById(R.id.textView_user_phone);
        user_phone.setText(Build.MODEL);
        user_id = (TextView) header.findViewById(R.id.textView_user_id);
        user_id.setText(device_id);
        user_name = (TextView) header.findViewById(R.id.textView_user_name);
        signature = sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號");
        if(signature.equals("尚未設定實驗編號")){
//            showStartDialog();
            user_name.setText("尚未設定實驗編號");
        } else {
            user_name.setText(signature);
        }
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

        setTitle("即時新聞");

        //check notification service running
        mYourService = new NewsNotificationService();
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

//        db.collection(TEST_USER_COLLECTION)
//                .document(device_id)
//                .collection(NEWS_SERVICE_COLLECTION)
////                .document(String.valueOf(Timestamp.now().toDate()))
//                .document(formatter.format(date))
//                .set(log_service);
        db.collection(NEWS_SERVICE_COLLECTION)
                .document(device_id + " " + formatter.format(date))
                .set(log_service);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this);
        mViewPager = (ViewPager) findViewById(R.id.container_hy);
        tabLayout = (TabLayout) findViewById(R.id.tabs_hy);
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
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
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
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
            }
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
        }


        //AppUsage
        startService(new Intent(getApplicationContext(), AppUsageReceiver.class));

    }
    @Override
    protected void onResume() {
        super.onResume();

        //Sessions
        if(TimerRunning == true) {
            pauseTimer();
        }else{
//            Log.e("TimerRunning", "FALSE");
//            Log.e("TimeLeftInMillis", String.valueOf(TimeLeftInMillis));
            if(TimeLeftInMillis < 1000){
                SessionID++;
            }
        }
//        Toast.makeText(this, "SESSION:"+SessionID, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        Log.d("log: activity cycle", "NewsHybridActivity On Start");
        Log.d("log: activity cycle", String.valueOf(Timestamp.now()));
        super.onStart();
        //regular timestated period
        startService(new Intent(getApplicationContext(), MyBackgroudService.class));
    }

    @Override
    protected void onPause() {
        Log.d("log: activity cycle", "NewsHybridActivity On Pause");
        super.onPause();
        resetTimer();
        startTimer();
    }

    @Override
    protected void onRestart() {
        Log.d("log: activity cycle", "NewsHybridActivity On Restart");
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
    private void showStartDialog() {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        AlertDialog.Builder editDialog = new AlertDialog.Builder(this);
        editDialog.setTitle("請輸入您的實驗編號");
        final EditText editText = new EditText(this);
        editText.setText("");
        editDialog.setView(editText);

        editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                signature = editText.getText().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(SHARE_PREFERENCE_USER_ID, signature);
                editor.apply();
//                textOut.setText(editText.getText().toString());
            }
        });
        editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
//...
            }
        });
        editDialog.show();
//        new AlertDialog.Builder(this)
//                .setTitle("注意事項(必做)")
////                .setMessage("1.帳號設定把通知存取打開\n2.帳號設定可以調整首頁媒體排序喔~\n3.帳號設定選擇想要收到推播的媒體吧~")
//                .setMessage("去帳號設定\n1.把通知存取打開\n2.問卷推播時間設定並按儲存~")
//                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .create().show();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
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
//            case R.id.nav_contactt :
////                long esm_time = sharedPrefs.getLong(ESM_LAST_TIME, 0);
////                Boolean esm_not_done = sharedPrefs.getBoolean(ESM_LAST_TIME, false);
////                Log.d("lognewsselect", String.valueOf(esm_not_done));
////
////                if(esm_not_done){
////                    Intent intent_esm = new Intent(context, AlarmReceiver.class);
////                    intent_esm.setAction(ESM_ALARM_ACTION);
////                    PendingIntent pendingIntent_esm = PendingIntent.getBroadcast(context, 77, intent_esm, 0);
////                    AlarmManager alarmManager_esm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
////                    Calendar cal_esm = Calendar.getInstance();
////                    cal_esm.add(Calendar.SECOND, 1);
////                    assert alarmManager_esm != null;
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                        alarmManager_esm.setExact(AlarmManager.RTC_WAKEUP, cal_esm.getTimeInMillis() , pendingIntent_esm);
////                    }else {
////                        alarmManager_esm.set(AlarmManager.RTC_WAKEUP, cal_esm.getTimeInMillis() , pendingIntent_esm);
////                    }
////                } else {
////                    Toast.makeText(this,"目前不在填答時間",Toast.LENGTH_LONG).show();
//////                    Toast.makeText(this,"permission denied",Toast.LENGTH_LONG).show();
////                }
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
//            case R.id.nav_tmp :
////                long diary_time = sharedPrefs.getLong(DIARY_LAST_TIME, 0);
////                Boolean diary_not_done = sharedPrefs.getBoolean(DIARY_LAST_TIME, false);
////                Log.d("lognewsselect", String.valueOf(diary_not_done));
////                if(diary_not_done){
////                    Intent intent_diary = new Intent(context, AlarmReceiver.class);
////                    intent_diary.setAction(DIARY_ALARM_ACTION);
////                    PendingIntent pendingIntent_diary = PendingIntent.getBroadcast(context, 78, intent_diary, 0);
////                    AlarmManager alarmManager_diary = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
////                    Calendar cal_diary = Calendar.getInstance();
////                    cal_diary.add(Calendar.SECOND, 1);
////                    assert alarmManager_diary != null;
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                        alarmManager_diary.setExact(AlarmManager.RTC_WAKEUP, cal_diary.getTimeInMillis() , pendingIntent_diary);
////                    }else {
////                        alarmManager_diary.set(AlarmManager.RTC_WAKEUP, cal_diary.getTimeInMillis() , pendingIntent_diary);
////                    }
////                } else {
////                    Toast.makeText(this,"目前不在填答時間",Toast.LENGTH_LONG).show();
//////                    Toast.makeText(this,"permission denied",Toast.LENGTH_LONG).show();
////                }
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
            default :
                return false;
        }

//        return false;
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
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
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }
    @Override
    public void onRefresh() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),context);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        swipeRefreshLayout.setRefreshing(false);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final Context context;
        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            //super.destroyItem(container, position, object);
//        }

        @Override
        public Fragment getItem(int position) {
//            Log.d ("mediaselect", String.valueOf(position));
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Set<String> ranking = sharedPrefs.getStringSet(SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, Collections.<String>emptySet());
            String media_name = "";
            if (!ranking.isEmpty()){
                switch (position) {
                    case 0:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==1){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 1:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==2){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 2:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==3){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 3:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==4){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 4:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==5){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 5:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==6){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 6:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==7){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 7:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==8){
                                media_name = out.get(0);
                                break;
                            }
                        }
                        break;
                    case 8:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
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
            Set<String> ranking = sharedPrefs.getStringSet(SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER, Collections.<String>emptySet());
            String media_name = "";
            if (!ranking.isEmpty()){
                switch (position) {
                    case 0:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==1){
                                media_name = out.get(0);
                                return out.get(0);
//                                break;
                            }
                        }
                    case 1:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==2){
                                media_name = out.get(0);
                                return out.get(0);
                            }
                        }
                    case 2:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==3){
                                media_name = out.get(0);
                                return out.get(0);
                            }
                        }
                    case 3:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==4){
                                media_name = out.get(0);
                                return out.get(0);
                            }
                        }
                    case 4:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==5){
                                media_name = out.get(0);
                                return out.get(0);
                            }
                        }
                    case 5:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==6){
                                media_name = out.get(0);
                                return out.get(0);
                            }
                        }
                    case 6:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==7){
                                media_name = out.get(0);
                                return out.get(0);
                            }
                        }
                    case 7:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==8){
                                media_name = out.get(0);
                                return out.get(0);
                            }
                        }
                    case 8:
                        for (String r : ranking){
                            List<String> out= new ArrayList<String>(Arrays.asList(r.split(" ")));
                            if(Integer.parseInt(out.get(1))==9){
                                media_name = out.get(0);
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
        Intent intent = new Intent( this, ActivityRecognitionReceiver.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        Task<Void> task = ActivityRecognition.getClient(this).requestActivityUpdates(DetectTime, pendingIntent);
        //mApiClient.disconnect();
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    public void startTimer(){
        countDownTimer = new CountDownTimer(TimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeftInMillis = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                TimerRunning = false;
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