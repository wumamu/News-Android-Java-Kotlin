package com.recoveryrecord.surveyandroid.example;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.recoveryrecord.surveyandroid.example.sqlite.DragObj;
import com.recoveryrecord.surveyandroid.example.sqlite.FlingObj;

public class GestureListener extends GestureDetector.SimpleOnGestureListener{

    public final static int SWIPE_UP    = 1;
    public final static int SWIPE_DOWN  = 2;
    public final static int SWIPE_LEFT  = 3;
    public final static int SWIPE_RIGHT = 4;

    public final static int MODE_TRANSPARENT = 0;
    public final static int MODE_SOLID       = 1;
    public final static int MODE_DYNAMIC     = 2;

    private final static int ACTION_FAKE = -13; //just an unlikely number
    private int swipe_Min_Distance = 100;//100
    private int swipe_Max_Distance = 350;//350
    private int swipe_Min_Velocity = 0;//100

    private int mode      = MODE_TRANSPARENT;
    private boolean running = true;
    private boolean tapIndicator = false;

    private Activity context;
    private GestureDetector detector;
    private SimpleGestureListener listener;


    public GestureListener(Activity context, SimpleGestureListener sgl) {

        this.context = context;
        this.detector = new GestureDetector(context, this);
        this.listener = sgl;
    }

    public void onTouchEvent(MotionEvent event){
        //disable lomg press
        detector.setIsLongpressEnabled(false);
        if(!this.running)
            return;

        boolean result = this.detector.onTouchEvent(event);

        if(this.mode == MODE_SOLID)
            event.setAction(MotionEvent.ACTION_CANCEL);
        else if (this.mode == MODE_DYNAMIC) {

            if(event.getAction() == ACTION_FAKE)
                event.setAction(MotionEvent.ACTION_UP);
            else if (result)
                event.setAction(MotionEvent.ACTION_CANCEL);
            else if(this.tapIndicator){
                event.setAction(MotionEvent.ACTION_DOWN);
                this.tapIndicator = false;
            }

        }
//        detector.setIsLongpressEnabled(false);
//        //else just do nothing, it's Transparent
//        if (detector.onTouchEvent(event)== true) {
//            //Fling or other gesture detected (not logpress because it is disabled)
//        } else {
//            //Manually handle the event.
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                //Remember the time and press position
////                Log.e("test","Action down");
//                Log.d("log: GestureListener", "Action down");
//            }
//            if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                //Check if user is actually longpressing, not slow-moving
//                // if current position differs much then press positon then discard whole thing
//                // If position change is minimal then after 0.5s that is a longpress. You can now process your other gestures
////                Log.e("test","Action move");
//                Log.d("log: GestureListener", "Action move");
//            }
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                //Get the time and position and check what that was :)
////                Log.("test","Action down");
//                Log.d("log: GestureListener", "Action up");
//
//            }
//
//        }
//        return true;
    }

    public void setMode(int m){
        this.mode = m;
    }

    public int getMode(){
        return this.mode;
    }

//    public void setEnabled(boolean status){
//        this.running = status;
//    }
//
//    public void setSwipeMaxDistance(int distance){
//        this.swipe_Max_Distance = distance;
//    }
//
//    public void setSwipeMinDistance(int distance){
//        this.swipe_Min_Distance = distance;
//    }
//
//    public void setSwipeMinVelocity(int distance){
//        this.swipe_Min_Velocity = distance;
//    }
//
//    public int getSwipeMaxDistance(){
//        return this.swipe_Max_Distance;
//    }
//
//    public int getSwipeMinDistance(){
//        return this.swipe_Min_Distance;
//    }
//
//    public int getSwipeMinVelocity(){
//        return this.swipe_Min_Velocity;
//    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        FlingObj tmpFlingObj = new FlingObj();

        final float xDistance = Math.abs(e1.getX() - e2.getX());
        final float yDistance = Math.abs(e1.getY() - e2.getY());
//        Log.d("log: GestureListener","fling first: (" + e1.getX() + "," + e1.getY() + ")");
//        Log.d("log: GestureListener","fling second: (" + e2.getX() + "," + e2.getY() + ")");

//        not long enough to be consider as fling
//        if(xDistance > this.swipe_Max_Distance || yDistance > this.swipe_Max_Distance)
//            return false;

        velocityX = Math.abs(velocityX);
        velocityY = Math.abs(velocityY);
        boolean result = false;
//        Log.d("log: velocityX", String.valueOf(velocityX));
//        Log.d("log: velocityY", String.valueOf(velocityY));
//        Log.d("log: xDistance", String.valueOf(xDistance));
//        Log.d("log: yDistance", String.valueOf(yDistance));
        tmpFlingObj.setPOINT_ONE_X(e1.getX());
        tmpFlingObj.setPOINT_ONE_Y(e1.getY());
        tmpFlingObj.setPOINT_TWO_X(e2.getX());
        tmpFlingObj.setPOINT_TWO_Y(e2.getY());
        tmpFlingObj.setDISTANCE_X(xDistance);
        tmpFlingObj.setDISTANCE_Y(yDistance);
        tmpFlingObj.setVELOCITY_X(velocityX);
        tmpFlingObj.setVELOCITY_Y(velocityY);

        if(velocityX > this.swipe_Min_Velocity && xDistance > this.swipe_Min_Distance){
            if(e1.getX() > e2.getX()) // right to left
                this.listener.onSwipe(SWIPE_LEFT, tmpFlingObj);
            else
                this.listener.onSwipe(SWIPE_RIGHT, tmpFlingObj);

            result = true;
        }
        else if(velocityY > this.swipe_Min_Velocity && yDistance > this.swipe_Min_Distance){
            if(e1.getY() > e2.getY()) // bottom to up
                this.listener.onSwipe(SWIPE_UP, tmpFlingObj);
            else
                this.listener.onSwipe(SWIPE_DOWN, tmpFlingObj);

            result = true;
        }
//        Log.d("log: GestureListener","fling");
        return result;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        this.tapIndicator = true;
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent arg0) {
//        this.listener.onDoubleTap();;
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

//    public boolean isLongpressEnabled (){
//        return false;
//    }

    @Override
    public void onLongPress(MotionEvent e) {
//        Log.e("", "Longpress detected");
        Log.d("log: GestureListener", "Longpress detected");
    }

//    public void setIsLongpressEnabled (boolean isLongpressEnabled){
//    }

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
//            Log.d("log: GestureListener","drag second: (" + e2.getX() + "," + e2.getY() + ")");
        }
        return result;
    }
    static interface SimpleGestureListener{
        void onSwipe(int direction, FlingObj flingObj);
        void onOnePoint(DragObj dragObj);
//        void setIsLongpressEnabled(boolean isLongpressEnabled);
//        void onDoubleTap();
    }

}