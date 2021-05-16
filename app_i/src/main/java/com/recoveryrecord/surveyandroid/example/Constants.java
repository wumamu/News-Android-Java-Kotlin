package com.recoveryrecord.surveyandroid.example;

public class Constants {
    public static final int NEWS_LIMIT_PER_PAGE = 30;
    //GENERATE ESM
    public static final int ESM_INTERVAL = 60 * 60 * 1000;//one hour
    public static final int ESM_TARGET_RANGE = 30 * 60;//30 min (seconds)
    public static final int NOTIFICATION_TARGET_RANGE = 15 * 60;//15 min (seconds)
    //PUSH ESM
    public static final int ESM_TIME_OUT = 15 * 60 * 1000;//one hour
    public static final long[] VIBRATE_EFFECT = {100, 200, 300, 300, 500, 300, 300};
    public static final String ESM_CHANNEL_ID = "10001";
    public static final String NEWS_CHANNEL_ID = "10002";
    public static final String DEFAULT_ESM_CHANNEL_ID = "default_esm";
    public static final String DEFAULT_NEWS_CHANNEL_ID = "default_news";
    //SUBMIT ESM CHECK
    public static final String TARGET_READ_NEWS_TITLE_ANSWER = "有印象，且沒看過相同的新聞";
    //SHARE PREFERENCE
    //ESM
    public static final String NOTIFICATION_UNCLICKED_CANDIDATE = "PushNotificationNewsTitleArray";
    public static final String READ_HISTORY_CANDIDATE = "ReadingBehaviorNewsTitleArray";
    public static final String TARGET_NEWS_TITLE = "TargetNewsTitleArray";

    public static final String ESM_ALARM = "esm_alarm";
    public static final String DAIRY_ALARM = "diary_alarm";
    public static final String SCHEDULE_ALARM = "schedule_alarm";

    public static final String DIARY_TITLE_CONTENT = "請填寫今天的日誌";
    public static final String DIARY_TEXT = "謝謝您的合作";


//    public static String JSON_TEMPLATE = "";

}