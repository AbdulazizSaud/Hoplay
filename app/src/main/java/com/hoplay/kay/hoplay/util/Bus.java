package com.hoplay.kay.hoplay.util;


import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.HandlerCondition;

import java.util.LinkedList;

public class Bus {


    private LinkedList<CallbackHandlerCondition> callbacks;

    public Bus()
    {
        callbacks = new LinkedList<>();
    }

    public void startExecute(){

        int count=0;

        CallbackHandlerCondition callback = callbacks.get(count);

        HandlerCondition handlerCondition = new HandlerCondition(callback,0);
    }

    public void addCallBack(CallbackHandlerCondition callbackHandlerCondition)
    {
        callbacks.add(callbackHandlerCondition);
    }
}
