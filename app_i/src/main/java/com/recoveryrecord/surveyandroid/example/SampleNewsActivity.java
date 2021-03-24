package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.icu.lang.UCharacter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static java.security.AccessController.getContext;
//import android.support.v7.widget.Toolbar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SampleNewsActivity extends AppCompatActivity implements MySimpleGestureListener.SimpleGestureListener {
    //    String TagCycle = "my activity cycle";
//    String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

    volatile boolean activityStopped = false;
    volatile boolean activityEnd = false;
    boolean share_clicked = false;
    boolean document_create = false;
    long in_time = System.currentTimeMillis();
    private ScreenStateReceiver mReceiver;//screen on or off
    boolean first_in = true;

    String time_ss = "";//time series
    String tmp_record = "";//viewport
    String news_id = "";
    String media_name = "";
    private String mUrl, mImg, mTitle, mDate, mSource;

    private static final String DEBUG_TAG = "Gestures";
    private MySimpleGestureListener detector;
    List<DragObj> dragObjArrayListArray = new ArrayList<>();//drag gesture

    ReadingBehavior myReadingBehavior = new ReadingBehavior();//sqlite
    ReadingBehaviorDbHelper dbHandler;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("log: activity cycle", "On create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_news);
        //open database ############################################################################
        dbHandler = new ReadingBehaviorDbHelper(SampleNewsActivity.this);
        //check trigger from #######################################################################
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            myReadingBehavior.setKEY_TRIGGER_BY(b.getString("trigger_from"));
            if (b.getString("news_id")!= null){
                news_id = b.getString("news_id");
            }
            if (b.getString("media_name")!= null){
                media_name = b.getString("media_name");
            }

        }
        Log.d("log: trigger_by", myReadingBehavior.getKEY_TRIGGER_BY());
        Log.d("log: news_id", news_id);
        Log.d("log: media_name", media_name);
        switch (media_name) {
            case "中央社":
                media_name = "cna";
                break;
            case "中時":
                media_name = "chinatimes";
                break;
            case "華視":
                media_name = "cts";
                break;
            case "東森":
                media_name = "ebc";
                break;
            case "自由時報":
                media_name = "ltn";
                break;
            case "風傳媒":
                media_name = "storm";
                break;
            case "聯合":
                media_name = "udn";
                break;
            case "ettoday":
                media_name = "ettoday";
                break;
            default:
                media_name = "";
                break;

        }
        Log.d("log: media_name", media_name);
        //set time in ##############################################################################
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String time_now = formatter.format(date);
        myReadingBehavior.setKEY_TIME_IN(time_now);
        Log.d("log: time_in", myReadingBehavior.getKEY_TIME_IN());
        //set gesture listener #####################################################################
        detector = new MySimpleGestureListener(this,this);
        //check screen on or off ###################################################################
        //screen off #########################
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenStateReceiver();
        registerReceiver(mReceiver, intentFilter);
        //screen size ##############################################################################
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        final float dpWidth = outMetrics.widthPixels / density;
        myReadingBehavior.setKEY_DISPLAY_HEIGHT(String.valueOf(dpHeight));
        myReadingBehavior.setKEY_DISPLAY_WIDTH(String.valueOf(dpWidth));
        Log.d("log: display_width_dp", myReadingBehavior.getKEY_DISPLAY_WIDTH());
        Log.d("log: display_height_dp", myReadingBehavior.getKEY_DISPLAY_HEIGHT());
        //whether is chinese #######################################################################
        final Set<UCharacter.UnicodeBlock> chineseUnicodeBlocks = new HashSet<UCharacter.UnicodeBlock>() {{
            add(UCharacter.UnicodeBlock.CJK_COMPATIBILITY);
            add(UCharacter.UnicodeBlock.CJK_COMPATIBILITY_FORMS);
            add(UCharacter.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
            add(UCharacter.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
            add(UCharacter.UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
            add(UCharacter.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
            add(UCharacter.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
            add(UCharacter.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
            add(UCharacter.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
            add(UCharacter.UnicodeBlock.KANGXI_RADICALS);
            add(UCharacter.UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS);
        }};
        //Scrollview has lost the acceleration######################################################
//        ScrollView mScrollView = findViewById(R.id.scroll_view);
//        mScrollView.setNestedScrollingEnabled(false);
        //news generate from server ################################################################
        Random rand = new Random();
        int random_news = ThreadLocalRandom.current().nextInt(1, 3 + 1);
        String doc_id = "";
//        Log.d("log: firebase news", String.valueOf(random_news));
        List<String> list = Arrays.asList("0000e3633ce3f3b0241d69749fc749f0", "0011f17045e0d4f40cc314f27ac91228", "001b575e65a5dd618051065f43b79974", "0030b7b0dada6069a76fb087f631bbb1", "003436a77eccd9d8f0cc9ffbced6844b");
        doc_id = list.get(rand.nextInt(list.size()));
        Log.d("log: firebase news", doc_id);
//        CollectionReference folder = db.collection("medias").document("chinatimes").collection("news");
//        folder.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    List<String> list = new ArrayList<>();
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        list.add(document.getId());
//                    }
//                    Random rand = new Random();
//                    doc_id[0] = list.get(rand.nextInt(list.size()));
//                    Log.d("log: firebase news", String.valueOf(doc_id));
//                    Log.d("log: firebase folder", list.toString());
//                } else {
//                    Log.d("log: firebase folder", String.valueOf(task.getException()));
//                }
//            }
//        });
        DocumentReference docRef;
        if (news_id=="" || media_name ==""){
            docRef = db.collection("medias").document("chinatimes").collection("news").document(doc_id);
        } else {
            docRef = db.collection("medias").document(media_name).collection("news").document(news_id);
        }
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("log: firebase", "Success");
//                        Log.d("log: firebase", "DocumentSnapshot data: " + document.getData());
                        mUrl = document.getString("url");
                        mImg = "";
//                        mImg = "https://cc.tvbs.com.tw/img/upload/2021/02/05/20210205183845-85cd46f0.jpg";
                        mTitle = document.getString("title");
                        mDate = document.getString("pubdate");
                        mSource = document.getString("media");
//                        mAuthor = "孟心怡";
                        myReadingBehavior.setKEY_NEWS_ID(document.getString("id"));
                        ArrayList<String> c_list = (ArrayList<String>) document.get("content");
//                        for (int i = 0; i < c_list.size(); i++) {
//                            Log.d("log: firebase", "DocumentSnapshot content: " + c_list.get(i));
//                        }
                        List<String> divList = new ArrayList<>();
//                        int cut_size = (int) (dpWidth / 26);
                        int cut_size = (int) (dpWidth / 24);
                        myReadingBehavior.setKEY_BYTE_PER_LINE(cut_size*2);
                        for (int i = 0; i < c_list.size(); i++) {
                            String str = c_list.get(i);
                            int remainder = (str.length()) % cut_size;
                            int str_length_byte = str.length();
                            for (char c : str.toCharArray()) {
                                if (chineseUnicodeBlocks.contains(UCharacter.UnicodeBlock.of(c))) {
                                    //System.out.println(c + " is chinese");
                                    str_length_byte += 1;
                                }
                            }
                            // 一共要分割成幾段
                            int front = 0, end = 0, iter_char = 0, line_count = 1;
                            char[] c = str.toCharArray();
                            while (true) {
                                //System.out.println("line  "+line_count);
                                line_count++;
                                //one paragraph
                                //end = front; // index for word
                                int count_byte = 0;
                                //System.out.print(c[i]);
                                while (count_byte < cut_size * 2) {
                                    //one sentence at most 40 bytes
                                    if (c[iter_char] == '\n') {
                                        break;
                                    }
                                    //System.out.println(iter_char);
                                    //System.out.println(c[iter_char]);
                                    if (isChineseChar(c[iter_char])) {
                                        //System.out.println(c[iter_char] + " is chinese");
                                        count_byte += 2;
                                    } else if ((c[iter_char] >= 'a' && c[iter_char] <= 'z') || (c[iter_char] >= 'A' && c[iter_char] <= 'Z')) {
                                        //english letter
                                        //check word
                                        int word_length = 0, word_index, tmp_count_byte = count_byte;
                                        for (word_index = iter_char; word_index + 1 <= str.length(); word_index++) {
                                            if ((c[word_index] >= 'a' && c[word_index] <= 'z') || (c[word_index] >= 'A' && c[word_index] <= 'Z' ) || c[word_index] == '-') {
                                                word_length += 1;
                                                tmp_count_byte += 1;
                                            } else {
                                                break;
                                            }
                                        }
                                        word_index -= 1;
                                        if (tmp_count_byte < cut_size * 2) {
                                            iter_char = word_index;
                                            count_byte = tmp_count_byte;
                                        } else {
                                            iter_char -= 1;
                                            break;
                                        }
//                        count_byte+=1;
                                    } else {
//                        System.out.println(c[iter_char] + " is not chinese");
                                        count_byte += 1;
                                    }
                                    if (iter_char + 1 < str.length()) {
                                        iter_char += 1;//c[iter_char]
                                    } else {
                                        break;
                                    }
                                }
                                String childStr = str.substring(front, iter_char);
                                divList.add(childStr);
                                front = iter_char;
                                if (iter_char + 1 == str.length()) {
                                    break;
                                }
                            }
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(40, 10, 40, 10);
                        //set viewport number ######################################################
                        int textview_num = divList.size();
                        myReadingBehavior.setKEY_VIEW_PORT_NUM(textview_num);
                        Log.d("log: view_port_num", String.valueOf(myReadingBehavior.getKEY_VIEW_PORT_NUM()));
                        //put textview into layout #################################################
                        //int text_size = (int) (dpWidth /30);
                        int text_size = 20;
                        final TextView myTextViewsTitle = new TextView(SampleNewsActivity.this);
                        final TextView myTextViewsDate = new TextView(SampleNewsActivity.this);
                        final TextView myTextViewsSrc = new TextView(SampleNewsActivity.this);
                        myTextViewsTitle.setText(mTitle);
                        myTextViewsDate.setText(mDate);
                        myTextViewsSrc.setText(mSource);
                        myTextViewsTitle.setTextColor(Color.parseColor("black"));
                        myTextViewsDate.setTextColor(Color.parseColor("black"));
                        myTextViewsSrc.setTextColor(Color.parseColor("black"));
                        myTextViewsTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
                        myTextViewsDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        myTextViewsSrc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        myTextViewsTitle.setGravity(Gravity.LEFT);
                        myTextViewsDate.setGravity(Gravity.LEFT);
                        myTextViewsSrc.setGravity(Gravity.LEFT);
                        myTextViewsTitle.setLayoutParams(params);
                        myTextViewsDate.setLayoutParams(params);
                        myTextViewsSrc.setLayoutParams(params);
                        ((LinearLayout) findViewById(R.id.layout_inside)).addView(myTextViewsTitle);
                        ((LinearLayout) findViewById(R.id.layout_inside)).addView(myTextViewsDate);
                        ((LinearLayout) findViewById(R.id.layout_inside)).addView(myTextViewsSrc);
                        if(mImg!=""){
                            ImageView imageView = new ImageView(SampleNewsActivity.this);
                            new DownloadImageTask(imageView).execute(mImg);
                            ((LinearLayout) findViewById(R.id.layout_inside)).addView(imageView);
                        }
                        final TextView[] myTextViews = new TextView[textview_num]; // create an empty array;
                        for (int i = 0; i < divList.size(); i++) {
                            final TextView rowTextView = new TextView(SampleNewsActivity.this);
                            String tmp = divList.get(i);
                            rowTextView.setText(tmp);
                            rowTextView.setTextColor(Color.parseColor("black"));
                            rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, text_size);
//                            rowTextView.setGravity(Gravity.LEFT);
                            rowTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            rowTextView.setSingleLine(true);
                            rowTextView.setLayoutParams(params);
                            //rowTextView.setBackgroundColor(0xFFFFFF99);
                            ((LinearLayout) findViewById(R.id.layout_inside)).addView(rowTextView);
                            myTextViews[i] = rowTextView;
//                            Rect bounds = new Rect();
//                            myTextViews[i].getPaint().getTextBounds(myTextViews[i].getText().toString(), 0, myTextViews[i].getText().length(), bounds);
//                            myTextViews[i].measure(0, 0);
//                            int h_dp_unit = pxToDp(myTextViews[i].getMeasuredHeight(), myTextViews[i].getContext());
//                            int w_dp_unit = pxToDp(myTextViews[i].getMeasuredWidth(), myTextViews[i].getContext());
//                            Log.d("log: textview h", String.valueOf(h_dp_unit));
//                            Log.d("log: textview w", String.valueOf(w_dp_unit));
                        }
                        myTextViews[0].measure(0, 0);
                        int h_dp_unit = pxToDp(myTextViews[0].getMeasuredHeight(), myTextViews[0].getContext());
                        myReadingBehavior.setKEY_ROW_SPACING(h_dp_unit);
                        final LinearLayout content_view = findViewById(R.id.layout_inside);
                        ViewTreeObserver viewTreeObserver = content_view.getViewTreeObserver();
                        if (viewTreeObserver.isAlive()) {
                            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onGlobalLayout() {
                                    content_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    int viewWidth = content_view.getWidth();
                                    int viewHeight = content_view.getHeight();
                                    int dp_unit = pxToDp(viewHeight, content_view.getContext());
                                    myReadingBehavior.setKEY_CONTENT_LENGTH(String.valueOf(dp_unit));
                                    Log.d("log: content_length_dp", myReadingBehavior.getKEY_CONTENT_LENGTH());
                                }
                            });
                        }
                        //create document
//                        addReadingBehavior();
                        final int N = textview_num;
                        class SquareCalculator {
                            private ExecutorService executor = Executors.newFixedThreadPool(1);
                            //...
                            //private ExecutorService executor = Executors.newSingleThreadExecutor();

                            public Future<Integer> calculate(final Integer input) {
                                return executor.submit(new Callable<Integer>() {
                                    @Override
                                    public Integer call() throws Exception {
                                        Thread.sleep(1000);
                                        return input * input;
                                    }
                                });
                            }

                            public Future<Integer> no_cal(final int input, final int start_count) {
                                return executor.submit(new Callable<Integer>() {
                                    float count_running = 0;
                                    int[] count = new int[N];
                                    boolean[] old_flag = new boolean[N];
                                    boolean[] new_flag = new boolean[N];

                                    @Override
                                    public Integer call() throws Exception {
                                        Arrays.fill(old_flag, Boolean.FALSE);
                                        Arrays.fill(new_flag, Boolean.FALSE);
                                        Arrays.fill(count, 0);
                                        Log.d("log: MyScrollView", "Start");
//                                        myReadingBehavior.setKEY_TIME_ON_PAGE(count_running / 10);
                                        while (!activityEnd) {
//                                            Log.d("log: time_on_page", String.valueOf(myReadingBehavior.getKEY_TIME_ON_PAGE()));
                                            if (activityStopped && !activityEnd) {
                                                Log.d("log: MyScrollView", "Stop");
                                                tmp_record = "";
                                                for (int i = 0; i < N; i++) {
//                                    Log.d("log: MyScrollView", i + " count: " + count[i] / 10);
//                                                    tmp_record+=i+1 + ": " + count[i] / 10 + "\n";
                                                    tmp_record+=count[i] / 10 + "#";
                                                }
                                                myReadingBehavior.setKEY_VIEW_PORT_RECORD(tmp_record);
                                                Log.d("log: view_port_record", myReadingBehavior.getKEY_VIEW_PORT_RECORD());
//                                                myReadingBehavior.setKEY_TIME_ON_PAGE(count_running / 10);
//                                                Log.d("log: time_on_page", String.valueOf(myReadingBehavior.getKEY_TIME_ON_PAGE()));
                                                while (activityStopped) {
                                                    Thread.sleep(100);
                                                }
                                                Log.d("log: MyScrollView", "Restart");
                                            }
                                            Rect scrollBounds = new Rect();
//                        mScrollView.getHitRect(scrollBounds);
                                            int first_view = -1, last_view = -1;
                                            for (int i = 0; i < N; i++) {
                                                if (!myTextViews[i].getLocalVisibleRect(scrollBounds)) {
                                                    new_flag[i] = false;
//                                Log.d(TAG, i + " false");
                                                } else {
                                                    new_flag[i] = true;
                                                    if(first_view==-1){
                                                        first_view = i;
                                                    } else {
                                                        last_view = i;
                                                    }
//                                Log.d(TAG, i + " true");
                                                }

                                                if (old_flag[i] && new_flag[i]) {
                                                    //                            Log.d(TAG, "still visible "+ block + " count: " + count);
                                                    count[i]++;
                                                } else if (old_flag[i] && !new_flag[i]) {
                                                    //                            Log.d(TAG, "no longer visible "+ block + " count: " + count);
                                                    old_flag[i] = new_flag[i];
                                                } else if (!old_flag[i] && new_flag[i]) {
                                                    //                            Log.d(TAG, "start visible "+ block +" count: " + count);
                                                    count[i]++;
                                                    old_flag[i] = new_flag[i];
                                                } else {
                                                    //                            Log.d(TAG, "still not visible "+ block + " count: " + count);
                                                }
//                            Log.d(TAG, i + " count: " + count[i]);
                                            }
                                            Thread.sleep(100);
                                            count_running++;
                                            float temp = count_running/10;
//                                            String output_string = temp + " top: " + (first_view+1) + " bottom: " + (last_view+1) + "\n";
                                            String output_string = temp + "," + (first_view+1) + "," + (last_view+1) + "#";
                                            time_ss+=output_string;
                                            tmp_record = "";
                                            for (int i = 0; i < N; i++) {
//                                                tmp_record+=i+1 + ": " + count[i] / 10 + "\n";
                                                tmp_record+=count[i] / 10 + "#";
                                            }
                                            myReadingBehavior.setKEY_VIEW_PORT_RECORD(tmp_record);
                                        }
                                        Log.d("log: MyScrollView", "Finish");
                                        String finish_record = "";
                                        for (int i = 0; i < N; i++) {
//                            Log.d("log: MyScrollView", i + " count: " + count[i] / 10);
                                            finish_record+=i+1 + ": " + count[i] / 10 + "\n";
                                            finish_record+=count[i] / 10 + "#";
                                        }
                                        myReadingBehavior.setKEY_VIEW_PORT_RECORD(finish_record);
                                        Log.d("log: view_port_record", myReadingBehavior.getKEY_VIEW_PORT_RECORD());
//                                        myReadingBehavior.setKEY_TIME_ON_PAGE(count_running / 10);
//                                        Log.d("log: time_on_page", String.valueOf(myReadingBehavior.getKEY_TIME_ON_PAGE()));
//                        Log.d("log: MyScrollView", "time_on_page: " + count_running / 10);
                                        return 1;
                                    }
                                });
                            }
                        }
                        SquareCalculator squareCalculator = new SquareCalculator();
                        Future<Integer> future1 = squareCalculator.no_cal(1, 0);
                    } else {
                        Log.d("log: firebase", "No such document");
                    }
                } else {
                    Log.d("log: firebase", "get failed with ", task.getException());
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (first_in){
            addReadingBehavior();
            first_in = false;
        }
        Log.d("log: activity cycle", "On start");
        activityStopped = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("log: activity cycle", "On resume");
        activityStopped = false;
        //isScreenOn(R.layout.activity_news_detail);
        in_time = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("log: activity cycle", "On pause");
        activityStopped = true;
        myReadingBehavior.setKEY_PAUSE_ON_PAGE(myReadingBehavior.getKEY_PAUSE_ON_PAGE()+1);
        long tmp = myReadingBehavior.getKEY_TIME_ON_PAGE() + (System.currentTimeMillis()-in_time)/1000;
        myReadingBehavior.setKEY_TIME_ON_PAGE(tmp);
//        Log.d("log: pause count", String.valueOf(myReadingBehavior.getKEY_PAUSE_ON_PAGE()));
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        myReadingBehavior.setKEY_TIME_OUT(formatter.format(date));
//        Log.d("log: time_out", myReadingBehavior.getKEY_TIME_OUT());

        myReadingBehavior.setKEY_TIME_SERIES(time_ss);
        Log.d("log: time_series", myReadingBehavior.getKEY_TIME_SERIES());

        String drag_str = "";
        int drag_count = 0;
        float drag_x_1 = 0;
        float drag_y_1 = 0;
        float drag_x_2 = 0;
        float drag_y_2 = 0;
        long time_one = 0, time_two = 0, tmp_long = 0;
        double duration = 0;

        for(int iter = 0; iter < dragObjArrayListArray.size(); iter++){
            if (drag_x_1==0 && drag_y_1==0){
                time_one = dragObjArrayListArray.get(iter).getTIME_ONE();
                drag_x_1 = dragObjArrayListArray.get(iter).getPOINT_ONE_X();
                drag_y_1 = dragObjArrayListArray.get(iter).getPOINT_ONE_Y();
                drag_x_2 = dragObjArrayListArray.get(iter).getPOINT_TWO_X();
                drag_y_2 = dragObjArrayListArray.get(iter).getPOINT_TWO_Y();
            } else if (drag_x_1==dragObjArrayListArray.get(iter).getPOINT_ONE_X() && drag_y_1==dragObjArrayListArray.get(iter).getPOINT_ONE_Y()){
                time_two = dragObjArrayListArray.get(iter).getTIME_ONE();
                drag_x_2 = dragObjArrayListArray.get(iter).getPOINT_TWO_X();
                drag_y_2 = dragObjArrayListArray.get(iter).getPOINT_TWO_Y();
            } else {
                //find end
//                duration = (time_two-time_one)/1000;
                drag_count+=1;
                if(time_one!=time_two){
                    tmp_long = time_two-time_one;
                    duration = (double)tmp_long/1000;
                } else {
                    duration = 0;
                }

                drag_str+=duration + "/";
                drag_str+="(" + drag_x_1 + "," + drag_y_1 + ")/";
                drag_str+="(" + drag_x_2 + "," + drag_y_2 + ")/";
                String direction = "";
                direction += drag_y_1 < drag_y_2 ? "N" : drag_y_1 > drag_y_2 ? "S" : "";
                direction += drag_x_1 < drag_x_2 ? "E" : drag_x_1 > drag_x_2 ? "W" : "";
                drag_str+=direction + "#";

                drag_x_1 = 0;
                drag_y_1 = 0;
                drag_x_2 = 0;
                drag_y_2 = 0;
                duration = 0;
            }
        }
        //last drag
        if ((drag_x_1+drag_y_1+drag_x_2+drag_y_2)==0){
            //end at final else
//            Log.d("log: drag_str", "123");
        } else {
            drag_count+=1;
            if(time_one!=time_two){
                tmp_long = time_two-time_one;
                duration = (double) tmp_long/1000;
            } else {
                duration = 0;
            }
            drag_str+=duration + "/";
            drag_str+="(" + drag_x_1 + "," + drag_y_1 + ")/";
            drag_str+="(" + drag_x_2 + "," + drag_y_2 + ")/";
            String direction = "";
            direction += drag_y_1 < drag_y_2 ? "N" : drag_y_1 > drag_y_2 ? "S" : "";
            direction += drag_x_1 < drag_x_2 ? "E" : drag_x_1 > drag_x_2 ? "W" : "";
            drag_str+=direction + "#";
        }
        myReadingBehavior.setKEY_DRAG_NUM(drag_count);
        myReadingBehavior.setKEY_DRAG_RECORD(drag_str);
        updateReadingBehavior();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("log: activity cycle", "On stop");
        activityStopped = true;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("log: activity cycle", "On restart");
        activityStopped = false;
//        in_time = System.currentTimeMillis();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("log: activity cycle", "On destroy");
        activityEnd = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    public int pxToDp(int px, Context tmp) {
        DisplayMetrics displayMetrics = tmp.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    private boolean isChineseChar(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ) {
            return true;
        }
        return false;
    }

    //image download ###############################################################################
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample_news, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if  (id == R.id.share){
            Toast.makeText(this, "share is being clicked", Toast.LENGTH_LONG).show();
            String share_field = "";
//            final DocumentReference rbRef = db.collection(Build.ID).document(String.valueOf(l_date)).collection("reading_behaviors").document(myReadingBehavior.getKEY_TIME_IN());
            String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            final DocumentReference rbRef = db.collection("test_users").document(device_id).collection("reading_behaviors").document(myReadingBehavior.getKEY_TIME_IN());
            if(share_clicked){
//                Date date = new Date(System.currentTimeMillis());
//                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//                String time_now = formatter.format(date);
//                share_field = time_now;
//                rbRef.update("share", FieldValue.arrayUnion(share_field));
                rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("log: firebase", "Success");
                                List<String> share_result = (List<String>) document.get("share");
                                share_result.add("none");
//                                share_result.set(0,"none");
                                rbRef.update("share", share_result)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("log: firebase", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("log: firebase", "Error updating document", e);
                                            }
                                        });
                            } else {
                                Log.d("log: firebase", "No such document");
                            }
                        } else {
                            Log.d("log: firebase", "get failed with ", task.getException());
                        }
                    }
                });

            } else {
                //first time
                share_clicked = true;

                rbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("log: firebase", "Success");
                                List<String> share_result = (List<String>) document.get("share");
                                share_result.set(0,"none");
                                rbRef.update("share", share_result)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("log: firebase", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("log: firebase", "Error updating document", e);
                                            }
                                        });
                            } else {
                                Log.d("log: firebase", "No such document");
                            }
                        } else {
                            Log.d("log: firebase", "get failed with ", task.getException());
                        }
                    }
                });
            }
            try{
                String url = mUrl;
//                Toast.makeText(this, "share is being clicked", Toast.LENGTH_LONG).show();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,url); // your above url

                Intent receiver = new Intent(this, ApplicationSelectorReceiver.class);
//                String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                receiver.putExtra("device_id", device_id);
                receiver.putExtra("doc_time", myReadingBehavior.getKEY_TIME_IN());
                receiver.putExtra("doc_date", String.valueOf(l_date));
                receiver.putExtra("share_field", share_field);
//                receiver.putExtra("sh", String.valueOf(l_date));
//                receiver.putExtra("share_via", "none");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
                Intent chooser = Intent.createChooser(shareIntent, "Share via...", pendingIntent.getIntentSender());
//                Log.d("log: share via", String.valueOf(pendingIntent.getIntentSender()));
                startActivity(chooser);

            }catch (Exception e){
                Toast.makeText(this, "Hmm.. Sorry, \nCannot be share", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction, FlingObj flingObj) {
        myReadingBehavior.setKEY_FLING_NUM(myReadingBehavior.getKEY_FLING_NUM()+1);
        flingObj.setFLING_ID(myReadingBehavior.getKEY_FLING_NUM());
        String str_fling = myReadingBehavior.getKEY_FLING_RECORD();
//        str_fling+="fling num:" + flingObj.getFLING_ID() + "/";
        str_fling+="(" + flingObj.getPOINT_ONE_X() + "," + flingObj.getPOINT_ONE_Y() + ")/";
        str_fling+="(" + flingObj.getPOINT_TWO_X() + "," + flingObj.getPOINT_TWO_Y() + ")/";
        str_fling+=flingObj.getDISTANCE_X() + "/";
        str_fling+=flingObj.getDISTANCE_Y() + "/";
        str_fling+=flingObj.getVELOCITY_X() + "/";
        str_fling+=flingObj.getVELOCITY_Y() + "/";
        String direction_f = "";
        direction_f += flingObj.getPOINT_ONE_Y() < flingObj.getPOINT_TWO_Y() ? "N" : flingObj.getPOINT_ONE_Y() > flingObj.getPOINT_TWO_Y() ? "S" : "";
        direction_f += flingObj.getPOINT_ONE_X() < flingObj.getPOINT_TWO_X() ? "E" : flingObj.getPOINT_ONE_X() > flingObj.getPOINT_TWO_X() ? "W" : "";
        str_fling+=direction_f + "#";
        myReadingBehavior.setKEY_FLING_RECORD(str_fling);
//        Toast.makeText(this, str_fling, Toast.LENGTH_SHORT).show();
//        Log.d("log: per_fling_record", myReadingBehavior.getKEY_FLING_RECORD());
    }

    @Override
    public void onOnePoint(DragObj dragObj) {
        dragObjArrayListArray.add(dragObj);
    }

    public static Point getTouchPositionFromDragEvent(View item, DragEvent event) {
        Rect rItem = new Rect();
        item.getGlobalVisibleRect(rItem);
        return new Point(rItem.left + Math.round(event.getX()), rItem.top + Math.round(event.getY()));
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LocalDate l_date = LocalDate.now();

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addReadingBehavior() {
        List<String> in_tt = new ArrayList<String>(Arrays.asList(myReadingBehavior.getKEY_TIME_IN().split(" ")));
        // [START add_ada_lovelace]
        // Create a new user with a first and last name
        Map<String, Object> readingBehavior = new HashMap<>();
        readingBehavior.put("news_id",  myReadingBehavior.getKEY_NEWS_ID());
        readingBehavior.put("trigger_by", myReadingBehavior.getKEY_TRIGGER_BY());
//        readingBehavior.put("time_in", myReadingBehavior.getKEY_TIME_IN());
        readingBehavior.put("in_date", in_tt.get(0));
        readingBehavior.put("in_time", in_tt.get(2));
        readingBehavior.put("out_date", "NA");
        readingBehavior.put("out_time", "NA");
        readingBehavior.put("content_length(dp)", "NA");
        readingBehavior.put("display_width(dp)", myReadingBehavior.getKEY_DISPLAY_WIDTH());
        readingBehavior.put("display_height(dp)", myReadingBehavior.getKEY_DISPLAY_HEIGHT());
        readingBehavior.put("time_on_page(s)", myReadingBehavior.getKEY_TIME_ON_PAGE());
        readingBehavior.put("pause_count", myReadingBehavior.getKEY_PAUSE_ON_PAGE());
        readingBehavior.put("viewport_num", myReadingBehavior.getKEY_VIEW_PORT_NUM());
        readingBehavior.put("viewport_record", Arrays.asList("NA"));
        readingBehavior.put("fling_num", myReadingBehavior.getKEY_FLING_NUM());
        readingBehavior.put("fling_record", Arrays.asList("NA"));
        readingBehavior.put("drag_num", myReadingBehavior.getKEY_DRAG_NUM());
        readingBehavior.put("drag_record", Arrays.asList("NA"));
        readingBehavior.put("share", Arrays.asList("NA"));
//        readingBehavior.put("share_via", "none");
        readingBehavior.put("time_series(s)", Arrays.asList("NA"));
        readingBehavior.put("byte_per_line", myReadingBehavior.getKEY_BYTE_PER_LINE());
        readingBehavior.put("row_spacing(dp)", myReadingBehavior.getKEY_ROW_SPACING());

        // Add a new document with my id ///////////////////////a generated ID
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        Log.d("log: device_id", device_id);
        db.collection("test_users")
                .document(device_id)
                .collection("reading_behaviors")
                .document(myReadingBehavior.getKEY_TIME_IN())
                .set(readingBehavior);
        document_create = true;
//        db.collection(Build.ID)
//                .document(String.valueOf(l_date))
//                .collection("reading_behaviors")
//                .document(myReadingBehavior.getKEY_TIME_IN())
//                .set(readingBehavior);
//                .add(readingBehavior)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("log: firebase", "DocumentSnapshot added with ID: " + documentReference.getId());
//                        //Log.d( tag: "firebase", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("log: firebase", "Error adding document");
//                        //Log.w(tag: "firebase", "Error adding document", e);
//                    }
//                });
    }
    public void updateReadingBehavior(){
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        DocumentReference rbRef = db.collection(Build.ID).document(String.valueOf(l_date)).collection("reading_behaviors").document(myReadingBehavior.getKEY_TIME_IN());
        @SuppressLint("MissingPermission")
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference rbRef = db.collection("test_users").document(device_id).collection("reading_behaviors").document(myReadingBehavior.getKEY_TIME_IN());

        List<String> time_series_list = new ArrayList<String>(Arrays.asList(myReadingBehavior.getKEY_TIME_SERIES().split("#")));
        List<String> viewport_record_list = new ArrayList<String>(Arrays.asList(myReadingBehavior.getKEY_VIEW_PORT_RECORD().split("#")));
        List<String> drag_record_list = new ArrayList<String>(Arrays.asList(myReadingBehavior.getKEY_DRAG_RECORD().split("#")));
        List<String> fling_record_list = new ArrayList<String>(Arrays.asList(myReadingBehavior.getKEY_FLING_RECORD().split("#")));
        for (int i = 0; i < fling_record_list.size(); i++) {
//            System.out.println(time_series_list.get(i));
            Log.d("log: firebase", fling_record_list.get(i));
        }
//        for (int i = 0; i < drag_record_list.size(); i++) {
////            System.out.println(time_series_list.get(i));
//            Log.d("log: firebase", drag_record_list.get(i));
//        }
        List<String> out_tt = new ArrayList<String>(Arrays.asList(myReadingBehavior.getKEY_TIME_OUT().split(" ")));

        // Set the "isCapital" field of the city 'DC'
        rbRef.update("content_length(dp)", myReadingBehavior.getKEY_CONTENT_LENGTH(),
                "drag_num", myReadingBehavior.getKEY_DRAG_NUM(),
                "drag_record", drag_record_list,
                "fling_num", myReadingBehavior.getKEY_FLING_NUM(),
                "fling_record", fling_record_list,
                "pause_count", myReadingBehavior.getKEY_PAUSE_ON_PAGE(),//auto
//                "share", myReadingBehavior.getKEY_SHARE(),//none
//                "share_via", "none",//none
                "time_on_page(s)", myReadingBehavior.getKEY_TIME_ON_PAGE(),//auto
//                "time_out", myReadingBehavior.getKEY_TIME_OUT(),
                "out_time", out_tt.get(2),
                "out_date", out_tt.get(0),
                "time_series(s)", time_series_list,//auto
                "viewport_record", viewport_record_list)//auto
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("log: firebase update", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("log: firebase update", "Error updating document", e);
                    }
                });
    }


}