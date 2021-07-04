package com.recoveryrecord.surveyandroid.example.sqlite;

public class ESM {
    private String KEY_DOC_ID;
    private String KEY_DEVICE_ID;
    private String KEY_USER_ID;

    private int KEY_ESM_TYPE;
    private String KEY_NOTI_SAMPLE;//by notification
    private String KEY_SELF_READ_SAMPLE;//self trigger

    //initiate
    private String KEY_ESM_SCHEDULE_ID;
    private String KEY_ESM_SCHEDULE_SOURCE;
    //open esm query db
    private long KEY_ESM_SAMPLE_TIME;
    private int KEY_SAMPLE;
    //default 0
    //answer without read 1
    //answer with read 2 -> 3
    //open diary
    private String KEY_SELECT_DIARY_ID;

    private long KEY_NOTI_TIMESTAMP;
    private long KEY_RECEIEVE_TIMESTAMP;
    private long KEY_OPEN_TIMESTAMP;
    private long KEY_CLOSE_TIMESTAMP;
    private long KEY_SUBMIT_TIMESTAMP;
    private long KEY_REMOVE_TIMESTAMP;
    private String KEY_REMOVE_TYPE;

    private String KEY_RESULT;
    //type 0
    private String KEY_NOTI_READ_NEWS_ID;
    private String KEY_NOTI_READ_TITLE;
    private String KEY_NOTI_READ_IN_TIME;
    private long KEY_NOTI_READ_RECEIEVE_TIME;
    private String KEY_NOTI_READ_SITUATION;
    private String KEY_NOTI_READ_PLACE;
    private String KEY_NOTI_NOT_READ_NEWS_ID;
    private String KEY_NOTI_NOT_READ_TITLE;
    private long KEY_NOTI_NOT_READ_RECEIEVE_TIME;
    //type 1
    private String KEY_SELF_READ_NEWS_ID;
    private String KEY_SELF_READ_TITLE;
    private long KEY_SELF_READ_IN_TIME;
    private long KEY_SELF_READ_RECEIEVE_TIME;
    private String KEY_SELF_READ_SITUATION;
    private String KEY_SELF_READ_PLACE;

