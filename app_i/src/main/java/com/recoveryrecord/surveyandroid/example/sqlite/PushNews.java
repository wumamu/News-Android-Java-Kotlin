package com.recoveryrecord.surveyandroid.example.sqlite;

public class PushNews {

    private String KEY_NEWS_ID;
    private String KEY_DEVICE_ID;
    private String KEY_USER_ID;
    private int KEY_CLICK;
    private String KEY_MEDIA;
    private String KEY_TYPE;
    private String KEY_TITLE;
    private long KEY_NOTI_TIMESTAMP;
    private long KEY_RECEIEVE_TIMESTAMP;
    private long KEY_PUBDATE;//

    public PushNews() {
        this.KEY_NEWS_ID = "NA";
        this.KEY_DEVICE_ID = "NA";
        this.KEY_USER_ID = "NA";
        this.KEY_CLICK = 0;
        this.KEY_MEDIA = "NA";
        this.KEY_TYPE = "NA";
        this.KEY_TITLE = "NA";
        this.KEY_NOTI_TIMESTAMP = 0;
        this.KEY_RECEIEVE_TIMESTAMP = 0;
        this.KEY_PUBDATE = 0;
    }



    public PushNews(String KEY_NEWS_ID,
                    String KEY_DEVICE_ID,
                    String KEY_USER_ID,
                    int KEY_CLICK,
                    String KEY_TYPE,
                    String KEY_MEDIA,
                    String KEY_TITLE,
                    long KEY_NOTI_TIMESTAMP,
                    long KEY_RECEIEVE_TIMESTAMP,
                    long KEY_PUBDATE) {
        this.KEY_NEWS_ID = KEY_NEWS_ID;
        this.KEY_DEVICE_ID = KEY_DEVICE_ID;
        this.KEY_USER_ID = KEY_USER_ID;
        this.KEY_TYPE = KEY_TYPE;
        this.KEY_CLICK = KEY_CLICK;
        this.KEY_MEDIA = KEY_MEDIA;
        this.KEY_TITLE = KEY_TITLE;
        this.KEY_NOTI_TIMESTAMP = KEY_NOTI_TIMESTAMP;
        this.KEY_RECEIEVE_TIMESTAMP = KEY_RECEIEVE_TIMESTAMP;
        this.KEY_PUBDATE = KEY_PUBDATE;
    }

    public String getKEY_NEWS_ID() {
        return KEY_NEWS_ID;
    }

    public void setKEY_NEWS_ID(String KEY_NEWS_ID) {
        this.KEY_NEWS_ID = KEY_NEWS_ID;
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

    public int getKEY_CLICK() {
        return KEY_CLICK;
    }

    public void setKEY_CLICK(int KEY_CLICK) {
        this.KEY_CLICK = KEY_CLICK;
    }

    public String getKEY_MEDIA() {
        return KEY_MEDIA;
    }

    public void setKEY_MEDIA(String KEY_MEDIA) {
        this.KEY_MEDIA = KEY_MEDIA;
    }

    public String getKEY_TITLE() {
        return KEY_TITLE;
    }

    public void setKEY_TITLE(String KEY_TITLE) {
        this.KEY_TITLE = KEY_TITLE;
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

    public long getKEY_PUBDATE() {
        return KEY_PUBDATE;
    }

    public void setKEY_PUBDATE(long KEY_PUBDATE) {
        this.KEY_PUBDATE = KEY_PUBDATE;
    }

    public String getKEY_TYPE() {
        return KEY_TYPE;
    }

    public void setKEY_TYPE(String KEY_TYPE) {
        this.KEY_TYPE = KEY_TYPE;
    }
}
