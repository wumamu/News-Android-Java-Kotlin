package com.recoveryrecord.surveyandroid.example;

import java.util.HashSet;

public class Constants {
    //email
    public static final String OUR_EMAIL = "wcclevi1225@gmail.com";
    public static final String MY_DEVICE = "564da153307f5547";
    //action
    public static final String CHECK_SERVICE_ACTION = "CheckService";//request code 50 60
    public static final String ESM_ALARM_ACTION = "EsmAlarm";//request code 0-15
    public static final String ESM_SCHEDULE_ID = "esm_schedule_id";
    public static final String SCHEDULE_SOURCE = "schedule_source";
    public static final String DIARY_ALARM_ACTION = "DiaryAlarm";//request code 100
    public static final String RESTART_ALARM_ACTION = "RestartAlarm";//request code 1050
    public static final String CANCEL_ALARM_ACTION = "CancelAlarm";//request code 1050
    public static final String SCHEDULE_ALARM_ACTION = "ScheduleAlarm";//request code 1000 1001
    public static final String UPLOAD_ACTION = "UploadSQLite";//request code 1000 1001

    public static final int SERVICE_CHECKER_INTERVAL = 20 * 60 * 1000;//every 20 min
    //other
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

    public static final int REPEAT_ALARM_CHECKER = 10 * 60 * 1000;//10 min
    public static final int ESM_TIME_ON_PAGE_THRESHOLD = 1;//5 seconds
    public static final int ESM_INTERVAL = 60 * 60 * 1000;//one hour
    public static final int DIARY_INTERVAL = 23 * 60 * 60 * 1000;//23 hour
    public static final int ESM_TARGET_RANGE = 60 * 60;//60 min (seconds)
    public static final int NOTIFICATION_TARGET_RANGE = 60 * 60;//60 min (seconds)
    public static final String ESM_NOTIFICATION_CONTENT_TITLE = "ESM";
    public static final String ESM_NOTIFICATION_CONTENT_TEXT = "是時候填寫問卷咯!";
    public static final String DIARY_NOTIFICATION_CONTENT_TITLE = "DIARY";
    public static final String DIARY_NOTIFICATION_CONTENT_TEXT = "是時候填寫問卷咯!";
    //GENERATE DIARY
    public static final int DIARY_TARGET_RANGE = 24 * 60 * 60;//24 hour
    //PUSH ESM
    public static final int ESM_TIME_OUT = 15 * 60 * 1000;//15 min
    public static final int DIARY_TIME_OUT = 120 * 60 * 1000;//120 min
    public static final long[] VIBRATE_EFFECT = {100, 200, 300, 300, 500, 300, 300};
    public static final String ESM_CHANNEL_ID = "10001";
    public static final String NEWS_CHANNEL_ID = "10002";
    public static final String DIARY_CHANNEL_ID = "10003";
    public static final String DEFAULT_ESM_CHANNEL_ID = "default_esm";
    public static final String DEFAULT_NEWS_CHANNEL_ID = "default_news";
    public static final String DEFAULT_DIARY_CHANNEL_ID = "default_diary";
    public static final String DEFAULT_ESM_NOTIFICATION_ID = "notification-id";
    public static final String DEFAULT_NEWS_NOTIFICATION_ID = "notification-id";
    public static final String DEFAULT_DIARY_NOTIFICATION_ID = "notification-id";
    public static final String DEFAULT_ESM_NOTIFICATION = "notification";
    public static final String DEFAULT_NEWS_NOTIFICATION = "notification";
    public static final String DEFAULT_DIARY_NOTIFICATION = "notification";

