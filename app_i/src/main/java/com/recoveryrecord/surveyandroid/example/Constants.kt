package com.recoveryrecord.surveyandroid.example

object Constants {
    //email
    const val OUR_EMAIL = "crctaiwan@gmail.com"

    //action
    const val CHECK_SERVICE_ACTION = "CheckService" //request code 50 60
    const val ESM_ALARM_ACTION = "EsmAlarm" //request code 0-15
    const val ESM_SCHEDULE_ID = "esm_schedule_id"
    const val SCHEDULE_SOURCE = "schedule_source"
    const val DIARY_ALARM_ACTION = "DiaryAlarm" //request code 100
    const val RESTART_ALARM_ACTION = "RestartAlarm" //request code 1050
    const val CANCEL_ALARM_ACTION = "CancelAlarm" //request code 1050
    const val SCHEDULE_ALARM_ACTION = "ScheduleAlarm" //request code 1000 1001

    //    public static final String UPLOAD_ACTION = "UploadSQLite";//request code 1000 1001
    const val SERVICE_CHECKER_INTERVAL = 20 * 60 * 1000 //every 20 min

    //other
    const val MY_APP_PACKAGE_NAME = "com.recoveryrecord.surveyandroid"
    const val CHINA_TIMES_PACKAGE_NAME = "cc.nexdoor.ct.activity"
    const val CNA_PACKAGE_NAME = "m.cna.com.tw.App"
    const val UDN_PACKAGE_NAME = "com.udn.news"
    const val LTN_PACKAGE_NAME = "com.ltnnews.news"
    const val ETTODAY_PACKAGE_NAME = "net.ettoday.phone"
    const val CTS_PACKAGE_NAME = "com.news.ctsapp"
    const val EBC_PACKAGE_NAME = "com.ebc.news"
    const val STORM_PACKAGE_NAME = "cc.nexdoor.stormmedia"
    const val SETS_PACKAGE_NAME = "com.set.newsapp"
    const val ZERO_RESULT_STRING = "zero_result"

    //    public static final String NA_STRING = "NA";
    //    public static final String NONE_STRING = "none";
    const val NEWS_LIMIT_PER_PAGE = 30
    const val PUSH_HISTORY_LIMIT_PER_PAGE = 50
    const val READ_HISTORY_LIMIT_PER_PAGE = 50

    @JvmField
    val VIBRATE_EFFECT = longArrayOf(100, 200, 300, 300, 500, 300, 300)
    const val ESM_CHANNEL_ID = "10001"
    const val NEWS_CHANNEL_ID = "10002"
    const val DIARY_CHANNEL_ID = "10003"
    const val DEFAULT_ESM_CHANNEL_ID = "default_esm"
    const val DEFAULT_NEWS_CHANNEL_ID = "default_news"
    const val DEFAULT_DIARY_CHANNEL_ID = "default_diary"
    const val DEFAULT_ESM_NOTIFICATION_ID = "notification-id"
    const val DEFAULT_NEWS_NOTIFICATION_ID = "notification-id"
    const val DEFAULT_DIARY_NOTIFICATION_ID = "notification-id"
    const val DEFAULT_ESM_NOTIFICATION = "notification"
    const val DEFAULT_NEWS_NOTIFICATION = "notification"
    const val DEFAULT_DIARY_NOTIFICATION = "notification"

    //INTENT PUT EXTRA
    //Login
    const val USER_NUM = "user_num"

    //NEWS NewsModuleActivity
    const val TRIGGER_BY_KEY = "trigger_by"
    const val TRIGGER_BY_VALUE_NOTIFICATION = "Notification"

    //    public static final String TRIGGER_BY_VALUE_SELF_TRIGGER = "self_trigger";
    const val NEWS_ID_KEY = "news_id"
    const val NEWS_MEDIA_KEY = "media"

