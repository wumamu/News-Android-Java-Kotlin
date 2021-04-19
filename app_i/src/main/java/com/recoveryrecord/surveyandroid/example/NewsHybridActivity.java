package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recoveryrecord.surveyandroid.example.chinatimes.ChinatimesMainFragment;
import com.recoveryrecord.surveyandroid.example.cna.CnaMainFragment;
import com.recoveryrecord.surveyandroid.example.cts.CtsMainFragment;
import com.recoveryrecord.surveyandroid.example.ebc.EbcMainFragment;
import com.recoveryrecord.surveyandroid.example.ettoday.EttodayMainFragment;
import com.recoveryrecord.surveyandroid.example.ltn.LtnMainFragment;
import com.recoveryrecord.surveyandroid.example.storm.StormMainFragment;
import com.recoveryrecord.surveyandroid.example.udn.UdnMainFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MotionEventCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

//public class NewsHybridActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
public class NewsHybridActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener{
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private static final String TAG = "NewsAllActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.document("server_push_notifications/start");
    private CollectionReference noteRefqq = db.collection("server_push_notifications");

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_news_hybrid);
        //initial value for user (first time)
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        @SuppressLint("HardwareIds")
        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference docIdRef = db.collection("test_users").document(device_id);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                    } else {
                        Log.d(TAG, "Document does not exist!");
                        Map<String, Object> first = new HashMap<>();
                        first.put("test", Timestamp.now());
                        first.put("user_id", device_id);
                        first.put("user_phone_id", Build.MODEL);
                        db.collection("test_users")
                                .document(device_id)
                                .set(first);
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
        //check preference
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //initial media list
        Set<String> ranking = sharedPrefs.getStringSet("media_rank", null);
        if (ranking==null){
            Set<String> set = new HashSet<String>();
            set.add("中時 1");
            set.add("中央社 2");
            set.add("華視 3");
            set.add("東森 4");
            set.add("自由時報 5");
            set.add("風傳媒 6");
            set.add("聯合 7");
            set.add("ettoday 8");
            SharedPreferences.Editor edit = sharedPrefs.edit();
            edit.clear();
            edit.putStringSet("media_rank", set);
            edit.apply();
            Toast.makeText(this, "帳號設定可以調整首頁媒體排序喔~", Toast.LENGTH_SHORT).show();
        }
        //notification media_select
        Set<String> selections = sharedPrefs.getStringSet("media_select", null);
        if (selections==null){
            Toast.makeText(this, "趕快去設定選擇想要收到推播的媒體吧~", Toast.LENGTH_SHORT).show();
        } else {
//            String[] selected = selections.toArray(new String[] {});
            Log.d("lognewsselect", Arrays.toString(new Set[]{selections}));
        }
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
        String signature = sharedPrefs.getString("signature", null);
        if (signature==null){
//            Toast.makeText(this, "趕快去設定選擇想要的媒體吧~", Toast.LENGTH_LONG).show();
            user_name.setText("使用者名稱");
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

        setTitle("Time Me 即時新聞");

        //check notification service running
        mYourService = new NewsNotificationService();
//        mServiceIntent = new Intent(this, mYourService.getClass());
        mServiceIntent = new Intent(this, NewsNotificationService.class);
        if (!isMyServiceRunning(mYourService.getClass())) {
            Toast.makeText(this, "service failed", Toast.LENGTH_SHORT).show();
            startService(mServiceIntent);
        } else {
            Toast.makeText(this, "service running", Toast.LENGTH_SHORT).show();
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this);
        mViewPager = (ViewPager) findViewById(R.id.container_hy);
        tabLayout = (TabLayout) findViewById(R.id.tabs_hy);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);




    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("log: activity cycle", "On resume");
//        mViewPager = (ViewPager) findViewById(R.id.container_hy);
//        tabLayout = (TabLayout) findViewById(R.id.tabs_hy);
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//        tabLayout.setupWithViewPager(mViewPager);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_setting :
                Log.d("log: navigation", "nav_setting " + item.getItemId());
                Intent intent_setting = new Intent(NewsHybridActivity.this, SettingsActivity.class);
                startActivity(intent_setting);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_progressing :
                Log.d("log: navigation", "nav_progressing " + item.getItemId());
                Intent intent_db = new Intent(NewsHybridActivity.this, SurveyProgressActivity.class);
                startActivity(intent_db);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_history :
                Log.d("log: navigation", "nav_history " + item.getItemId());
                Intent intent_noti = new Intent(NewsHybridActivity.this, ReadHistoryActivity.class);
                startActivity(intent_noti);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_reschedule :
                Log.d("log: navigation", "nav_reschedule " + item.getItemId());
                scheduleNotification_esm(getNotification_esm("Please fill out the questionnaire" ), 1000 );
//                Toast.makeText(this, "開始每小時固定發送esm~", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "發送esm~", Toast.LENGTH_SHORT).show();
//                Intent intent_base = new Intent(NewsAllActivity.this, TestBasicActivity.class);
//                startActivity(intent_base);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_contact :
                Log.d("log: navigation", "nav_contact " + item.getItemId());
//                if (Helper.isAppRunning(NewsHybridActivity.this, getPackageName())) {
//                    // App is running
//                    Log.d("apprunning", "1");
//                } else {
//                    // App is not running
//                    Log.d("apprunning", "2");
//                }
                Toast.makeText(this, "目前什麼都沒有拉~", Toast.LENGTH_SHORT).show();
                Intent intent_ems = new Intent(NewsHybridActivity.this, MainActivity.class);
                startActivity(intent_ems);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            default :
                return false;
        }

//        return false;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d ("Servicestatus", "Running");
                return true;
            }
        }
        Log.d ("Servicestatus", "Not running");
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
    private Notification getNotification_esm (String content) {
        //replace content with time
        Date date = new Date(System.currentTimeMillis());
        String esm_id = "";
        esm_id = String.valueOf(date);
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String time_now = formatter.format(date);

        int nid = (int) System.currentTimeMillis();
        Log.d("logesm", "esm id " + nid + " " + Timestamp.now());
        Intent intent_esm = new Intent();
        intent_esm.setClass(NewsHybridActivity.this, ESMActivity.class);
        intent_esm.putExtra("trigger_from", "Notification");
        intent_esm.putExtra("esm_id", esm_id);
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
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Map<String, Object> esm = new HashMap<>();
        esm.put("noti_time", time_now);
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
        Intent notificationIntent = new Intent(this, NotificationListenerNews.class);
        notificationIntent.putExtra(NotificationListenerESM.NOTIFICATION_ID, 1 ) ;
        notificationIntent.putExtra(NotificationListenerESM.NOTIFICATION, notification) ;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000 + 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, randomNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
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
            Log.d ("mediaselect", String.valueOf(position));
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Set<String> ranking = sharedPrefs.getStringSet("media_rank", null);
            String media_name = "";
            if (ranking!=null){
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
                    default:
                        return Tab3Fragment.newInstance();
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
                    default:
                        return Tab3Fragment.newInstance();
                }
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Set<String> ranking = sharedPrefs.getStringSet("media_rank", null);
            String media_name = "";
            if (ranking!=null){
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
                }
                return null;
            }
//            return null;
//            Log.d ("mediaselect", media_name);
//
        }
    }

}