    //SUBMIT ESM CHECK
    public static final String TARGET_READ_NEWS_TITLE_ANSWER = "有點入閱讀，且沒看過相同的新聞";
    public static final String NOT_TARGET_READ_NEWS_TITLE_ANSWER = "沒有點入閱讀過或有看過相同的新聞";
    public static final String NOTI_UNCLICK_LAST_ONE = "沒有印象";
    //INTENT PUT EXTRA
    //NEWS NewsModuleActivity
    public static final String TRIGGER_BY_KEY = "trigger_by";
    public static final String TRIGGER_BY_VALUE_NOTIFICATION = "Notification";
    public static final String TRIGGER_BY_VALUE_SELF_TRIGGER = "self_trigger";
    public static final String NEWS_ID_KEY = "news_id";
    public static final String NEWS_MEDIA_KEY = "media";
    //NotificationListenerService
    public static final String GROUP_NEWS = "NewsMoment";
    public static final String GROUP_NEWS_SERVICE = "NewsMoment Service";
    public static final String DOC_ID_KEY = "id";
    public static final String NOTIFICATION_TYPE_KEY = "type";
    public static final String NOTIFICATION_ESM_TYPE_KEY = "esm_type";//0 noti 1 read
    public static final String NOTIFICATION_TYPE_VALUE_NEWS = "news";
    public static final String NOTIFICATION_TYPE_VALUE_ESM = "esm";
    public static final String NOTIFICATION_TYPE_VALUE_DIARY = "diary";
    public static final String NOTIFICATION_TYPE_VALUE_SERVICE = "service";
    //ESMLoadingPageActivity SurveyActivity
//    public static final String ESM_ID = "esm_id";
    public static final String LOADING_PAGE_ID = "id";
    public static final String SURVEY_PAGE_ID = "id";
//    public static final String DIARY_PAGE_ID = "id";
    public static final String LOADING_PAGE_TYPE_KEY = "type";
    public static final String LOADING_PAGE_TYPE_ESM = "esm";
    public static final String LOADING_PAGE_TYPE_DIARY = "diary";
    //DiaryLoadingPageActivity SurveyActivity
    public static final String DIARY_ID = "diary_id";
    //Survey
    public static final String ESM_EXIST_READ_SAMPLE = "esm_exist_read";
    public static final String ESM_EXIST_NOTIFICATION_SAMPLE = "esm_exist_notification";
    public static final String DIARY_EXIST_ESM_SAMPLE = "diary_exist_esm_sample";
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    public static final String TEST_USER_COLLECTION = "test_users";