    public ESM() {
        this.KEY_DOC_ID = "NA";
        this.KEY_DEVICE_ID = "NA";
        this.KEY_USER_ID = "NA";

        this.KEY_ESM_TYPE = 0;
        this.KEY_NOTI_SAMPLE = "NA";
        this.KEY_SELF_READ_SAMPLE = "NA";

        this.KEY_ESM_SCHEDULE_ID = "NA";
        this.KEY_ESM_SCHEDULE_SOURCE = "NA";
        this.KEY_ESM_SAMPLE_TIME = 0;
        this.KEY_SAMPLE = 0;
        this.KEY_SELECT_DIARY_ID = "NA";

        this.KEY_NOTI_TIMESTAMP = 0;
        this.KEY_RECEIEVE_TIMESTAMP = 0;
        this.KEY_OPEN_TIMESTAMP = 0;
        this.KEY_CLOSE_TIMESTAMP = 0;
        this.KEY_SUBMIT_TIMESTAMP = 0;
        this.KEY_REMOVE_TIMESTAMP = 0;
        this.KEY_REMOVE_TYPE = "NA";

        this.KEY_RESULT = "NA";

        this.KEY_NOTI_READ_NEWS_ID = "NA";
        this.KEY_NOTI_READ_TITLE = "NA";
        this.KEY_NOTI_READ_IN_TIME = "NA";
        this.KEY_NOTI_READ_RECEIEVE_TIME = 0;
        this.KEY_NOTI_READ_SITUATION = "NA";
        this.KEY_NOTI_READ_PLACE = "NA";
        this.KEY_NOTI_NOT_READ_NEWS_ID = "NA";
        this.KEY_NOTI_NOT_READ_TITLE = "NA";
        this.KEY_NOTI_NOT_READ_RECEIEVE_TIME = 0;

        this.KEY_SELF_READ_NEWS_ID = "NA";
        this.KEY_SELF_READ_TITLE = "NA";
        this.KEY_SELF_READ_IN_TIME = 0;
        this.KEY_SELF_READ_RECEIEVE_TIME = 0;
        this.KEY_SELF_READ_SITUATION = "NA";
        this.KEY_SELF_READ_PLACE = "NA";
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

    public int getKEY_ESM_TYPE() {
        return KEY_ESM_TYPE;
    }

    public void setKEY_ESM_TYPE(int KEY_ESM_TYPE) {
        this.KEY_ESM_TYPE = KEY_ESM_TYPE;
    }

    public String getKEY_NOTI_SAMPLE() {
        return KEY_NOTI_SAMPLE;
    }

    public void setKEY_NOTI_SAMPLE(String KEY_NOTI_SAMPLE) {
        this.KEY_NOTI_SAMPLE = KEY_NOTI_SAMPLE;
    }

    public String getKEY_SELF_READ_SAMPLE() {
        return KEY_SELF_READ_SAMPLE;
    }

    public void setKEY_SELF_READ_SAMPLE(String KEY_SELF_READ_SAMPLE) {
        this.KEY_SELF_READ_SAMPLE = KEY_SELF_READ_SAMPLE;
    }

    public String getKEY_ESM_SCHEDULE_ID() {
        return KEY_ESM_SCHEDULE_ID;
    }

    public void setKEY_ESM_SCHEDULE_ID(String KEY_ESM_SCHEDULE_ID) {
        this.KEY_ESM_SCHEDULE_ID = KEY_ESM_SCHEDULE_ID;
    }

    public String getKEY_ESM_SCHEDULE_SOURCE() {
        return KEY_ESM_SCHEDULE_SOURCE;
    }

    public void setKEY_ESM_SCHEDULE_SOURCE(String KEY_ESM_SCHEDULE_SOURCE) {
        this.KEY_ESM_SCHEDULE_SOURCE = KEY_ESM_SCHEDULE_SOURCE;
    }

    public long getKEY_ESM_SAMPLE_TIME() {
        return KEY_ESM_SAMPLE_TIME;
    }

    public void setKEY_ESM_SAMPLE_TIME(long KEY_ESM_SAMPLE_TIME) {
        this.KEY_ESM_SAMPLE_TIME = KEY_ESM_SAMPLE_TIME;
    }

    public int getKEY_SAMPLE() {
        return KEY_SAMPLE;
    }

    public void setKEY_SAMPLE(int KEY_SAMPLE) {
        this.KEY_SAMPLE = KEY_SAMPLE;
    }

    public String getKEY_SELECT_DIARY_ID() {
        return KEY_SELECT_DIARY_ID;
    }

    public void setKEY_SELECT_DIARY_ID(String KEY_SELECT_DIARY_ID) {
        this.KEY_SELECT_DIARY_ID = KEY_SELECT_DIARY_ID;
    }

    public long getKEY_NOTI_TIMESTAMP() {
        return KEY_NOTI_TIMESTAMP;
    }

    public void setKEY_NOTI_TIMESTAMP(long KEY_NOTI_TIMESTAMP) {
        this.KEY_NOTI_TIMESTAMP = KEY_NOTI_TIMESTAMP;
    }

    public long getKEY_RECEIEVE_TIMESTAMP() {
        return KEY_RECEIEVE_TIMESTAMP;
    }

    public void setKEY_RECEIEVE_TIMESTAMP(long KEY_RECEIEVE_TIMESTAMP) {
        this.KEY_RECEIEVE_TIMESTAMP = KEY_RECEIEVE_TIMESTAMP;
    }

    public long getKEY_OPEN_TIMESTAMP() {
        return KEY_OPEN_TIMESTAMP;
    }

    public void setKEY_OPEN_TIMESTAMP(long KEY_OPEN_TIMESTAMP) {
        this.KEY_OPEN_TIMESTAMP = KEY_OPEN_TIMESTAMP;
    }

    public long getKEY_CLOSE_TIMESTAMP() {
        return KEY_CLOSE_TIMESTAMP;
    }

    public void setKEY_CLOSE_TIMESTAMP(long KEY_CLOSE_TIMESTAMP) {
        this.KEY_CLOSE_TIMESTAMP = KEY_CLOSE_TIMESTAMP;
    }

    public long getKEY_SUBMIT_TIMESTAMP() {
        return KEY_SUBMIT_TIMESTAMP;
    }

    public void setKEY_SUBMIT_TIMESTAMP(long KEY_SUBMIT_TIMESTAMP) {
        this.KEY_SUBMIT_TIMESTAMP = KEY_SUBMIT_TIMESTAMP;
    }

    public long getKEY_REMOVE_TIMESTAMP() {
        return KEY_REMOVE_TIMESTAMP;
    }

    public void setKEY_REMOVE_TIMESTAMP(long KEY_REMOVE_TIMESTAMP) {
        this.KEY_REMOVE_TIMESTAMP = KEY_REMOVE_TIMESTAMP;
    }

    public String getKEY_REMOVE_TYPE() {
        return KEY_REMOVE_TYPE;
    }

    public void setKEY_REMOVE_TYPE(String KEY_REMOVE_TYPE) {
        this.KEY_REMOVE_TYPE = KEY_REMOVE_TYPE;
    }

    public String getKEY_RESULT() {
        return KEY_RESULT;
    }

    public void setKEY_RESULT(String KEY_RESULT) {
        this.KEY_RESULT = KEY_RESULT;
    }

    public String getKEY_NOTI_READ_NEWS_ID() {
        return KEY_NOTI_READ_NEWS_ID;
    }

    public void setKEY_NOTI_READ_NEWS_ID(String KEY_NOTI_READ_NEWS_ID) {
        this.KEY_NOTI_READ_NEWS_ID = KEY_NOTI_READ_NEWS_ID;
    }

    public String getKEY_NOTI_READ_TITLE() {
        return KEY_NOTI_READ_TITLE;
    }

    public void setKEY_NOTI_READ_TITLE(String KEY_NOTI_READ_TITLE) {
        this.KEY_NOTI_READ_TITLE = KEY_NOTI_READ_TITLE;
    }

    public String getKEY_NOTI_READ_IN_TIME() {
        return KEY_NOTI_READ_IN_TIME;
    }

    public void setKEY_NOTI_READ_IN_TIME(String KEY_NOTI_READ_IN_TIME) {
        this.KEY_NOTI_READ_IN_TIME = KEY_NOTI_READ_IN_TIME;
    }

    public long getKEY_NOTI_READ_RECEIEVE_TIME() {
        return KEY_NOTI_READ_RECEIEVE_TIME;
    }

    public void setKEY_NOTI_READ_RECEIEVE_TIME(long KEY_NOTI_READ_RECEIEVE_TIME) {
        this.KEY_NOTI_READ_RECEIEVE_TIME = KEY_NOTI_READ_RECEIEVE_TIME;
    }

    public String getKEY_NOTI_READ_SITUATION() {
        return KEY_NOTI_READ_SITUATION;
    }

    public void setKEY_NOTI_READ_SITUATION(String KEY_NOTI_READ_SITUATION) {
        this.KEY_NOTI_READ_SITUATION = KEY_NOTI_READ_SITUATION;
    }

    public String getKEY_NOTI_READ_PLACE() {
        return KEY_NOTI_READ_PLACE;
    }

    public void setKEY_NOTI_READ_PLACE(String KEY_NOTI_READ_PLACE) {
        this.KEY_NOTI_READ_PLACE = KEY_NOTI_READ_PLACE;
    }

    public String getKEY_NOTI_NOT_READ_NEWS_ID() {
        return KEY_NOTI_NOT_READ_NEWS_ID;
    }

    public void setKEY_NOTI_NOT_READ_NEWS_ID(String KEY_NOTI_NOT_READ_NEWS_ID) {
        this.KEY_NOTI_NOT_READ_NEWS_ID = KEY_NOTI_NOT_READ_NEWS_ID;
    }

    public String getKEY_NOTI_NOT_READ_TITLE() {
        return KEY_NOTI_NOT_READ_TITLE;
    }

    public void setKEY_NOTI_NOT_READ_TITLE(String KEY_NOTI_NOT_READ_TITLE) {
        this.KEY_NOTI_NOT_READ_TITLE = KEY_NOTI_NOT_READ_TITLE;
    }

    public long getKEY_NOTI_NOT_READ_RECEIEVE_TIME() {
        return KEY_NOTI_NOT_READ_RECEIEVE_TIME;
    }

    public void setKEY_NOTI_NOT_READ_RECEIEVE_TIME(long KEY_NOTI_NOT_READ_RECEIEVE_TIME) {
        this.KEY_NOTI_NOT_READ_RECEIEVE_TIME = KEY_NOTI_NOT_READ_RECEIEVE_TIME;
    }

    public String getKEY_SELF_READ_NEWS_ID() {
        return KEY_SELF_READ_NEWS_ID;
    }

    public void setKEY_SELF_READ_NEWS_ID(String KEY_SELF_READ_NEWS_ID) {
        this.KEY_SELF_READ_NEWS_ID = KEY_SELF_READ_NEWS_ID;
    }

    public String getKEY_SELF_READ_TITLE() {
        return KEY_SELF_READ_TITLE;
    }

    public void setKEY_SELF_READ_TITLE(String KEY_SELF_READ_TITLE) {
        this.KEY_SELF_READ_TITLE = KEY_SELF_READ_TITLE;
    }

    public long getKEY_SELF_READ_IN_TIME() {
        return KEY_SELF_READ_IN_TIME;
    }

    public void setKEY_SELF_READ_IN_TIME(long KEY_SELF_READ_IN_TIME) {
        this.KEY_SELF_READ_IN_TIME = KEY_SELF_READ_IN_TIME;
    }

    public long getKEY_SELF_READ_RECEIEVE_TIME() {
        return KEY_SELF_READ_RECEIEVE_TIME;
    }

    public void setKEY_SELF_READ_RECEIEVE_TIME(long KEY_SELF_READ_RECEIEVE_TIME) {
        this.KEY_SELF_READ_RECEIEVE_TIME = KEY_SELF_READ_RECEIEVE_TIME;
    }

    public String getKEY_SELF_READ_SITUATION() {
        return KEY_SELF_READ_SITUATION;
    }

    public void setKEY_SELF_READ_SITUATION(String KEY_SELF_READ_SITUATION) {
        this.KEY_SELF_READ_SITUATION = KEY_SELF_READ_SITUATION;
    }

    public String getKEY_SELF_READ_PLACE() {
        return KEY_SELF_READ_PLACE;
    }

    public void setKEY_SELF_READ_PLACE(String KEY_SELF_READ_PLACE) {
        this.KEY_SELF_READ_PLACE = KEY_SELF_READ_PLACE;
    }
}
