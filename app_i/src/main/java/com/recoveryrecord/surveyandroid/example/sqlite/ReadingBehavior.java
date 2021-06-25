package com.recoveryrecord.surveyandroid.example.sqlite;

public class ReadingBehavior {

    private String KEY_DOC_ID;
    private String KEY_DEVICE_ID;
    private String KEY_USER_ID;
    private String KEY_SELECT_ESM_ID;
    private String KEY_TRIGGER_BY;
    private String KEY_NEWS_ID;
    private String KEY_TITLE;
    private String KEY_MEDIA;
    private int KEY_HAS_IMG;
    private long KEY_PUBDATE;
    private int KEY_ROW_SPACING;
    private int KEY_BYTE_PER_LINE;
    private String KEY_FONT_SIZE;
    private int KEY_CONTENT_LENGTH;
    private float KEY_DISPLAY_WIDTH;
    private float KEY_DISPLAY_HEIGHT;
    private long KEY_IN_TIMESTAMP;
    private long KEY_OUT_TIMESTAMP;
    private long KEY_TIME_ON_PAGE;
    private int KEY_PAUSE_ON_PAGE;//
    private int KEY_VIEW_PORT_NUM;
    private String KEY_VIEW_PORT_RECORD;
    private int KEY_FLING_NUM;//
    private String KEY_FLING_RECORD;//
    private int KEY_DRAG_NUM;//
    private String KEY_DRAG_RECORD;//
    private String KEY_SHARE;//
    private String KEY_TIME_SERIES;

    public ReadingBehavior() {
        this.KEY_DOC_ID = "NA";
        this.KEY_DEVICE_ID = "NA";
        this.KEY_USER_ID = "NA";
        this.KEY_SELECT_ESM_ID = "NA";
        this.KEY_TRIGGER_BY = "NA";
        this.KEY_NEWS_ID = "NA";
        this.KEY_TITLE = "NA";
        this.KEY_MEDIA = "NA";
        this.KEY_HAS_IMG = 0;
        this.KEY_PUBDATE = 0;
        this.KEY_ROW_SPACING = 0;
        this.KEY_BYTE_PER_LINE = 0;
        this.KEY_FONT_SIZE = "NA";
        this.KEY_CONTENT_LENGTH = 0;
        this.KEY_DISPLAY_WIDTH = 0;
        this.KEY_DISPLAY_HEIGHT = 0;
        this.KEY_IN_TIMESTAMP = 0;
        this.KEY_OUT_TIMESTAMP = 0;
        this.KEY_TIME_ON_PAGE = 0;
        this.KEY_PAUSE_ON_PAGE = 0;
        this.KEY_VIEW_PORT_NUM = 0;
        this.KEY_VIEW_PORT_RECORD = "NA";
        this.KEY_FLING_NUM = 0;
        this.KEY_FLING_RECORD = "NA";
        this.KEY_DRAG_NUM = 0;
        this.KEY_DRAG_RECORD = "NA";
        this.KEY_SHARE = "NA";
        this.KEY_TIME_SERIES = "NA";
    }