    public static final String USER_COLLECTION = "user";
    public static final String APP_VERSION_KEY = "app_version";
    public static final String APP_VERSION_VALUE = "21.07.11-1";
    public static final String UPDATE_TIME = "update_timestamp";
    public static final String INITIAL_TIME = "initial_timestamp";
    public static final String LAST_LAUNCH_TIME = "last_launch_timestamp";
    public static final String USER_DEVICE_ID = "device_id";
    public static final String USER_PHONE_ID = "phone_id";
    public static final String USER_ANDROID_SDK = "android_sdk";
    public static final String USER_ANDROID_RELEASE = "android_release";
    public static final String PUSH_MEDIA_SELECTION = "push_media_selection";
    public static final String MEDIA_BAR_ORDER = "media_bar_order";
    public static final String USER_SURVEY_NUMBER = "user_survey_number";
    //MEDIA
    public static final String MEDIA_COLLECTION = "medias";
    public static final String NEWS_COLLECTION = "news";
    public static final String NEWS_URL = "url";
    public static final String NEWS_CATEGORY = "category";
    public static final String NEWS_TITLE = "title";
    public static final String NEWS_PUBDATE = "pubdate";
    public static final String NEWS_IMAGE = "image";
    public static final String NEWS_CONTENT = "content";
    public static final String NEWS_MEDIA = "media";
    public static final String NEWS_ID = "id";
    //ALARM SERVICE
    public static final String ALARM_SERVICE_COLLECTION = "alarm_service";
//    public static final String ALARM_SERVICE_POST_COLLECTION = "alarm_service_post";
//    public static final String DIARY_STATUS = "diary_status";
//    public static final String DIARY_PUSH = "success";
//    public static final String DIARY_NOT_IN_PUSH_RANGE = "fail_not_in_range";
//    public static final String DIARY_OUT_OF_INTERVAL_LIMIT = "fail_more_than_one_per_day";//ONE DAY
//    public static final String ESM_STATUS = "esm_status";
//    public static final String ESM_PUSH = "success";
//    public static final String ESM_NOT_IN_PUSH_RANGE= "fail_not_in_range";
//    public static final String ESM_OUT_OF_INTERVAL_LIMIT = "fail_more_than_one_per_hour";//ONE HOUR
    //NOTIFICATION BAR NEWS NOT APP
    public static final String NOTIFICATION_BAR_NEWS_MONITOR_COLLECTION = "notification_bar_news(not app)";
//    //NOTIFICATION BAR ESM
//    public static final String NOTIFICATION_BAR_ESM_COLLECTION = "notification_bar_esm";
//    //NOTIFICATION BAR DIARY
//    public static final String NOTIFICATION_BAR_DIARY_COLLECTION = "notification_bar_diary";
//    //NOTIFICATION BAR SERVICE
//    public static final String NOTIFICATION_BAR_SERVICE_COLLECTION = "notification_bar_my_news";
    public static final String NOTIFICATION_BAR_NEWS_TITLE = "title";
    public static final String NOTIFICATION_BAR_NEWS_TEXT = "text";
    public static final String NOTIFICATION_BAR_NEWS_NOTI_TIME = "noti_timestamp";
    public static final String NOTIFICATION_BAR_NEWS_PACKAGE_ID = "package_id";
    public static final String NOTIFICATION_BAR_NEWS_SOURCE = "media";
    public static final String NOTIFICATION_BAR_NEWS_DEVICE_ID = "device_id";
    //COMPARE RESULT
    public static final String COMPARE_RESULT_COLLECTION = "compare_result";
    public static final String COMPARE_RESULT_PUBDATE = "pubdate";
    public static final String COMPARE_RESULT_MEDIA = "media";
    public static final String COMPARE_RESULT_ID = "id";
    public static final String COMPARE_RESULT_NEW_TITLE = "news_title";
//    public static final String COMPARE_RESULT_TYPE = "type";
    public static final String COMPARE_RESULT_CLICK = "click";
    //ALARM SCHEDULE
    public static final String SCHEDULE_ALARM_COLLECTION = "schedule_alarm";
    public static final String SCHEDULE_ALARM_TRIGGER_TIME = "trigger_time";
    public static final String SCHEDULE_ALARM_SURVEY_START = "survey_start_hour";
    public static final String SCHEDULE_ALARM_SURVEY_END = "survey_end_hour";
    public static final String SCHEDULE_ALARM_MAX_ESM = "max_esm";
    public static final String SCHEDULE_ALARM_DEVICE_ID = "device_id";
    public static final String SCHEDULE_ALARM_ACTION_TYPE = "action";
    //NEWS SERVICE CHECKER
    public static final String NEWS_SERVICE_COLLECTION = "news_service";
    public static final String NEWS_SERVICE_DEVICE_ID = "device_id";
    public static final String NEWS_SERVICE_TIME = "service_timestamp";
    public static final String NEWS_SERVICE_STATUS_KEY = "service_status";
    public static final String NEWS_SERVICE_STATUS_VALUE_RESTART = "service_restart";
    public static final String NEWS_SERVICE_STATUS_VALUE_FAILED = "service_failed";
    public static final String NEWS_SERVICE_STATUS_VALUE_RUNNING = "service_running";
    public static final String NEWS_SERVICE_STATUS_VALUE_INITIAL = "service_initial";
    public static final String NEWS_SERVICE_CYCLE_KEY = "service_cycle";
    public static final String NEWS_SERVICE_CYCLE_VALUE_BOOT_UP = "service_boot_up";
    public static final String NEWS_SERVICE_CYCLE_VALUE_ALARM = "alarm";
    public static final String NEWS_SERVICE_CYCLE_VALUE_FAILED_RESTART = "service_failed_restart";
    public static final String NEWS_SERVICE_CYCLE_VALUE_MAIN_PAGE = "main_page";
    public static final String NEWS_SERVICE_CYCLE_VALUE_SERVICE_PAGE = "service_page";

