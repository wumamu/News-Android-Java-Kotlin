package com.recoveryrecord.surveyandroid.example.FSM;

import android.os.Message;

import com.recoveryrecord.surveyandroid.example.StateMachine.State;
import com.recoveryrecord.surveyandroid.example.StateMachine.StateMachine;

public class ViewingState extends State {
    private FBStateMachine mStateMachine;

    public ViewingState(StateMachine sm) {
        this.mStateMachine = (FBStateMachine) sm;
    }

    @Override
    public boolean processMessage(Message msg) {
        System.out.println("ViewingState:" + msg.what);

        boolean ret;

        switch (msg.what) {
            case StateDef.CMD_CLICK_COMMENT:
            case StateDef.CMD_READ_COMMENT:
                System.out.println("ViewingState->ReadingState");
                //mStateMachine.deferMessage(mStateMachine.obtainMessage(StateDef.CMD_OPEN_FB));
                mStateMachine.transitionTo(mStateMachine.mReadingState);
                ret = StateMachine.HANDLED;
                break;
            case StateDef.CMD_TYPING:
                System.out.println("ViewingState->CommentingState");
                mStateMachine.transitionTo(mStateMachine.mCommentingState);
                ret = StateMachine.HANDLED;
                break;
            case StateDef.CMD_CLICK_OUTER:
                System.out.println("ViewingState->OuterState");
                mStateMachine.transitionTo(mStateMachine.mOuterState);
                ret = StateMachine.HANDLED;
                break;
            case StateDef.CMD_CLICK_SHARE:
                System.out.println("ViewingState->SharingState");
                mStateMachine.transitionTo(mStateMachine.mSharingState);
                ret = StateMachine.HANDLED;
                break;
            /*case StateDef.CMD_CLOSE_FB:
                System.out.println("ViewingState->DefaultState");
                mStateMachine.transitionTo(mStateMachine.mDefaultState);
                ret = StateMachine.HANDLED;
                break;*/

            default:
                ret = StateMachine.NOT_HANDLED;
                break;
        }

        return ret;
    }

    @Override
    public void enter() {
        System.out.println(getName() + "enter");
    }

    @Override
    public void exit() {
        System.out.println(getName() + "exit");
    }

    @Override
    public String getName() {
        return "ViewingState";
    }
}
