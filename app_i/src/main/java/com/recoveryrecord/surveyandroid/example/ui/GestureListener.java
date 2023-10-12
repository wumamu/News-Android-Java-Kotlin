package com.recoveryrecord.surveyandroid.example.ui;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.recoveryrecord.surveyandroid.SimpleGestureListener;
import com.recoveryrecord.surveyandroid.example.sqlite.DragObj;
import com.recoveryrecord.surveyandroid.example.sqlite.FlingObj;

public class GestureListener extends GestureDetector.SimpleOnGestureListener{

    public final static int SWIPE_UP    = 1;
    public final static int SWIPE_DOWN  = 2;
    public final static int SWIPE_LEFT  = 3;
    public final static int SWIPE_RIGHT = 4;

    public final static int MODE_TRANSPARENT = 0;
    public final static int MODE_SOLID = 1;
    public final static int MODE_DYNAMIC = 2;

    private final static int ACTION_FAKE = -13; //just an unlikely number

    private int mode = MODE_TRANSPARENT;
    private boolean tapIndicator = false;

    private final Activity context;
    private final GestureDetector detector;
    private final SimpleGestureListener listener;


    public GestureListener(Activity context, SimpleGestureListener sgl) {

        this.context = context;
        this.detector = new GestureDetector(context, this);
        this.listener = sgl;
    }

    public void onTouchEvent(MotionEvent event) {
        //disable lomg press
        detector.setIsLongpressEnabled(false);
        boolean running = true;
        if (!running)
            return;

        boolean result = this.detector.onTouchEvent(event);

        if (this.mode == MODE_SOLID)
            event.setAction(MotionEvent.ACTION_CANCEL);
        else if (this.mode == MODE_DYNAMIC) {

            if (event.getAction() == ACTION_FAKE)
                event.setAction(MotionEvent.ACTION_UP);
            else if (result)
                event.setAction(MotionEvent.ACTION_CANCEL);
            else if(this.tapIndicator){
                event.setAction(MotionEvent.ACTION_DOWN);
                this.tapIndicator = false;
            }

        }
    }

    public void setMode(int m){
        this.mode = m;
    }

    public int getMode(){
        return this.mode;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        FlingObj tmpFlingObj = new FlingObj();

        final float xDistance = Math.abs(e1.getX() - e2.getX());
        final float yDistance = Math.abs(e1.getY() - e2.getY());

        velocityX = Math.abs(velocityX);
        velocityY = Math.abs(velocityY);
        boolean result = false;
        tmpFlingObj.setPOINT_ONE_X(e1.getX());
        tmpFlingObj.setPOINT_ONE_Y(e1.getY());
        tmpFlingObj.setPOINT_TWO_X(e2.getX());
        tmpFlingObj.setPOINT_TWO_Y(e2.getY());
        tmpFlingObj.setDISTANCE_X(xDistance);
        tmpFlingObj.setDISTANCE_Y(yDistance);
        tmpFlingObj.setVELOCITY_X(velocityX);
        tmpFlingObj.setVELOCITY_Y(velocityY);

        //100
        int swipe_Min_Distance = 100;
        //100
        int swipe_Min_Velocity = 0;
        if (velocityX > swipe_Min_Velocity && xDistance > swipe_Min_Distance) {
            if (e1.getX() > e2.getX()) // right to left
                this.listener.onSwipe(SWIPE_LEFT, tmpFlingObj);
            else
                this.listener.onSwipe(SWIPE_RIGHT, tmpFlingObj);

            result = true;
        } else if (velocityY > swipe_Min_Velocity && yDistance > swipe_Min_Distance) {
            if (e1.getY() > e2.getY()) // bottom to up
                this.listener.onSwipe(SWIPE_UP, tmpFlingObj);
            else
                this.listener.onSwipe(SWIPE_DOWN, tmpFlingObj);

            result = true;
        }
        return result;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        this.tapIndicator = true;
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent arg0) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent arg0) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent arg0) {

        if(this.mode == MODE_DYNAMIC){        // we owe an ACTION_UP, so we fake an
            arg0.setAction(ACTION_FAKE);      //action which will be converted to an ACTION_UP later.
            this.context.dispatchTouchEvent(arg0);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
//        Log.e("", "Longpress detected");
        Log.d("log: GestureListener", "Longpress detected");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
        DragObj dragObj = new DragObj();
        boolean result = super.onScroll(e1, e2, distanceX, distanceY);
        if(!result){
            Log.d("log: GestureListener", System.currentTimeMillis()+ " drag first: (" + e1.getX() + "," + e1.getY() + ")");
            Log.d("log: GestureListener", System.currentTimeMillis()+ " drag second: (" + e2.getX() + "," + e2.getY() + ")");
            dragObj.setTIME_ONE(System.currentTimeMillis());
            dragObj.setPOINT_ONE_X(e1.getX());
            dragObj.setPOINT_ONE_Y(e1.getY());
            dragObj.setPOINT_TWO_X(e2.getX());
            dragObj.setPOINT_TWO_Y(e2.getY());
            this.listener.onOnePoint(dragObj);
        }
        return result;
    }
}