    //READING BEHAVIOR
    public static final String READING_BEHAVIOR_COLLECTION = "reading_behaviors";
//    public static final String READING_BEHAVIOR_MID = " RB ";

    public static final String READING_BEHAVIOR_DOC_ID = "doc_id";
    public static final String READING_BEHAVIOR_DEVICE_ID = "device_id";
    public static final String READING_BEHAVIOR_USER_ID = "user_id";
    public static final String READING_BEHAVIOR_SAMPLE_CHECK_ID = "select_esm_id";

    public static final String READING_BEHAVIOR_TRIGGER_BY = "trigger_by";
    public static final String READING_BEHAVIOR_NEWS_ID = "id";
    public static final String READING_BEHAVIOR_TITLE = "title";
    public static final String READING_BEHAVIOR_MEDIA = "media";
    public static final String READING_BEHAVIOR_HAS_IMAGE = "has_img";
    public static final String READING_BEHAVIOR_PUBDATE = "pubdate";
//    public static final String READING_BEHAVIOR_CATEGORY = "category";
    public static final String READING_BEHAVIOR_ROW_SPACING = "row_spacing(dp)";
    public static final String READING_BEHAVIOR_BYTE_PER_LINE = "byte_per_line";
    public static final String READING_BEHAVIOR_FONT_SIZE = "font_size";

    public static final String READING_BEHAVIOR_CONTENT_LENGTH = "content_length(dp)";
    public static final String READING_BEHAVIOR_DISPLAY_WIDTH = "display_width(dp)";
    public static final String READING_BEHAVIOR_DISPLAY_HEIGHT = "display_height(dp)";

    public static final String READING_BEHAVIOR_IN_TIME = "in_timestamp";
    public static final String READING_BEHAVIOR_OUT_TIME = "out_timestamp";

    public static final String READING_BEHAVIOR_TIME_ON_PAGE = "time_on_page(s)";
    public static final String READING_BEHAVIOR_PAUSE_COUNT = "pause_count";
    public static final String READING_BEHAVIOR_VIEWPORT_NUM = "viewport_num";
    public static final String READING_BEHAVIOR_VIEWPORT_RECORD = "viewport_record";
    public static final String READING_BEHAVIOR_FLING_NUM = "fling_num";
    public static final String READING_BEHAVIOR_FLING_RECORD = "fling_record";
    public static final String READING_BEHAVIOR_DRAG_NUM = "drag_num";
    public static final String READING_BEHAVIOR_DRAG_RECORD = "drag_record";
    public static final String READING_BEHAVIOR_SHARE = "share";
    public static final String READING_BEHAVIOR_TIME_SERIES = "time_series(s)";


    public static final String READING_BEHAVIOR_TRIGGER_BY_NOTIFICATION = "Notification";
    public static final String READING_BEHAVIOR_TRIGGER_BY_SELF_TRIGGER = "self_trigger";


    public static final String READING_BEHAVIOR_CHECK_MARK = "check_mark";
    public static final String READING_BEHAVIOR_SAMPLE_CHECK = "select";

    //PUSH NEWS
    public static final String PUSH_NEWS_COLLECTION = "push_news";
    public static final String PUSH_NEWS_DOC_ID = "doc_id";
    public static final String PUSH_NEWS_DEVICE_ID = "device_id";
    public static final String PUSH_NEWS_USER_ID = "user_id";

    public static final String PUSH_NEWS_ID = "id";
    public static final String PUSH_NEWS_TITLE = "title";
    public static final String PUSH_NEWS_MEDIA = "media";
    public static final String PUSH_NEWS_PUBDATE = "pubdate";

