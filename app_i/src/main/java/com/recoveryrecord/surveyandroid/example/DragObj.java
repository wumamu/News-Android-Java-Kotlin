package com.recoveryrecord.surveyandroid.example;

public class DragObj {
    private long TIME_ONE;
//    private long TIME_TWO;
    private float POINT_ONE_X;
    private float POINT_ONE_Y;
    private float POINT_TWO_X;
    private float POINT_TWO_Y;

    public DragObj() {
        this.TIME_ONE = 0;
//        this.TIME_TWO = 0;
        this.POINT_ONE_X = 0;
        this.POINT_ONE_Y = 0;
        this.POINT_TWO_X = 0;
        this.POINT_TWO_Y = 0;
    }

    public long getTIME_ONE() {
        return TIME_ONE;
    }

    public void setTIME_ONE(long TIME_ONE) {
        this.TIME_ONE = TIME_ONE;
    }

//    public long getTIME_TWO() {
//        return TIME_TWO;
//    }
//
//    public void setTIME_TWO(long TIME_TWO) {
//        this.TIME_TWO = TIME_TWO;
//    }

    public float getPOINT_ONE_X() {
        return POINT_ONE_X;
    }

    public void setPOINT_ONE_X(float POINT_ONE_X) {
        this.POINT_ONE_X = POINT_ONE_X;
    }

    public float getPOINT_ONE_Y() {
        return POINT_ONE_Y;
    }

    public void setPOINT_ONE_Y(float POINT_ONE_Y) {
        this.POINT_ONE_Y = POINT_ONE_Y;
    }

    public float getPOINT_TWO_X() {
        return POINT_TWO_X;
    }

    public void setPOINT_TWO_X(float POINT_TWO_X) {
        this.POINT_TWO_X = POINT_TWO_X;
    }

    public float getPOINT_TWO_Y() {
        return POINT_TWO_Y;
    }

    public void setPOINT_TWO_Y(float POINT_TWO_Y) {
        this.POINT_TWO_Y = POINT_TWO_Y;
    }


}
