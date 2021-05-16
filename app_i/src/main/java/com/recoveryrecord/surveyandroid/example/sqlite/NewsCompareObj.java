package com.recoveryrecord.surveyandroid.example.sqlite;

public class NewsCompareObj implements Comparable{
    private int share_or_not;
    private int trigger_by;
    private String category;
    private int index;
    private String news_title;
    private int sum;

    public NewsCompareObj(int share_or_not, int trigger_by, String category, int index, String news_title, int sum) {
        this.share_or_not = share_or_not;
        this.trigger_by = trigger_by;
        this.category = category;
        this.index = index;
        this.news_title = news_title;
        this.sum = sum;
    }

    public int getShare_or_not() {
        return share_or_not;
    }

    public void setShare_or_not(int share_or_not) {
        this.share_or_not = share_or_not;
    }

    public int getTrigger_by() {
        return trigger_by;
    }

    public void setTrigger_by(int trigger_by) {
        this.trigger_by = trigger_by;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }


    @Override
    public int compareTo(Object comparestu) {
        int compareage=((NewsCompareObj)comparestu).getSum();
        return this.sum-compareage;
//        return 0;
    }

//    @Override
//    public int compareTo(Object o) {
//        return 0;
//    }
}