    public static final String PUSH_NEWS_NOTI_TIME = "noti_timestamp";
    public static final String PUSH_NEWS_RECEIEVE_TIME = "receieve_timestamp";
    public static final String PUSH_NEWS_OPEN_TIME = "open_timestamp";
    public static final String PUSH_NEWS_REMOVE_TIME = "remove_timestamp";
    public static final String PUSH_NEWS_REMOVE_TYPE = "remove_type";
    public static final String PUSH_NEWS_READING_BEHAVIOR_ID = "reading_behavior_id";

    public static final String PUSH_NEWS_CLICK = "click";
    public static final String PUSH_NEWS_TYPE = "type";
//    public static final String PUSH_NEWS_SELECTION = "selections";
//    public static final String PUSH_NEWS_SAMPLE_CHECK_ID = "select_esm_id";


    //PUSH ESM
    public static final String PUSH_ESM_COLLECTION = "push_esm";
    public static final String PUSH_ESM_DOC_ID = "doc_id";
    public static final String PUSH_ESM_DEVICE_ID = "device_id";
    public static final String PUSH_ESM_USER_ID = "user_id";

    public static final String PUSH_ESM_TYPE = "esm_type";
    public static final String PUSH_ESM_READ_ARRAY = "ReadNewsTitle";
    public static final String PUSH_ESM_NOTI_ARRAY = "NotiNewTitle";
    public static final String PUSH_ESM_SAMPLE_TIME = "esm_sample_time";

    public static final String PUSH_ESM_SCHEDULE_ID = "esm_schedule_id";
    public static final String PUSH_ESM_SCHEDULE_SOURCE = "esm_schedule_source";

    public static final String PUSH_ESM_SAMPLE = "sample";
    public static final String PUSH_ESM_SAMPLE_ID = "sample_diary_id";

//    public static final String PUSH_ESM_TRIGGER_BY_NOTIFICATION = "notification_service";
//    public static final String PUSH_ESM_TRIGGER_BY_ALARM = "alarm";
//    public static final String PUSH_ESM_TRIGGER_BY_SELF = "self";
//    public static final String PUSH_ESM_TRIGGER_BY = "trigger_by";


    public static final String PUSH_ESM_NOTI_TIME = "noti_timestamp";
    public static final String PUSH_ESM_RECEIEVE_TIME = "receieve_timestamp";
    public static final String PUSH_ESM_OPEN_TIME = "open_timestamp";
    public static final String PUSH_ESM_CLOSE_TIME = "close_timestamp";
    public static final String PUSH_ESM_SUBMIT_TIME = "submit_timestamp";
    public static final String PUSH_ESM_REMOVE_TIME = "remove_timestamp";
    public static final String PUSH_ESM_REMOVE_TYPE = "remove_type";

    public static final String PUSH_ESM_RESULT = "result";
    public static final String PUSH_ESM_TARGET = "target";
    public static final String PUSH_ESM_TARGET_NEWS_ID = "target_news_id";
    public static final String PUSH_ESM_TARGET_TITLE = "target_read_title";
    public static final String PUSH_ESM_TARGET_IN_TIME = "target_in_time";
    public static final String PUSH_ESM_TARGET_RECEIEVE_TIME = "target_receieve_time";
    public static final String PUSH_ESM_TARGET_RECEIEVE_SITUATION = "target_receieve_situation";
    public static final String PUSH_ESM_TARGET_RECEIEVE_PLACE = "target_receieve_place";
    public static final String PUSH_ESM_TARGET_READ_SITUATION = "target_read_situation";
    public static final String PUSH_ESM_TARGET_READ_PLACE = "target_read_place";


