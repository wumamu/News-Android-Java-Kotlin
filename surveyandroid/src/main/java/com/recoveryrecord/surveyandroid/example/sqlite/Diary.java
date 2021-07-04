package com.recoveryrecord.surveyandroid.example.sqlite;

public class Diary {
    private String KEY_DOC_ID;
    private String KEY_DEVICE_ID;
    private String KEY_USER_ID;

    private String KEY_ESM_RESULT_SAMPLE;//
    private String KEY_DIARY_SCHEDULE_SOURCE;
    private long KEY_DIARY_SAMPLE_TIME;

    private long KEY_NOTI_TIMESTAMP;
    private long KEY_RECEIEVE_TIMESTAMP;
    private long KEY_OPEN_TIMESTAMP;
    private long KEY_CLOSE_TIMESTAMP;
    private long KEY_SUBMIT_TIMESTAMP;
    private long KEY_REMOVE_TIMESTAMP;
    private String KEY_REMOVE_TYPE;

    private String KEY_RESULT;
    private String KEY_INOPPORTUNTE_RESULT;
    private String KEY_OPPORTUNTE_RESULT;

    public Diary() {
        this.KEY_DOC_ID = "NA";
        this.KEY_DEVICE_ID = "NA";
        this.KEY_USER_ID = "NA";

        this.KEY_ESM_RESULT_SAMPLE = "NA";//option
        this.KEY_DIARY_SCHEDULE_SOURCE = "NA";
        this.KEY_DIARY_SAMPLE_TIME = 0;

        this.KEY_NOTI_TIMESTAMP = 0;
        this.KEY_RECEIEVE_TIMESTAMP = 0;
        this.KEY_OPEN_TIMESTAMP = 0;
        this.KEY_CLOSE_TIMESTAMP = 0;
        this.KEY_SUBMIT_TIMESTAMP = 0;
        this.KEY_REMOVE_TIMESTAMP = 0;
        this.KEY_REMOVE_TYPE = "NA";

        this.KEY_RESULT = "NA";
        this.KEY_INOPPORTUNTE_RESULT = "NA";
        this.KEY_OPPORTUNTE_RESULT = "NA";

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

    public String getKEY_ESM_RESULT_SAMPLE() {
        return KEY_ESM_RESULT_SAMPLE;
    }

    public void setKEY_ESM_RESULT_SAMPLE(String KEY_ESM_RESULT_SAMPLE) {
        this.KEY_ESM_RESULT_SAMPLE = KEY_ESM_RESULT_SAMPLE;
    }

    public String getKEY_DIARY_SCHEDULE_SOURCE() {
        return KEY_DIARY_SCHEDULE_SOURCE;
    }

    public void setKEY_DIARY_SCHEDULE_SOURCE(String KEY_DIARY_SCHEDULE_SOURCE) {
        this.KEY_DIARY_SCHEDULE_SOURCE = KEY_DIARY_SCHEDULE_SOURCE;
    }

    public long getKEY_DIARY_SAMPLE_TIME() {
        return KEY_DIARY_SAMPLE_TIME;
    }

    public void setKEY_DIARY_SAMPLE_TIME(long KEY_DIARY_SAMPLE_TIME) {
        this.KEY_DIARY_SAMPLE_TIME = KEY_DIARY_SAMPLE_TIME;
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

    public String getKEY_INOPPORTUNTE_RESULT() {
        return KEY_INOPPORTUNTE_RESULT;
    }

    public void setKEY_INOPPORTUNTE_RESULT(String KEY_INOPPORTUNTE_RESULT) {
        this.KEY_INOPPORTUNTE_RESULT = KEY_INOPPORTUNTE_RESULT;
    }

    public String getKEY_OPPORTUNTE_RESULT() {
        return KEY_OPPORTUNTE_RESULT;
    }

    public void setKEY_OPPORTUNTE_RESULT(String KEY_OPPORTUNTE_RESULT) {
        this.KEY_OPPORTUNTE_RESULT = KEY_OPPORTUNTE_RESULT;
    }
}
