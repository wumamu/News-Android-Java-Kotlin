package com.recoveryrecord.surveyandroid.example.ui

import android.app.Activity
import android.view.GestureDetector
import android.view.MotionEvent
import com.recoveryrecord.surveyandroid.example.sqlite.DragObj
import com.recoveryrecord.surveyandroid.example.sqlite.FlingObj
import com.recoveryrecord.surveyandroid.example.util.SimpleGestureListener
import timber.log.Timber
import kotlin.math.abs

class GestureListener(private val context: Activity, private val listener: SimpleGestureListener) :
    GestureDetector.SimpleOnGestureListener() {
    private var mode = MODE_TRANSPARENT
    private var tapIndicator = false
    private val detector: GestureDetector = GestureDetector(context, this)

    fun onTouchEvent(event: MotionEvent) {
        // disable lomg press
        detector.setIsLongpressEnabled(false)
        val running = true
        if (!running) return
        val result = detector.onTouchEvent(event)
        if (mode == MODE_SOLID) {
            event.action =
                MotionEvent.ACTION_CANCEL
        } else if (mode == MODE_DYNAMIC) {
            if (event.action == ACTION_FAKE) {
                event.action =
                    MotionEvent.ACTION_UP
            } else if (result) {
                event.action =
                    MotionEvent.ACTION_CANCEL
            } else if (tapIndicator) {
                event.action = MotionEvent.ACTION_DOWN
                tapIndicator = false
            }
        }
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        _velocityX: Float,
        _velocityY: Float,
    ): Boolean {
        var velocityX = _velocityX
        var velocityY = _velocityY
        val tmpFlingObj = FlingObj()
        val xDistance = Math.abs(e1.x - e2.x)
        val yDistance = Math.abs(e1.y - e2.y)
        velocityX = abs(velocityX)
        velocityY = abs(velocityY)
        var result = false
        tmpFlingObj.POINT_ONE_X = e1.x
        tmpFlingObj.POINT_ONE_Y = e1.y
        tmpFlingObj.POINT_TWO_X = e2.x
        tmpFlingObj.POINT_TWO_Y = e2.y
        tmpFlingObj.DISTANCE_X = xDistance
        tmpFlingObj.DISTANCE_Y = yDistance
        tmpFlingObj.VELOCITY_X = velocityX
        tmpFlingObj.VELOCITY_Y = velocityY

        // 100
        val swipe_Min_Distance = 100
        // 100
        val swipe_Min_Velocity = 0
        if (velocityX > swipe_Min_Velocity && xDistance > swipe_Min_Distance) {
            if (e1.x > e2.x) {
                // right to left
                listener.onSwipe(SWIPE_LEFT, tmpFlingObj)
            } else {
                listener.onSwipe(
                    SWIPE_RIGHT,
                    tmpFlingObj,
                )
            }
            result = true
        } else if (velocityY > swipe_Min_Velocity && yDistance > swipe_Min_Distance) {
            if (e1.y > e2.y) {
                // bottom to up
                listener.onSwipe(SWIPE_UP, tmpFlingObj)
            } else {
                listener.onSwipe(
                    SWIPE_DOWN,
                    tmpFlingObj,
                )
            }
            result = true
        }
        return result
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        tapIndicator = true
        return false
    }

    override fun onDoubleTap(arg0: MotionEvent): Boolean {
        return true
    }

    override fun onDoubleTapEvent(arg0: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapConfirmed(arg0: MotionEvent): Boolean {
        if (mode == MODE_DYNAMIC) { // we owe an ACTION_UP, so we fake an
            arg0.action = ACTION_FAKE // action which will be converted to an ACTION_UP later.
            context.dispatchTouchEvent(arg0)
        }
        return false
    }

    override fun onLongPress(e: MotionEvent) {
        Timber.d("Longpress detected")
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float,
    ): Boolean {
        val dragObj = DragObj()
        val result = super.onScroll(e1, e2, distanceX, distanceY)
        if (!result) {
            Timber.d(
                "%s)",
                System.currentTimeMillis().toString() + " drag first: (" + e1.x + "," + e1.y,
            )
            Timber.d(
                System.currentTimeMillis().toString() + " drag second: (" + e2.x + "," + e2.y + ")",
            )
            dragObj.TIME_ONE = System.currentTimeMillis()
            dragObj.POINT_ONE_X = e1.x
            dragObj.POINT_ONE_Y = e1.y
            dragObj.POINT_TWO_X = e2.x
            dragObj.POINT_TWO_Y = e2.y
            listener.onOnePoint(dragObj)
        }
        return result
    }

    companion object {
        const val SWIPE_UP = 1
        const val SWIPE_DOWN = 2
        const val SWIPE_LEFT = 3
        const val SWIPE_RIGHT = 4
        const val MODE_TRANSPARENT = 0
        const val MODE_SOLID = 1
        const val MODE_DYNAMIC = 2
        private const val ACTION_FAKE = -13 // just an unlikely number
    }
}
