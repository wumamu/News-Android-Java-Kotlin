package com.recoveryrecord.surveyandroid.example

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Block horizontal scroll
 */
internal class CustomSwipeRefreshLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(
    context!!, attrs
) {
    private val mTouchSlop: Int
    private var mPrevx = 0f
    private var mPrevy = 0f

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    @SuppressLint("Recycle")
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mPrevx = MotionEvent.obtain(ev).x
                mPrevy = MotionEvent.obtain(ev).y
            }

            MotionEvent.ACTION_MOVE -> {
                val evX = ev.x
                val evy = ev.y
                val xDiff = Math.abs(evX - mPrevx)
                val yDiff = Math.abs(evy - mPrevy)
                if (xDiff > mTouchSlop && xDiff > yDiff) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}