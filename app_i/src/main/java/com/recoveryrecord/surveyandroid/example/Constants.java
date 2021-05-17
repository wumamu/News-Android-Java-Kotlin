package com.recoveryrecord.surveyandroid.example;

import java.util.HashSet;

public class Constants {
    public static final String MY_APP_PACKAGE_NAME = "com.recoveryrecord.surveyandroid";
    public static final String CHINA_TIMES_PACKAGE_NAME = "cc.nexdoor.ct.activity";
    public static final String CNA_PACKAGE_NAME = "m.cna.com.tw.App";
    public static final String UDN_PACKAGE_NAME = "com.udn.news";
    public static final String LTN_PACKAGE_NAME = "com.ltnnews.news";
    public static final String ETTODAY_PACKAGE_NAME = "net.ettoday.phone";
    public static final String CTS_PACKAGE_NAME = "com.news.ctsapp";
    public static final String EBC_PACKAGE_NAME = "com.ebc.news";
    public static final String STORM_PACKAGE_NAME = "cc.nexdoor.stormmedia";
    public static final String SETS_PACKAGE_NAME = "com.set.newsapp";
    public static final String ZERO_RESULT_STRING = "zero_result";
    public static final String NA_STRING = "NA";
    public static final String NONE_STRING = "none";
    public static final int NEWS_LIMIT_PER_PAGE = 30;
    public static final int PUSH_HISTORY_LIMIT_PER_PAGE = 50;
    public static final int READ_HISTORY_LIMIT_PER_PAGE = 50;
    //GENERATE ESM
    public static final int ESM_TIME_ON_PAGE_THRESHOLD = 5;//5 seconds
    public static final int ESM_INTERVAL = 60 * 60 * 1000;//one hour
    public static final int ESM_TARGET_RANGE = 30 * 60;//30 min (seconds)
    public static final int NOTIFICATION_TARGET_RANGE = 15 * 60;//15 min (seconds)
    public static final String ESM_NOTIFICATION_CONTENT_TITLE = "ESM";
    public static final String ESM_NOTIFICATION_CONTENT_TEXT = "是時候填寫問卷咯!";
    //PUSH ESM
    public static final int ESM_TIME_OUT = 15 * 60 * 1000;//15 min
    public static final long[] VIBRATE_EFFECT = {100, 200, 300, 300, 500, 300, 300};
    public static final String ESM_CHANNEL_ID = "10001";
    public static final String NEWS_CHANNEL_ID = "10002";
    public static final String DEFAULT_ESM_CHANNEL_ID = "default_esm";
    public static final String DEFAULT_NEWS_CHANNEL_ID = "default_news";
    public static final String DEFAULT_ESM_NOTIFICATION_ID = "notification-id";
    public static final String DEFAULT_NEWS_NOTIFICATION_ID = "notification-id";
    public static final String DEFAULT_ESM_NOTIFICATION = "notification";
    public static final String DEFAULT_NEWS_NOTIFICATION = "notification";

    //SUBMIT ESM CHECK
    public static final String TARGET_READ_NEWS_TITLE_ANSWER = "有印象，且沒看過相同的新聞";
    public static final String NOT_TARGET_READ_NEWS_TITLE_ANSWER = "沒有印象或有看過相同的新聞";
    public static final String NOTI_UNCLICK_LAST_ONE = "沒有印象";
    //INTENT PUT EXTRA
    //NEWS NewsModuleActivity
    public static final String TRIGGER_BY_KEY = "trigger_by";
    public static final String TRIGGER_BY_VALUE_NOTIFICATION = "Notification";
    public static final String NEWS_ID_KEY = "news_id";
    public static final String NEWS_MEDIA_KEY = "media";
    //NotificationListenerService
    public static final String DOC_ID_KEY = "id";
    public static final String NOTIFICATION_TYPE_KEY = "type";
    public static final String NOTIFICATION_TYPE_VALUE_NEWS = "news";
    public static final String NOTIFICATION_TYPE_VALUE_ESM = "esm";
    public static final String NOTIFICATION_TYPE_VALUE_DIARY = "diary";
    //ESMLoadingPageActivity SurveyActivity
    public static final String ESM_ID = "esm_id";
    //DiaryLoadingPageActivity SurveyActivity
    public static final String DIARY_ID = "diary_id";
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    public static final String TEST_USER_COLLECTION = "test_users";
    //NOTIFICATION BAR NEWS NOT APP
    public static final String NOTIFICATION_BAR_NEWS_MONITOR_COLLECTION = "notification_bar_news(not app)";
    //NOTIFICATION BAR ESM
    public static final String NOTIFICATION_BAR_ESM_COLLECTION = "notification_bar_esm";
    //NOTIFICATION BAR SERVICE
    public static final String NOTIFICATION_BAR_SERVICE_COLLECTION = "notification_bar_service_check";


