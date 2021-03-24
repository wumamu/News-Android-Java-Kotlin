package com.recoveryrecord.surveyandroid.example;

public class NewsModel {
    private String title, media, pubdate, id;
    public NewsModel(){

    }
    public NewsModel(String title, String media, String pubdate, String id) {
        this.title = title;
//        this.content = content;
        this.media = media;
        this.pubdate = pubdate;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }


    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