    //NotificationListenerService
    const val GROUP_NEWS = "NewsMoment"
    const val GROUP_NEWS_SERVICE = "NewsMoment Service"
    const val DOC_ID_KEY = "id"
    const val NOTIFICATION_TYPE_KEY = "type"
    const val NOTIFICATION_ESM_TYPE_KEY = "esm_type" //0 noti 1 read
    const val NOTIFICATION_TYPE_VALUE_NEWS = "news"
    const val NOTIFICATION_TYPE_VALUE_ESM = "esm"
    const val NOTIFICATION_TYPE_VALUE_DIARY = "diary"
    const val NOTIFICATION_TYPE_VALUE_SERVICE = "service"

    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    const val TEST_USER_COLLECTION = "test_users"
    const val USER_COLLECTION = "user"
    const val APP_VERSION_KEY = "app_version"
    const val APP_VERSION_VALUE = "2022-09-07-1"
    const val UPDATE_TIME = "update_timestamp"

    //    public static final String INITIAL_TIME = "initial_timestamp";
    //    public static final String LAST_LAUNCH_TIME = "last_launch_timestamp";
    const val USER_DEVICE_ID = "device_id"
    const val USER_FIRESTORE_ID = "firestore_id"
    const val USER_PHONE_ID = "phone_id"
    const val USER_ANDROID_SDK = "android_sdk"
    const val USER_ANDROID_RELEASE = "android_release"
    const val PUSH_MEDIA_SELECTION = "push_media_selection"
    const val MEDIA_BAR_ORDER = "media_bar_order"
    const val USER_SURVEY_NUMBER = "user_survey_number"

    //MEDIA
    const val MEDIA_COLLECTION = "medias"
    const val NEWS_COLLECTION = "news"
    const val NEWS_URL = "url"

    //    public static final String NEWS_CATEGORY = "category";
    const val NEWS_TITLE = "title"
    const val NEWS_PUBDATE = "pubdate"
    const val NEWS_IMAGE = "image"
    const val NEWS_CONTENT = "content"

    //    public static final String NEWS_MEDIA = "media";
    const val NEWS_ID = "id"

    //NOTIFICATION BAR NEWS NOT APP
    const val NOTIFICATION_BAR_NEWS_MONITOR_COLLECTION = "notification_bar_news(not app)"

    const val NOTIFICATION_BAR_NEWS_TITLE = "title"
    const val NOTIFICATION_BAR_NEWS_TEXT = "text"
    const val NOTIFICATION_BAR_NEWS_NOTI_TIME = "noti_timestamp"
    const val NOTIFICATION_BAR_NEWS_PACKAGE_ID = "package_id"
    const val NOTIFICATION_BAR_NEWS_SOURCE = "media"
    const val NOTIFICATION_BAR_NEWS_DEVICE_ID = "device_id"

    //COMPARE RESULT
    const val COMPARE_RESULT_COLLECTION = "compare_result"
    const val COMPARE_RESULT_PUBDATE = "pubdate"
    const val COMPARE_RESULT_MEDIA = "media"
    const val COMPARE_RESULT_ID = "id"
    const val COMPARE_RESULT_NEW_TITLE = "news_title"

    //    public static final String COMPARE_RESULT_TYPE = "type";
    //    public static final String COMPARE_RESULT_CLICK = "click";
    //ALARM SCHEDULE
    const val SCHEDULE_ALARM_COLLECTION = "schedule_alarm"
    const val SCHEDULE_ALARM_TRIGGER_TIME = "trigger_time"
    const val SCHEDULE_ALARM_SURVEY_START = "survey_start_hour"
    const val SCHEDULE_ALARM_SURVEY_END = "survey_end_hour"
    const val SCHEDULE_ALARM_MAX_ESM = "max_esm"
    const val SCHEDULE_ALARM_DEVICE_ID = "device_id"
    const val SCHEDULE_ALARM_ACTION_TYPE = "action"

    //NEWS SERVICE CHECKER
    const val NEWS_SERVICE_COLLECTION = "news_service"
    const val NEWS_SERVICE_DEVICE_ID = "device_id"
    const val NEWS_SERVICE_TIME = "service_timestamp"
    const val NEWS_SERVICE_STATUS_KEY = "service_status"
    const val NEWS_SERVICE_STATUS_VALUE_RESTART = "service_restart"

