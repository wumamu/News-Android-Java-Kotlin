package com.recoveryrecord.surveyandroid

import com.recoveryrecord.surveyandroid.example.sqlite.DragObj
import com.recoveryrecord.surveyandroid.example.sqlite.FlingObj

internal interface SimpleGestureListener {
    fun onSwipe(direction: Int, flingObj: FlingObj?)
    fun onOnePoint(dragObj: DragObj?)
}