package com.recoveryrecord.surveyandroid.example;

import java.util.HashSet;

public class Constants {
    public static final String ZERO_RESULT_STRING = "zero_result";
    public static final String NA_STRING = "NA";
    public static final String NONE_STRING = "none";
    public static final int NEWS_LIMIT_PER_PAGE = 30;
    //GENERATE ESM
    public static final int ESM_TIME_ON_PAGE_THRESHOLD = 5;//5 seconds
    public static final int ESM_INTERVAL = 60 * 60 * 1000;//one hour
    public static final int ESM_TARGET_RANGE = 30 * 60;//30 min (seconds)
    public static final int NOTIFICATION_TARGET_RANGE = 15 * 60;//15 min (seconds)
    //PUSH ESM
    public static final int ESM_TIME_OUT = 15 * 60 * 1000;//15 min
    public static final long[] VIBRATE_EFFECT = {100, 200, 300, 300, 500, 300, 300};
    public static final String ESM_CHANNEL_ID = "10001";
    public static final String NEWS_CHANNEL_ID = "10002";
    public static final String DEFAULT_ESM_CHANNEL_ID = "default_esm";
    public static final String DEFAULT_NEWS_CHANNEL_ID = "default_news";

    //SUBMIT ESM CHECK
    public static final String TARGET_READ_NEWS_TITLE_ANSWER = "有印象，且沒看過相同的新聞";
    //FIRESTORE FIELD###############################################################################
    public static final String TEST_USER_COLLECTION = "test_users";
    //READING BEHAVIOR
    public static final String READING_BEHAVIOR_COLLECTION = "reading_behaviors";
    public static final String READING_BEHAVIOR_SHARE = "share";
    public static final String READING_BEHAVIOR_CATEGORY = "category";
    public static final String READING_BEHAVIOR_TRIGGER_BY = "trigger_by";
    public static final String READING_BEHAVIOR_TRIGGER_BY_NOTIFICATION = "Notification";
    public static final String READING_BEHAVIOR_TRIGGER_BY_SELF_TRIGGER = "self_trigger";
    public static final String READING_BEHAVIOR_TIME_ON_PAGE = "time_on_page(s)";
    public static final String READING_BEHAVIOR_TITLE = "title";
    public static final String READING_BEHAVIOR_IN_TIME = "in_timestamp";
    public static final String READING_BEHAVIOR_OUT_TIME = "out_timestamp";
    public static final String READING_BEHAVIOR_SAMPLE_CHECK = "select";
    //PUSH NEWS
    public static final String PUSH_NEWS_COLLECTION = "push_news";
    public static final String PUSH_NEWS_CLICK = "click";
    public static final String PUSH_NEWS_NOTI_TIME = "noti_timestamp";
    public static final String PUSH_NEWS_TITLE = "title";
    //PUSH ESM
    public static final String PUSH_ESM_COLLECTION = "push_esm";
    public static final String PUSH_ESM_SAMPLE = "sample";
    public static final String PUSH_ESM_READ_ARRAY = "ReadNewsTitle";
    public static final String PUSH_ESM_NOTI_ARRAY = "NotiNewTitle";
    public static final String PUSH_ESM_TARGET_TITLE = "target_read_title";






    //SHARE PREFERENCE #############################################################################
    //ESM
    public static final String NOTIFICATION_UNCLICKED_CANDIDATE = "PushNotificationNewsTitleArray";
    public static final String READ_HISTORY_CANDIDATE = "ReadingBehaviorNewsTitleArray";
    public static final String TARGET_NEWS_TITLE = "TargetNewsTitleArray";
    public static final String ESM_START_TIME_HOUR = "ESMStartTimeHour";
    public static final String ESM_START_TIME_MIN = "ESMStartTimeMin";
    public static final String ESM_END_TIME_HOUR = "ESMEndTimeHour";
    public static final String ESM_END_TIME_MIN = "ESMEndTimeMin";
    public static final String ESM_SET_ONCE = "ESMSetOnce";


//    public static final String CATEGORY_HASH_SET_SIZE = "category_hash_set_size";
//    public static final String CATEGORY_HASH_SET_PREFIX = "category_";


    public static final String ESM_ALARM = "esm_alarm";
    public static final String DAIRY_ALARM = "diary_alarm";
    public static final String SCHEDULE_ALARM = "schedule_alarm";

    public static final String DIARY_TITLE_CONTENT = "請填寫今天的日誌";
    public static final String DIARY_TEXT = "謝謝您的合作";


//    public static String JSON_TEMPLATE = "";

}