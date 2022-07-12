package com.recoveryrecord.surveyandroid.example.adapter;

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemSwiped(int position);
}
