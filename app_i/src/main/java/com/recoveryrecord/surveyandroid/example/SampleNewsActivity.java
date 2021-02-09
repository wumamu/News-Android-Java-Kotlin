package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
//import android.support.v7.widget.Toolbar;

public class SampleNewsActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {
    //    String TagCycle = "my activity cycle";
    volatile boolean activityStopped = false;
    volatile boolean activityEnd = false;
    private ScreenStateReceiver mReceiver;

//    private ImageView imageView;
    private String mUrl, mImg, mTitle, mDate, mSource, mAuthor;
//    private Instant Glide;

    private static final String DEBUG_TAG = "Gestures";
//    private GestureDetectorCompat mDetector;
    private SimpleGestureFilter detector;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_news);

        detector = new SimpleGestureFilter(this,this);

        mUrl = "https://news.tvbs.com.tw/focus/1460200";
        URL img_url = null;
        try {
            img_url = new URL("https://news.tvbs.com.tw/focus/1460200");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mImg = "https://cc.tvbs.com.tw/img/upload/2021/02/05/20210205183845-85cd46f0.jpg";
        mTitle = "外交回歸　拜登懲俄抗中　美戰艦駛台灣海峽";
        mDate = "2021/02/05 20:00";
        mSource = "TVBS";
        mAuthor = "孟心怡";

        Log.d("log: activity cycle", "On create");

        //check screen on or off ###################################################################
        //screen off #########################
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenStateReceiver();
        registerReceiver(mReceiver, intentFilter);
        //screen size ##############################################################################
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.d("log: display-size", "display-width:" + width);
        Log.d("log: display-size", "display-height:" + height);
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth = outMetrics.widthPixels / density;
        Log.d("log: display-size", "display-width in dp:" + dpWidth);
        Log.d("log: display-size", "display-height in dp:" + dpHeight);
        //whether is chinese #######################################################################
        Set<UCharacter.UnicodeBlock> chineseUnicodeBlocks = new HashSet<UCharacter.UnicodeBlock>() {{
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
        // textview generate #######################################################################
        List<String> content_list = new ArrayList<>();
        final int paragraph_number = 16; // total number of paragraph
        content_list.add("美國總統拜登和副手賀錦麗，周四前往國務院發表談話，強調新政府重視外交，將與盟國修復關係，重返國際舞台。拜登宣布了他的外交重大政策，首先是要反制俄羅斯侵略，並將中國大陸視為是具有威脅性的對手，拜登也觸及了大陸的人權問題。拜登雖然未提及敏感的美中台關係，不過他上任後，也首次派出美國戰艦航行台灣海峽。拜登其他的外交政策，還包括取消撤軍德國的計畫、不再支持沙烏地阿拉伯對葉門的軍事行動；另外他也簽署行政命令，將增加美國收容難民的數量。\n");
        content_list.add("美國總統拜登和副手賀錦麗，周四前往國務院發表談話，拜登開宗明義對外交官員們表示，外交將重回美國對外政策的中心，致力於修復與盟友間的裂痕。\n");
        content_list.add("美國總統 拜登：「美國回來了！美國回來了！外交回來。」\n");
        content_list.add("拜登上任後首次公布重大外交政策，他第一個點名的國家就是俄羅斯，強調他不會像川普政府的無作為，將對俄羅的侵略，採取反制行動。\n");
        content_list.add("美國總統 拜登：「今日的美國任其所為，面對俄羅斯侵略、干預我們的選舉、網路攻擊和對人民下毒，全都結束了！我們不會遲疑，定要俄羅斯付出更高的代價，捍衛美國人利益。」\n");
        content_list.add("拜登也公開要求俄羅斯政府，釋放反對派派領袖納瓦尼。\n");
        content_list.add("美國總統 拜登：「他是因為揭露貪腐而被鎖定，應該立刻無條件釋放他。」\n");
        content_list.add("拜登20分鐘的演說中，中國大陸被點名5次，拜登並沒有具體說明對中政策，但明確將中國列為有威脅的競爭對象。拜登也觸及大陸的人權問題。\n");
        content_list.add("美國總統 拜登：「我們要正視中國，迎戰他們的經濟施暴，對抗好鬥和強制的脅迫行動。反對中國侵害人權、智慧財產以及全球管理機制。但我們也準備好要和北京合作，基於美國的利益。」\n");
        content_list.add("美國和歐盟都譴責大陸迫害維吾爾族人，大陸外交部近日也在日內瓦，邀請50多位駐聯合國大使，觀看大陸治理新疆的宣傳影片，大陸也藉此反擊西方，將人權問題政治化。\n");
        content_list.add("大陸外交部發言人 汪文斌：「不少使節(聯合國)回憶起近年來，應邀訪問新疆的情況，高度讚揚中國政府致力於促進可持續發展、維護社會安寧、保障人權、增進人民福祉，堅決反對有關國家，將人權問題政治化，利用涉疆問題干涉中國內政。」\n");
        content_list.add("除了人權，美中間另一個敏感議題就是美中台關係，拜登在演說中並雖沒有提及；但美中近日在台海的軍事較勁越趨頻繁，4號，美國第七艦隊對外宣布，伯克級神盾驅逐艦麥肯號 （USS John S. McCain , DDG 56），依國際法執行通過台灣海峽的例行任務。這也是拜登上任來，首次有美戰艦通過台灣海峽。中國大陸也立即回應。\n");
        content_list.add("大陸外交部發言人 汪文斌：「中方密切關注並全程掌握，美國軍艦過航台灣海峽的情況。中方將時刻繼續保持高度戒備，隨時應對一切威脅挑釁，堅決捍衛國家主權和領土完整。希望美方為地區和平穩定，發揮建設性作用，而不是相反。」\n");
        content_list.add("拜登其他的外交重大決定，還包括結束支持沙烏地阿拉伯對葉門的攻擊。前任川普政府因為不滿德國積欠北約組織 (NATO)經費，去年7月宣布要從德國撤出1.2萬名美軍；拜登宣布將停止這項政策，北約組織對拜登新政府的回歸，表示歡迎。\n");
        content_list.add("北約組織秘書長 史托騰伯格：「不可否認，在過去4年，我們面臨挑戰，這也不是秘密了，我們和美國前總統川普有些溝通困難。觀察美國，我們看到兩黨都強力支持NATO。所以我認為跨大西洋的結合，端看我們實際的作為，還有就是過去8年，我們看到有更多美軍駐守歐洲。」\n");
        content_list.add("拜登也宣布將簽署行政命令，增加美國收容難民數量，從川普時期的1萬5千人增加到12.5萬人。過去四年，川普常以推特宣達外交政策，國務院幾乎失去功能；拜登也在演說中向外交官員保證，他們現在可以各司其職，政府將是他們最大的後盾。\n");
        List<String> divList = new ArrayList<>();
//        int cut_size = 20;
        int cut_size = (int) (dpWidth / 16);
//        dynamically adjust
        for (int i = 0; i < paragraph_number; i++) {
            String str = content_list.get(i);
            int remainder = (str.length()) % cut_size;
            int str_length_byte = str.length();
            for (char c : str.toCharArray()) {
                if (chineseUnicodeBlocks.contains(UCharacter.UnicodeBlock.of(c))) {
//                        System.out.println(c + " is chinese");
                    str_length_byte += 1;
                }
            }
            // 一共要分割成幾段
            int front = 0, end = 0, iter_char = 0, line_count = 1;
            char[] c = str.toCharArray();
            while (true) {
//                System.out.println("line  "+line_count);
                line_count++;
                //one paragraph
//                end = front; // index for word
                int count_byte = 0;
//                System.out.print(c[i]);
//                System.out.println(end.getClass());
                while (count_byte < cut_size * 2) {

                    //one sentence at most 40 bytes
                    if (c[iter_char] == '\n') {
                        break;
                    }
//                    System.out.println(iter_char);
//                    System.out.println(c[iter_char]);
                    if (isChineseChar(c[iter_char])) {
//                        System.out.println(c[iter_char] + " is chinese");
                        count_byte += 2;
                    } else if ((c[iter_char] >= 'a' && c[iter_char] <= 'z') || (c[iter_char] >= 'A' && c[iter_char] <= 'Z')) {
                        //english letter
                        //check word
                        int word_length = 0, word_index, tmp_count_byte = count_byte;
                        for (word_index = iter_char; word_index + 1 <= str.length(); word_index++) {
                            if ((c[word_index] >= 'a' && c[word_index] <= 'z') || (c[word_index] >= 'A' && c[word_index] <= 'Z')) {
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
        params.setMargins(100, 10, 100, 10);
        int textview_num = divList.size();
//        final ScrollView mScrollView = findViewById(R.id.scroll_content);
//        int text_size = (int) (dpWidth /30);
        int text_size = 12;

        final TextView myTextViewsTitle = new TextView(this);
        final TextView myTextViewsDate = new TextView(this);
        final TextView myTextViewsAuthor = new TextView(this);
        final TextView myTextViewsSrc = new TextView(this);
        myTextViewsTitle.setText(mTitle);
        myTextViewsDate.setText(mDate);
        myTextViewsAuthor.setText(mAuthor);
        myTextViewsSrc.setText(mSource);
        myTextViewsTitle.setTextColor(Color.parseColor("black"));
        myTextViewsDate.setTextColor(Color.parseColor("black"));
        myTextViewsAuthor.setTextColor(Color.parseColor("black"));
        myTextViewsSrc.setTextColor(Color.parseColor("black"));
        myTextViewsTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        myTextViewsDate.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        myTextViewsAuthor.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        myTextViewsSrc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        myTextViewsTitle.setGravity(Gravity.LEFT);
        myTextViewsDate.setGravity(Gravity.LEFT);
        myTextViewsAuthor.setGravity(Gravity.LEFT);
        myTextViewsSrc.setGravity(Gravity.LEFT);
        myTextViewsTitle.setLayoutParams(params);
        myTextViewsDate.setLayoutParams(params);
        myTextViewsAuthor.setLayoutParams(params);
        myTextViewsSrc.setLayoutParams(params);
        ((LinearLayout) findViewById(R.id.layout_inside)).addView(myTextViewsTitle);
        ((LinearLayout) findViewById(R.id.layout_inside)).addView(myTextViewsDate);
        ((LinearLayout) findViewById(R.id.layout_inside)).addView(myTextViewsAuthor);
        ((LinearLayout) findViewById(R.id.layout_inside)).addView(myTextViewsSrc);
//        ImageView image = listItemView.findViewById(R.id.article_image);

        ImageView imageView = new ImageView(this);
        new DownloadImageTask(imageView).execute(mImg);
        ((LinearLayout) findViewById(R.id.layout_inside)).addView(imageView);

        final TextView[] myTextViews = new TextView[textview_num]; // create an empty array;
        for (int i = 0; i < divList.size(); i++) {
            final TextView rowTextView = new TextView(this);
            String tmp = divList.get(i);
            rowTextView.setText(tmp);
            rowTextView.setTextColor(Color.parseColor("black"));
//            rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_MM, 2);
            rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, text_size);
//            rowTextView.setWidth(250);
            rowTextView.setGravity(Gravity.LEFT);
            rowTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            rowTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, width));
            rowTextView.setSingleLine(true);
//            rowTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));

            rowTextView.setLayoutParams(params);
//            rowTextView.setBackgroundColor(0xFFFFFF99);
            ((LinearLayout) findViewById(R.id.layout_inside)).addView(rowTextView);

            myTextViews[i] = rowTextView;
        }
        // textview generate JSON ##################################################################
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
                    int count_running = 0;
                    int[] count = new int[N];
                    boolean[] old_flag = new boolean[N];
                    boolean[] new_flag = new boolean[N];

                    @Override
                    public Integer call() throws Exception {
                        Arrays.fill(old_flag, Boolean.FALSE);
                        Arrays.fill(new_flag, Boolean.FALSE);
                        Arrays.fill(count, 0);
                        Log.d("log: MyScrollView", "Start");
                        while (!activityEnd) {
                            if (activityStopped && !activityEnd) {
                                Log.d("log: MyScrollView", "Stop");
                                for (int i = 0; i < N; i++) {
                                    Log.d("log: MyScrollView", i + " count: " + count[i] / 10);
                                }
                                Log.d("log: MyScrollView", "time_on_page: " + count_running / 10);
                                while (activityStopped) {
                                    Thread.sleep(100);
                                }
                                Log.d("log: MyScrollView", "Restart");
                            }
                            Rect scrollBounds = new Rect();
//                        mScrollView.getHitRect(scrollBounds);
                            for (int i = 0; i < N; i++) {
                                if (!myTextViews[i].getLocalVisibleRect(scrollBounds)) {
                                    new_flag[i] = false;
//                                Log.d(TAG, i + " false");
                                } else {
                                    new_flag[i] = true;
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

                        }
                        Log.d("log: MyScrollView", "Finish");
                        for (int i = 0; i < N; i++) {
                            Log.d("log: MyScrollView", i + " count: " + count[i] / 10);
                        }
                        Log.d("log: MyScrollView", "time_on_page: " + count_running / 10);
                        return 1;
                    }
                });
            }
        }
        SquareCalculator squareCalculator = new SquareCalculator();
        Future<Integer> future1 = squareCalculator.no_cal(1, 0);
        // screen total height #####################################################################
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
                    String s_final = String.valueOf(viewHeight);
                    Log.d("log: content-length", s_final);
                }
            });
        }
        //drag position ############################################################################

