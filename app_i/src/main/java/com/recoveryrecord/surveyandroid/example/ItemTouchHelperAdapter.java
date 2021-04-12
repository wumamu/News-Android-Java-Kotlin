package com.recoveryrecord.surveyandroid.example;

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemSwiped(int position);
}
