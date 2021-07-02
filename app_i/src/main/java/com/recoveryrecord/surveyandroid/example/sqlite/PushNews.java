package com.recoveryrecord.surveyandroid.example.sqlite;

public class PushNews {
    private String KEY_DOC_ID;
    private String KEY_DEVICE_ID;
    private String KEY_USER_ID;
    private String KEY_NEWS_ID;
    private String KEY_TITLE;
    private String KEY_MEDIA;
    private long KEY_PUBDATE;//
    private long KEY_NOTI_TIMESTAMP;
    private long KEY_RECEIEVE_TIMESTAMP;
    private long KEY_OPEN_TIMESTAMP;
    private long KEY_REMOVE_TIMESTAMP;
    private String KEY_REMOVE_TYPE;
    private String KEY_TYPE;

    private int KEY_CLICK;



    public PushNews() {
        this.KEY_DOC_ID = "NA";
        this.KEY_DEVICE_ID = "NA";
        this.KEY_USER_ID = "NA";
        this.KEY_NEWS_ID = "NA";
        this.KEY_TITLE = "NA";
        this.KEY_MEDIA = "NA";
        this.KEY_PUBDATE = 0;
        this.KEY_NOTI_TIMESTAMP = 0;
        this.KEY_RECEIEVE_TIMESTAMP = 0;
        this.KEY_OPEN_TIMESTAMP = 0;
        this.KEY_REMOVE_TIMESTAMP = 0;
        this.KEY_REMOVE_TYPE = "NA";
        this.KEY_TYPE = "NA";
        this.KEY_CLICK = 0;
    }

//    public PushNews(String KEY_DOC_ID,
//                    String KEY_DEVICE_ID,
//                    String KEY_USER_ID,
//                    String KEY_NEWS_ID,
//                    String KEY_TITLE,
//                    String KEY_MEDIA,
//                    long KEY_PUBDATE,
//                    long KEY_NOTI_TIMESTAMP,
//                    long KEY_RECEIEVE_TIMESTAMP,
//                    long KEY_OPEN_TIMESTAMP,
//                    long KEY_REMOVE_TIMESTAMP,
//                    String KEY_REMOVE_TYPE,
//                    String KEY_TYPE,
//                    int KEY_CLICK) {
//        this.KEY_DOC_ID = KEY_DOC_ID;
//        this.KEY_DEVICE_ID = KEY_DEVICE_ID;
//        this.KEY_USER_ID = KEY_USER_ID;
//        this.KEY_NEWS_ID = KEY_NEWS_ID;
//        this.KEY_TITLE = KEY_TITLE;
//        this.KEY_MEDIA = KEY_MEDIA;
//        this.KEY_PUBDATE = KEY_PUBDATE;
//        this.KEY_NOTI_TIMESTAMP = KEY_NOTI_TIMESTAMP;
//        this.KEY_RECEIEVE_TIMESTAMP = KEY_RECEIEVE_TIMESTAMP;
//        this.KEY_OPEN_TIMESTAMP = KEY_OPEN_TIMESTAMP;
//        this.KEY_REMOVE_TIMESTAMP = KEY_REMOVE_TIMESTAMP;
//        this.KEY_REMOVE_TYPE = KEY_REMOVE_TYPE;
//        this.KEY_TYPE = KEY_TYPE;
//        this.KEY_CLICK = KEY_CLICK;
//    }

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

    public long getKEY_PUBDATE() {
        return KEY_PUBDATE;
    }

    public void setKEY_PUBDATE(long KEY_PUBDATE) {
        this.KEY_PUBDATE = KEY_PUBDATE;
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

    public String getKEY_TYPE() {
        return KEY_TYPE;
    }

    public void setKEY_TYPE(String KEY_TYPE) {
        this.KEY_TYPE = KEY_TYPE;
    }

    public int getKEY_CLICK() {
        return KEY_CLICK;
    }

    public void setKEY_CLICK(int KEY_CLICK) {
        this.KEY_CLICK = KEY_CLICK;
    }
}
