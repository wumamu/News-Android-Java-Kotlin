package com.recoveryrecord.surveyandroid.example.util

import com.recoveryrecord.surveyandroid.example.sqlite.DragObj
import com.recoveryrecord.surveyandroid.example.sqlite.FlingObj

interface SimpleGestureListener {
    fun onSwipe(
        direction: Int,
        flingObj: FlingObj,
    )

    fun onOnePoint(dragObj: DragObj)
}
