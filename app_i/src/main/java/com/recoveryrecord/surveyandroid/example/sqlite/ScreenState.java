package com.recoveryrecord.surveyandroid.example.sqlite;

public class ScreenState {
    private String KEY_DOC_ID;
    private long KEY_TIMESTAMP;
    private String KEY_DEVICE_ID;
    private int KEY_SESSION;
    private String KEY_USING_APP;
    private String KEY_SCREEN;
    private String KEY_USER_ID;

    public ScreenState(){
        this.KEY_DOC_ID = "NA";
        this.KEY_TIMESTAMP = 0;
        this.KEY_DEVICE_ID = "NA";
        this.KEY_SESSION = 0;
        this.KEY_USING_APP = "NA";
        this.KEY_SCREEN = "NA";
        this.KEY_USER_ID = "NA";
    }
    public ScreenState(String KEY_DOC_ID,
                       long KEY_TIMESTAMP,
                       String KEY_DEVICE_ID,
                       String KEY_USER_ID,
                       int KEY_SESSION,
                       String KEY_USING_APP,
                       String KEY_SCREEN){
        this.KEY_DOC_ID = KEY_DOC_ID;
        this.KEY_TIMESTAMP = KEY_TIMESTAMP;
        this.KEY_DEVICE_ID = KEY_DEVICE_ID;
        this.KEY_USER_ID = KEY_USER_ID;
        this.KEY_SESSION = KEY_SESSION;
        this.KEY_USING_APP = KEY_USING_APP;
        this.KEY_SCREEN = KEY_SCREEN;
    }
    public String getKEY_DOC_ID(){
        return KEY_DOC_ID;
    }

    public void setKEY_DOC_ID(String KEY_DOC_ID){
        this.KEY_DOC_ID = KEY_DOC_ID;
    }

    public long getKEY_TIMESTAMP(){
        return KEY_TIMESTAMP;
    }

    public void setKEY_TIMESTAMP(long KEY_TIMESTAMP){
        this.KEY_TIMESTAMP = KEY_TIMESTAMP;
    }

    public String getKEY_DEVICE_ID(){
        return KEY_DEVICE_ID;
    }

    public void setKEY_DEVICE_ID(String KEY_DEVICE_ID){
        this.KEY_DEVICE_ID = KEY_DEVICE_ID;
    }

    public String getKEY_USER_ID() {
        return KEY_USER_ID;
    }

    public void setKEY_USER_ID(String KEY_USER_ID) {
        this.KEY_USER_ID = KEY_USER_ID;
    }

    public int getKEY_SESSION(){
        return KEY_SESSION;
    }

    public void setKEY_SESSION(int KEY_SESSION){
        this.KEY_SESSION = KEY_SESSION;
    }

    public String getKEY_USING_APP(){
        return KEY_USING_APP;
    }

    public void setKEY_USING_APP(String KEY_USING_APP){
        this.KEY_USING_APP = KEY_USING_APP;
    }

    public String getKEY_SCREEN(){
        return KEY_SCREEN;
    }

    public void setKEY_SCREEN(String KEY_SCREEN){

        this.KEY_SCREEN = KEY_SCREEN;
    }
}