////        final LinearLayout outside_view = findViewById(R.id.layout_outside);
//        imageView.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                //get event positions
//                switch (event.getAction()) {
//                    case DragEvent.ACTION_DRAG_STARTED:
//                        //etc etc. do some stuff with the drag event
//                        Log.d("log: DragListener", "ACTION_DRAG_STARTED");
//                        break;
//                    case DragEvent.ACTION_DRAG_LOCATION:
//                        Point touchPosition = getTouchPositionFromDragEvent(v, event);
//                        Log.d("log: DragListener", String.valueOf(touchPosition));
//                        //do something with the position (a scroll i.e);
//                        break;
//                    default:
//                        Log.d("log: DragListener", "************");
//                }
//                return true;
//            }
//        });
        // screen touch position ###################################################################
//        content_view.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
////                        Log.d("position", "moving: (" + motionEvent.getX() + ", " + motionEvent.getY() + ")");
//                        return true;
//                    case MotionEvent.ACTION_DOWN:
//                        Log.d("log: on touch", "touched down");
//                        Log.d("log: on touch", "moving: (" +  motionEvent.getXPrecision()*motionEvent.getX() + ", " + motionEvent.getY() + ")");
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        Log.d("log: on touch", "touched up");
//                        Log.d("log: on touch", "moving: (" + motionEvent.getX() + ", " + motionEvent.getY() + ")");
//                        return true;
//                }
//                Log.d("log: on touch", "pressure: " + motionEvent.getPressure());
//                return false;
//            }
//        });
        // on scroll change ########################################################################