    public static final String PUSH_ESM_READ_EXIST = "ExistReadSample";
    public static final String PUSH_ESM_NOTIFICATION_EXIST = "ExistNotificationSample";
    public static final String PUSH_ESM_NOT_SAMPLE_READ_SHORT = "NotSampleReadShort";
    public static final String PUSH_ESM_NOT_SAMPLE_READ_FAR = "NotSampleReadFar";
    public static final String PUSH_ESM_NOT_SAMPLE_NOTIFICATION_FAR = "NotSampleNotificationFar";





    public static final String SAMPLE_NEWS_ID = "sample_news_id";
    public static final String SAMPLE_NEWS_TITLE = "sample_news_title";
    public static final String SAMPLE_NEWS_MEDIA = "sample_news_media";
    public static final String SAMPLE_NEWS_RECEIEVE_TIME = "sample_news_receieve_time";
    public static final String SAMPLE_NEWS_IN_TIME = "sample_news_in_time";



    //PUSH DIARY
    public static final String PUSH_DIARY_COLLECTION = "push_diary";
    public static final String PUSH_DIARY_DOC_ID = "doc_id";
    public static final String PUSH_DIARY_DEVICE_ID = "device_id";
    public static final String PUSH_DIARY_USER_ID = "user_id";


    public static final String PUSH_DIARY_SCHEDULE_SOURCE = "diary_schedule_source";
    public static final String PUSH_DIARY_SAMPLE_TIME = "diary_sample_time";
    public static final String PUSH_DIARY_OPTION_READ = "target_history_candidate_read";
    public static final String PUSH_DIARY_OPTION_NOTI = "target_history_candidate_noti";

    public static final String PUSH_DIARY_NOTI_TIME = "noti_timestamp";
    public static final String PUSH_DIARY_RECEIEVE_TIME = "receieve_timestamp";
    public static final String PUSH_DIARY_OPEN_TIME = "open_timestamp";
    public static final String PUSH_DIARY_CLOSE_TIME = "close_timestamp";
    public static final String PUSH_DIARY_SUBMIT_TIME = "submit_timestamp";
    public static final String PUSH_DIARY_REMOVE_TIME = "remove_timestamp";
    public static final String PUSH_DIARY_REMOVE_TYPE = "remove_type";

    public static final String PUSH_DIARY_RESULT = "result";
    public static final String PUSH_DIARY_INOPPORTUNE_TARGET_READ = "inopportune_read_target";
    public static final String PUSH_DIARY_OPPORTUNE_TARGET_RAED = "opportune_read_target";
    public static final String PUSH_DIARY_INOPPORTUNE_TARGET_NOTI = "inopportune_noti_target";
    public static final String PUSH_DIARY_OPPORTUNE_TARGET_NOTI = "opportune_noti_target";

    public static final String PUSH_DIARY_DONE = "done";
    public static final String PUSH_DIARY_TRIGGER_BY_NOTIFICATION = "notification_service";
    public static final String PUSH_DIARY_TRIGGER_BY_ALARM = "alarm";
    public static final String PUSH_DIARY_TRIGGER_BY_SELF = "self";
    public static final String PUSH_DIARY_TRIGGER_BY = "trigger_by";

    //PUSH SERVICE
    public static final String PUSH_SERVICE_COLLECTION = "push_service";
    public static final String PUSH_SERVICE_RECEIEVE_TIME = "receieve_timestamp";
    //NOTIFICATION BAR
    public static final String NOTIFICATION_BAR_OTHER_APP_COLLECTION = "notification_bar(others)";
    public static final String NOTIFICATION_BAR_RECEIEVE_TIME = "receieve_timestamp";
    public static final String NOTIFICATION_BAR_REMOVE_TIME = "remove_timestamp";
    public static final String NOTIFICATION_BAR_REMOVE_TYPE = "remove_type";
    public static final String NOTIFICATION_BAR_PACKAGE_NAME = "package_name";
    public static final String NOTIFICATION_BAR_PACKAGE_ID = "package_id";
    public static final String NOTIFICATION_BAR_DEVICE_ID = "device_id";
    //SHARE PREFERENCE #############################################################################
    //SHARE PREFERENCE #############################################################################
    //SHARE PREFERENCE #############################################################################
    //SHARE PREFERENCE #############################################################################
    //SHARE PREFERENCE #############################################################################
    public static final String SHARE_PREFERENCE_CLEAR_CACHE = "SharePreferenceClear";
    public static final String SHARE_PREFERENCE_TEST_SIZE = "text_size";
    public static final String SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION = "media_select";
    public static final String SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER = "media_rank";
    public static final String SHARE_PREFERENCE_USER_ID = "signature";
    public static final String SHARE_NOTIFICATION_FIRST_CREATE = "new_group";
    public static final String SHARE_PREFERENCE_DEVICE_ID = "device_id";


