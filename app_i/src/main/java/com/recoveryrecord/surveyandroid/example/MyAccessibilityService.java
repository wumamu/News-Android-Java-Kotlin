//package com.recoveryrecord.surveyandroid.example;
//
//import android.accessibilityservice.AccessibilityService;
//import android.accessibilityservice.AccessibilityServiceInfo;
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.ActivityManager;
//import android.app.UiAutomation;
//import android.app.usage.UsageStats;
//import android.app.usage.UsageStatsManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageInfo;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Message;
//import android.os.PowerManager;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.View;
//import android.view.accessibility.AccessibilityEvent;
//import android.view.accessibility.AccessibilityNodeInfo;
//import android.view.accessibility.AccessibilityWindowInfo;
//import android.widget.Button;
//import android.widget.Toast;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Deque;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.Set;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
//import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_DEVICE_ID;
//import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_MONITOR_COLLECTION;
//import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_NOTI_TIME;
//import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_PACKAGE_ID;
//import static com.recoveryrecord.surveyandroid.example.Constants.NOTIFICATION_BAR_NEWS_SOURCE;
//import static com.recoveryrecord.surveyandroid.example.Constants.SHARE_PREFERENCE_USER_ID;
//import static com.recoveryrecord.surveyandroid.example.Utils.facebook;
//import static com.recoveryrecord.surveyandroid.example.Utils.line;
//import static com.recoveryrecord.surveyandroid.example.Utils.messenger;
//import static com.recoveryrecord.surveyandroid.example.Utils.ptt;
//import static com.recoveryrecord.surveyandroid.example.Utils.youtube;
//import static com.recoveryrecord.surveyandroid.example.Utils.screen_on;
//
//import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
//import androidx.preference.PreferenceManager;
//
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.recoveryrecord.surveyandroid.example.DbHelper.FBMovementDbHelper;
//import com.recoveryrecord.surveyandroid.example.DbHelper.ReadingBehaviorDbHelper;
//import com.recoveryrecord.surveyandroid.example.FSM.FBStateMachine;
//import com.recoveryrecord.surveyandroid.example.FSM.StateDef;
//import com.recoveryrecord.surveyandroid.example.StateMachine.State;
//import com.recoveryrecord.surveyandroid.example.CSVDataRecord.CSVHelper;
//import com.recoveryrecord.surveyandroid.example.sqlite.FBMovement;
//import com.recoveryrecord.surveyandroid.example.sqlite.ReadingBehavior;
//
//public class MyAccessibilityService extends AccessibilityService{
//    final String TAG = "Accessibility service";
//
//    private String MyEventText = "";
//    private long[] vibrate_effect = {0, 200};
//    private static String CHANNEL_ID = "AccessibilityService";
//    public static final int Accessibility_ID = 6;
//
//    private String name = "";
//    private int log_counter = 0;
//    Handler mMainThread = new Handler();
//    private String[] NewsPack;
//    private String[] NewsName;
//    private String[] WebPack;
//    private String[] WebEvent;
//    private String[] HomePackage;
//    private String[] AppPack;
//    private String[] PttPack;
//    private PowerManager mPowerManager;
//    private boolean exit_room = true;
//    private boolean home = false;
//    private boolean random_first = true;
//    private boolean watch_video = false;
//    private boolean home_first = false;
//    private boolean[] ScreenCaptureTransition = {false, false, false};
//    private boolean line_interrupted_by_messenger = false;
//    private int appear = 0;
//    private SharedPreferences pref;
//    private int googlebox_count = 0;
//    private int youtube_count = 0;
//    private int agree_interval;
//    public static  Intent intent;
//    private boolean[] state_array = {false, false, false, false, false, false, false, false, false, false};
//    private String date;
//    long lastid;
//    private long messenger_duration = 0;
//
//    FBStateMachine sm = new FBStateMachine("FB");
//    int msg_what = StateDef.CMD_CLOSE_FB;
//    private String tmpComment = "";
//    Random rand = new Random();
//
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            mMainThread.postDelayed(this, 5 * 1000);
//        }
//    };
//
//    @Override
//    public void onAccessibilityEvent(AccessibilityEvent event) {
////        AccessibilityEvent.eventTypeToString(event.getEventType()).contains("CLICK")
////        if(true){
////            AccessibilityNodeInfo nodeInfo = event.getSource();
////            dfs(nodeInfo);
////        }
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        @SuppressLint("HardwareIds")
//        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        Timestamp mTimestamp = Timestamp.now();
//        Map<String, Object> FB_movement = new HashMap<>();
//        FB_movement.put("timestamp", mTimestamp);
//        Date myTimestamp = mTimestamp.toDate();
//        FB_movement.put("device_id", device_id);
//        FB_movement.put("user_id", sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
//
//        FBMovement myFBMovement = new FBMovement();
//        myFBMovement.setKEY_DEVICE_ID(device_id);
//        myFBMovement.setKEY_USER_ID(sharedPrefs.getString(SHARE_PREFERENCE_USER_ID, "尚未設定實驗編號"));
//        myFBMovement.setKEY_TIMESTAMP(mTimestamp.getSeconds());
//
//        //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        //String todaydate = sdf.format(myTimestamp);
//        //String filename = "NewsMomentLog_"+ todaydate+".csv";
//
//        Log.d(TAG, "-------------------------------------------------------------");
//        if(event.getPackageName()!=null && event.getPackageName().toString().equals("com.facebook.katana")) {
//            switch (event.getEventType()) {
//                case AccessibilityEvent.TYPE_VIEW_CLICKED: {
//                    Log.d(TAG, "TYPE_VIEW_CLICKED");
//                    //android內建返回
//                    AccessibilityNodeInfo info = event.getSource();
//                    if ((info != null && info.getContentDescription() != null && info.getContentDescription().toString().equals("返回")) || dfsInclude(info, "返回")) {
//                        msg_what = StateDef.CMD_CLICK_BACK;
//
//                        if (sm.getStateName().equals("OuterState")) {
//                            //CSVHelper.storeToCSV(filename, "TYPE_VIEW_CLICKED", sm.getStateName(), "關閉外部連結", "");
//                            FB_movement.put("event", "TYPE_VIEW_CLICKED");
//                            FB_movement.put("state", sm.getStateName());
//                            FB_movement.put("state_text", "關閉外部連結");
//                            FB_movement.put("content", "");
//                            DocumentReference dc = db.collection("FB_movement").document();
//                            dc.set(FB_movement);
//
//                            myFBMovement.setKEY_DOC_ID(dc.getId());
//                            myFBMovement.setKEY_EVENT("TYPE_VIEW_CLICKED");
//                            myFBMovement.setKEY_STATE(sm.getStateName());
//                            myFBMovement.setKEY_STATE_TEXT("關閉外部連結");
//                            myFBMovement.setKEY_CONTENT("");
//                            FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                            dbHandler.insertFBMovement(myFBMovement);
//                        } else {
//                            //CSVHelper.storeToCSV(filename, "TYPE_VIEW_CLICKED", sm.getStateName(), "返回", "");
//                            FB_movement.put("event", "TYPE_VIEW_CLICKED");
//                            FB_movement.put("state", sm.getStateName());
//                            FB_movement.put("state_text", "返回");
//                            FB_movement.put("content", "");
//                            DocumentReference dc = db.collection("FB_movement").document();
//                            dc.set(FB_movement);
//
//                            myFBMovement.setKEY_DOC_ID(dc.getId());
//                            myFBMovement.setKEY_EVENT("TYPE_VIEW_CLICKED");
//                            myFBMovement.setKEY_STATE(sm.getStateName());
//                            myFBMovement.setKEY_STATE_TEXT("返回");
//                            myFBMovement.setKEY_CONTENT("");
//                            FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                            dbHandler.insertFBMovement(myFBMovement);
//                        }
//                    }
//
//                    if (getEventText(event).equals("關閉")) {
//                        msg_what = StateDef.CMD_CLICK_BACK;
//                        //CSVHelper.storeToCSV(filename, "TYPE_VIEW_CLICKED", sm.getStateName(), "關閉外部連結", "");
//                        FB_movement.put("event", "TYPE_VIEW_CLICKED");
//                        FB_movement.put("state", sm.getStateName());
//                        FB_movement.put("state_text", "關閉外部連結");
//                        FB_movement.put("content", "");
//                        DocumentReference dc = db.collection("FB_movement").document();
//                        dc.set(FB_movement);
//
//                        myFBMovement.setKEY_DOC_ID(dc.getId());
//                        myFBMovement.setKEY_EVENT("TYPE_VIEW_CLICKED");
//                        myFBMovement.setKEY_STATE(sm.getStateName());
//                        myFBMovement.setKEY_STATE_TEXT("關閉外部連結");
//                        myFBMovement.setKEY_CONTENT("");
//                        FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                        dbHandler.insertFBMovement(myFBMovement);
//                    }
//
//                    if (info != null && info.getContentDescription() != null && info.getContentDescription().toString().contains("傳送")) {
//                        //CSVHelper.storeToCSV(filename, "TYPE_VIEW_CLICKED", sm.getStateName(), "發佈留言", tmpComment);
//                        FB_movement.put("event", "TYPE_VIEW_CLICKED");
//                        FB_movement.put("state", sm.getStateName());
//                        FB_movement.put("state_text", "發佈留言");
//                        FB_movement.put("content", tmpComment);
//                        DocumentReference dc = db.collection("FB_movement").document();
//                        dc.set(FB_movement);
//
//                        myFBMovement.setKEY_DOC_ID(dc.getId());
//                        myFBMovement.setKEY_EVENT("TYPE_VIEW_CLICKED");
//                        myFBMovement.setKEY_STATE(sm.getStateName());
//                        myFBMovement.setKEY_STATE_TEXT("發佈留言");
//                        myFBMovement.setKEY_CONTENT(tmpComment);
//                        FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                        dbHandler.insertFBMovement(myFBMovement);
//                        //Log.e("SQLite:", myFBMovement.getKEY_DOC_ID());
//
//                        tmpComment = "";
//                    }
//
//                    if (info != null && info.getContentDescription() != null && info.getContentDescription().toString().equals("發佈") && !sm.getStateName().equals("SharingState")) {
//                        //String text = Readingdfs(info);
//                        //Log.e(TAG, text);
//                        //CSVHelper.storeToCSV(filename, "TYPE_VIEW_CLICKED", sm.getStateName(), "發佈貼文", tmpComment);
//                        FB_movement.put("event", "TYPE_VIEW_CLICKED");
//                        FB_movement.put("state", sm.getStateName());
//                        FB_movement.put("state_text", "發佈貼文");
//                        FB_movement.put("content", tmpComment);
//                        DocumentReference dc = db.collection("FB_movement").document();
//                        dc.set(FB_movement);
//
//                        myFBMovement.setKEY_DOC_ID(dc.getId());
//                        myFBMovement.setKEY_EVENT("TYPE_VIEW_CLICKED");
//                        myFBMovement.setKEY_STATE(sm.getStateName());
//                        myFBMovement.setKEY_STATE_TEXT("發佈貼文");
//                        myFBMovement.setKEY_CONTENT(tmpComment);
//                        FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                        dbHandler.insertFBMovement(myFBMovement);
//
//                        tmpComment = "";
//                    }
//
//                    if (info != null && info.getContentDescription() != null && info.getContentDescription().toString().contains("捨棄")) {
//                        if(sm.getStateName().equals("SharingState")) msg_what = StateDef.CMD_CLICK_BACK;
//                        tmpComment = "";
//                    }
//                    if (info != null && info.getText() !=null && info.getText().toString().equals("分享")) {
//                        msg_what = StateDef.CMD_CLICK_SHARE;
//                    }
//
//                    if (info != null && info.getContentDescription() != null && info.getContentDescription().toString().contains("按鈕，已按下。點按兩下並按住可變更心情。")) {
//                        //CSVHelper.storeToCSV(filename, "TYPE_VIEW_CLICKED", sm.getStateName(), "按讚(心情)", info.getContentDescription().toString());
//                        FB_movement.put("event", "TYPE_VIEW_CLICKED");
//                        FB_movement.put("state", sm.getStateName());
//                        FB_movement.put("state_text", "按讚(心情)");
//                        FB_movement.put("content", info.getContentDescription().toString());
//                        DocumentReference dc = db.collection("FB_movement").document();
//                        dc.set(FB_movement);
//
//                        myFBMovement.setKEY_DOC_ID(dc.getId());
//                        myFBMovement.setKEY_EVENT("TYPE_VIEW_CLICKED");
//                        myFBMovement.setKEY_STATE(sm.getStateName());
//                        myFBMovement.setKEY_STATE_TEXT("按讚(心情)");
//                        myFBMovement.setKEY_CONTENT(info.getContentDescription().toString());
//                        FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                        dbHandler.insertFBMovement(myFBMovement);
//                    }
//
//                    if (info != null && info.getContentDescription() != null && (info.getContentDescription().toString().contains("已分享的連結：") || info.getContentDescription().toString().contains("已分享的連結圖像"))) {
//                        msg_what = StateDef.CMD_CLICK_OUTER;
//                        //CSVHelper.storeToCSV(filename, "TYPE_VIEW_CLICKED", sm.getStateName(), "外部連結", info.getContentDescription().toString());
//                        FB_movement.put("event", "TYPE_VIEW_CLICKED");
//                        FB_movement.put("state", sm.getStateName());
//                        FB_movement.put("state_text", "外部連結");
//                        FB_movement.put("content", info.getContentDescription().toString());
//                        DocumentReference dc = db.collection("FB_movement").document();
//                        dc.set(FB_movement);
//
//                        myFBMovement.setKEY_DOC_ID(dc.getId());
//                        myFBMovement.setKEY_EVENT("TYPE_VIEW_CLICKED");
//                        myFBMovement.setKEY_STATE(sm.getStateName());
//                        myFBMovement.setKEY_STATE_TEXT("外部連結");
//                        myFBMovement.setKEY_CONTENT(info.getContentDescription().toString());
//                        FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                        dbHandler.insertFBMovement(myFBMovement);
//                    }
//
//                    if (info != null) {
//                        String text = Readingdfs(info);
//                        Log.e("CLICK MESSAGE:", text);
//                        //讚按鈕，已按下。點按兩下並按住可變更心情。
//                        //已分享的連結：
//                        //
//                    }
//
//
//                    //configureScrollButton(event.getSource());
//                    //Log.e(TAG,getAllChildNodeText(new AccessibilityNodeInfoCompat(event.getSource())).toString());
//                    break;
//                }
//                case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
//                    Log.d(TAG, "TYPE_VIEW_LONG_CLICKED");
//                    break;
//                case AccessibilityEvent.TYPE_VIEW_FOCUSED: {
//                    Log.d(TAG, "TYPE_VIEW_FOCUSED");
//                    //TYPE_VIEW_FOCUSED+"留言……" = 準備寫留言
//                    //AccessibilityNodeInfo info = event.getSource();
//                    //if (dfsInclude(info, "留言……")) msg_what = StateDef.CMD_TYPING;
//
//                    AccessibilityNodeInfo info = event.getSource();
//                    if (info != null && info.getContentDescription() != null && info.getContentDescription().toString().contains("按鈕，已按下。點按兩下並按住可變更心情。")) {
//                        //CSVHelper.storeToCSV(filename, "TYPE_VIEW_FOCUSED", sm.getStateName(), "按讚(變更心情)", info.getContentDescription().toString());
//                        FB_movement.put("event", "TYPE_VIEW_FOCUSED");
//                        FB_movement.put("state", sm.getStateName());
//                        FB_movement.put("state_text", "按讚(變更心情)");
//                        FB_movement.put("content", info.getContentDescription().toString());
//                        DocumentReference dc = db.collection("FB_movement").document();
//                        dc.set(FB_movement);
//
//                        myFBMovement.setKEY_DOC_ID(dc.getId());
//                        myFBMovement.setKEY_EVENT("TYPE_VIEW_FOCUSED");
//                        myFBMovement.setKEY_STATE(sm.getStateName());
//                        myFBMovement.setKEY_STATE_TEXT("按讚(變更心情)");
//                        myFBMovement.setKEY_CONTENT(info.getContentDescription().toString());
//                        FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                        dbHandler.insertFBMovement(myFBMovement);
//                    }
//                    if (info != null && info.getContentDescription() != null && info.getContentDescription().toString().contains("按鈕。點按兩下並按住即可傳達心情。")) {
//                        //CSVHelper.storeToCSV(filename, "TYPE_VIEW_FOCUSED", sm.getStateName(), "按讚(取消心情)", "");
//                        FB_movement.put("event", "TYPE_VIEW_FOCUSED");
//                        FB_movement.put("state", sm.getStateName());
//                        FB_movement.put("state_text", "按讚(取消心情)");
//                        FB_movement.put("content", "");
//                        DocumentReference dc = db.collection("FB_movement").document();
//                        dc.set(FB_movement);
//
//                        myFBMovement.setKEY_DOC_ID(dc.getId());
//                        myFBMovement.setKEY_EVENT("TYPE_VIEW_FOCUSED");
//                        myFBMovement.setKEY_STATE(sm.getStateName());
//                        myFBMovement.setKEY_STATE_TEXT("按讚(取消心情)");
//                        myFBMovement.setKEY_CONTENT("");
//                        FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                        dbHandler.insertFBMovement(myFBMovement);
//                    }
//
//                    break;
//                }
//                case AccessibilityEvent.TYPE_VIEW_SELECTED:
//                    Log.d(TAG, "TYPE_VIEW_SELECTED");
//                    break;
//                case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED: {
//                    Log.d(TAG, "TYPE_VIEW_TEXT_CHANGED");
//                    msg_what = StateDef.CMD_TYPING;
//                    //AccessibilityNodeInfo info = event.getSource();
//                    tmpComment = getEventText(event);
//                    //if(info!=null && info.getText() !=null)tmpComment = info.getText().toString();
//                    //info.getText()：留言內容
//                    //info.getClassName()：android.widget.EditText
//                    break;
//                }
//                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: {
//                    Log.d(TAG, "TYPE_WINDOW_STATE_CHANGED");
//                    break;
//                }
//                case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED: {
//                    Log.d(TAG, "TYPE_NOTIFICATION_STATE_CHANGED");
//                    AccessibilityNodeInfo info = event.getSource();
//                    //分享
//                    if (getEventText(event).contains("分享了") || getEventText(event).contains("已分享")) {
//                        msg_what = StateDef.CMD_SEND;
//                        if(info != null && info.getContentDescription() != null) {
//                            String text = Readingdfs(info);
//                            Log.e(TAG, text);
//                        }
//
//                        //CSVHelper.storeToCSV(filename, "TYPE_NOTIFICATION_STATE_CHANGED", sm.getStateName(), "分享貼文", tmpComment);
//                        FB_movement.put("event", "TYPE_NOTIFICATION_STATE_CHANGED");
//                        FB_movement.put("state", sm.getStateName());
//                        FB_movement.put("state_text", "分享貼文");
//                        FB_movement.put("content", tmpComment);
//                        DocumentReference dc = db.collection("FB_movement").document();
//                        dc.set(FB_movement);
//
//                        myFBMovement.setKEY_DOC_ID(dc.getId());
//                        myFBMovement.setKEY_EVENT("TYPE_NOTIFICATION_STATE_CHANGED");
//                        myFBMovement.setKEY_STATE(sm.getStateName());
//                        myFBMovement.setKEY_STATE_TEXT("分享貼文");
//                        myFBMovement.setKEY_CONTENT(tmpComment);
//                        FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                        dbHandler.insertFBMovement(myFBMovement);
//
//                        tmpComment = "";
//                    }//留個話吧......
//
//                    break;
//                }
////            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
////                Log.d(TAG, "TYPE_TOUCH_EXPLORATION_GESTURE_START");
////                break;
////            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
////                Log.d(TAG, "TYPE_TOUCH_EXPLORATION_GESTURE_END");
////                break;
////            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
////                Log.d(TAG, "TYPE_VIEW_HOVER_ENTER");
////                break;
////            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
////                Log.d(TAG, "TYPE_VIEW_HOVER_EXIT");
////                break;
//                case AccessibilityEvent.TYPE_VIEW_SCROLLED: {
//                    Log.d(TAG, "TYPE_VIEW_SCROLLED");
//                    msg_what = StateDef.CMD_SCROLL_DOWN;
//                    break;
//                }
//                case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
//                    Log.d(TAG, "TYPE_VIEW_TEXT_SELECTION_CHANGED");
//                    break;
//                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED: {
//                    Log.d(TAG, "TYPE_WINDOW_CONTENT_CHANGED");
//                    //TYPE_WINDOW_CONTENT_CHANGED+"留言……" = 觀看留言
//                    AccessibilityNodeInfo info = event.getSource();
//                    if (dfsInclude(info, "留言……") || dfsInclude(info, "按鈕。點按兩下並按住即可傳達心情。") || dfsInclude(info, "按鈕，已按下。點按兩下並按住可變更心情。")) {
//                        msg_what = StateDef.CMD_READ_COMMENT;
//                        String text = Readingdfs(info);
//                        Log.e(TAG, text);
//                        if (!text.equals("") && !text.equals("「讚」按鈕。點按兩下並按住即可傳達心情。")) {
//                            String[] cutTextEnd = text.split("留言\\.\\.\\.\\.\\.\\.");
//                            String[] cutTextFront = cutTextEnd[0].split("讚;;留言;;分享");
//                            if (cutTextFront.length < 2) {
//                                //CSVHelper.storeToCSV(filename, "TYPE_WINDOW_CONTENT_CHANGED", sm.getStateName(), "閱讀留言", cutTextEnd[0]);
//                                FB_movement.put("event", "TYPE_WINDOW_CONTENT_CHANGED");
//                                FB_movement.put("state", sm.getStateName());
//                                FB_movement.put("state_text", "閱讀留言");
//                                FB_movement.put("content", cutTextEnd[0]);
//                                DocumentReference dc = db.collection("FB_movement").document();
//                                dc.set(FB_movement);
//
//                                myFBMovement.setKEY_DOC_ID(dc.getId());
//                                myFBMovement.setKEY_EVENT("TYPE_WINDOW_CONTENT_CHANGED");
//                                myFBMovement.setKEY_STATE(sm.getStateName());
//                                myFBMovement.setKEY_STATE_TEXT("閱讀留言");
//                                myFBMovement.setKEY_CONTENT(cutTextEnd[0]);
//                                FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                                dbHandler.insertFBMovement(myFBMovement);
//                            } else if (cutTextFront.length > 2) {
//                                //CSVHelper.storeToCSV(filename, "TYPE_WINDOW_CONTENT_CHANGED", sm.getStateName(), "閱讀留言", cutTextFront[2]);
//                                FB_movement.put("event", "TYPE_WINDOW_CONTENT_CHANGED");
//                                FB_movement.put("state", sm.getStateName());
//                                FB_movement.put("state_text", "閱讀留言");
//                                FB_movement.put("content", cutTextFront[2]);
//                                DocumentReference dc = db.collection("FB_movement").document();
//                                dc.set(FB_movement);
//
//                                myFBMovement.setKEY_DOC_ID(dc.getId());
//                                myFBMovement.setKEY_EVENT("TYPE_WINDOW_CONTENT_CHANGED");
//                                myFBMovement.setKEY_STATE(sm.getStateName());
//                                myFBMovement.setKEY_STATE_TEXT("閱讀留言");
//                                myFBMovement.setKEY_CONTENT(cutTextFront[2]);
//                                FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                                dbHandler.insertFBMovement(myFBMovement);
//                            } else {
//                                //CSVHelper.storeToCSV(filename, "TYPE_WINDOW_CONTENT_CHANGED", sm.getStateName(), "閱讀留言", cutTextFront[1]);
//                                FB_movement.put("event", "TYPE_WINDOW_CONTENT_CHANGED");
//                                FB_movement.put("state", sm.getStateName());
//                                FB_movement.put("state_text", "閱讀留言");
//                                FB_movement.put("content", cutTextFront[1]);
//                                DocumentReference dc = db.collection("FB_movement").document();
//                                dc.set(FB_movement);
//
//                                myFBMovement.setKEY_DOC_ID(dc.getId());
//                                myFBMovement.setKEY_EVENT("TYPE_WINDOW_CONTENT_CHANGED");
//                                myFBMovement.setKEY_STATE(sm.getStateName());
//                                myFBMovement.setKEY_STATE_TEXT("閱讀留言");
//                                myFBMovement.setKEY_CONTENT(cutTextFront[1]);
//                                FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                                dbHandler.insertFBMovement(myFBMovement);
//                            }
//                        }
//                    }
//                    //FB左上返回
//                    else if ((info != null && info.getContentDescription() != null && info.getContentDescription().toString().contains("返回")) || dfsInclude(info, "返回")) {
//                        msg_what = StateDef.CMD_CLICK_BACK;
//                        //CSVHelper.storeToCSV(filename, "TYPE_WINDOW_CONTENT_CHANGED", sm.getStateName(), "返回", "");
//                        FB_movement.put("event", "TYPE_WINDOW_CONTENT_CHANGED");
//                        FB_movement.put("state", sm.getStateName());
//                        FB_movement.put("state_text", "返回");
//                        FB_movement.put("content", "");
//                        DocumentReference dc = db.collection("FB_movement").document();
//                        dc.set(FB_movement);
//
//                        myFBMovement.setKEY_DOC_ID(dc.getId());
//                        myFBMovement.setKEY_EVENT("TYPE_WINDOW_CONTENT_CHANGED");
//                        myFBMovement.setKEY_STATE(sm.getStateName());
//                        myFBMovement.setKEY_STATE_TEXT("返回");
//                        myFBMovement.setKEY_CONTENT("");
//                        FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                        dbHandler.insertFBMovement(myFBMovement);
//                    }
//
//                    if (event.getPackageName().toString().equals("com.facebook.katana") && sm.getStateName().equals("ViewingState")) {
//                        String text = Readingdfs(info);
//                        //Log.e(TAG, text);
//                        if (!text.equals("")) {
//                            //CSVHelper.storeToCSV(filename, "TYPE_WINDOW_CONTENT_CHANGED", sm.getStateName(), "瀏覽貼文", text);
//                            FB_movement.put("event", "TYPE_WINDOW_CONTENT_CHANGED");
//                            FB_movement.put("state", sm.getStateName());
//                            FB_movement.put("state_text", "瀏覽貼文");
//                            FB_movement.put("content", text);
//                            DocumentReference dc = db.collection("FB_movement").document();
//                            dc.set(FB_movement);
//
//                            myFBMovement.setKEY_DOC_ID(dc.getId());
//                            myFBMovement.setKEY_EVENT("TYPE_WINDOW_CONTENT_CHANGED");
//                            myFBMovement.setKEY_STATE(sm.getStateName());
//                            myFBMovement.setKEY_STATE_TEXT("瀏覽貼文");
//                            myFBMovement.setKEY_CONTENT(text);
//                            FBMovementDbHelper dbHandler = new FBMovementDbHelper(getApplicationContext());
//                            dbHandler.insertFBMovement(myFBMovement);
//                        }
//                    }
//
//                /*if(dfsInclude(info, "留言……")) {
//                    msg_what = StateDef.CMD_READ_COMMENT;
//                    String text = Readingdfs(info);
//                    Log.e(TAG, text);
//                    if(!text.equals("")) {
//                        String[] cutTextFront = text.split("讚 留言 分享");
//                        String[] cutTextEnd;
//                        if(cutTextFront.length<2) cutTextEnd = text.split("留言\\.\\.\\.\\.\\.\\.");
//                        else cutTextEnd = cutTextFront[1].split("留言\\.\\.\\.\\.\\.\\.");
//                        CSVHelper.storeToCSV(filename, "TYPE_WINDOW_CONTENT_CHANGED", sm.getStateName(), "閱讀留言", cutTextEnd[0]);
//                    }
//                }*/
//
//                    break;
//                }
//                case AccessibilityEvent.TYPE_ANNOUNCEMENT:
//                    Log.d(TAG, "TYPE_ANNOUNCEMENT");
//                    break;
////            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
////                Log.d(TAG, "TYPE_GESTURE_DETECTION_START");
////                break;
////            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
////                Log.d(TAG, "TYPE_GESTURE_DETECTION_END");
////                break;
////            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
////                Log.d(TAG, "TYPE_TOUCH_INTERACTION_START");
////                break;
////            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
////                Log.d(TAG, "TYPE_TOUCH_INTERACTION_END");
////                break;
////            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
////                Log.d(TAG, "TYPE_VIEW_ACCESSIBILITY_FOCUSED");
////                break;
//                case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
//                    Log.d(TAG, "TYPE_WINDOWS_CHANGED");
//                    break;
////            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
////                Log.d(TAG, "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED");
////                break;
//            }
//        }
//
//        //pref.edit().putString("AccessibilityTime", ScheduleAndSampleManager.getCurrentTimeString()).apply(); // 12/14
//
//        AccessibilityNodeInfo nodeInfo = event.getSource();
//        if(nodeInfo != null)
//        {
//            Log.d(TAG, "NodeInfo: " + nodeInfo);
//            Log.d(TAG, "NodeInfo: " + nodeInfo.getContentDescription());
//            //dfs(nodeInfo);
//        }
//        int EventType = event.getEventType();
//        String EventText = getEventText(event);
//        //String EventPackage = event.getPackageName().toString();
//        //Log.d(TAG, "The package name is: " + EventPackage);
//        Log.d(TAG, "The event message is: " + EventText);
//
////        String last_active = getSharedPreferences("test", MODE_PRIVATE)
////                .getString("ForegroundApp", "");
//        //intent = new Intent(getApplicationContext(), ScreenCaptureActivity.class);
//
//        String now_active = printForegroundTask();
//        screen_on = getScreenStatus();
//
//        //Log.d(TAG, "Screen Status:" + screen_on);
//        //boolean screenshot = isMyServiceRunning(com.recoveryrecord.surveyandroid.example.ScreenCapture.class);
//        //String time = pref.getString("agree_dialog_time","0");
//
//        /*if(time != null) {
//            agree_interval = Integer.parseInt(time);
//        }
//        else{
//            agree_interval = 0;
//        }
//        MyEventText = "NA";*/
//
//        if(event.getPackageName() != null) {
//            home = ((getEventText(event).contains("螢幕") && !getEventText(event).contains("全螢幕模式") && !getEventText(event).contains("全螢幕")) || getEventText(event).contains("主畫面") || getEventText(event).matches("第.*頁，共.*頁")
//                    || getEventText(event).contains("預設頁面") || getEventText(event).contains("桌面") || !screen_on);
//            if(event.getPackageName().equals("com.android.systemui"))home = false;
////            Log.d(TAG, "screen on: " + screen_on + " random first: " + random_first);
//            if(!screen_on){
//                home = true;
//                if(random_first) {
//                    /*Random_session_num = (int) (Math.random() * 3 + 1);
//                    CSVHelper.storeToCSV("ESM_random_number.csv", "because screen is off, reset random number to: " + Random_session_num);
//                    Random_session_counter = 0;*/
////                    pref.edit().putLong("Phone_SessionID", -1).apply();
////                    Log.d(TAG, "Phone session id: -1");
//                    random_first = false;
//                }
//            }
//            else{
//                if(!random_first) {
//                    random_first = true;
//                }
//            }
//
//            IsFacebookRunning(EventType, EventText, now_active, intent);
//            //IsFacebookRunning(EventType, EventText, EventPackage, intent, screenshot);
//
//            String extra = "";
//            if(nodeInfo != null)
//            {
//                if(nodeInfo.getContentDescription() != null)
//                {
//                    extra = nodeInfo.getContentDescription().toString();
//                }
//            }
//            //DetectLastForeground(EventType, EventText, EventPackage, extra);
//
//            //SaveLineLastSelected(EventType, EventText, EventPackage, now_active, nodeInfo);
//
//
//            pref.edit()
//                    .putString("ForegroundApp", now_active)
//                    .apply();
//        }
//        if(nodeInfo != null) {
//            nodeInfo.recycle();
//        }
//
//        sm.sendMessage(sm.obtainMessage(msg_what));
//        System.out.println("msg_what:"+msg_what);
//    }
//
//    public boolean dfsInclude(AccessibilityNodeInfo info, String str){
//        if(info == null)
//            return false;
//        if(info.getText() != null && info.getText().length() > 0){
//            if(info.getText().toString().contains(str))
//            {
//                return true;
//            }
//        }
//
//        for(int i=0;i<info.getChildCount();i++){
//            AccessibilityNodeInfo child = info.getChild(i);
//            if(dfsInclude(child, str)) return true;
//            if(child != null){
//                child.recycle();
//            }
//        }
//        return false;
//    }
//
//    public String Readingdfs(AccessibilityNodeInfo info){
//        StringBuilder result = new StringBuilder();
//        if(info == null)
//            return "";
//        //else Log.w(TAG, "Start DFS");
//
//        if(info.getText() != null && info.getText().length() > 0){
//            //Log.d(TAG, "dfs: " + info.getText() + " class: "+info.getClassName());
//            //if(info.getText().toString().equals("「讚」按鈕。點按兩下並按住即可傳達心情。")) result="讚";
//            //else if(info.getText().toString().equals("「回覆」按鈕。點按兩下即可回覆。")) result="回覆";
//            //else
//            result = new StringBuilder(info.getText().toString());
//        }
//
//        for(int i=0;i<info.getChildCount();i++){
//            AccessibilityNodeInfo child = info.getChild(i);
//            result.append(";;").append(Readingdfs(child));
//            if(child != null){
//                child.recycle();
//            }
//        }
//
//        return result.toString();
//    }
//
//    int lastEvent = 0;
//    boolean isTyping = false;
//    int eventCounter = 0;
//    String UserInput = "";
//    int finishCount = 0;
//
//    private void getUserInput(int EventType, String EventText, String EventPackage, AccessibilityEvent e){
////        UserInput = "";
//
//        if(Arrays.asList(AppPack).contains(EventPackage) || Arrays.asList(WebPack).contains(EventPackage) || Arrays.asList(NewsPack).contains(EventPackage)) {
//            Log.d(TAG, "get user input: " + isTyping + " " + eventCounter);
//
//            if (EventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
//                Log.d("Semantic", "Source: " + e.getSource());
//                Log.d("Semantic", "Class Name: " + e.getClassName());
//                Log.d("Semantic", "Event Time: " + e.getEventTime());
//                Log.d("Semantic", "Text: " + e.getText());
//                Log.d("Semantic", "Before Text: " + e.getBeforeText());
//                Log.d("Semantic", "From Index: " + e.getFromIndex());
//                Log.d("Semantic", "Add Count: " + e.getAddedCount());
//                Log.d("Semantic", "Remove Count: " + e.getRemovedCount());
//                StringBuilder sb = new StringBuilder();
//                for (CharSequence s : e.getText()) {
//                    sb.append(s);
//                }
//                EventText =  sb.toString();
//
//                if(e.getBeforeText().equals("")){
//                    Log.d("Semantic", "Store: " + UserInput);
//                    if(!UserInput.equals("")){
//                        //prepareApi();
//                        //analyzeSentiment(UserInput);
//                    }
//                }
//                if(e.getRemovedCount() == 0){
//                    UserInput = EventText;
//                    Log.d("Semantic", "This is input: " + UserInput);
//                }
//                Log.d("Semantic", "----------------------");
//            }
//            lastEvent = EventType;
//        }
//    }
//
//    private void IsFacebookRunning(int EventType, String EventText, String EventPackage, Intent service_intent)
//    {
//        Log.d(TAG, "Cancel: " + Utils.cancel);
//        //Log.d(TAG, "Screen shot: " + screenshot);
//        if (true) { // !screenshot 1/18
//            if ((EventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || EventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
//                    && EventPackage.equals("com.facebook.katana") && EventText.equals("Facebook") && !facebook && !Utils.cancel) {
//                clean_prevApp(state_array, 1);
//                state_array[1] = true;
//                facebook = true;
//                MyEventText = "Facebook is open";
//                Log.d(TAG, "Facebook is open!!");
//                //storeSession("Facebook", "Image");
//                //NewsRelated(true);
//
//                pref.edit().putString("Trigger", "Facebook").apply();
//                //service_intent.putExtra("FromNotification", false);
//                //service_intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//
////                CSVHelper.storeToCSV("AccessibilityDetect.csv", "Send facebook screen shot notification");
//
//                msg_what = StateDef.CMD_OPEN_FB;
//            }
//            else if(!EventPackage.equals("com.facebook.katana"))
//            {
//                System.out.println("EventPackage: "+EventPackage);
//                msg_what = StateDef.CMD_CLOSE_FB;
//            }
//        }
//    }
//
//    private void clean_prevApp(boolean[] now_state, int index){
//        for(int i = 0; i < now_state.length; i++){
////            CSVHelper.storeToCSV("AccessibilityDetect.csv", i + " " + now_state[i]);
//            if(now_state[i]) {
//                switch (i) {
//                    case 1: //Facebook
////                        init_application();
//                        facebook = false;
//                        if(Utils.cancel){
//                            Utils.cancel = false;
////                            CSVHelper.storeToCSV("AccessibilityDetect.csv", "In facebook cleaned, set cancel flag to " + Utils.cancel);
//                        }
//                        else{
//                            intent.putExtra("Facebook", false);
//                            pref.edit().putString("Trigger", "").apply();
//
//                            //stopService(new Intent(this, ScreenCapture.class));
//                        }
////                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "In facebook cleaned, set facebook flag to " + facebook);
//
//                        MyEventText = "Facebook" + " is close";
////                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "Facebook is cleaned");
//                        //notificationListenService.updateRecordingNotification(this, Accessibility_ID, "Facebook", true, intent);
////                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "Send Facebook screen shot update notification");
//
////                        lastid = pref.getLong("SessionID", 0);
////                        db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
//                        Log.d(TAG, "Facebook" + " is close!!");
//                        endSession();
//                        break;
//                    case 5: // youtube
////                        init_application();
//                        youtube = false;
//                        watch_video = false;
//                        if(Utils.cancel){
//                            Utils.cancel = false;
////                            CSVHelper.storeToCSV("AccessibilityDetect.csv", "In youtube cleaned, set cancel flag to " + Utils.cancel);
//                        }
//                        else{
//                            intent.putExtra("Youtube", false);
//                            pref.edit().putString("Trigger", "").apply();
//
//                            //stopService(new Intent(this, ScreenCapture.class));
//                        }
////                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "In youtube cleaned, set youtube flag to " + youtube);
//
//                        MyEventText = "Youtube" + " is close";
////                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "Youtube is cleaned");
//                        //notificationListenService.updateRecordingNotification(this, Accessibility_ID, "Youtube", true, intent);
////                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "Send youtube screen shot update notification");
//                        endSession();
////                        lastid = pref.getLong("SessionID", 0);
////                        db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
//                        Log.d(TAG, "Youtube" + " is close!!");
//                    case 7: //Line Today
//                        Utils.first = true;
//                        line  = false;
////                        init_application();
//                        if(Utils.cancel){
//                            Utils.cancel = false;
////                            CSVHelper.storeToCSV("AccessibilityDetect.csv", "In line today cleaned, set cancel flag to " + Utils.cancel);
//                        }
//                        else{
//                            intent.putExtra("LineToday", false);
//                            pref.edit().putString("Trigger", "").apply();
//
//                            //stopService(new Intent(getApplicationContext(), ScreenCapture.class));
//                        }
////                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "In line today cleaned, set line today flag to " + line);
////                        CSVHelper.storeToCSV("MyDataRecord.csv", "LineToday is close!!");
//
//                        MyEventText = "LineToday" + " is close";
////                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "LineToday is cleaned");
//                        //notificationListenService.updateRecordingNotification(this, Accessibility_ID, "LineToday", true, intent);
////                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "Send line today screen shot update notification");
//                        endSession();
//                        //                        lastid = pref.getLong("SessionID", 0);
////                        db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
//                        Log.d(TAG, "LineToday" + " is close!!");
//                    case 8: // PTT
//                        ptt = false;
////                        init_application();
//                        lastid = pref.getLong("SessionID", 0);
//                        //db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
////                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "PTT is cleaned");
//                        Log.d(TAG, "Ptt is close!!");
//                        MyEventText = "Ptt is close";
////                        CSVHelper.storeToCSV("AccessibilityDetect.csv", "In ptt cleaned, set ptt flag to false");
//                }
//                now_state[i] = false;
//            }
////            CSVHelper.storeToCSV("AccessibilityDetect.csv", i + " " + now_state[i]);
//        }
//    }
//
//    public boolean Dialog_pop(long last, long now){
//        if(now - last < agree_interval*60*60*1000){
//            return false;
//        }
//        else{
//            return true;
//        }
//    }
//
//    @Override
//    protected void onServiceConnected() {
//        super.onServiceConnected();
//        Toast.makeText(getApplicationContext(),"已連接",Toast.LENGTH_SHORT).show();
//
//        pref = getSharedPreferences("test", MODE_PRIVATE);
//        SharedPreferences pref = getSharedPreferences("test", MODE_PRIVATE);
//        pref.edit()
//                .putBoolean("LineToday", false)
//                .apply();
//
//        mPowerManager = (PowerManager)getSystemService(POWER_SERVICE);
//        //db = appDatabase.getDatabase(getApplicationContext());
//        //userRecord = db.userDataRecordDao().getLastRecord();
//        Log.d(TAG, "onServiceConnected");
//
//        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//
////        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED |
////                            AccessibilityEvent.TYPE_WINDOWS_CHANGED |
////                            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED |
////                            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED |
////                            AccessibilityEvent.TYPE_VIEW_CLICKED |
////                            AccessibilityEvent.TYPE_VIEW_SCROLLED |
////                            AccessibilityEvent.TYPE_VIEW_SELECTED |
////                            AccessibilityEvent.TYPE_VIEW_FOCUSED|
////                            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED |
////                            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED |
////                            AccessibilityEvent.TYPE_ANNOUNCEMENT |
////                            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED;
//        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
//        info.flags = AccessibilityServiceInfo.DEFAULT;
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
//        /*mScheduledExecutorService = Executors.newScheduledThreadPool(Constants.MAIN_THREAD_SIZE);
//        mScheduledFuture = mScheduledExecutorService.scheduleAtFixedRate(
//                uploadAppUsageRunnable,
//                Constants.STREAM_UPDATE_DELAY,
//                Constants.STREAM_UPDATE_FREQUENCY,
//                TimeUnit.SECONDS);*/
//        setServiceInfo(info);
//    }
//
//    public MyAccessibilityService() {
//        super();
//        //apptimesStreamGenerator.setLatestInAppAction(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//        //apptimesStreamGenerator.updateStream();
//        Log.d(TAG, "Initial MyAccessibility Service");
//    }
//
//    private String getEventText(AccessibilityEvent event) {
//        StringBuilder sb = new StringBuilder();
//        for (CharSequence s : event.getText()) {
//            sb.append(s);
//        }
//        return sb.toString();
//    }
//
//    private String printForegroundTask() {
//        String currentApp = "NULL";
//        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
//            long time = System.currentTimeMillis();
//            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
//            if (appList != null && appList.size() > 0) {
//                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
//                for (UsageStats usageStats : appList) {
//                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
//                }
//                if (mySortedMap != null && !mySortedMap.isEmpty()) {
//                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
//                }
//            }
//        } else {
//            ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
//            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
//            currentApp = tasks.get(0).processName;
//        }
//        Log.e(TAG, "Current App in foreground is: " + currentApp);
//        return currentApp;
//    }
//
//    @Override
//    public void onInterrupt() {
//        Log.d(TAG, "onInterrupt");
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        sm.quit();
//        //CSVHelper.storeToCSV("TestService.csv", "Accessibility service onDestroy");
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        /*if (mAccessTokenLoader != null) {
//            mAccessTokenLoader.unregisterListener(mLoaderListener);
//            mAccessTokenLoader.cancelLoad();
//            mAccessTokenLoader.stopLoading();
//        }*/
//        return super.onUnbind(intent);
//    }
//
//    public static void init_application()
//    {
//        facebook = false;
//        youtube = false;
//    }
//
//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private void DetectLastForeground(int EventType, String EventText, String EventPackage, String extra) {
//        //Fixed: Window State Changed from the same application (showing keyboard within an app) should be ignored
//        //boolean same_app = false;
//
//        //String ForegroundNow = printForegroundTask();
//        String Deviceid = getSharedPreferences("test", MODE_PRIVATE).getString("UserID", "");
////        String text = "";
////        String type = "";
////        String packageName = "";
//        String Eventtype = EventTypeIwant(EventType);
////        if(Eventtype.equals("TYPE_VIEW_SCROLLED") && event_scroll){
////            Eventtype = "NA";
////        }
////        if(Eventtype.equals("TYPE_VIEW_SCROLLED") && !event_scroll){
////            event_scroll = true;
////        }
//        if (!Eventtype.equals("NA") || !MyEventText.equals("NA")) {
////            if(!Eventtype.equals("TYPE_VIEW_SCROLLED"))
////            {
////                event_scroll = false;
////            }
////            type = Eventtype;
////            text = getEventText(event);
////            packageName = event.getPackageName().toString();
////            if (event.getContentDescription() != null) {
////                extra = event.getContentDescription().toString();
////                // Log.d(TAG,"extra : "+ extra);
////            }
//
//            /*PackageInfo pkgInfo;
//            try {
//                pkgInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
//            } catch (PackageManager.NameNotFoundException | NullPointerException | Resources.NotFoundException e) {
//                pkgInfo = null;
//            }*/
//
//            if(isOurTarget(EventPackage))
//            {
////                JSONObject jobject = new JSONObject();
//                try {
////                    JSONObject appobject = new JSONObject();
////                    appobject.put(TIMESTAMP, System.currentTimeMillis());
////                    appobject.put(READABLE, getReadableTimeLong(System.currentTimeMillis()));
////                    appobject.put(DEVICEID, Deviceid);
////                    appobject.put(PACKAGENAME, packageName);
////                    appobject.put("MyEventText", MyEventText);
////                    appobject.put(EVENTTEXT, text);
////                    appobject.put(EVENTTYPE, type);
////                    appobject.put(EXTRA, extra);
////                    jobject.put("myAccessibility", appobject.toString());
//                    /*if (accessibilityStreamGenerator != null) {
//                        Log.d(TAG, "accessibilityStreamGenerator not null");
//                        int index = Arrays.asList(NewsPack).indexOf(EventPackage);
//                        String NewsApp_Name;
//                        if(index >= 0){
//                            NewsApp_Name = NewsName[index];
//                        }
//                        else{
//                            NewsApp_Name = "";
//                        }
//                        Log.d(TAG, "News name: " + NewsApp_Name);
//                        accessibilityStreamGenerator.setLatestInAppAction(System.currentTimeMillis(), Deviceid, EventPackage, EventText, Eventtype, MyEventText, extra, NewsApp_Name);
//                        accessibilityStreamGenerator.updateStream();
//                    }*/
////                    CSVHelper.storeAccessibilityCSV("CheckStoreAccess.csv", packageName, text, type, extra);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public boolean isOurTarget(String pkgName){
//        if(Arrays.asList(AppPack).contains(pkgName) || Arrays.asList(WebPack).contains(pkgName)
//                || Arrays.asList(NewsPack).contains(pkgName) || Arrays.asList(PttPack).contains(pkgName)){
//            return true;
//        }
//        return false;
//    }
//
//    public static boolean isSystemPackage(PackageInfo pkgInfo) {
//        return pkgInfo != null && ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1);
//    }
//
//    public static String EventTypeIwant(int EventType) {
//        String type = "NA";
//        switch (EventType) {
//            case AccessibilityEvent.TYPE_VIEW_CLICKED:
//                type = "TYPE_VIEW_CLICKED";
//                break;
//            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
//                type = "TYPE_VIEW_LONG_CLICKED";
//                break;
//            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
//                type = "TYPE_VIEW_FOCUSED";
//                break;
//            case AccessibilityEvent.TYPE_VIEW_SELECTED:
//                type = "TYPE_VIEW_SELECTED";
//                break;
//            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
//                type = "TYPE_VIEW_TEXT_CHANGED";
//                break;
//            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
//                type = "TYPE_NOTIFICATION_STATE_CHANGED";
//                break;
//            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
//                type = "TYPE_ANNOUNCEMENT";
//                break;
//            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
//                type = "TYPE_VIEW_SCROLLED";
//                break;
//            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
//                type = "TYPE_WINDOWS_CHANGED";
//                break;
//            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                type = "TYPE_WINDOW_STATE_CHANGED";
//                break;
////            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
////                type = "YPE_WINDOW_CONTENT_CHANGED";
////                break;
//        }
//        return type;
//    }
//
//    public boolean getScreenStatus() {
//        boolean screenOn = false;
//        //use isInteractive after api 20
//        if (mPowerManager.isInteractive()) screenOn = true;
//        else screenOn = false;
//
//        return screenOn;
//    }
//
//    public void endSession(){
//        lastid = pref.getLong("SessionID", 0);
//        //db.SessionDataRecordDao().updateSession(lastid, ScheduleAndSampleManager.getCurrentTimeString(), System.currentTimeMillis());
//    }
//}
