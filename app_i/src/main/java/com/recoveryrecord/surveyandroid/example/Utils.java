package com.recoveryrecord.surveyandroid.example;

import android.content.Intent;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Utils {
    public static boolean first = true;
    public static boolean LineMes_first = true;
    public static boolean google_first = true;
    public static boolean cancel = false;
    public static boolean facebook = false;
    public static boolean chrome = false;
    public static boolean messenger = false;
    public static boolean youtube = false;
    public static boolean instagram = false;
    public static boolean ptt = false;
    public static boolean line = false;
    public static boolean line_mes = false;
    public static boolean news = false;
    public static boolean google = false;
    public static boolean reading = false;
    public static boolean screen_on = true;
    public static boolean takeNews = false;
    public static int StudyDay = 1;
    public static Intent screenshotPermission = null;
    public static boolean FirstscreenshotPermission = true;
    public static final String[] TriggerApp_array = {"Facebook","LineToday","NewsApp","googleNews","LineMes","Youtube","Instagram",
            "PTT", "Messenger", "Chrome"};
    public static HashMap<String, Integer> TriggerApp_permission = new HashMap<String, Integer>();
    //public static List<String> TitleAndWeb = new ArrayList<>();
    public static final String DATE_FORMAT_HOUR_MIN_SECOND = "HH-mm-ss";
    public static final String DATE_FORMAT_NOW_DAY = "yyyy-MM-dd";
    public static final String DATE_FORMAT_NOW_HOUR_MIN = "yyyy-MM-dd-HH-mm-ss";
    public static Uri cueRecallImg;
    public static Uri Crop_Uri;
    public static boolean finish_crop = false;
    public static boolean FirstGetID = true;
    public static String userID = "";

    public static String[] Home = {"主螢幕", "主畫面"};
    public static String[] Permission_ui = {"取消", "立即開始","不要再顯示","完成","允許","拒絕","確定","不要顯示","不顯示","顯示"};
    public static String[] HomePackage = {"com.android.launcher3", "com.sonymobile.home", "com.sonymobile.launcher", "com.sec.android.app.launcher",
            "com.htc.launcher", "com.asus.launcher", "com.lge.launcher3", "com.oppo.launcher","com.google.android.apps.nexuslauncher"};

    public static String[] eventText = {"udn News", "新聞", "台視新聞", "三立新聞網", "壹電視", "旺旺中時", "華視新聞", "星光雲",
            "新聞雲", "民視新聞", "台灣大紀元", "東森新聞", "TVBS新聞", "新浪新闻", "蘋果新聞網", "Now 新聞", "BBC News", "CNN",
            "台灣壹週刊", "東森財經新聞", "今日新聞", "蘋果動新聞","Yahoo奇摩"};

    public static String[] NewsPack = {"com.udn.news", "com.yahoo.mobile.client.android.newstw", "com.totvnow.ttv", "com.set.newsapp",
            "tw.com.nexttv.tvnews", "cc.nexdoor.ct.activity", "com.news.ctsapp", "net.ettoday.ETstar", "net.ettoday.phone", "com.formosatv.ftvnews",
            "com.epochtimes.tw.android", "com.ebc.news", "com.tvbs.news", "com.sina.news", "com.nextmediatw", "com.now.newsapp",
            "bbc.mobile.news.ww", "com.cnn.mobile.android.phone", "tw.com.nextmedia.magazine", "tw.net.ebc.fncyoutube", "com.nownews", "com.nextmedia",
            "com.yahoo.mobile.client.android.superapp"};

    public static String[] PTTevent = {"Mo PTT"};

//    public static String[] PTTPack = {"mong.moptt", "y_studio.ka5983.ptt2", "com.ihad.ptt", "com.bbs.reader", "com.joshua.jptt", "com.jiecode.ptt"};

    public static String getTimeString(String sdf){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(sdf);
        return df.format(date);
    }
}