package com.recoveryrecord.surveyandroid.example

object Constants {
    //email
    const val OUR_EMAIL = "crctaiwan@gmail.com"

    //other
    const val PACKAGE_NAME = "com.recoveryrecord.surveyandroid"
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

    const val NO_VALUE = "NA";

    //    public static final String NONE_STRING = "none";
    const val NEWS_LIMIT_PER_PAGE = 30L
    const val PUSH_HISTORY_LIMIT_PER_PAGE = 50
    const val READ_HISTORY_LIMIT_PER_PAGE = 50

    @JvmField
    val VIBRATE_EFFECT = longArrayOf(100, 200, 300, 300, 500, 300, 300)
    const val TEST_CHANNEL_ID = "10001"
    const val NEWS_CHANNEL_ID = "10002"
    const val DEFAULT_TEST_CHANNEL_ID = "default_test"
    const val DEFAULT_NEWS_CHANNEL_ID = "default_news"

    //NEWS NewsModuleActivity
    const val TRIGGER_BY_KEY = "trigger_by"
    const val TRIGGER_BY_VALUE_NOTIFICATION = "Notification"
    const val TRIGGER_BY_SELF = "self_trigger"

    const val NEWS_ID_KEY = "news_id"
    const val NEWS_MEDIA_KEY = "media"
    const val NEWS_TITLE_KEY = "title"

    //NotificationListenerService
    const val GROUP_NEWS = "NewsMoment"

    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    //FIRESTORE FIELD###############################################################################
    const val USER_COLLECTION = "user"
    const val APP_VERSION_KEY = "app_version"

    // TODO need to update every time
    const val APP_VERSION_VALUE = "2023/10/12"
    const val UPDATE_TIME = "update_timestamp"

    const val USER_DEVICE_ID = "device_id"
    const val USER_FIRESTORE_ID = "firestore_id"
    const val USER_PHONE_ID = "phone_id"
    const val USER_ANDROID_SDK = "android_sdk"
    const val USER_ANDROID_RELEASE = "android_release"
    const val PUSH_MEDIA_SELECTION = "push_media_selection"
    const val MEDIA_BAR_ORDER = "media_bar_order"
    const val USER_SURVEY_NAME = "user_survey_number"

    //MEDIA
    const val MEDIA_COLLECTION = "medias"
    const val NEWS_COLLECTION = "news"
    const val NEWS_URL = "url"

    const val NEWS_TITLE = "title"
    const val NEWS_PUBDATE = "pubdate"
    const val NEWS_IMAGE = "image"
    const val NEWS_CONTENT = "content"

    const val NEWS_ID = "id"

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
    const val PUSH_NEWS_TYPE = "type"

    //SHARE PREFERENCE #############################################################################
    const val SHARE_PREFERENCE_CLEAR_CACHE = "SharePreferenceClear"
    const val SHARE_PREFERENCE_TEST_SIZE = "text_size"
    const val SHARE_PREFERENCE_PUSH_NEWS_MEDIA_LIST_SELECTION = "media_select"

    const val SHARE_PREFERENCE_USER_NAME = "signature"
    const val UNKNOWN_USER_NAME = "尚未設定實驗編號"
    const val MEDIA_ORDER = "media_order"


    const val READ_TOTAL = "ReadTotal_"

    // share preference for fetch
    const val LAST_UPDATE_TIME = "last_update_time"
    const val CATEGORY_POST_FIX = "_category"

    // collection path
    const val NEWS_CATEGORY = "news_category"
    const val CONFIG = "config"

    const val STORM_ENG = "storm"
    const val SETN_ENG = "setn"
    const val LTN_ENG = "ltn"
    const val ETTODAY_ENG = "ettoday"
    const val EBC_ENG = "ebc"
    const val CTS_ENG = "cts"
    const val CNA_ENG = "cna"
    const val CHINATIMES_ENG = "chinatimes"
    const val UDN_ENG = "udn"

    const val STORM_CHI = "風傳媒"
    const val SETN_CHI = "三立新聞網"
    const val LTN_CHI = "自由時報"
    const val ETTODAY_CHI = "Ettoday"
    const val EBC_CHI = "東森"
    const val CTS_CHI = "華視新聞"
    const val CNA_CHI = "中央社"
    const val CHINATIMES_CHI = "旺旺中時"
    const val UDN_CHI = "聯合"
}