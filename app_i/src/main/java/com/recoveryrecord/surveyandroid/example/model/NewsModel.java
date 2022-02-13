package com.recoveryrecord.surveyandroid.example.model;

import com.google.firebase.Timestamp;

public class NewsModel {
    private String title, media, id, image;
    private Timestamp pubdate;
    public NewsModel(){

    }
    public NewsModel(String title, String media, String id, Timestamp pubdate, String image) {
        this.title = title;
        this.media = media;
        this.pubdate = pubdate;
        this.id = id;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getPubdate() {
        return pubdate;
    }

    public void setPubdate(Timestamp pubdate) {
        this.pubdate = pubdate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
