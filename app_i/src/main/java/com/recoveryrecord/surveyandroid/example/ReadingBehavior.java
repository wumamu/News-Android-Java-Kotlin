package com.recoveryrecord.surveyandroid.example;

public class ReadingBehavior {

    private String KEY_NEWS_ID;
    private String KEY_TRIGGER_BY;
    private String KEY_TIME_IN;
    private String KEY_TIME_OUT;
    private String KEY_CONTENT_LENGTH;
    private String KEY_DISPLAY_WIDTH;
    private String KEY_DISPLAY_HEIGHT;
    private long KEY_TIME_ON_PAGE;
    private int KEY_PAUSE_ON_PAGE;//
    private int KEY_VIEW_PORT_NUM;
    private String KEY_VIEW_PORT_RECORD;
    private int KEY_FLING_NUM;//
    private String KEY_FLING_RECORD;//
    private int KEY_DRAG_NUM;//
    private String KEY_DRAG_RECORD;//
    private int KEY_SHARE;//
    private String KEY_TIME_SERIES;
    private int KEY_BYTE_PER_LINE;
    private int KEY_ROW_SPACING;

    public ReadingBehavior() {
        this.KEY_NEWS_ID = "";
        this.KEY_TRIGGER_BY = "";
        this.KEY_TIME_IN = "";
        this.KEY_TIME_OUT = "";
        this.KEY_CONTENT_LENGTH = "";
        this.KEY_DISPLAY_WIDTH = "";
        this.KEY_DISPLAY_HEIGHT = "";
        this.KEY_TIME_ON_PAGE = 0;
        this.KEY_PAUSE_ON_PAGE = 0;
        this.KEY_VIEW_PORT_NUM = 0;
        this.KEY_VIEW_PORT_RECORD = "";
        this.KEY_FLING_NUM = 0;
        this.KEY_FLING_RECORD = "";
        this.KEY_DRAG_NUM = 0;
        this.KEY_DRAG_RECORD = "";
        this.KEY_SHARE = 0;
        this.KEY_TIME_SERIES = "";
        this.KEY_BYTE_PER_LINE = 0;
        this.KEY_ROW_SPACING = 0;
    }



    public ReadingBehavior(String KEY_NEWS_ID,
                           String KEY_TRIGGER_BY,
                           String KEY_TIME_IN,
                           String KEY_TIME_OUT,
                           String KEY_CONTENT_LENGTH,
                           String KEY_DISPLAY_WIDTH,
                           String KEY_DISPLAY_HEIGHT,
                           long KEY_TIME_ON_PAGE,
                           int KEY_PAUSE_ON_PAGE,
                           int KEY_VIEW_PORT_NUM,
                           String KEY_VIEW_PORT_RECORD,
                           int KEY_FLING_NUM,
                           String KEY_FLING_RECORD,
                           int KEY_DRAG_NUM,
                           String KEY_DRAG_RECORD,
                           int KEY_SHARE,
                           String KEY_TIME_SERIES,
                           int KEY_BYTE_PER_LINE,
                           int KEY_ROW_SPACING) {
        this.KEY_NEWS_ID = KEY_NEWS_ID;
        this.KEY_TRIGGER_BY = KEY_TRIGGER_BY;
        this.KEY_TIME_IN = KEY_TIME_IN;
        this.KEY_TIME_OUT = KEY_TIME_OUT;
        this.KEY_CONTENT_LENGTH = KEY_CONTENT_LENGTH;
        this.KEY_DISPLAY_WIDTH = KEY_DISPLAY_WIDTH;
        this.KEY_DISPLAY_HEIGHT = KEY_DISPLAY_HEIGHT;
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
        this.KEY_BYTE_PER_LINE = KEY_BYTE_PER_LINE;
        this.KEY_ROW_SPACING = KEY_ROW_SPACING;
    }


    public String getKEY_NEWS_ID() {
        return KEY_NEWS_ID;
    }

    public void setKEY_NEWS_ID(String KEY_NEWS_ID) {
        this.KEY_NEWS_ID = KEY_NEWS_ID;
    }

    public String getKEY_TRIGGER_BY() {
        return KEY_TRIGGER_BY;
    }

    public void setKEY_TRIGGER_BY(String KEY_TRIGGER_BY) {
        this.KEY_TRIGGER_BY = KEY_TRIGGER_BY;
    }

    public String getKEY_TIME_IN() {
        return KEY_TIME_IN;
    }

    public void setKEY_TIME_IN(String KEY_TIME_IN) {
        this.KEY_TIME_IN = KEY_TIME_IN;
    }

    public String getKEY_TIME_OUT() {
        return KEY_TIME_OUT;
    }

    public void setKEY_TIME_OUT(String KEY_TIME_OUT) {
        this.KEY_TIME_OUT = KEY_TIME_OUT;
    }

    public String getKEY_CONTENT_LENGTH() {
        return KEY_CONTENT_LENGTH;
    }

    public void setKEY_CONTENT_LENGTH(String KEY_CONTENT_LENGTH) {
        this.KEY_CONTENT_LENGTH = KEY_CONTENT_LENGTH;
    }

    public String getKEY_DISPLAY_WIDTH() {
        return KEY_DISPLAY_WIDTH;
    }

    public void setKEY_DISPLAY_WIDTH(String KEY_DISPLAY_WIDTH) {
        this.KEY_DISPLAY_WIDTH = KEY_DISPLAY_WIDTH;
    }

    public String getKEY_DISPLAY_HEIGHT() {
        return KEY_DISPLAY_HEIGHT;
    }

    public void setKEY_DISPLAY_HEIGHT(String KEY_DISPLAY_HEIGHT) {
        this.KEY_DISPLAY_HEIGHT = KEY_DISPLAY_HEIGHT;
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

    public int getKEY_SHARE() {
        return KEY_SHARE;
    }

    public void setKEY_SHARE(int KEY_SHARE) {
        this.KEY_SHARE = KEY_SHARE;
    }

    public String getKEY_TIME_SERIES() {
        return KEY_TIME_SERIES;
    }

    public void setKEY_TIME_SERIES(String KEY_TIME_SERIES) {
        this.KEY_TIME_SERIES = KEY_TIME_SERIES;
    }

    public int getKEY_BYTE_PER_LINE() {
        return KEY_BYTE_PER_LINE;
    }

    public void setKEY_BYTE_PER_LINE(int KEY_BYTE_PER_LINE) {
        this.KEY_BYTE_PER_LINE = KEY_BYTE_PER_LINE;
    }

    public int getKEY_ROW_SPACING() {
        return KEY_ROW_SPACING;
    }

    public void setKEY_ROW_SPACING(int KEY_ROW_SPACING) {
        this.KEY_ROW_SPACING = KEY_ROW_SPACING;
    }
}
