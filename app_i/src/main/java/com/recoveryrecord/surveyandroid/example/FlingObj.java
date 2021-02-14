package com.recoveryrecord.surveyandroid.example;

public class FlingObj {

    private int FLING_ID;
    private float POINT_ONE_X;
    private float POINT_ONE_Y;
    private float POINT_TWO_X;
    private float POINT_TWO_Y;
    private float VELOCITY_X;
    private float VELOCITY_Y;
    private float DISTANCE_X;
    private float DISTANCE_Y;

    public FlingObj() {
        this.FLING_ID = 0;
        this.POINT_ONE_X = 0;
        this.POINT_ONE_Y = 0;
        this.POINT_TWO_X = 0;
        this.POINT_TWO_Y = 0;
        this.VELOCITY_X = 0;
        this.VELOCITY_Y = 0;
        this.DISTANCE_X = 0;
        this.DISTANCE_Y = 0;
    }

    public int getFLING_ID() {
        return FLING_ID;
    }

    public void setFLING_ID(int FLING_ID) {
        this.FLING_ID = FLING_ID;
    }

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

    public float getVELOCITY_X() {
        return VELOCITY_X;
    }

    public void setVELOCITY_X(float VELOCITY_X) {
        this.VELOCITY_X = VELOCITY_X;
    }

    public float getVELOCITY_Y() {
        return VELOCITY_Y;
    }

    public void setVELOCITY_Y(float VELOCITY_Y) {
        this.VELOCITY_Y = VELOCITY_Y;
    }

    public float getDISTANCE_X() {
        return DISTANCE_X;
    }

    public void setDISTANCE_X(float DISTANCE_X) {
        this.DISTANCE_X = DISTANCE_X;
    }

    public float getDISTANCE_Y() {
        return DISTANCE_Y;
    }

    public void setDISTANCE_Y(float DISTANCE_Y) {
        this.DISTANCE_Y = DISTANCE_Y;
    }
}
