package com.hoplay.kay.hoplay.Services;


import android.os.Handler;
import android.util.Log;

public class HandlerCondition {

    private int repatedDelayTimer;
    private Handler handler;
    private boolean removeCallback=false;
    private Runnable runnable;

    public HandlerCondition(CallbackHandlerCondition callbackHandlerCondition,int repatedDelayTimer) {
        handler = new Handler();

        this.repatedDelayTimer = repatedDelayTimer;
        excute(callbackHandlerCondition);
    }



    private void excute(final CallbackHandlerCondition callbackHandlerCondition) {
         runnable = new Runnable() {
            @Override
            public void run() {
                handler.post(this);
                if(callbackHandlerCondition.callBack())
                    removeCallback();

            }
        };

        handler.postDelayed(runnable,repatedDelayTimer);

    }


    public void removeCallback()
    {
        handler.removeCallbacks(runnable);

    }

}