//        content_view.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                int scrollY = content_view.getScrollY();
//                //Log.d("scrollY ",  Integer.toString(scrollY));
//            }
//        });
//        super.onCreate(savedInstanceState);
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
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("log: activity cycle", "On start");
        activityStopped = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("log: activity cycle", "On resume");
        activityStopped = false;
        //isScreenOn(R.layout.activity_news_detail);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("log: activity cycle", "On pause");
        activityStopped = true;
    }

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("log: activity cycle", "On destroy");
//        activityStopped = true;
        activityEnd = true;
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if  (id == R.id.share){
            Toast.makeText(this, "share is being clicked", Toast.LENGTH_LONG).show();
            try{
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plan");
//                i.putExtra(Intent.EXTRA_SUBJECT, mSource);
//                String body = mTitle + "\n" + mUrl + "\n" + "Share from the News App" + "\n";
//                i.putExtra(Intent.EXTRA_TEXT, body);
//                startActivity(Intent.createChooser(i, "Share with :"));
                String url = mUrl;
                Toast.makeText(this, "share is being clicked", Toast.LENGTH_LONG).show();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,url); // your above url
                Intent receiver = new Intent(this, ApplicationSelectorReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
                Intent chooser = Intent.createChooser(shareIntent, "Share via...", pendingIntent.getIntentSender());
//                Log.d("log: share via", String.valueOf(pendingIntent.getIntentSender()));
                startActivity(chooser);

            }catch (Exception e){
                Toast.makeText(this, "Hmm.. Sorry, \nCannot be share", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action_esm){
            Intent intent = new Intent();
//            intent.setClass(NewsDetailActivity.this, ExampleSurveyActivity.class);
            intent.setClass(SampleNewsActivity.this, ExampleSurveyActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {
        String str = "";
        switch (direction) {
            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                break;
            case SimpleGestureFilter.SWIPE_LEFT : str = "Swipe Left";
                break;
            case SimpleGestureFilter.SWIPE_DOWN : str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP : str = "Swipe Up";
                break;
        }
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        Log.d("log: GestureListener",str);
    }

//    @Override
//    public void onDoubleTap() {
////        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
//    }

    public static Point getTouchPositionFromDragEvent(View item, DragEvent event) {
        Rect rItem = new Rect();
        item.getGlobalVisibleRect(rItem);
        return new Point(rItem.left + Math.round(event.getX()), rItem.top + Math.round(event.getY()));
    }

}