    //    public static final String NEWS_SERVICE_STATUS_VALUE_FAILED = "service_failed";
    const val NEWS_SERVICE_STATUS_VALUE_RUNNING = "service_running"
    const val NEWS_SERVICE_STATUS_VALUE_INITIAL = "service_initial"
    const val NEWS_SERVICE_CYCLE_KEY = "service_cycle"
    const val NEWS_SERVICE_CYCLE_VALUE_BOOT_UP = "service_boot_up"
    const val NEWS_SERVICE_CYCLE_VALUE_ALARM = "alarm"
    const val NEWS_SERVICE_CYCLE_VALUE_FAILED_RESTART = "service_failed_restart"
    const val NEWS_SERVICE_CYCLE_VALUE_MAIN_PAGE = "main_page"
    const val NEWS_SERVICE_CYCLE_VALUE_SERVICE_PAGE = "service_page"

    //READING BEHAVIOR
    const val READING_BEHAVIOR_COLLECTION = "reading_behaviors"

    //    public static final String READING_BEHAVIOR_MID = " RB ";
    const val READING_BEHAVIOR_DOC_ID = "doc_id"
    const val READING_BEHAVIOR_DEVICE_ID = "device_id"
    const val READING_BEHAVIOR_USER_ID = "user_id"
    const val READING_BEHAVIOR_SAMPLE_CHECK_ID = "select_esm_id"
    const val READING_BEHAVIOR_TRIGGER_BY = "trigger_by"
    const val READING_BEHAVIOR_NEWS_ID = "id"
    const val READING_BEHAVIOR_TITLE = "title"
    const val READING_BEHAVIOR_MEDIA = "media"
    const val READING_BEHAVIOR_HAS_IMAGE = "has_img"
    const val READING_BEHAVIOR_IMAGE_URL = "image"
    const val READING_BEHAVIOR_PUBDATE = "pubdate"

    //    public static final String READING_BEHAVIOR_CATEGORY = "category";
    const val READING_BEHAVIOR_ROW_SPACING = "row_spacing(dp)"
    const val READING_BEHAVIOR_BYTE_PER_LINE = "byte_per_line"
    const val READING_BEHAVIOR_FONT_SIZE = "font_size"
    const val READING_BEHAVIOR_CONTENT_LENGTH = "content_length(dp)"
    const val READING_BEHAVIOR_DISPLAY_WIDTH = "display_width(dp)"
    const val READING_BEHAVIOR_DISPLAY_HEIGHT = "display_height(dp)"
    const val READING_BEHAVIOR_IN_TIME = "in_timestamp"
    const val READING_BEHAVIOR_OUT_TIME = "out_timestamp"
    const val READING_BEHAVIOR_IN_TIME_LONG = "in_timestamp_long"

    //    public static final String READING_BEHAVIOR_OUT_TIME_LONG = "out_timestamp_long";
    const val READING_BEHAVIOR_TIME_ON_PAGE = "time_on_page(s)"
    const val READING_BEHAVIOR_PAUSE_COUNT = "pause_count"
    const val READING_BEHAVIOR_VIEWPORT_NUM = "viewport_num"
    const val READING_BEHAVIOR_VIEWPORT_RECORD = "viewport_record"
    const val READING_BEHAVIOR_FLING_NUM = "fling_num"
    const val READING_BEHAVIOR_FLING_RECORD = "fling_record"
    const val READING_BEHAVIOR_DRAG_NUM = "drag_num"
    const val READING_BEHAVIOR_DRAG_RECORD = "drag_record"
    const val READING_BEHAVIOR_SHARE = "share"
    const val READING_BEHAVIOR_TIME_SERIES = "time_series(s)"
    const val READING_BEHAVIOR_TRIGGER_BY_NOTIFICATION = "Notification"

    //    public static final String READING_BEHAVIOR_TRIGGER_BY_SELF_TRIGGER = "self_trigger";
    //    public static final String READING_BEHAVIOR_CHECK_MARK = "check_mark";
    const val READING_BEHAVIOR_SAMPLE_CHECK = "select"

