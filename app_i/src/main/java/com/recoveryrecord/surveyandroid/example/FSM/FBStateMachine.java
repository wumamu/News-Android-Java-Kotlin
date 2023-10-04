package com.recoveryrecord.surveyandroid.example.FSM;

import com.recoveryrecord.surveyandroid.example.StateMachine.State;
import com.recoveryrecord.surveyandroid.example.StateMachine.StateMachine;

public class FBStateMachine extends StateMachine {
    DefaultState mDefaultState = new DefaultState(this);
    ViewingState mViewingState = new ViewingState(this);
    ReadingState mReadingState = new ReadingState(this);
    CommentingState mCommentingState = new CommentingState(this);
    SharingState mSharingState = new SharingState(this);
    OuterState mOuterState = new OuterState(this);

    public FBStateMachine(String name) {
        super(name);

        init();
    }

    //初始化狀態，層次關係，樹形結構。
    private void init() {
        //addState(mDefaultState);
        addState(mViewingState);
        addState(mReadingState);
        addState(mCommentingState);
        addState(mSharingState);
        addState(mOuterState);

        setInitialState(mViewingState);
        start();
    }

    public String getStateName() {
        return getCurrentState().getName();
    }
}