    public ReadingBehavior(String KEY_DOC_ID,
                           String KEY_DEVICE_ID,
                           String KEY_USER_ID,
                           String KEY_SELECT_ESM_ID,
                           String KEY_TRIGGER_BY,
                           String KEY_NEWS_ID,
                           String KEY_TITLE,
                           String KEY_MEDIA,
                           int KEY_HAS_IMG,
                           long KEY_PUBDATE,
                           int KEY_ROW_SPACING,
                           int KEY_BYTE_PER_LINE,
                           String KEY_FONT_SIZE,
                           int KEY_CONTENT_LENGTH,
                           float KEY_DISPLAY_WIDTH,
                           float KEY_DISPLAY_HEIGHT,
                           long KEY_IN_TIMESTAMP,
                           long KEY_OUT_TIMESTAMP,
                           long KEY_TIME_ON_PAGE,
                           int KEY_PAUSE_ON_PAGE,
                           int KEY_VIEW_PORT_NUM,
                           String KEY_VIEW_PORT_RECORD,
                           int KEY_FLING_NUM,
                           String KEY_FLING_RECORD,
                           int KEY_DRAG_NUM,
                           String KEY_DRAG_RECORD,
                           String KEY_SHARE,
                           String KEY_TIME_SERIES) {
        this.KEY_DOC_ID = KEY_DOC_ID;
        this.KEY_DEVICE_ID = KEY_DEVICE_ID;
        this.KEY_USER_ID = KEY_USER_ID;
        this.KEY_SELECT_ESM_ID = KEY_SELECT_ESM_ID;
        this.KEY_TRIGGER_BY = KEY_TRIGGER_BY;
        this.KEY_NEWS_ID = KEY_NEWS_ID;
        this.KEY_TITLE = KEY_TITLE;
        this.KEY_MEDIA = KEY_MEDIA;
        this.KEY_HAS_IMG = KEY_HAS_IMG;
        this.KEY_PUBDATE = KEY_PUBDATE;
        this.KEY_ROW_SPACING = KEY_ROW_SPACING;
        this.KEY_BYTE_PER_LINE = KEY_BYTE_PER_LINE;
        this.KEY_FONT_SIZE = KEY_FONT_SIZE;
        this.KEY_CONTENT_LENGTH = KEY_CONTENT_LENGTH;
        this.KEY_DISPLAY_WIDTH = KEY_DISPLAY_WIDTH;
        this.KEY_DISPLAY_HEIGHT = KEY_DISPLAY_HEIGHT;
        this.KEY_IN_TIMESTAMP = KEY_IN_TIMESTAMP;
        this.KEY_OUT_TIMESTAMP = KEY_OUT_TIMESTAMP;
        this.KEY_TIME_ON_PAGE = KEY_TIME_ON_PAGE;
        this.KEY_PAUSE_ON_PAGE = KEY_PAUSE_ON_PAGE;
        this.KEY_VIEW_PORT_NUM = KEY_VIEW_PORT_NUM;
        this.KEY_VIEW_PORT_RECORD = KEY_VIEW_PORT_RECORD;
        this.KEY_FLING_NUM = KEY_FLING_NUM;
        this.KEY_FLING_RECORD = KEY_FLING_RECORD;
        this.KEY_DRAG_NUM = KEY_DRAG_NUM;
        this.KEY_DRAG_RECORD = KEY_DRAG_RECORD;
        this.KEY_SHARE = KEY_SHARE;
        this.KEY_TIME_SERIES = KEY_TIME_SERIES;
    }

    public String getKEY_DOC_ID() {
        return KEY_DOC_ID;
    }

    public void setKEY_DOC_ID(String KEY_DOC_ID) {
        this.KEY_DOC_ID = KEY_DOC_ID;
    }

    public String getKEY_DEVICE_ID() {
        return KEY_DEVICE_ID;
    }

    public void setKEY_DEVICE_ID(String KEY_DEVICE_ID) {
        this.KEY_DEVICE_ID = KEY_DEVICE_ID;
    }

    public String getKEY_USER_ID() {
        return KEY_USER_ID;
    }

    public void setKEY_USER_ID(String KEY_USER_ID) {
        this.KEY_USER_ID = KEY_USER_ID;
    }

    public String getKEY_SELECT_ESM_ID() {
        return KEY_SELECT_ESM_ID;
    }

    public void setKEY_SELECT_ESM_ID(String KEY_SELECT_ESM_ID) {
        this.KEY_SELECT_ESM_ID = KEY_SELECT_ESM_ID;
    }

    public String getKEY_TRIGGER_BY() {
        return KEY_TRIGGER_BY;
    }

    public void setKEY_TRIGGER_BY(String KEY_TRIGGER_BY) {
        this.KEY_TRIGGER_BY = KEY_TRIGGER_BY;
    }

    public String getKEY_NEWS_ID() {
        return KEY_NEWS_ID;
    }

    public void setKEY_NEWS_ID(String KEY_NEWS_ID) {
        this.KEY_NEWS_ID = KEY_NEWS_ID;
    }

    public String getKEY_TITLE() {
        return KEY_TITLE;
    }

    public void setKEY_TITLE(String KEY_TITLE) {
        this.KEY_TITLE = KEY_TITLE;
    }

    public String getKEY_MEDIA() {
        return KEY_MEDIA;
    }

    public void setKEY_MEDIA(String KEY_MEDIA) {
        this.KEY_MEDIA = KEY_MEDIA;
    }

    public int getKEY_HAS_IMG() {
        return KEY_HAS_IMG;
    }

    public void setKEY_HAS_IMG(int KEY_HAS_IMG) {
        this.KEY_HAS_IMG = KEY_HAS_IMG;
    }

    public long getKEY_PUBDATE() {
        return KEY_PUBDATE;
    }

    public void setKEY_PUBDATE(long KEY_PUBDATE) {
        this.KEY_PUBDATE = KEY_PUBDATE;
    }

    public int getKEY_ROW_SPACING() {
        return KEY_ROW_SPACING;
    }

    public void setKEY_ROW_SPACING(int KEY_ROW_SPACING) {
        this.KEY_ROW_SPACING = KEY_ROW_SPACING;
    }

    public int getKEY_BYTE_PER_LINE() {
        return KEY_BYTE_PER_LINE;
    }

    public void setKEY_BYTE_PER_LINE(int KEY_BYTE_PER_LINE) {
        this.KEY_BYTE_PER_LINE = KEY_BYTE_PER_LINE;
    }

