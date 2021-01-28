package com.recoveryrecord.surveyandroid.example;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.icu.lang.UCharacter;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class SampleNewsActivity extends AppCompatActivity {
//    String TagCycle = "my activity cycle";
    volatile boolean activityStopped = false;
    volatile boolean activityEnd = false;
    private ScreenStateReceiver mReceiver;
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("log: activity cycle", "On create");
        setContentView(R.layout.activity_sample_news);
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
        final int paragraph_number = 5; // total number of paragraph
        content_list.add("美國國會6日遭到美國總統川普支持者闖入大肆破壞，造成5人喪生，美國聯邦眾議院以煽動叛亂為名，對川普提出彈劾，13日展開美國表決，最終以232票贊成、197票反對、４票棄權，表決通過彈劾案，正式指控他「煽動叛亂」，這讓川普成為美國史上第一位任內兩度被彈劾的總統，而在眾院表決通過彈劾案後，仍須經過參議院表決，目前的參議院仍是共和黨佔多數。參院共和黨領袖麥康奈（Mitch McConnell）在眾院投票前表明，參院不會在拜登就職前重新開會審議彈劾案，這意味著川普不會在任期結束前被彈劾免職。\n");
        content_list.add("民主黨議員指控川普「煽動叛亂」，導致「本月6日美國國會舉行2020年總統大選結果認證時，部分川普的支持者闖入國會大廈、與警方爆發衝突」，因而提出彈劾案。經過約4小時的辯論，眾院最終以232票贊成、197票反對通過彈劾條款。其中有10位共和黨議員「倒戈」投下贊成票，其餘197名共和黨眾議員投下反對票。在川普2019年底第一次被眾院彈劾時，「濫權」及「妨礙國會」兩項條款表決中，都沒有任何一位共和黨員跑票。\n");
        content_list.add("眾院共和黨領袖麥卡錫（Kevin McCarthy）坦言，川普必須為國會暴力事件負責；事情發生時，他應該立即譴責暴民，負起責任，平息醞釀中的動盪局面，並確保拜登能成功就任，但麥卡錫也說，彈劾川普是項錯誤，彈劾將進一步分裂國家，他認為成立調查委員會會更適當。而《紐約時報》報導指出，參、眾兩院共和黨領袖雖然從沒說要求川普辭職或贊成彈劾，但也反映共和黨現況，過去不過川普有怎樣極端言行，共和黨議員仍會力挺，如今卻把川普當做政治的負資產，急著切割。\n");
        content_list.add("在稍早之前的彈劾案辯論中，民主黨籍的眾議院議長裴洛西（Nancy Pelosi）說：「我們都知道總統煽動這場暴動，這場對抗我們國家的武裝叛亂。他必須下台，他對我們都熱愛的國家，是很清楚的威脅。」川普則發布聲明為自己辯護：「鑑於報導說有更多示威，我敦促『沒有』暴力、『不能』違法、『不要』破壞，那不是我所捍衛的東西（指暴力），也不是美國人民支持的；我呼籲所有美國人協助降低緊張，冷靜情緒，謝謝。」\n");
        content_list.add("彈劾案通過確定讓川普成為美國首位2度遭到國會彈劾的總統，在美國政治史上留下難堪紀錄。不僅如此，川普的2次彈劾都在一任4年任期內，相隔僅短短13 個月。川普的2次彈劾都在一任4年任期內，相隔僅短短13 個月。川普在2019年12月就曾因濫用職權與妨礙國會調查為由，遭到眾議院提請彈劾。雖然彈劾案最後未能在參議院獲得2/3票數同意，但川普已成為繼1868年的強生（Andrew Johnson）與1998年的柯林頓（Bill Clinton）後，美國第3位遭到國會彈劾的總統。\n");
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
        params.setMargins(100,10,100,10);
        int textview_num = divList.size();
//        final ScrollView mScrollView = findViewById(R.id.scroll_content);
//        int text_size = (int) (dpWidth /30);
        int text_size = 12;
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
            rowTextView.setBackgroundColor(0xFFFFFF99);
            ((LinearLayout) findViewById(R.id.layout_inside)).addView(rowTextView);

            myTextViews[i] = rowTextView;
        }
        // textview generate JSON ##################################################################
        final int N = textview_num;
        class SquareCalculator {
            private ExecutorService executor = Executors.newFixedThreadPool(1);
            //...
            //private ExecutorService executor = Executors.newSingleThreadExecutor();

            public Future<Integer> calculate(final Integer input) { return executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    Thread.sleep(1000);
                    return input * input;
                }
            });
            }
            public Future<Integer> no_cal(final int input, final int start_count) { return executor.submit(new Callable<Integer>() {
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
                    while(!activityEnd)
                    {
                        if (activityStopped && !activityEnd)
                        {
                            Log.d("log: MyScrollView", "Stop");
                            for (int i = 0; i < N; i++)
                            {
                                Log.d("log: MyScrollView", i + " count: " + count[i]/10);
                            }
                            Log.d("log: MyScrollView",  "time_on_page: " + count_running/10);
                            while (activityStopped)
                            {
                                Thread.sleep(100);
                            }
                            Log.d("log: MyScrollView", "Restart");
                        }
                        Rect scrollBounds = new Rect();
//                        mScrollView.getHitRect(scrollBounds);
                        for (int i = 0; i < N; i++)
                        {
                            if (!myTextViews[i].getLocalVisibleRect(scrollBounds)) {
                                new_flag[i] = false;
//                                Log.d(TAG, i + " false");
                            }
                            else {
                                new_flag[i] = true;
//                                Log.d(TAG, i + " true");
                            }

                            if (old_flag[i] && new_flag[i]){
                                //                            Log.d(TAG, "still visible "+ block + " count: " + count);
                                count[i]++;
                            }
                            else if (old_flag[i] && !new_flag[i]){
                                //                            Log.d(TAG, "no longer visible "+ block + " count: " + count);
                                old_flag[i] = new_flag[i];
                            }
                            else if (!old_flag[i] && new_flag[i]){
                                //                            Log.d(TAG, "start visible "+ block +" count: " + count);
                                count[i]++;
                                old_flag[i] = new_flag[i];
                            }
                            else {
                                //                            Log.d(TAG, "still not visible "+ block + " count: " + count);
                            }
//                            Log.d(TAG, i + " count: " + count[i]);
                        }
                        Thread.sleep(100);
                        count_running++;

                    }
                    Log.d("log: MyScrollView", "Finish");
                    for (int i = 0; i < N; i++) {
                        Log.d("log: MyScrollView", i + " count: " + count[i]/10);
                    }
                    Log.d("log: MyScrollView",  "time_on_page: " + count_running/10);
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
        // screen touch position ###################################################################
        content_view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
//                        Log.d("position", "moving: (" + motionEvent.getX() + ", " + motionEvent.getY() + ")");
                        return true;
                    case MotionEvent.ACTION_DOWN:
                        Log.d("log: on touch", "touched down");
                        Log.d("log: on touch", "moving: (" +  motionEvent.getXPrecision()*motionEvent.getX() + ", " + motionEvent.getY() + ")");
                        return true;
                    case MotionEvent.ACTION_UP:
                        Log.d("log: on touch", "touched up");
                        Log.d("log: on touch", "moving: (" + motionEvent.getX() + ", " + motionEvent.getY() + ")");
                        return true;
                }
                Log.d("log: on touch", "pressure: " + motionEvent.getPressure());
                return false;
            }
        });
        // on scroll change ########################################################################
        content_view.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = content_view.getScrollY();
                //Log.d("scrollY ",  Integer.toString(scrollY));
            }
        });
        super.onCreate(savedInstanceState);
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
                String url = "123123123";
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
}
