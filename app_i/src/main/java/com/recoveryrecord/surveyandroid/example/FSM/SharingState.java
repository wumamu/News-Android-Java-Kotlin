package com.recoveryrecord.surveyandroid.example.FSM;

import android.os.Message;

import com.recoveryrecord.surveyandroid.example.StateMachine.State;
import com.recoveryrecord.surveyandroid.example.StateMachine.StateMachine;

public class SharingState extends State {
    private FBStateMachine mStateMachine;

    public SharingState(StateMachine sm) {
        this.mStateMachine = (FBStateMachine) sm;
    }

    @Override
    public boolean processMessage(Message msg) {
        System.out.println("SharingState:" + msg.what);

        boolean ret;

        switch (msg.what) {
            case StateDef.CMD_SEND:
            case StateDef.CMD_CLICK_BACK:
            case StateDef.CMD_SCROLL_DOWN: {
                System.out.println("SharingState->ViewingState");
                mStateMachine.transitionTo(mStateMachine.mViewingState);
                ret = StateMachine.HANDLED;
                break;
            }
            default: {
                ret = StateMachine.NOT_HANDLED;
                break;
            }
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
        return "SharingState";
    }
}
