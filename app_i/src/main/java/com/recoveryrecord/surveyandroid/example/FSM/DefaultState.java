package com.recoveryrecord.surveyandroid.example.FSM;

import android.os.Message;

import com.recoveryrecord.surveyandroid.example.StateMachine.State;
import com.recoveryrecord.surveyandroid.example.StateMachine.StateMachine;

public class DefaultState extends State {
    private FBStateMachine mStateMachine;

    public DefaultState(StateMachine sm) {
        this.mStateMachine = (FBStateMachine) sm;
    }

    @Override
    public boolean processMessage(Message msg) {
        System.out.println("DefaultState:" + msg.what);

        boolean ret;

        switch (msg.what) {
            case StateDef.CMD_OPEN_FB:
            case StateDef.CMD_CHK_OPEN_FB:
                System.out.println("DefaultState->ViewingState");
                //mStateMachine.deferMessage(mStateMachine.obtainMessage(StateDef.CMD_OPEN_FB));
                mStateMachine.transitionTo(mStateMachine.mViewingState);
                ret = StateMachine.HANDLED;
                break;
            default:
                System.out.println("NOTHING");
                ret = StateMachine.HANDLED;
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
        return "DefaultState";
    }
}