    public String getKEY_FONT_SIZE() {
        return KEY_FONT_SIZE;
    }

    public void setKEY_FONT_SIZE(String KEY_FONT_SIZE) {
        this.KEY_FONT_SIZE = KEY_FONT_SIZE;
    }

    public int getKEY_CONTENT_LENGTH() {
        return KEY_CONTENT_LENGTH;
    }

    public void setKEY_CONTENT_LENGTH(int KEY_CONTENT_LENGTH) {
        this.KEY_CONTENT_LENGTH = KEY_CONTENT_LENGTH;
    }

    public float getKEY_DISPLAY_WIDTH() {
        return KEY_DISPLAY_WIDTH;
    }

    public void setKEY_DISPLAY_WIDTH(float KEY_DISPLAY_WIDTH) {
        this.KEY_DISPLAY_WIDTH = KEY_DISPLAY_WIDTH;
    }

    public float getKEY_DISPLAY_HEIGHT() {
        return KEY_DISPLAY_HEIGHT;
    }

    public void setKEY_DISPLAY_HEIGHT(float KEY_DISPLAY_HEIGHT) {
        this.KEY_DISPLAY_HEIGHT = KEY_DISPLAY_HEIGHT;
    }

    public long getKEY_IN_TIMESTAMP() {
        return KEY_IN_TIMESTAMP;
    }

    public void setKEY_IN_TIMESTAMP(long KEY_IN_TIMESTAMP) {
        this.KEY_IN_TIMESTAMP = KEY_IN_TIMESTAMP;
    }

    public long getKEY_OUT_TIMESTAMP() {
        return KEY_OUT_TIMESTAMP;
    }

    public void setKEY_OUT_TIMESTAMP(long KEY_OUT_TIMESTAMP) {
        this.KEY_OUT_TIMESTAMP = KEY_OUT_TIMESTAMP;
    }

    public long getKEY_TIME_ON_PAGE() {
        return KEY_TIME_ON_PAGE;
    }

    public void setKEY_TIME_ON_PAGE(long KEY_TIME_ON_PAGE) {
        this.KEY_TIME_ON_PAGE = KEY_TIME_ON_PAGE;
    }

    public int getKEY_PAUSE_ON_PAGE() {
        return KEY_PAUSE_ON_PAGE;
    }

    public void setKEY_PAUSE_ON_PAGE(int KEY_PAUSE_ON_PAGE) {
        this.KEY_PAUSE_ON_PAGE = KEY_PAUSE_ON_PAGE;
    }

    public int getKEY_VIEW_PORT_NUM() {
        return KEY_VIEW_PORT_NUM;
    }

    public void setKEY_VIEW_PORT_NUM(int KEY_VIEW_PORT_NUM) {
        this.KEY_VIEW_PORT_NUM = KEY_VIEW_PORT_NUM;
    }

    public String getKEY_VIEW_PORT_RECORD() {
        return KEY_VIEW_PORT_RECORD;
    }

    public void setKEY_VIEW_PORT_RECORD(String KEY_VIEW_PORT_RECORD) {
        this.KEY_VIEW_PORT_RECORD = KEY_VIEW_PORT_RECORD;
    }

    public int getKEY_FLING_NUM() {
        return KEY_FLING_NUM;
    }

    public void setKEY_FLING_NUM(int KEY_FLING_NUM) {
        this.KEY_FLING_NUM = KEY_FLING_NUM;
    }

    public String getKEY_FLING_RECORD() {
        return KEY_FLING_RECORD;
    }

    public void setKEY_FLING_RECORD(String KEY_FLING_RECORD) {
        this.KEY_FLING_RECORD = KEY_FLING_RECORD;
    }

    public int getKEY_DRAG_NUM() {
        return KEY_DRAG_NUM;
    }

    public void setKEY_DRAG_NUM(int KEY_DRAG_NUM) {
        this.KEY_DRAG_NUM = KEY_DRAG_NUM;
    }

    public String getKEY_DRAG_RECORD() {
        return KEY_DRAG_RECORD;
    }

    public void setKEY_DRAG_RECORD(String KEY_DRAG_RECORD) {
        this.KEY_DRAG_RECORD = KEY_DRAG_RECORD;
    }

    public String getKEY_SHARE() {
        return KEY_SHARE;
    }

    public void setKEY_SHARE(String KEY_SHARE) {
        this.KEY_SHARE = KEY_SHARE;
    }

    public String getKEY_TIME_SERIES() {
        return KEY_TIME_SERIES;
    }

    public void setKEY_TIME_SERIES(String KEY_TIME_SERIES) {
        this.KEY_TIME_SERIES = KEY_TIME_SERIES;
    }
}