    public static final String UPLOAD_TIME = "last_upload_time";





    //ESM
    public static final String ESM_NOTIFICATION_UNCLICKED_CANDIDATE = "PushNotificationNewsTitleArray";
    public static final String ESM_READ_HISTORY_CANDIDATE = "ReadingBehaviorNewsTitleArray";
    public static final String ESM_TARGET_NEWS_TITLE = "TargetNewsTitleArray";
    public static final String ESM_START_TIME_HOUR = "ESMStartTimeHour";
    public static final String ESM_START_TIME_MIN = "ESMStartTimeMin";
    public static final String ESM_END_TIME_HOUR = "ESMEndTimeHour";
    public static final String ESM_END_TIME_MIN = "ESMEndTimeMin";
    public static final String ESM_SET_ONCE = "ESMSetOnce";
    public static final String LAST_ESM_TIME = "LastEsmTime";
    public static final String LAST_DIARY_TIME = "LastDiaryTime";

    public static final String SAMPLE_ID = "sample_id";
    public static final String SAMPLE_TITLE = "sample_title";
    public static final String SAMPLE_MEDIA = "sample_media";
    public static final String SAMPLE_RECEIEVE = "sample_receieve";
    public static final String SAMPLE_IN = "sample_in";
    public static final String EXIST_READ = "exist_read";
    public static final String ESM_TYPE = "esm_type";


    public static final String ESM_DELAY_COUNT = "esm_delay_count";





    //DIARY
    public static final String DIARY_READ_HISTORY_CANDIDATE = "DiaryTargetOptionArrayRead";
    public static final String DIARY_NOTI_HISTORY_CANDIDATE = "DiaryTargetOptionArrayNoti";
    public static final String EXIST_ESM = "exist_esm";
    //{news_title}\n{news_time}\n{news_situation}\n{news_place}\n{news_id}#
    public static final String TO_DIARY_LIST = "DiaryList";
    //OTHER

    //ESM DIARY COUNT
    public static final String ESM_LAST_TIME = "ESMLastTime";
    public static final String DIARY_LAST_TIME = "DiaryLastTime";


    public static final String ESM_PUSH_TOTAL = "ESMPushTotal";
    public static final String ESM_DONE_TOTAL = "ESMDoneTotal";
    public static final String ESM_DAY_PUSH_PREFIX = "ESMDayPush_";
    public static final String ESM_DAY_DONE_PREFIX = "ESMDayDone_";

    public static final String DIARY_PUSH_TOTAL = "DiaryPushTotal";
    public static final String DIARY_DONE_TOTAL = "DiaryDoneTotal";
    public static final String DIARY_DAY_PUSH_PREFIX = "DiaryDayPush_";
    public static final String DIARY_DAY_DONE_PREFIX = "DiaryDayDone_";

    public static final String READ_TOTAL = "ReadTotal_";

//    public static final String CATEGORY_HASH_SET_SIZE = "category_hash_set_size";
//    public static final String CATEGORY_HASH_SET_PREFIX = "category_";




    public static final String DIARY_TITLE_CONTENT = "請填寫今天的日誌";
    public static final String DIARY_TEXT = "謝謝您的合作";


//    public static String JSON_TEMPLATE = "";

}