    //PUSH NEWS
    const val PUSH_NEWS_COLLECTION = "push_news"
    const val PUSH_NEWS_DOC_ID = "doc_id"
    const val PUSH_NEWS_DEVICE_ID = "device_id"
    const val PUSH_NEWS_USER_ID = "user_id"
    const val PUSH_NEWS_ID = "id"
    const val PUSH_NEWS_TITLE = "title"
    const val PUSH_NEWS_MEDIA = "media"
    const val PUSH_NEWS_PUBDATE = "pubdate"
    const val PUSH_NEWS_NOTI_TIME = "noti_timestamp"
    const val PUSH_NEWS_RECEIEVE_TIME = "receieve_timestamp"
    const val PUSH_NEWS_OPEN_TIME = "open_timestamp"
    const val PUSH_NEWS_REMOVE_TIME = "remove_timestamp"
    const val PUSH_NEWS_REMOVE_TYPE = "remove_type"

    //    public static final String PUSH_NEWS_READING_BEHAVIOR_ID = "reading_behavior_id";
    const val PUSH_NEWS_CLICK = "click"
    const val PUSH_NEWS_TYPE = "type"

    //PUSH SERVICE
    const val PUSH_SERVICE_COLLECTION = "push_service"
    const val PUSH_SERVICE_RECEIEVE_TIME = "receieve_timestamp"

    //NOTIFICATION BAR
    const val NOTIFICATION_BAR_OTHER_APP_COLLECTION = "notification_bar(others)"
    const val NOTIFICATION_BAR_RECEIEVE_TIME = "receieve_timestamp"
    const val NOTIFICATION_BAR_REMOVE_TIME = "remove_timestamp"
    const val NOTIFICATION_BAR_REMOVE_TYPE = "remove_type"
    const val NOTIFICATION_BAR_PACKAGE_NAME = "package_name"
    const val NOTIFICATION_BAR_PACKAGE_ID = "package_id"
    const val NOTIFICATION_BAR_DEVICE_ID = "device_id"

    //SHARE PREFERENCE #############################################################################
    //SHARE PREFERENCE #############################################################################
    //SHARE PREFERENCE #############################################################################
    //SHARE PREFERENCE #############################################################################
    //SHARE PREFERENCE #############################################################################
    const val SHARE_PREFERENCE_CLEAR_CACHE = "SharePreferenceClear"
    const val SHARE_PREFERENCE_TEST_SIZE = "text_size"
    const val SHARE_PREFERENCE_SILENT_ESM = "silent"
    const val SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION = "media_select"

    //    const val SHARE_PREFERENCE_MAIN_PAGE_MEDIA_ORDER = "media_rank"
    const val SHARE_PREFERENCE_USER_ID = "signature"
    const val SHARE_NOTIFICATION_FIRST_CREATE = "new_group"
    const val SHARE_PREFERENCE_DEVICE_ID = "device_id"
    const val UPLOAD_TIME = "last_upload_time"
    const val UPLOAD_TIME_FB = "last_upload_time_fb"
    const val MEDIA_ORDER = "media_order"
    const val DEFAULT_MEDIA_ORDER =
        "自由時報 5, 華視 3, ettoday 8, 風傳媒 6, 中央社 2, 聯合 7, 三立 9, 東森 4, 中時 1"

    //OTHER
    //ESM DIARY COUNT
    const val ESM_LAST_TIME = "ESMLastTime"
    const val DIARY_LAST_TIME = "DiaryLastTime"
    const val ESM_PUSH_TOTAL = "ESMPushTotal"
    const val ESM_DONE_TOTAL = "ESMDoneTotal"
    const val ESM_DAY_PUSH_PREFIX = "ESMDayPush_"
    const val ESM_DAY_DONE_PREFIX = "ESMDayDone_"
    const val DIARY_PUSH_TOTAL = "DiaryPushTotal"
    const val DIARY_DONE_TOTAL = "DiaryDoneTotal"
    const val DIARY_DAY_PUSH_PREFIX = "DiaryDayPush_"
    const val DIARY_DAY_DONE_PREFIX = "DiaryDayDone_"
    const val READ_TOTAL = "ReadTotal_"
}