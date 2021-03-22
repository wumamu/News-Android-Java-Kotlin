package com.recoveryrecord.surveyandroid.example;

public class NewsModel {
    private String title, text, media, time;
    public NewsModel(){

    }
    public NewsModel(String title, String text, String media) {
        this.title = title;
        this.text = text;
        this.media = media;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
