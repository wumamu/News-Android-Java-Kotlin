package com.recoveryrecord.surveyandroid.example.sqlite;

public class FBMovement {

    private String KEY_DOC_ID;
    private String KEY_DEVICE_ID;
    private String KEY_USER_ID;
    private String KEY_EVENT;
    private String KEY_STATE;
    private String KEY_STATE_TEXT;
    private String KEY_CONTENT;
    private long KEY_TIMESTAMP;

    public FBMovement() {
        this.KEY_DOC_ID = "NA";
        this.KEY_DEVICE_ID = "NA";
        this.KEY_USER_ID = "NA";
        this.KEY_EVENT = "NA";
        this.KEY_STATE = "NA";
        this.KEY_STATE_TEXT = "NA";
        this.KEY_CONTENT = "NA";
        this.KEY_TIMESTAMP = 0;
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

    public String getKEY_EVENT() {
        return KEY_EVENT;
    }

    public void setKEY_EVENT(String KEY_EVENT) {
        this.KEY_EVENT = KEY_EVENT;
    }

    public String getKEY_STATE() {
        return KEY_STATE;
    }

    public void setKEY_STATE(String KEY_STATE) {
        this.KEY_STATE = KEY_STATE;
    }

    public String getKEY_STATE_TEXT() {
        return KEY_STATE_TEXT;
    }

    public void setKEY_STATE_TEXT(String KEY_STATE_TEXT) {
        this.KEY_STATE_TEXT = KEY_STATE_TEXT;
    }

    public String getKEY_CONTENT() {
        return KEY_CONTENT;
    }

    public void setKEY_CONTENT(String KEY_CONTENT) {
        this.KEY_CONTENT = KEY_CONTENT;
    }

    public long getKEY_TIMESTAMP() {
        return KEY_TIMESTAMP;
    }

    public void setKEY_TIMESTAMP(long KEY_TIMESTAMP) {
        this.KEY_TIMESTAMP = KEY_TIMESTAMP;
    }
}