    public static final String NOTIFICATION_BAR_TITLE = "title";
    public static final String NOTIFICATION_BAR_TEXT = "text";
    public static final String NOTIFICATION_BAR_NOTI_TIME = "noti_timestamp";
    public static final String NOTIFICATION_BAR_SOURCE = "media";
    //COMPARE RESULT
    public static final String COMPARE_RESULT_COLLECTION = "compare_result";
    public static final String COMPARE_RESULT_PUBDATE = "pubdate";
    public static final String COMPARE_RESULT_MEDIA = "media";
    public static final String COMPARE_RESULT_ID = "id";
    public static final String COMPARE_RESULT_NEW_TITLE = "news_title";
//    public static final String COMPARE_RESULT_TYPE = "type";
    public static final String COMPARE_RESULT_CLICK = "click";
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
    //PUSH DIARY
    public static final String PUSH_DIARY_COLLECTION = "push_diary";
    //PUSH NEWS
    public static final String PUSH_NEWS_COLLECTION = "push_news";
    public static final String PUSH_NEWS_CLICK = "click";
    public static final String PUSH_NEWS_NOTI_TIME = "noti_timestamp";
    public static final String PUSH_NEWS_PUBDATE = "pubdate";
    public static final String PUSH_NEWS_SELECTION = "selections";
    public static final String PUSH_NEWS_TITLE = "title";
    public static final String PUSH_NEWS_MEDIA = "media";
    public static final String PUSH_NEWS_ID = "id";
    public static final String PUSH_NEWS_TYPE = "type";
    //PUSH ESM
    public static final String PUSH_ESM_COLLECTION = "push_esm";
    public static final String PUSH_ESM_SAMPLE = "sample";
    public static final String PUSH_ESM_RESULT = "result";
    public static final String PUSH_ESM_REMOVE_TIME = "remove_timestamp";
    public static final String PUSH_ESM_NOTI_TIME = "noti_timestamp";
    public static final String PUSH_ESM_OPEN_TIME = "open_timestamp";
    public static final String PUSH_ESM_CLOSE_TIME = "close_timestamp";
    public static final String PUSH_ESM_SUBMIT_TIME = "submit_timestamp";
    public static final String PUSH_ESM_READ_ARRAY = "ReadNewsTitle";
    public static final String PUSH_ESM_NOTI_ARRAY = "NotiNewTitle";
    public static final String PUSH_ESM_TARGET_TITLE = "target_read_title";
    public static final String PUSH_ESM_TARGET_IN_TIME = "target_in_time";
    public static final String PUSH_ESM_TARGET_SITUATION = "target_situation";
    public static final String PUSH_ESM_TARGET_PLACE = "target_place";





    //SHARE PREFERENCE #############################################################################
    //SHARE PREFERENCE #############################################################################
    //SHARE PREFERENCE #############################################################################
    //SHARE PREFERENCE #############################################################################
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
    public static final String LAST_ESM_TIME = "LastEsmTime";
    public static final String PUSH_NEWS_MEDIA_LIST_SELECTION = "media_select";
    public static final String MAIN_PAGE_MEDIA_ORDER = "media_rank";


    public static final String ESM_PUSH_TOTAL = "ESMPushTotal";
    public static final String ESM_DONE_TOTAL = "ESMDoneTotal";
    public static final String ESM_DAY_PUSH_PREFIX = "ESMDayPush_";
    public static final String ESM_DAY_DONE_PREFIX = "ESMDayDone_";

    public static final String DIARY_PUSH_TOTAL = "DiaryPushTotal";
    public static final String DIARY_DONE_TOTAL = "DiaryDoneTotal";
    public static final String DIARY_DAY_PUSH_PREFIX = "DiaryDayPush_";
    public static final String DIARY_DAY_DONE_PREFIX = "DiaryDayDone_";



//    public static final String CATEGORY_HASH_SET_SIZE = "category_hash_set_size";
//    public static final String CATEGORY_HASH_SET_PREFIX = "category_";


    public static final String ESM_ALARM = "esm_alarm";
    public static final String DAIRY_ALARM = "diary_alarm";
    public static final String SCHEDULE_ALARM = "schedule_alarm";

    public static final String DIARY_TITLE_CONTENT = "請填寫今天的日誌";
    public static final String DIARY_TEXT = "謝謝您的合作";


//    public static String JSON_TEMPLATE = "";

}